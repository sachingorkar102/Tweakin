package com.github.sachin.tweakin.modules.mobheads;

import com.google.common.base.Enums;
import com.google.common.base.Optional;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeadManager {


    // private final Map<EntityType,Head> singleHeadMap = new HashMap<>();
    private final Map<EntityType,List<Head>> multiHeadMap = new HashMap<>();

    public HeadManager(MobHeadsTweak instance){

        int i = 0;
        for(Head h : Head.values()){
            Optional<EntityType> type = Enums.getIfPresent(EntityType.class, h.getEntityType());
            if(type.isPresent()){
                i++;
                if(multiHeadMap.containsKey(type.get())){
                    multiHeadMap.get(type.get()).add(h);
                }
                else{
                    List<Head> l = new ArrayList<>();
                    l.add(h);
                    multiHeadMap.put(type.get(), l);
                }
            }
        }
        instance.getPlugin().getLogger().info("Generated "+i+" mob heads..");
        
    }

    public Map<EntityType, List<Head>> getMultiHeadMap() {
        return multiHeadMap;
    }

    
}
