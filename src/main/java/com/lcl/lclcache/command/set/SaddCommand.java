package com.lcl.lclcache.command.set;

import com.lcl.lclcache.core.Command;
import com.lcl.lclcache.core.LclCache;
import com.lcl.lclcache.core.Reply;

/**
 * @Author conglongli
 * @date 2024/6/23 20:32
 */
public class SaddCommand implements Command {
    @Override
    public String name() {
        return "SADD";
    }

    @Override
    public Reply<?> exec(LclCache cache, String[] args) {
        String key = getKey(args);
        String[] vals = getParamsNoKey(args);
        return Reply.integer(cache.sadd(key, vals));
    }
}
