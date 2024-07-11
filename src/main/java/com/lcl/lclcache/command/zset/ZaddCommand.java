package com.lcl.lclcache.command.zset;

import com.lcl.lclcache.core.Command;
import com.lcl.lclcache.core.LclCache;
import com.lcl.lclcache.core.Reply;

import java.util.Arrays;

/**
 * @Author conglongli
 * @date 2024/6/23 20:32
 */
public class ZaddCommand implements Command {
    @Override
    public String name() {
        return "ZADD";
    }

    @Override
    public Reply<?> exec(LclCache cache, String[] args) {
        String key = getKey(args);
        String[] scores = getHKeys(args);
        String[] vals = getHValues(args);
        return Reply.integer(cache.zadd(key, vals, toDouble(scores)));
    }


    double[] toDouble(String[] scores) {
        return Arrays.stream(scores).mapToDouble(Double::parseDouble).toArray();
    }


}
