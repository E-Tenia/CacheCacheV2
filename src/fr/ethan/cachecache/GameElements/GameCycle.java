package fr.ethan.cachecache.GameElements;

import fr.ethan.cachecache.Configs.GameConfig;
import fr.ethan.cachecache.Mains.CacheCache;
import fr.ethan.cachecache.Utils.ActionBarAPI.ActionBarAPI;
import fr.ethan.cachecache.Utils.Broadcast;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;

public class GameCycle extends BukkitRunnable {
    private static CacheCache plugin = CacheCache.plugin;

    public static String name;
    public static int lobbyTime;
    public static int gameTime;
    public static int time;
    public static Location spawnPosition;

    public GameCycle(String initName) {
        if(initName == null) { name = randomGame(); }
        else { name = initName + ".yml"; }

        File file = new File(plugin.getDataFolder() + File.separator + "games", name);
        FileConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);

        lobbyTime = 20/*yamlConfiguration.getInt("lobbyTime")*/; //rendre ça automatique
        gameTime = yamlConfiguration.getInt("Time");
        spawnPosition = yamlConfiguration.getLocation("Location");
        time = lobbyTime + gameTime + 10;

       EventManager EventManager = new EventManager();
    }

    /*public GameCycle(){
        name = "default";
        lobbyTime = 40;
        gameTime = 40;
        totalTime = lobbyTime + gameTime + 10;
        time = totalTime;
    }*/

    @Override
    public void run() {
        if(time == lobbyTime + gameTime + 10){
            System.out.println(name);
            ArrayList<Player> players = new ArrayList<Player>();
        }

        if(time == (gameTime + 10)) {
            Team hiders = new Team("hiders");
            Team seekers = new Team("seekers");
            Broadcast.broadcaster("Fin du lobby - téléportation à la partie"+time);
        }

        if(time == (gameTime + 5)) {
            Broadcast.broadcaster("Début de la partie"+time);
        }

        if(time == 0) { cancel(); }
        ActionBarAPI.sendActionBarToAllPlayers(""+time,-1);
        time--;
    }

    public static String randomGame() {
        String[] gameList = GameConfig.listGame();

        int min = 0, max = gameList.length;
        int r = min + (int)(Math.random() * ((max - min)));

        return gameList[r];
    }

    /*public static void readGameConfig(String configName) { //ici on lit le X.yml et on stock dans les variables
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        File file = new File(CacheCache.plugin.getDataFolder(), configName);

        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        //spawnPosition = stringToDouble(yamlConfiguration.getString("position-spawn", "0,0,0").split(","));
    }*/
}
