package com.lcl.lclcache.command.string;

import com.lcl.lclcache.core.Command;
import com.lcl.lclcache.core.LclCache;
import com.lcl.lclcache.core.Reply;

/**
 * @Author conglongli
 * @date 2024/6/23 20:32
 */
public class IncrCommand implements Command {
    @Override
    public String name() {
        return "INCR";
    }

    @Override
    public Reply<?> exec(LclCache cache, String[] args) {
        String key = getKey(args);
        return Reply.integer(cache.incr(key));
    }
}
