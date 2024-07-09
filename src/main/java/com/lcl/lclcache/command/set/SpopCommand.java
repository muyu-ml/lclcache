package com.lcl.lclcache.command.set;

import com.lcl.lclcache.core.Command;
import com.lcl.lclcache.core.LclCache;
import com.lcl.lclcache.core.Reply;

/**
 * @Author conglongli
 * @date 2024/6/23 20:32
 */
public class SpopCommand implements Command {
    @Override
    public String name() {
        return "SPOP";
    }

    @Override
    public Reply<?> exec(LclCache cache, String[] args) {
        String key = getKey(args);
        int count = 1;
        if(args.length > 6){
            String val = getValue(args);
            count = Integer.parseInt(val);
            return Reply.array(cache.spop(key, count));
        }
        String[] spop = cache.spop(key, count);
        return Reply.bulkString(spop == null ? null : spop[0]);
    }
}
