package fr.ethan.cachecache.GameElements;

import fr.ethan.cachecache.Configs.GameConfig;
import fr.ethan.cachecache.Mains.CacheCache;
import fr.ethan.cachecache.Utils.ActionBarAPI.ActionBarAPI;
import fr.ethan.cachecache.Utils.Broadcast;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;

public class GameCycle extends BukkitRunnable {
    private static CacheCache plugin = CacheCache.plugin;

    public String name;
    public int lobbyTime;
    public static int gameTime;
    public static int time;
    public static Team hiders;
    public static Team seekers;
    public static Location spawnPosition;
    public static ArrayList<Player> playerList = new ArrayList<Player>();

    public GameCycle(String initName) {
        //on initialise le EventManager
        EventManager em = new EventManager();

        //si lors de la commande on a pas renseigné de nom on lance une partie aléatoire, sinon partie indiquée
        if(initName == null) { name = randomGame(); }
        else { name = initName; }

        File file = new File(plugin.getDataFolder() + File.separator + "games", name);
        FileConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);

        //temps
        lobbyTime = 20/*yamlConfiguration.getInt("lobbyTime")*/; //TODO : rendre ça automatique
        gameTime = yamlConfiguration.getInt("Time");
        spawnPosition = yamlConfiguration.getLocation("Location");
        time = lobbyTime + gameTime + 10;
    }

    public String getName() {
        return name;
    }

    @Override
    public void run() {
        if(time == lobbyTime + gameTime + 10){
            System.out.println(name);
        }

        if(time == (gameTime + 10)) {
            hiders = new Team("hiders");
            seekers = new Team("seekers");
            Broadcast.broadcaster("Fin du lobby - téléportation vers la partie"+time);
        }

        if(time <= (gameTime + 5) && time > gameTime) {
            Broadcast.broadcaster("Début de la partie dans "+time);
        }
        
        if(time == gameTime) {
        	//lancement partie
            ArrayList<Player> teams = random(playerList);
        	for(Player p : teams) {
        		if(seekers.getSize() >= hiders.getSize()) {
        			hiders.add(p);
        			p.setNoDamageTicks(20*60);
        		}
        		else {
        			seekers.add(p);
        			p.setNoDamageTicks(20*60);
        			p.addPotionEffect((new PotionEffect(PotionEffectType.BLINDNESS, 20*60, 1)));
					p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW, 20*60, 100)));
					p.addPotionEffect((new PotionEffect(PotionEffectType.JUMP, 20*60, 200)));
        		}
        		p.teleport(spawnPosition);
        		p.setGameMode(GameMode.ADVENTURE);
        	}
        }

        if(time == 0 /*|| EventManager.cancel*/) {
        	for(Player p : playerList) {
        		p.setGameMode(GameMode.SURVIVAL);
        		//TODO : restaurer inventaires
        	}
        	
        	if(!hiders.isEmpty()) {
        			//TODO : victoire hiders
        	}
        	
        	CacheCache.gamelist.remove(this);
        	cancel(); 
        }
        ActionBarAPI.sendActionBarToAllPlayers(""+time,-1);
        time--;
    }

    public static String randomGame() {
        String[] gameList = GameConfig.listGame();

        int min = 0, max = gameList.length;
        int r = min + (int)(Math.random() * ((max - min)));

        return gameList[r];
    }
    
    public ArrayList<Player> random(ArrayList<Player> list){
		int min = 0, max = list.size();
        int r = min + (int)(Math.random() * ((max - min) + 1));
        for(int i = 0; i < r; i++) {
            min = 0; 
            max = list.size() - 1;
            int nombreAleat = min + (int)(Math.random() * ((max - min) + 1));

            list.add(list.get(nombreAleat));
            list.remove(nombreAleat);
        }
       return list;
	}
    
    public void addPlayer(Player p) {
    	playerList.add(p);
    }
}
