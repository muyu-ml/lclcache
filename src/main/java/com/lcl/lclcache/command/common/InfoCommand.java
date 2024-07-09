package com.lcl.lclcache.command.common;

import com.lcl.lclcache.core.Command;
import com.lcl.lclcache.core.LclCache;
import com.lcl.lclcache.core.Reply;

import java.time.LocalDate;

/**
 *
 * @Author conglongli
 * @date 2024/6/23 20:32
 */
public class InfoCommand implements Command {

    private static final String INFO = "LclCache Server[v1.0.1], created by lcl" + CRLF
            + "Mock Redis Server at " + LocalDate.now() + " in Beijing" + CRLF;

    @Override
    public String name() {
        return "INFO";
    }

    @Override
    public Reply<?> exec(LclCache cache, String[] args) {
        return Reply.bulkString(INFO);
    }
}
