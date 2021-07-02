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
        System.out.println( Math.random() > 1);
        System.out.println( !(Math.random() < 1));
    }
}
