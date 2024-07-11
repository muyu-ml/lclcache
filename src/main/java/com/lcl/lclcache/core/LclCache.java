package com.lcl.lclcache.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Stream;

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

    // =============== 2.list end ===============


    // =============== 3.set begin ===============

    public Integer sadd(String key, String[] vals) {
        CacheEntry<LinkedHashSet<String>> cacheEntry = (CacheEntry<LinkedHashSet<String>>) map.get(key);
        if(cacheEntry == null ){
            cacheEntry = new CacheEntry<>(new LinkedHashSet<>());
            map.put(key, cacheEntry);
        }
        HashSet<String> set = cacheEntry.getValue();
        set.addAll(Arrays.asList(vals));
        return vals.length;
    }

    public String[] smembers(String key) {
        CacheEntry<LinkedHashSet<String>> cacheEntry = (CacheEntry<LinkedHashSet<String>>) map.get(key);
        if(cacheEntry == null ){
            return null;
        }
        LinkedHashSet<String> set = cacheEntry.getValue();
        if(set == null){
            return null;
        }
        return set.toArray(String[]::new);
    }

    public Integer scard(String key) {
        CacheEntry<LinkedHashSet<String>> cacheEntry = (CacheEntry<LinkedHashSet<String>>) map.get(key);
        if(cacheEntry == null ){
            return null;
        }
        LinkedHashSet<String> set = cacheEntry.getValue();
        if(set == null){
            return null;
        }
        return set.size();
    }

    public Integer issmember(String key, String val) {
        CacheEntry<LinkedHashSet<String>> cacheEntry = (CacheEntry<LinkedHashSet<String>>) map.get(key);
        if(cacheEntry == null ){
            return 0;
        }
        LinkedHashSet<String> set = cacheEntry.getValue();
        if(set == null){
            return 0;
        }
        return set.contains(val) ? 1: 0;
    }

    public Integer sremove(String key, String[] vals) {
        CacheEntry<LinkedHashSet<String>> cacheEntry = (CacheEntry<LinkedHashSet<String>>) map.get(key);
        if(cacheEntry == null ){
            return 0;
        }
        LinkedHashSet<String> set = cacheEntry.getValue();
        if(set == null){
            return 0;
        }
        return vals == null ? 0 : (int)Arrays.stream(vals).map(set :: remove).filter(x -> x).count();
    }

    Random random = new Random();
    public String[] spop(String key, int count) {
        CacheEntry<LinkedHashSet<String>> cacheEntry = (CacheEntry<LinkedHashSet<String>>) map.get(key);
        if(cacheEntry == null ){
            return null;
        }
        LinkedHashSet<String> set = cacheEntry.getValue();
        if(set == null){
            return null;
        }
        int len = Math.min(count, set.size());
        String[] ret = new String[len];

        int index = 0;
        while(index < len){
            // 随机删除一个元素
            String obj = set.toArray(String[]::new)[random.nextInt(set.size())];
            set.remove(obj);
            ret[index++] = obj;
        }
        return ret;
    }

    // =============== 3.set end ===============


    // =============== 4.hash begin ===============

    public Integer hset(String key, String[] hkeys, String[] hvalues) {
        if(hkeys == null || hkeys.length == 0 || hvalues == null || hvalues.length == 0){
            return 0;
        }
        CacheEntry<LinkedHashMap<String, String>> cacheEntry = (CacheEntry<LinkedHashMap<String, String>>) map.get(key);
        if(cacheEntry == null ){
            cacheEntry = new CacheEntry<>(new LinkedHashMap<String, String>());
            map.put(key, cacheEntry);
        }
        LinkedHashMap<String, String> hashMap = cacheEntry.getValue();
        for(int i=0; i<hkeys.length; i++){
            hashMap.put(hkeys[i], hvalues[i]);
        }
        return hvalues.length;
    }

    public String hget(String key, String hkey) {
        CacheEntry<LinkedHashMap<String, String>> cacheEntry = (CacheEntry<LinkedHashMap<String, String>>) map.get(key);
        if(cacheEntry == null ){
            return null;
        }
        LinkedHashMap<String, String> hashMap = cacheEntry.getValue();
        if(hashMap == null){
            return null;
        }
        return hashMap.get(hkey);
    }

    public String[] hgetall(String key) {
        CacheEntry<LinkedHashMap<String, String>> cacheEntry = (CacheEntry<LinkedHashMap<String, String>>) map.get(key);
        if(cacheEntry == null ){
            return null;
        }
        LinkedHashMap<String, String> hashMap = cacheEntry.getValue();
        if(hashMap == null){
            return null;
        }
        return hashMap.entrySet().stream().flatMap(e -> Stream.of(e.getKey(), e.getValue())).toArray(String[] :: new);
    }

    public String[] hmget(String key, String[] hkeys) {
        if(hkeys == null){
            return new String[0];
        }
        CacheEntry<LinkedHashMap<String, String>> cacheEntry = (CacheEntry<LinkedHashMap<String, String>>) map.get(key);
        if(cacheEntry == null ){
            return null;
        }
        LinkedHashMap<String, String> hashMap = cacheEntry.getValue();
        if(hashMap == null){
            return null;
        }
        return Arrays.stream(hkeys).map(hashMap::get).toArray(String[]::new);
    }

    public Integer hlen(String key) {
        CacheEntry<LinkedHashMap<String, String>> cacheEntry = (CacheEntry<LinkedHashMap<String, String>>) map.get(key);
        if(cacheEntry == null ){
            return 0;
        }
        LinkedHashMap<String, String> hashMap = cacheEntry.getValue();
        return hashMap == null ? 0 : hashMap.size();
    }

    public Integer hexists(String key, String hkey) {
        CacheEntry<LinkedHashMap<String, String>> cacheEntry = (CacheEntry<LinkedHashMap<String, String>>) map.get(key);
        if(cacheEntry == null ){
            return 0;
        }
        LinkedHashMap<String, String> hashMap = cacheEntry.getValue();
        if(hashMap == null){
            return 0;
        }
        return hashMap.containsKey(hkey) ? 1 : 0;
    }

    public Integer hdel(String key, String[] hkeys) {
        CacheEntry<LinkedHashMap<String, String>> cacheEntry = (CacheEntry<LinkedHashMap<String, String>>) map.get(key);
        if(cacheEntry == null ){
            return 0;
        }
        LinkedHashMap<String, String> hashMap = cacheEntry.getValue();
        if(hashMap == null){
            return 0;
        }
        return hkeys == null ? 0 : (int)Arrays.stream(hkeys).map(hashMap::remove).filter(Objects::nonNull).count();
    }

    // =============== 4.hash end ===============


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CacheEntry<T> {
        private T value;
    }
}
