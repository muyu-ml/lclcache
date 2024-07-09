package com.lcl.lclcache.command.string;

import com.lcl.lclcache.core.Command;
import com.lcl.lclcache.core.LclCache;
import com.lcl.lclcache.core.Reply;

/**
 * @Author conglongli
 * @date 2024/6/23 20:32
 */
public class DelCommand implements Command {
    @Override
    public String name() {
        return "DEL";
    }

    /**
     *  // strlen 收到的报文如下（允许删除多个）
     *             //*5,$3,del,$2,k1,$2,k2,$2,k3,$2,k4
     * @param cache
     * @param args
     * @return
     */
    @Override
    public Reply<?> exec(LclCache cache, String[] args) {
        // strlen 收到的报文如下（允许删除多个）
        //*5,$3,del,$2,k1,$2,k2,$2,k3,$2,k4
        String[] keys = getParams(args);
        int del = cache.del(keys);
        return Reply.integer(del);
    }
}
