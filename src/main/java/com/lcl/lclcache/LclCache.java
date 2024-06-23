package com.lcl.lclcache;

import java.util.HashMap;
import java.util.Map;

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

}
