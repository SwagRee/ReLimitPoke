package io.github.swagree.relimitpoke;

import io.github.swagree.relimitpoke.PokeListener.ListenerBind;
import io.github.swagree.relimitpoke.PokeListener.ListenerBreed;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {
    public static Main plugin;
    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("§7[ReLimitPoke] §b作者§fSwagRee §cQQ:§f352208610");
        Bukkit.getPluginManager().registerEvents(new ListenerBreed(),this);
        Bukkit.getPluginManager().registerEvents(new ListenerBind(),this);

        getCommand("rlp").setExecutor(new CommandLimit());


        getDataFolder().mkdirs();

        this.saveDefaultConfig();

        this.reloadConfig();

        File blackListFile = new File(getDataFolder(), "LimitPoke.yml");

        if (!blackListFile.exists()) {
            saveResource("LimitPoke.yml", false);
        }


        plugin = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
