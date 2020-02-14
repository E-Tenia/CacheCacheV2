package fr.ethan.cachecache.Configs;

import fr.ethan.cachecache.Utils.Broadcast;
import fr.ethan.cachecache.Utils.FileManager;
import fr.ethan.cachecache.Mains.CacheCache;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class MainConfig {

    public static CommandSender reloader;

    public static CacheCache plugin = CacheCache.plugin;

    public static MainConfig mainConfig = MainConfig.mainConfig;

    public static double[] spawnPosition;

    static boolean checkConfig() {
        File cfg = new File(plugin.getDataFolder(), "config.yml");
        if (!cfg.exists()) { //si le fichier n'existe pas on le créé
            Broadcast.log("§a§lIl n'y a pas de config.yml, on en créé un par défaut!");
            InputStream defconf = plugin.getResource("config.yml");
            if (defconf == null) {
                Broadcast.errorLog("Le config.yml par défaut ne peut être trouvé. Arrêt de CacheCache!");
                return false;
            }
            try {
                FileManager.saveFile(defconf, plugin.getDataFolder(), "config.yml", false);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static void readConfig(FileConfiguration config) { //ici on lit le config.yml et on stock dans les variables
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        File file = new File(CacheCache.plugin.getDataFolder(), "config.yml");

        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        spawnPosition = stringToDouble(yamlConfiguration.getString("position-spawn", "0,0,0").split(","));
        //spawnPosition = readString("position-spawn").split(",");
        //mainConfig.spawnPosition = stringToInt(spawnPosition);
    }

    public static FileConfiguration loadConfigFile() {
        File file = new File(CacheCache.plugin.getDataFolder(), "config.yml");
        if (!checkConfig()) {
            return null;
        }

        try {
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            if (cfg.contains("position-spawn")) {
                return (FileConfiguration)cfg;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    	/*if (plugin.languageReader != null) {
    		CacheCache.plugin.errorLog(plugin.languageReader.get("Error_YmlRead", new String[0]));
    	} else {
    		CacheCache.plugin.errorLog("config.yml introuvable, please make sure the file is in valid yml format (espaces correctes etc.)");
    	}*/
        return null;
    }

    /*public void loadkeys(){
        Utils.log("Chargement du fichier de configuration...");
        spawnPosition = readString("position-spawn").split(",");
        String[] safe2 = readString("safe2").split(",");
        String[] count1 = readString("count1").split(",");
        String[] count2 = readString("count2").split(",");
        String[] dq1 = readString("dq1").split(",");
        String[] dq2 = readString("dq2").split(",");
        plugin.safe1 = stringToInt(safe1);
        plugin.safe2 = stringToInt(safe2);
        plugin.count1 = stringToInt(count1);
        plugin.count2 = stringToInt(count2);
        plugin.dq1 = stringToInt(dq1);
        plugin.dq2 = stringToInt(dq2);
    }*/

    public static double[] stringToDouble(String[] source) { //utile pour le lecture de coordonnées
        double[] returnDouble = new double[3];
        for(int i=0;i<3;i++) {
            returnDouble[i] = Double.parseDouble(String.valueOf(Double.parseDouble(source[i])));
        }
        return returnDouble;
    }

    /*public void write(File file, String root, Object x) {
    	FileConfiguration config = load();
        try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }*/

}
