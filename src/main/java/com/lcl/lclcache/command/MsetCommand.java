package com.lcl.lclcache.command;

import com.lcl.lclcache.core.Command;
import com.lcl.lclcache.core.LclCache;
import com.lcl.lclcache.core.Reply;

/**
 * @Author conglongli
 * @date 2024/6/23 20:32
 */
public class MsetCommand implements Command {
    @Override
    public String name() {
        return "MSET";
    }

    @Override
    public Reply<?> exec(LclCache cache, String[] args) {
        String[] keys = getKeys(args);
        String[] values = getValues(args);
        cache.mset(keys, values);
        return Reply.string(OK);
    }
}
