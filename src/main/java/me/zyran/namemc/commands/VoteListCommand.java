package me.zyran.namemc.commands;

import com.google.gson.Gson;
import me.zyran.namemc.NameMC;
import me.zyran.namemc.utils.Callback;
import me.zyran.namemc.utils.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class VoteListCommand extends Command {

    private final Gson gson = new Gson();

    public VoteListCommand(String name) {
        super(name);
    }

    public boolean execute(CommandSender sender, String aliases, String[] args) {

        if (sender instanceof Player && sender.hasPermission(NameMC.getConfiguration().getString("Server.permission"))) {
            sender.sendMessage(NameMC.getMessages().getColouredString("General.noPermission"));
            return false;
        }

        String linkAPI = (NameMC.getConfiguration().getString("Server.Links.likes") == null) ? "https://api.namemc.com/server/<server>/likes" : NameMC.getConfiguration().getString("Server.Links.likes");
        String serverIP = (NameMC.getConfiguration().getString("Server.ip") == null) ? "" : NameMC.getConfiguration().getString("Server.ip");

        String finalURL = linkAPI.replace("<server>", serverIP);

        Util.getString(finalURL, new Callback<String>() {

            public void done(String response) {
                ArrayList list = gson.fromJson(response, ArrayList.class);

                sender.sendMessage(NameMC.getMessages().getColouredString("General.numberPlayersVotedForTheServer")
                        .replaceAll("<serverName>", NameMC.getConfiguration().getColouredString("Server.serverName"))
                        .replaceAll("<votes>", String.valueOf(list.size()))
                );
            }

            public void exception(Exception exception) {
                System.out.println(exception.getMessage());
            }
        });

        return true;
    }
}