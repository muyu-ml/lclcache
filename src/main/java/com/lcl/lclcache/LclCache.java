package com.lcl.lclcache;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * cache entries
 * @Author conglongli
 * @date 2024/6/23 15:11
 */
public class LclCache {

    Map<String, String> map = new HashMap<>();

    public String get(String key) {
        return map.get(key);
    }

    public void set(String key, String value) {
        map.put(key, value);
    }

    public int del(String...keys){
        return keys == null ? 0 : (int)Arrays.stream(keys).map(map :: remove).filter(Objects :: nonNull).count();
    }

    public int exists(String...keys){
        return keys == null ? 0 : (int)Arrays.stream(keys).map(map :: containsKey).filter(x -> x).count();
    }

    public String[] mget(String...keys){
        return keys == null ? new String[0] : Arrays.stream(keys).map(map :: get).toArray(String[]::new);
    }

    public void mset(String[] keys, String[] values) {
        if(keys == null || keys.length == 0){
            return;
        }
        for(int i=0; i< keys.length; i++){
            map.put(keys[i], values[i]);
        }
    }
}
