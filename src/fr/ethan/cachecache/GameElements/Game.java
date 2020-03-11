package fr.ethan.cachecache.GameElements;

import fr.ethan.cachecache.Configs.GameConfig;
import fr.ethan.cachecache.Mains.CacheCache;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;

import static org.bukkit.Bukkit.getServer;

public class Game {
    private static CacheCache plugin = CacheCache.plugin;

    public String name;

    public static BukkitTask joinTime;
    //static final BukkitScheduler scheduler = Bukkit.getScheduler();

    //methodes d'écriture et de lecture
    public static void setGame(String name, String time, String limit, Location loc) {
        CacheCache plugin = CacheCache.plugin;

        File file = new File(plugin.getDataFolder() + File.separator + "games", name + ".yml");

        if(!file.exists()) { GameConfig.createConfig(plugin,name + ".yml",true); }

        FileConfiguration gameConfig = YamlConfiguration.loadConfiguration(file);

        gameConfig.set("Time",Integer.parseInt(time));
        gameConfig.set("Limit",limit);
        gameConfig.set("Location",loc);

        try {
            gameConfig.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void spawnGame(String name, Player player) {
        CacheCache plugin = CacheCache.plugin;

        File file = new File(plugin.getDataFolder() + File.separator + "games", name + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        Location loc = config.getLocation("Location");

        player.teleport(loc);
    }

    public static void removeGame(String name) {
        CacheCache plugin = CacheCache.plugin;

        File file = new File(plugin.getDataFolder() + File.separator + "games", name + ".yml");
        file.delete();
    }
}
