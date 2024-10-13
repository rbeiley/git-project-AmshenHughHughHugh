import java.io.File;
import java.util.LinkedList;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;

public class Git {
    public static LinkedList commitList;

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

    }

    public static void initGitRepo() throws IOException {
        int existsCounter = 0;
        File gitFile = new File("git");
        gitFile.mkdir();
        if (gitFile.exists()) {
            existsCounter++;
        }
        File objectsFile = new File("git/objects");
        objectsFile.mkdir();
        if (objectsFile.exists()) {
            existsCounter++;
        }
        File index = new File("git/index");
        if (index.exists()) {
            existsCounter++;
        }
        index.createNewFile();
        if (existsCounter >= 3) {
            System.out.println("Git Repositorys already exists");
        }
        File head = new File("HEAD");
        head.createNewFile();
        File commitFile = new File("git/objects/commit");
        commitFile.createNewFile();
    }

    public static void checkInitGitRepo() throws IOException {
        File indexPath = new File("git/index");
        if (indexPath.exists()) {
            System.out.println("index file was created successfully");
            deleteRecursively(indexPath);
        } else {
            System.out.println("creating the index file did not work");
        }

        File objectsPath = new File("git/objects");
        if (objectsPath.exists()) {
            System.out.println("objects file was created successfully");
            deleteRecursively(objectsPath);
        } else {
            System.out.println("creating the objects file did not work");
        }

        File gitPath = new File("git");
        if (gitPath.exists()) {
            System.out.println("git file was created successfully");
            deleteRecursively(gitPath);
        } else {
            System.out.println("creating the git file did not work");
        }
        File headPath = new File("git/HEAD");
        if (headPath.exists()) {
            System.out.println("HEAD file was created successfully");
            deleteRecursively(headPath);
        } else {
            System.out.println("creating the HEAD file did not work");
        }
    }

    public static void deleteRecursively(File f) throws IOException {
        if (f.isDirectory()) {
            for (File c : f.listFiles()) {
                deleteRecursively(c);
            }
            f.delete();
        } else {
            f.delete();
        }
    }

    public static void makeCommit(File file, String author, String message)
            throws IOException, NoSuchAlgorithmException {
        File commitFile = new File("");
        String dataInFile = getContents(commitFile.getPath());
        BufferedWriter bR = new BufferedWriter(new FileWriter(commitFile.getPath()));

        Blob blob = new Blob(file.toPath()); // stages the commit by creating a blob of the tree

        // writing out the commit file
        bR.write("tree: " + blob.convertToSha(dataInFile) + "\n");
        bR.write("parent: " + getContents("HEAD") + "\n");
        bR.write("author: " + author + "\n");
        LocalDate date = LocalDate.now();
        bR.write("date: " + date + "\n");
        bR.write("message: " + message);

        commitList.add(commitFile);
        updateHeadFile(commitFile);
        bR.close();
    }

    public static void updateHeadFile(File commitFile) throws IOException {
        BufferedWriter bR = new BufferedWriter(new FileWriter("HEAD"));
        bR.write(getContents(commitFile.getPath()));
        bR.close();
    }

    public static String getContents(String filePath) throws IOException {
        StringBuilder sB = new StringBuilder();
        BufferedReader bR = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = bR.readLine()) != null) {
            sB.append(line).append("\n");
        }
        bR.close();
        return sB.toString();
    }

}
