package com.lcl.lclcache.core;

import com.lcl.lclcache.command.common.CommandCommand;
import com.lcl.lclcache.command.common.InfoCommand;
import com.lcl.lclcache.command.common.PingCommand;
import com.lcl.lclcache.command.hash.*;
import com.lcl.lclcache.command.set.*;
import com.lcl.lclcache.command.list.*;
import com.lcl.lclcache.command.string.*;
import com.lcl.lclcache.command.zset.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * registry commands
 * @Author conglongli
 * @date 2024/6/23 20:34
 */
public class Commands {
    private static Map<String, Command> ALL_COMMAND = new LinkedHashMap<>();

    static {
        initCommands();
    }

    private static void initCommands() {
        // COMMON COMMAND
        registry(new PingCommand());
        registry(new InfoCommand());
        registry(new CommandCommand());

        // STRING COMMAND
        registry(new SetCommand());
        registry(new GetCommand());
        registry(new StrlenCommand());
        registry(new DelCommand());
        registry(new ExistsCommand());
        registry(new IncrCommand());
        registry(new DecrCommand());
        registry(new MsetCommand());
        registry(new MgetCommand());

        // LIST COMMAND
        registry(new LpushCommand());
        registry(new LpopCommand());
        registry(new RpushCommand());
        registry(new RpopCommand());
        registry(new LlenCommand());
        registry(new LIndexCommand());
        registry(new LRangeCommand());

        // SET COMMAND
        registry(new SaddCommand());
        registry(new SmembersCommand());
        registry(new SismemberCommand());
        registry(new ScardCommand());
        registry(new SremCommand());
        registry(new SpopCommand());

        // HASH COMMAND
        registry(new HsetCommand());
        registry(new HgetCommand());
        registry(new HgetAllCommand());
        registry(new HmgetCommand());
        registry(new HlenCommand());
        registry(new HexistsCommand());
        registry(new HdelCommand());


        // ZSET COMMAND
        registry(new ZaddCommand());
        registry(new ZcardCommand());
        registry(new ZcountCommand());
        registry(new ZrankCommand());
        registry(new ZrevCommand());
        registry(new ZscoreCommand());
    }

    public static void registry(Command command){
        ALL_COMMAND.put(command.name(), command);
    }

    public static Command get(String name){
        return ALL_COMMAND.get(name);
    }

    public static String[] getCommandNames(){
        return ALL_COMMAND.keySet().toArray(new String[0]);
    }
}
