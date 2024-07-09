package com.lcl.lclcache.command.string;

import com.lcl.lclcache.core.Command;
import com.lcl.lclcache.core.LclCache;
import com.lcl.lclcache.core.Reply;

/**
 * @Author conglongli
 * @date 2024/6/23 20:32
 */
public class GetCommand implements Command {
    @Override
    public String name() {
        return "GET";
    }

    /**
     * // get 收到的报文如下
     *             //*2
     *             //    $3
     *             //            get
     *             //    $2
     *             //            k1
     * @param cache
     * @param args
     * @return
     */
    @Override
    public Reply<?> exec(LclCache cache, String[] args) {
        String value = cache.get(getKey(args));
        return Reply.bulkString(value);
    }
}
