package com.gmail.zyran.dev;

import com.gmail.zyran.dev.commands.VoteCommand;
import com.gmail.zyran.dev.commands.VoteListCommand;
import com.gmail.zyran.dev.utils.Settings;
import com.gmail.zyran.dev.utils.Util;
import org.bukkit.plugin.java.JavaPlugin;

public class NameMC extends JavaPlugin {

    private static Settings config, messages;

    public NameMC() {
        config = new Settings(this, "config");
        messages = new Settings(this, "messages");
    }

    public void onEnable() {
        String commandVoteList = (NameMC.config.getString("Commands.voteList") == null) ? "votelist" : NameMC.config.getString("Commands.voteList");
        String commandCheckVote = (NameMC.config.getString("Commands.checkVote") == null) ? "checkvote" : NameMC.config.getString("Commands.checkVote");
        try {
            Util.getCommandMap().register(commandVoteList, new VoteListCommand(commandVoteList));
            Util.getCommandMap().register(commandCheckVote, new VoteCommand(commandCheckVote));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Settings getConfiguration() {
        return config;
    }

    public static Settings getMessages() {
        return messages;
    }
}