package me.zyran.namemc;

import me.zyran.namemc.commands.VoteCommand;
import me.zyran.namemc.commands.VoteListCommand;
import me.zyran.namemc.utils.Settings;
import me.zyran.namemc.utils.Util;
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