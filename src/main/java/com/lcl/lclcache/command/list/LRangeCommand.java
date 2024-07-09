package com.lcl.lclcache.command.list;

import com.lcl.lclcache.core.Command;
import com.lcl.lclcache.core.LclCache;
import com.lcl.lclcache.core.Reply;

/**
 * @Author conglongli
 * @date 2024/6/23 20:32
 */
public class LRangeCommand implements Command {
    @Override
    public String name() {
        return "LRANGE";
    }

    @Override
    public Reply<?> exec(LclCache cache, String[] args) {
        String key = getKey(args);
        String[] params = getParams(args);
        int start = Integer.parseInt(params[1]);
        int end = Integer.parseInt(params[2]);
        return Reply.array(cache.lrange(key, start, end));
    }
}
