package com.gmail.zyran.dev.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.UUID;
import java.util.stream.Collectors;

public class Util {

    public static void getString(String url, Callback callback) {
        try {
            callback.done(new BufferedReader(new InputStreamReader(new URL(url).openStream())).lines().collect(Collectors.joining()));
        } catch (IOException exception) {
            callback.exception(exception);
        }
    }

    public static CommandMap getCommandMap() throws NoSuchFieldException, IllegalAccessException {
        Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        boolean accessible = commandMapField.isAccessible();
        commandMapField.setAccessible(true);
        CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
        commandMapField.setAccessible(accessible);
        return commandMap;
    }

    public static UUID setDashedUUID(String uuidString) {
        if (uuidString != null) {
            String uuid = uuidString.replace("\"", "");
            return UUID.fromString(uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid
                    .substring(12, 16) + "-" + uuid
                    .substring(16, 20) + "-" + uuid.substring(20, 32));
        }
        return UUID.fromString(uuidString);
    }
}