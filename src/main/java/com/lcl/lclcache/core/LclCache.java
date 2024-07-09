package com.lcl.lclcache.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * cache entries
 * @Author conglongli
 * @date 2024/6/23 15:11
 */
public class LclCache {

    Map<String, CacheEntry<?>> map = new HashMap<>();

    // =============== 1.String begin ===============

    public String get(String key) {
        CacheEntry<String> cacheEntry = (CacheEntry<String>) map.get(key);
        return cacheEntry.getValue();
    }

    public void set(String key, String value) {
        CacheEntry<String> cacheEntry = new CacheEntry<>(value);
        map.put(key, cacheEntry);
    }

    public int del(String...keys){
        return keys == null ? 0 : (int)Arrays.stream(keys).map(map :: remove).filter(Objects :: nonNull).count();
    }

    public int exists(String...keys){
        return keys == null ? 0 : (int)Arrays.stream(keys).map(map :: containsKey).filter(x -> x).count();
    }

    public String[] mget(String...keys){
        return keys == null ? new String[0] : Arrays.stream(keys)
                .map(this :: get).toArray(String[]::new);
    }

    public void mset(String[] keys, String[] values) {
        if(keys == null || keys.length == 0){
            return;
        }
        for(int i=0; i< keys.length; i++){
            set(keys[i], values[i]);
        }
    }

    public int incr(String key) {
        String str = get(key);
        int val = 0;
        try {
            if(str != null){
                val = Integer.parseInt(str);
            }
            val++;
            set(key, String.valueOf(val));
        } catch (NumberFormatException nfe) {
            throw nfe;
        }
        return val;
    }

    public int decr(String key) {
        String str = get(key);
        int val = 0;
        try {
            if(str != null){
                val = Integer.parseInt(str);
            }
            val--;
            set(key, String.valueOf(val));
        } catch (NumberFormatException nfe) {
            throw nfe;
        }
        return val;
    }

    public Integer strlen(String key) {
        String value = get(key);
        return value == null ? 0 : value.length();
    }

    // =============== 1.String end ===============



    // =============== 2.list begin ===============

    public Integer lpush(String key, String...vals) {
        CacheEntry<LinkedList<String>> cacheEntry = (CacheEntry<LinkedList<String>>) map.get(key);
        if(cacheEntry == null ){
            cacheEntry = new CacheEntry<>(new LinkedList<>());
            map.put(key, cacheEntry);
        }
        LinkedList<String> list = cacheEntry.getValue();
        Arrays.stream(vals).forEach(list :: addFirst);
        return vals.length;
    }

    public String[] lpop(String key, int count) {
        CacheEntry<LinkedList<String>> cacheEntry = (CacheEntry<LinkedList<String>>) map.get(key);
        if(cacheEntry == null ){
            return null;
        }
        LinkedList<String> list = cacheEntry.getValue();
        if(list == null){
            return null;
        }
        int len = Math.min(count, list.size());
        String[] ret = new String[len];

        int index = 0;
        while(index < len){
            ret[index++] = list.removeFirst();
        }
        return ret;
    }


    public Integer rpush(String key, String...vals) {
        if(vals == null){
            return 0;
        }
        CacheEntry<LinkedList<String>> cacheEntry = (CacheEntry<LinkedList<String>>) map.get(key);
        if(cacheEntry == null ){
            cacheEntry = new CacheEntry<>(new LinkedList<>());
            map.put(key, cacheEntry);
        }
        LinkedList<String> list = cacheEntry.getValue();
//        Arrays.stream(vals).forEach(list :: addLast);
        list.addAll(List.of(vals));
        return vals.length;
    }

    public String[] rpop(String key, int count) {
        CacheEntry<LinkedList<String>> cacheEntry = (CacheEntry<LinkedList<String>>) map.get(key);
        if(cacheEntry == null ){
            return null;
        }
        LinkedList<String> list = cacheEntry.getValue();
        if(list == null){
            return null;
        }
        int len = Math.min(count, list.size());
        String[] ret = new String[len];

        int index = 0;
        while(index < len){
            ret[index++] = list.removeLast();
        }
        return ret;
    }

    public Integer llen(String key) {
        CacheEntry<LinkedList<String>> cacheEntry = (CacheEntry<LinkedList<String>>) map.get(key);
        if(cacheEntry == null ){
            return 0;
        }
        LinkedList<String> list = cacheEntry.getValue();
        if(list == null){
            return 0;
        }
        return list.size();
    }

    public String lindex(String key, int index) {
        CacheEntry<LinkedList<String>> cacheEntry = (CacheEntry<LinkedList<String>>) map.get(key);
        if(cacheEntry == null ){
            return null;
        }
        LinkedList<String> list = cacheEntry.getValue();
        if(list == null){
            return null;
        }
        if(index >= list.size()){
            return null;
        }
        return list.get(index);
    }

    public String[] lrange(String key, int start, int end) {
        CacheEntry<LinkedList<String>> cacheEntry = (CacheEntry<LinkedList<String>>) map.get(key);
        if(cacheEntry == null ){
            return null;
        }
        LinkedList<String> list = cacheEntry.getValue();
        if(list == null){
            return null;
        }
        int size = list.size();
        if(start >= size) {
            return null;
        }
        if(end >= size || end == -1){
            end = size - 1;
        }
        int len = Math.min(size, end - start + 1);
        String[] ret = new String[len];
        for (int i=0;i<len;i++){
            ret[i] = list.get(start+i);
        }
        return ret;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CacheEntry<T> {
        private T value;
    }
}
