package com.lcl.lclcache.command.hash;

import com.lcl.lclcache.core.Command;
import com.lcl.lclcache.core.LclCache;
import com.lcl.lclcache.core.Reply;

/**
 * @Author conglongli
 * @date 2024/6/23 20:32
 */
public class HsetCommand implements Command {
    @Override
    public String name() {
        return "HSET";
    }

    @Override
    public Reply<?> exec(LclCache cache, String[] args) {
        String key = getKey(args);
        String[] hkeys = getHKeys(args);
        String[] hvalues = getHValues(args);
        return Reply.integer(cache.hset(key, hkeys, hvalues));
    }
}
