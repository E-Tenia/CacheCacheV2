package fr.ethan.cachecache.Mains;

import fr.ethan.cachecache.Configs.GameConfig;
import fr.ethan.cachecache.GameElements.GameCycle;

import org.bukkit.plugin.java.JavaPlugin;
import fr.ethan.cachecache.Commands.UserCommands;
import fr.ethan.cachecache.Commands.AdminCommands;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CacheCache extends JavaPlugin {
    public static CacheCache plugin;
    public static ArrayList<GameCycle> gamelist = new ArrayList<GameCycle>();

    // name , task
    public static Map<String, BukkitRunnable> gameQueue = new HashMap<>();

    String[] userCommandes = { "test" , "cc" };
    String[] adminCommandes = { "acc" };

    @Override
    public void onEnable() {
        plugin = this;
        System.out.println("Lancement de CacheCache");

        //on initialise les commandes
        for (int i = 0; i < userCommandes.length ; i++) { getCommand(userCommandes[i]).setExecutor(new UserCommands()); };
        for (int i = 0; i < adminCommandes.length ; i++) { getCommand(adminCommandes[i]).setExecutor(new AdminCommands()); };

        //config.yml
        GameConfig.createConfig(plugin, "config.yml",false);//TODO : utiliser la méthode qui le créé par défaut ?
        //games directory
        new File(plugin.getDataFolder() + File.separator + "games").mkdirs();
        //usercaches directory
        new File(plugin.getDataFolder() + File.separator + "usercaches").mkdirs();
    }

    @Override
    public void onDisable() {
        System.out.println("Arrêt de CacheCache");
    }

    public static void printGameQueue()
    {
        gameQueue.entrySet().forEach(entry->{
            System.out.println(entry.getKey() + " " + entry.getValue());
        });
    }
}
