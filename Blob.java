import java.nio.file.Path;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

public class Blob {
    public File file;
    public String data;
    public String hashedName;
    // potentially need to initialize it as data = ""??

    public Blob(String fileName) throws IOException, NoSuchAlgorithmException {
        file = new File(fileName);
        data = copyData();
        hashedName = convertToSha(data);
    }

    public String copyData() throws IOException {
        Path path = file.toPath();
        return new String(Files.readAllBytes(path));
    }

    public String convertToSha(String dataInFile) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(dataInFile.getBytes("UTF-8"));
        return new BigInteger(1, crypt.digest()).toString(16);
    }
    // NOT PRINTING CORRECTLY

    public String getData() {
        return data;
    }

    public String getHashedName() {
        return hashedName;
    }
}
