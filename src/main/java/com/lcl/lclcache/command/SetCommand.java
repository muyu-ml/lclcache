package com.lcl.lclcache.command;

import com.lcl.lclcache.core.Command;
import com.lcl.lclcache.core.LclCache;
import com.lcl.lclcache.core.Reply;

/**
 * @Author conglongli
 * @date 2024/6/23 20:32
 */
public class SetCommand implements Command {

    private static final String OK = "OK";

    @Override
    public String name() {
        return "SET";
    }

    /**
     *             // set 收到的报文如下
     *             //*3
     *             //    $3
     *             //            set
     *             //    $2
     *             //            k1
     *             //    $5
     *             //            aa100
     *             //
     * @param cache
     * @param args
     * @return
     */
    @Override
    public Reply<?> exec(LclCache cache, String[] args) {
        cache.set(getKey(args), getValue(args));
        return Reply.string(OK);
    }
}
