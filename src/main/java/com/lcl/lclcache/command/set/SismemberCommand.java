package com.lcl.lclcache.command.set;

import com.lcl.lclcache.core.Command;
import com.lcl.lclcache.core.LclCache;
import com.lcl.lclcache.core.Reply;

/**
 * @Author conglongli
 * @date 2024/6/23 20:32
 */
public class SismemberCommand implements Command {
    @Override
    public String name() {
        return "SISMEMBER";
    }

    @Override
    public Reply<?> exec(LclCache cache, String[] args) {
        String key = getKey(args);
        String val = getValue(args);
        return Reply.integer(cache.issmember(key, val));
    }
}
