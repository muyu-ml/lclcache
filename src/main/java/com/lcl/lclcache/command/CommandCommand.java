package com.lcl.lclcache.command;

import com.lcl.lclcache.core.Command;
import com.lcl.lclcache.core.Commands;
import com.lcl.lclcache.core.LclCache;
import com.lcl.lclcache.core.Reply;

import java.time.LocalDate;

/**
 * @Author conglongli
 * @date 2024/6/23 20:32
 */
public class CommandCommand implements Command {

    @Override
    public String name() {
        return "COMMAND";
    }

    @Override
    public Reply<?> exec(LclCache cache, String[] args) {
        return Reply.array(Commands.getCommandNames());
    }

}
