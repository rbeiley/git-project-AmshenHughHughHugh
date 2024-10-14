import java.io.File;
import java.util.LinkedList;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Path;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Paths;

public class Git {
    public static LinkedList<File> commitList = new LinkedList<>();

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        // initGitRepo();

        // File testFile1 = new File("test1.txt");
        // BufferedWriter writer1 = new BufferedWriter(new FileWriter(testFile1));
        // writer1.write("This is test file 1");
        // writer1.close();

        // File testFile2 = new File("test2.txt");
        // BufferedWriter writer2 = new BufferedWriter(new FileWriter(testFile2));
        // writer2.write("This is test file 2");
        // writer2.close();

        // stage("test1.txt");
        // stage("test2.txt");

        // commit("Roen", "commit 1");

        // System.out.println(getContents("git/commitFile"));

        initGitRepo();

        File testFile1 = new File("test1.txt");
        BufferedWriter writer1 = new BufferedWriter(new FileWriter(testFile1));
        writer1.write("test file 1");
        writer1.close();

        stage("test1.txt");

        commit("Roen", "First commit");

        System.out.println(getContents("git/commitFile"));

        File testFile2 = new File("test2.txt");
        BufferedWriter writer2 = new BufferedWriter(new FileWriter(testFile2));
        writer2.write("test file 2");
        writer2.close();

        stage("test2.txt");

        commit("Ian", "Second commit");

        System.out.println(getContents("git/commitFile"));

        File testFile3 = new File("test3.txt");
        BufferedWriter writer3 = new BufferedWriter(new FileWriter(testFile3));
        writer3.write("test file 3");
        writer3.close();

        stage("test3.txt");

        commit("Bjorn", "Third commit");

        System.out.println(getContents("git/commitFile"));
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

    public static void stage(String filePath) throws NoSuchAlgorithmException, IOException {
        Path path = Paths.get(filePath);
        Blob blob = new Blob(path);
    }

    public static String commit(String author, String message)
            throws IOException, NoSuchAlgorithmException {
        File commitFile = new File("git/commitFile");
        commitFile.createNewFile();
        String dataInFile = getContents(commitFile.getPath());
        BufferedWriter bR = new BufferedWriter(new FileWriter(commitFile.getPath()));

        // make the tree file and write in index stuff
        File treeFile = new File("git/treeFile");
        treeFile.createNewFile();
        BufferedWriter treeFileWriter = new BufferedWriter(new FileWriter(treeFile.getPath()));
        String indexContents = getContents("git/index");
        treeFileWriter.write(indexContents);

        // write in previous files
        if (commitList.size() != 0) {
            treeFileWriter.write(getContents("HEAD"));
            treeFileWriter.close();
        }

        // writing out the commit file
        Blob treeFileBlob = new Blob(treeFile.toPath());
        bR.write("tree: " + treeFileBlob.convertToSha(getContents(treeFile.getPath())) + "\n");

        if (getContents("HEAD") == "") {
            bR.write("parent: " + getContents("HEAD") + "\n");
        } else {
            bR.write("parent: " + getContents("HEAD"));
        }

        bR.write("author: " + author + "\n");
        bR.write("date: Feb. 14 1946\n");
        bR.write("message: " + message);

        // adding commit to linked list
        commitList.add(commitFile);
        // update the head
        updateHeadFile(commitFile);
        bR.close();

        // return the sha1 of the commitFile
        Blob blob = new Blob(commitFile.toPath());
        return blob.convertToSha(dataInFile);
    }

    public static void updateHeadFile(File commitFile) throws IOException, NoSuchAlgorithmException {
        BufferedWriter bR = new BufferedWriter(new FileWriter("HEAD"));
        Blob blob = new Blob(commitFile.toPath());
        bR.write(blob.convertToSha(getContents(commitFile.getPath())));
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