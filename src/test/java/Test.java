import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class Test {
    public static void main(String[] args) {
        File read = new File("test.yml");
        File write = new File("test2.yml");
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(write), StandardCharsets.UTF_8));
            BufferedReader reader = new BufferedReader(new FileReader(read));
            String st;
            while ((st = reader.readLine()) != null){
                writer.write(st);
                writer.write("\n");
            }
            writer.close();
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
