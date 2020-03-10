package fr.ethan.cachecache.Mains;

import fr.ethan.cachecache.Configs.GameConfig;
import fr.ethan.cachecache.Listeners.GameCycle;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import fr.ethan.cachecache.Commands.UserCommands;
import fr.ethan.cachecache.Commands.AdminCommands;

import java.io.File;

public class CacheCache extends JavaPlugin implements Listener {
    public static CacheCache plugin;

    String[] userCommandes = { "test" , "cc" };
    String[] adminCommandes = { "acc" };

    @Override
    public void onEnable() {
        plugin = this;
        System.out.println("Lancement de CacheCache");

        getServer().getPluginManager().registerEvents(this, this);

        //on initialise les commandes
        for (int i = 0; i < userCommandes.length ; i++) { getCommand(userCommandes[i]).setExecutor(new UserCommands()); };
        for (int i = 0; i < adminCommandes.length ; i++) { getCommand(adminCommandes[i]).setExecutor(new AdminCommands()); };

        //config.yml
        GameConfig.createConfig(plugin, "config.yml",false);
        //games directory
        new File(plugin.getDataFolder() + File.separator + "games").mkdirs();
        //usercaches directory
        new File(plugin.getDataFolder() + File.separator + "usercaches").mkdirs();

        //tests

    }

    @Override
    public void onDisable() {
        System.out.println("Arrêt de CacheCache");
    }
}
