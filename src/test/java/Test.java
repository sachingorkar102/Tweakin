import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import com.google.gson.Gson;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

public class Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
        List<String> l = Arrays.asList("OAK_LOG|STRIPPED_OAK_LOG|BIRCH_LOG|STRIPPED_BIRCH_LOG|SPRUCE_LOG|STRIPPED_SPRUCE_LOG|JUNGLE_LOG|STRIPPED_JUNGLE_LOG|ACACIA_LOG|STRIPPED_ACACIA_LOG|DARK_OAK_LOG|STRIPPED_DARK_OAK_LOG|CRIMSON_STEM|WARPED_STEM|STRIPPED_CRIMSON_STEM|STRIPPED_WARPED_STEM".split("\\|"));
        System.out.println(l);
        // System.out.println(Math.toDegrees(1));
        // Gson gson = new Gson();
        // System.out.println(gson.toJson(new PresetPose("test", "test", new EulerAngle(1,1,1), new EulerAngle(1,1,1),new EulerAngle(1,1,1), new EulerAngle(1,1,1),new EulerAngle(1,1,1), new EulerAngle(1,1,1))));;
        // Map<Integer,String> map = new HashMap<>();
        // map.put(2, "Hello");
        // map.put(5, "Hello");
        // map.put(3, "Hello");
        // map.put(6, "Hello");
        // map.put(8, "Hello");
        // map.put(1, "Hellossss");
        
        // System.out.println();
        // Class<?> miscItemsClass = Class.forName("com.github.sachin.tweakin.utils.MiscItems");
        // Field f = miscItemsClass.getField("ENABLED_BUTTON");
        // f.get(new ItemStack(Material.ACACIA_BOAT));
        // File file = new File("new-config.yml");
        // FileConfiguration newConfig = YamlConfiguration.loadConfiguration(new File("config.yml"));
        // FileConfiguration oldConfig = YamlConfiguration.loadConfiguration(file);
        // oldConfig.options().copyDefaults();
        // for(String key : newConfig.getKeys(false)){
        //     if(!oldConfig.contains(key)){
        //         oldConfig.set(key, newConfig.get(key));
                
        //     }
        // }
        // oldConfig.save(file);
        // FileReader reader = new FileReader(file);
        // BufferedReader br = new BufferedReader(reader);
        // String l;
        // StringBuffer buffer = new StringBuffer();
        // while((l=br.readLine()) != null){
        //     System.out.println(l);
        //     if(l.equals("empty: false")){
        //         buffer.append("\n");
        //     }
        //     else{
        //         buffer.append(l);
        //         buffer.append("\n");
        //     }
        // }
        // FileWriter writer = new FileWriter(file);
        // writer.write(buffer.toString());
        // writer.close();
        // reader.close();
    }



}
