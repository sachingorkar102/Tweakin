package com.github.sachin.tweakin.modules.betterarmorstands;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.EulerAngle;


public class PoseManager {
    

    private BetterArmorStandTweak instance;
    private File file;
    private final Gson gson = new Gson();
    private final Map<String,PresetPose> poses=new HashMap<>();

    public PoseManager(BetterArmorStandTweak instance){
        this.instance = instance;
        loadFile();
        
    }

    protected void loadPoses(){
        instance.getPlugin().getLogger().info("Loading Poses...");
        poses.clear();
        try (FileReader reader = new FileReader(file)) {
            JsonObject obj = gson.fromJson(reader, JsonObject.class);
            if(obj != null){
                for(Entry<String,JsonElement> entry : obj.entrySet()){
                    JsonObject element = entry.getValue().getAsJsonObject();
                    poses.put(entry.getKey(), new PresetPose(element.get("id").getAsString(),element.get("display").getAsString(),fromJson(element.getAsJsonObject("head")),fromJson(element.getAsJsonObject("torso")),fromJson(element.getAsJsonObject("leftarm")),fromJson(element.getAsJsonObject("rightarm")),fromJson(element.getAsJsonObject("leftleg")),fromJson(element.getAsJsonObject("rightleg"))));
                }
            }
            instance.getPlugin().getLogger().info("Loaded "+String.valueOf(poses.size())+" armor stand poses.");
            instance.getPlugin().getCommandManager().getCommandCompletions().registerCompletion("tweakinposes", c -> poses.keySet());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addPose(PresetPose pose){
        poses.put(pose.id, pose);
        instance.getPlugin().getCommandManager().getCommandCompletions().registerCompletion("tweakinposes", c -> poses.keySet());
    }

    private EulerAngle fromJson(JsonObject obj){
        return new EulerAngle(obj.get("x").getAsDouble(),obj.get("y").getAsDouble(),obj.get("z").getAsDouble());
    }

    private JsonObject toJson(EulerAngle angle){
        JsonObject obj = new JsonObject();
        obj.addProperty("x", angle.getX());
        obj.addProperty("y", angle.getY());
        obj.addProperty("z", angle.getZ());
        return obj;
    }

    protected void savePoses(){
        instance.getPlugin().getLogger().info("Saving Poses...");
        try (FileWriter writer = new FileWriter(file)) {
            JsonObject obj = new JsonObject();
            for(String id : poses.keySet()){
                JsonObject Jpose = new JsonObject();
                PresetPose pose = poses.get(id);
                Jpose.addProperty("id", pose.id);
                Jpose.addProperty("display", pose.display);
                Jpose.add("head", toJson(pose.head));
                Jpose.add("torso", toJson(pose.torso));
                Jpose.add("leftarm", toJson(pose.leftarm));
                Jpose.add("rightarm", toJson(pose.rightarm));
                Jpose.add("leftleg", toJson(pose.leftleg));
                Jpose.add("rightleg", toJson(pose.rightleg));
                obj.add(id, Jpose);
            }
            gson.toJson(obj, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, PresetPose> getPoses() {
        return poses;
    }

    private void loadFile(){
        this.file = new File(instance.getPlugin().getDataFolder(),"preset-poses.json");
        if(!file.exists()){
            instance.getPlugin().saveResource("preset-poses.json", false);
        }
    }

}
