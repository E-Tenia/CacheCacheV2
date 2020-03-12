package fr.ethan.cachecache.Configs;

import fr.ethan.cachecache.Utils.Broadcast;
import fr.ethan.cachecache.Utils.FileManager;
import fr.ethan.cachecache.Mains.CacheCache;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class GameConfig {
    private static CacheCache plugin = CacheCache.plugin;
    public static FileConfiguration config;

    public static void createConfig(CacheCache main, String fileName, Boolean gameConfig) {
        File file = new File("");
        //this.main = main;
        if(gameConfig == true) {
            file = new File(main.getDataFolder()+File.separator+"games", fileName);
        } else if (gameConfig == false) {
            file = new File(main.getDataFolder(), fileName);
        }
        if (!file.exists()) {
            Broadcast.log("§a§lIl n'y a pas de "+fileName+", on en créé un par défaut!");
            try {
                if(main.getResource(fileName) == null) {
                    file.createNewFile();
                } else {
                    InputStream defconf = main.getResource(fileName);
                    if(gameConfig == true) {
                       FileManager.saveFile(defconf, new File(main.getDataFolder() + File.separator + "games"), fileName, false);
                    } else if (gameConfig == false) {
                        FileManager.saveFile(defconf, main.getDataFolder(), fileName, false);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Broadcast.log("§a§lChargement de "+fileName);
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static String[] listGame() {
        File dir = new File(plugin.getDataFolder() + File.separator + "games");
        String[] gameConfigs = dir.list((d, name) -> name.endsWith(".yml"));

        return gameConfigs;
    }

    public static void printListGame(String[] gameList, Player p) {
        String inlineGameList = "";
        for(int i = 0; i < gameList.length; i++) {
            inlineGameList = inlineGameList + gameList[i].substring(0,gameList[i].lastIndexOf("."));
            if(i != gameList.length - 1){ inlineGameList = inlineGameList + ", "; }
        }
        p.sendMessage(inlineGameList);
    }

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
