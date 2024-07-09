package com.lcl.lclcache.command;

import com.lcl.lclcache.core.Command;
import com.lcl.lclcache.core.LclCache;
import com.lcl.lclcache.core.Reply;

/**
 * @Author conglongli
 * @date 2024/6/23 20:32
 */
public class PingCommand implements Command {
    @Override
    public String name() {
        return "PING";
    }

    @Override
    public Reply<?> exec(LclCache cache, String[] args) {
        String ret = "PONG";
        if(args.length >= 5){
            ret = args[4];
        }
        return Reply.string(ret);
    }
}
