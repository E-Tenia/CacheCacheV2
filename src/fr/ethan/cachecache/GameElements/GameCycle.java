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
import java.util.Arrays;

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
    public static ArrayList<String> gameList = new ArrayList<String>();
    public static boolean hasStarted;

    public GameCycle(String initName) {
        //on initialise le EventManager
        EventManager em = new EventManager(hiders,seekers);

        //si lors de la commande on a pas renseigné de nom on lance une partie aléatoire, sinon partie indiquée
        if(initName == null) {
            name = randomGame(); }
        else { name = initName; }

        File file = new File(plugin.getDataFolder() + File.separator + "games", name + ".yml");
        FileConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);

        //temps
        lobbyTime = 20/*yamlConfiguration.getInt("lobbyTime")*/; //TODO : rendre ça automatique
        gameTime = yamlConfiguration.getInt("Time");
        spawnPosition = yamlConfiguration.getLocation("Location");
        Double Limits = yamlConfiguration.getDouble("limits");
        time = lobbyTime + gameTime + 10;

        hasStarted = false;
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
            hasStarted = true;
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
            if(playerList.size() < 2){
                Broadcast.broadcaster(ChatColor.RED+"Pas assez de joueurs pour commencer la partie.");
                cancel();
            }
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
            hasStarted = false;
            CacheCache.gameQueue.remove(this);
            cancel();
        }
        ActionBarAPI.sendActionBarToAllPlayers(""+time,-1); //TODO : remplacer par le scoreboard (uniquement sur les joueurs ingame)
        time--;
    }

    public static String randomGame() {
        gameList = new ArrayList<String>(Arrays.asList(GameConfig.listGame()));

        for(int i = 0; i < gameList.size(); i++){
            gameList.set(i,gameList.get(i).substring(0,gameList.get(i).lastIndexOf(".")));
        }

        gameList.removeAll(CacheCache.gameQueue.keySet());

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
            p.sendMessage(ChatColor.RED + "Cette partie n'est pas lancée.");
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
