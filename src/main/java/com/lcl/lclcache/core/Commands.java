package com.lcl.lclcache.core;

import com.lcl.lclcache.command.common.CommandCommand;
import com.lcl.lclcache.command.common.InfoCommand;
import com.lcl.lclcache.command.common.PingCommand;
import com.lcl.lclcache.command.set.*;
import com.lcl.lclcache.command.list.*;
import com.lcl.lclcache.command.string.*;

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

        // HASH COMMAND

        // SET COMMAND
        registry(new SaddCommand());
        registry(new SmembersCommand());
        registry(new SismemberCommand());
        registry(new ScardCommand());
        registry(new SremoveCommand());
        registry(new SpopCommand());
        //

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
