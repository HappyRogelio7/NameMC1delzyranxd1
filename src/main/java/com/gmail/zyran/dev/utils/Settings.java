package com.gmail.zyran.dev.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Settings {

    private final File file;
    private FileConfiguration configuration = null;

    public Settings(Plugin plugin, String name) {
        this.file = new File(plugin.getDataFolder(), name + ".yml");
        if (!this.file.exists()) {
            try {
                this.file.getParentFile().mkdir();
                this.file.createNewFile();
                Reader reader = new InputStreamReader(plugin.getResource(name + ".yml"), StandardCharsets.UTF_8);
                YamlConfiguration configuration = YamlConfiguration.loadConfiguration(reader);
                configuration.options().copyDefaults();
                configuration.save(file);
            } catch (Exception exception) {
                plugin.getLogger().warning(exception.getMessage());
            }
        }
        this.configuration = YamlConfiguration.loadConfiguration(file);
    }

    public boolean createSection(String path) {
        configuration.createSection(path);
        return save();
    }

    public void set(String path, Object obj) {
        configuration.set(path, obj);
        save();
    }

    public boolean contains(String path) {
        return configuration.contains(path);
    }

    public Object get(String path) {
        return configuration.get(path);
    }

    public double getDouble(String path) {
        return configuration.getDouble(path);
    }

    public int getInt(String path) {
        return configuration.getInt(path);
    }

    public String getString(String path) {
        return configuration.getString(path);
    }

    public String getColouredString(String path) {
        String message;
        if (!configuration.isSet(path)) {
            message = "&cMsg not found | &e" + path;
            set(path, message);
        } else message = configuration.getString(path);
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public boolean getBoolean(String path) {
        return configuration.getBoolean(path);
    }

    public List<String> getStringList(String path) {
        return configuration.getStringList(path);
    }

    public List<String> getColouredStringList(String path) {
        List<String> f = new ArrayList<>();
        for (String stringList : configuration.getStringList(path)) {
            f.add(ChatColor.translateAlternateColorCodes('&', stringList));
        }
        return f;
    }

    public ConfigurationSection getConfigurationSection(String path) {
        return configuration.getConfigurationSection(path);
    }

    public Long getLong(String path) {
        return configuration.getLong(path);
    }

    public boolean isSet(String path) {
        return configuration.isSet(path);
    }

    public boolean save() {
        try {
            configuration.save(this.file);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean reload() {
        try {
            configuration.load(this.file);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}