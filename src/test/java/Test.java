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

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

public class Test {
    public static void main(String[] args) {
        Some a = Some.A1;
        Some b = Some.A2;
        
    }


    private enum Some{

        A1,A2,A3;


        private Some(){
            System.out.println();
        }
    }
}
