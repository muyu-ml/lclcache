package com.lcl.lclcache.command.set;

import com.lcl.lclcache.core.Command;
import com.lcl.lclcache.core.LclCache;
import com.lcl.lclcache.core.Reply;

/**
 * @Author conglongli
 * @date 2024/6/23 20:32
 */
public class SmembersCommand implements Command {
    @Override
    public String name() {
        return "SMEMBERS";
    }

    @Override
    public Reply<?> exec(LclCache cache, String[] args) {
        String key = getKey(args);
        return Reply.array(cache.smembers(key));
    }
}
