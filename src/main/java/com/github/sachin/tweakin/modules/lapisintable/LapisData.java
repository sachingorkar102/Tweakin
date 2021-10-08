package com.github.sachin.tweakin.modules.lapisintable;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class LapisData implements ConfigurationSerializable{

    private Location location;
    private int count;

    public LapisData(int count,Location location){
        this.count = count;
        this.location = location;
    }

	@Override
	public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("count", count);
        map.put("location", location);
		return map;
	}

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Location getLocation() {
        return location;
    }

    public static LapisData deserialize(Map<String, Object> map) {
        return new LapisData((int)map.get("count"),(Location)map.get("location"));
    }
    
}
