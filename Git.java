import java.io.File;
import java.io.IOException;

public class Git {
    public static void main(String[] args) throws IOException {
        initGitRepo();
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
        File index = new File("git/objects/index");
        if (index.exists()) {
            existsCounter++;
        }
        index.createNewFile();
        if (existsCounter >= 3) {
            System.out.println("Git Repository already exists");
        }
    }
}
