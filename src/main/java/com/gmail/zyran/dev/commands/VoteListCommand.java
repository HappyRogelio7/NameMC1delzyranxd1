package com.gmail.zyran.dev.commands;

import com.github.kevinsawicki.http.HttpRequest;
import com.gmail.zyran.dev.NameMC;
import com.google.gson.Gson;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class VoteListCommand extends Command {

    private final Gson gson = new Gson();

    public VoteListCommand(String name) {
        super(name);
    }

    public boolean execute(CommandSender sender, String aliases, String[] args) {

        if (sender instanceof Player && !sender.hasPermission(NameMC.getNameMC().getConfigFile().getString("Server.permission"))) {
            sender.sendMessage(NameMC.getNameMC().getMessages().getColouredString("General.noPermission"));
            return false;
        }

        String linkAPI = (NameMC.getNameMC().getConfigFile().getString("Server.Links.likes") == null) ? "https://api.namemc.com/server/<server>/likes" : NameMC.getNameMC().getConfigFile().getString("Server.Links.likes");
        String serverIP = (NameMC.getNameMC().getConfigFile().getString("Server.ip") == null) ? "" : NameMC.getNameMC().getConfigFile().getString("Server.ip");

        String finalURL = linkAPI.replace("<server>", serverIP);

        try {
            HttpRequest httpRequest = HttpRequest.get(new URL(finalURL));
            ArrayList list = gson.fromJson(httpRequest.body(), ArrayList.class);

            sender.sendMessage(NameMC.getNameMC().getMessages().getColouredString("General.numberPlayersVotedForTheServer")
                    .replaceAll("<serverName>", NameMC.getNameMC().getConfigFile().getColouredString("Server.serverName"))
                    .replaceAll("<votes>", String.valueOf(list.size()))
            );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return true;
    }
}