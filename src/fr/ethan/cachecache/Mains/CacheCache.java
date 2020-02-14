package fr.ethan.cachecache.Mains;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import fr.ethan.cachecache.Commands.UserCommands;
import fr.ethan.cachecache.Configs.MainConfig;

public class CacheCache extends JavaPlugin {
    public static CacheCache plugin;
    public int[] spawnPosition;

    public static CacheCache getInstance() {
        return plugin;
    }

    //private final Config config = new Config();
    String[] userCommandes = { "test" , "cc" };

    @Override
    public void onEnable() {
        plugin = this;
        System.out.println("Lancement de CacheCache");

        //on initialise les commandes
        for (int i = 0; i < userCommandes.length ; i++) { getCommand(userCommandes[i]).setExecutor(new UserCommands()); };

		//on charge le fichier config.yml
        try {
            FileConfiguration cfg = MainConfig.loadConfigFile();
            if (cfg == null) {
                plugin = null;
                getServer().getPluginManager().disablePlugin((Plugin)this);
                return;
            }
            MainConfig.readConfig(cfg);
        } catch (Exception e) {
            e.printStackTrace();
            plugin = null;
            getServer().getPluginManager().disablePlugin((Plugin)this);
            return;
        }
    }

    @Override
    public void onDisable() {
        System.out.println("Arrêt de CacheCache");
    }
}
