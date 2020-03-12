package fr.ethan.cachecache.GameElements;

import fr.ethan.cachecache.Configs.GameConfig;
import fr.ethan.cachecache.Mains.CacheCache;
import fr.ethan.cachecache.Utils.ActionBarAPI.ActionBarAPI;
import fr.ethan.cachecache.Utils.Broadcast;

import org.bukkit.ChatColor;
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
    public static boolean hasStarted;

    public GameCycle(String initName) {
        //on initialise le EventManager
        EventManager em = new EventManager();

        //si lors de la commande on a pas renseign� de nom on lance une partie al�atoire, sinon partie indiqu�e
        if(initName == null) {
            String g = randomGame();
            name = randomGame(); }
        else { name = initName; }

        File file = new File(plugin.getDataFolder() + File.separator + "games", name + ".yml");
        FileConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);

        //temps
        lobbyTime = 20/*yamlConfiguration.getInt("lobbyTime")*/; //TODO : rendre �a automatique
        gameTime = yamlConfiguration.getInt("Time");
        spawnPosition = yamlConfiguration.getLocation("Location");
        Double Limits = yamlConfiguration.getDouble("limits");
        time = lobbyTime + gameTime + 10;
    }

    public static int getTime() {
        return time;
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
            Broadcast.broadcaster("Fin du lobby - t�l�portation vers la partie"+time);
        }

        if(time <= (gameTime + 5) && time > gameTime) {
            Broadcast.broadcaster("D�but de la partie dans "+time);
        }
        
        if(time == gameTime) {
        	//lancement partie
            hasStarted = true;
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

        if(time == 0 || EventManager.cancel) {
        	for(Player p : playerList) {
        		p.setGameMode(GameMode.SURVIVAL);
        		CacheCache.inGame.remove(p);
                try {
                    PlayerManager.restoreInventory(p);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        	
        	if(!hiders.isEmpty()) {
        	    //TODO : victoire hiders
        	}

            CacheCache.gameQueue.remove(this);
            cancel();
        }
        ActionBarAPI.sendActionBarToAllPlayers(""+time,-1);
        time--;
    }

    public static String randomGame() {
        ArrayList<String> gameList = new ArrayList<String>();
        for(String game : GameConfig.listGame()){
            if(CacheCache.gameQueue.containsKey(game) && CacheCache.gameQueue.get(game).hasStarted){
                gameList.add(game);
            }
        }

        int min = 0, max = gameList.size();
        int r = min + (int)(Math.random() * ((max - min)));

        return gameList.get(r);
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

	public static void printPlayerList(String gameName, Player p) {
        if(gameName.isEmpty()){
            p.sendMessage(ChatColor.RED + "Il n'y a pas de partie en cours.");
        } else if(!CacheCache.gameQueue.containsKey(gameName)){
            p.sendMessage(ChatColor.RED + "Cette partie n'est pas lanc�.");
        } else {
            if(playerList.isEmpty()){
                p.sendMessage(ChatColor.RED + "Il n'y a pas de joueur dans cette partie.");
            } else {
                for(int i = 0; i < playerList.size(); i++) {
                    p.sendMessage(""+playerList.get(i));
                }
            }
        }
    }
    
    public void addPlayer(Player p) {
    	playerList.add(p);
    }
}
