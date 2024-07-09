package com.lcl.lclcache.command.string;

import com.lcl.lclcache.core.Command;
import com.lcl.lclcache.core.LclCache;
import com.lcl.lclcache.core.Reply;

/**
 * @Author conglongli
 * @date 2024/6/23 20:32
 */
public class ExistsCommand implements Command {
    @Override
    public String name() {
        return "EXISTS";
    }

    /**
     * // EXISTS 收到的报文如下（允许删除多个）
     *             //*2,$6,EXISTS,$2,k1,$2,k2,$2,k3,$2,k4
     * @param cache
     * @param args
     * @return
     */
    @Override
    public Reply<?> exec(LclCache cache, String[] args) {
        String[] keys = getParams(args);
        int existsCount = cache.exists(keys);
        return Reply.integer(existsCount);
    }
}
