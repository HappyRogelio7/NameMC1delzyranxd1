package com.gmail.zyran.dev;

import com.gmail.zyran.dev.commands.VoteCommand;
import com.gmail.zyran.dev.commands.VoteListCommand;
import com.gmail.zyran.dev.utils.Settings;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public class NameMC extends JavaPlugin {

    private static NameMC nameMC;
    private Settings config, messages;

    public NameMC() {
        nameMC = this;
        config = new Settings(this, "config");
        messages = new Settings(this, "messages");
    }

    public void onEnable() {
        String commandVoteList = (config.getString("Commands.voteList") == null) ? "votelist" : config.getString("Commands.voteList");
        String commandCheckVote = (config.getString("Commands.checkVote") == null) ? "checkvote" : config.getString("Commands.checkVote");
        try {
            getCommandMap().register(commandVoteList, new VoteListCommand(commandVoteList));
            getCommandMap().register(commandCheckVote, new VoteCommand(commandCheckVote));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public CommandMap getCommandMap() throws NoSuchFieldException, IllegalAccessException {
        Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        boolean accessible = commandMapField.isAccessible();
        commandMapField.setAccessible(true);
        CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
        commandMapField.setAccessible(accessible);
        return commandMap;
    }

    public Settings getConfigFile() {
        return config;
    }

    public Settings getMessages() {
        return messages;
    }

    public static NameMC getNameMC() {
        return nameMC;
    }
}