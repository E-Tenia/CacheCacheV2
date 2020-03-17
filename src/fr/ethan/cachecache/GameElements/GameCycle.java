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
import org.bukkit.event.HandlerList;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.*;

public class GameCycle {
    enum GameState {
        LOBBY,
        GAME;
    }

    private static CacheCache plugin = CacheCache.plugin;

    private HashMap<Player, CacheCachePlayer> registeredPlayer;
    private Team hiders;
    private Team seekers;
    public String name;
    public int lobbyTime;
    public int gameTime;
    public int time;
    public BukkitTask lobbyTimer;
    public BukkitTask gameTimer;
    public Location spawnPosition;
    public List<Player> playerList = new ArrayList<Player>();
    public ArrayList<String> gameList = new ArrayList<String>();
    public GameState state;

    public GameCycle(String initName) {
        //si lors de la commande on a pas renseigné de nom on lance une partie aléatoire, sinon partie indiquée
        if(initName == null) { name = randomGame(); }
        else { name = initName; }

        File file = new File(plugin.getDataFolder() + File.separator + "games", name + ".yml");
        FileConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);

        //teams
        this.hiders = new Team(this);
        this.seekers = new Team(this);

        //temps
        lobbyTime = 60/*yamlConfiguration.getInt("lobbyTime")*/; //TODO : rendre ça automatique
        gameTime = yamlConfiguration.getInt("Time");
        spawnPosition = yamlConfiguration.getLocation("Location");
        Double Limits = yamlConfiguration.getDouble("limits");
        Broadcast.gameLaunch(name);
        startLobby();

        state = GameState.LOBBY;
    }

    public int getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public void startLobby() {
        int cooldown = 10;
        time = lobbyTime + cooldown;

        lobbyTimer = new BukkitRunnable() {
            @Override
            public void run() {
                System.out.println("LOBBY");
                if(time == 0 /*&& GameState.LOBBY*/) {
                    if(playerList.size() < 2){
                        for(Player p : playerList) { p.sendMessage(ChatColor.RED+"Pas assez de joueurs pour commencer la partie."); }
                        cancel();
                    } else {
                        cancel();
                        startGame();
                    }
                }
                ActionBarAPI.sendActionBarToAllPlayers(""+time,-1); //TODO : remplacer par le scoreboard (uniquement sur les joueurs ingame)
                time--;
            }
        }.runTaskTimer(plugin, 0,20);
    }

    public void startGame() {
        state = GameState.GAME;

        int cooldown = 10;
        time = gameTime + cooldown;

        playerList = random(playerList);

        for (Player player : playerList) {
            if (!seekers.hasMember(player) && !hiders.hasMember(player)) {
                if (seekers.getMembers().size() >= hiders.getMembers().size()) {
                    setTeamHiders(player);
                }
                else {
                    setTeamSeekers(player);
                }
            }
        }

        for(Player p : playerList) {
            p.sendMessage("La partie est sur le point de commencer.");
            p.setNoDamageTicks(20*60);
            if(hiders.hasMember(p) == true) {
                p.sendMessage("Tu es "+ChatColor.BLUE+"hider "+ChatColor.RESET+"pour cette partie.");//TODO : changer par titleAPI ?
            } else if(seekers.hasMember(p) == true) {
                p.sendMessage("Tu es "+ChatColor.RED+"seeker "+ChatColor.RESET+"pour cette partie.");//TODO : changer par titleAPI ?
                p.addPotionEffect((new PotionEffect(PotionEffectType.BLINDNESS, 20*60, 1)));
                p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW, 20*60, 100)));
                p.addPotionEffect((new PotionEffect(PotionEffectType.JUMP, 20*60, 200)));
            }
            p.teleport(spawnPosition);
            p.setGameMode(GameMode.ADVENTURE);
        }

        EventManager em = new EventManager(this,hiders,seekers,playerList);

        gameTimer = new BukkitRunnable() {
            @Override
            public void run() {
                System.out.println("GAME");
                if(time == gameTime){
                    System.out.println("après le if");
                } else if(time == 0) {
                    for(Player p : playerList) {
                        p.setGameMode(GameMode.SURVIVAL);
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
                ActionBarAPI.sendActionBarToAllPlayers(""+time,-1); //TODO : remplacer par le scoreboard (uniquement sur les joueurs ingame)
                time--;
            }
        }.runTaskTimer(plugin, 0,20);
    }

    public static void seePlayerList(List<Player> list){ //TODO : supprimer après correction  equipes
        for(Player p: list) {
            System.out.println(p.getName());
        }
    }

    public String randomGame() {
        gameList = new ArrayList<String>(Arrays.asList(GameConfig.listGame()));

        for(int i = 0; i < gameList.size(); i++){
            gameList.set(i,gameList.get(i).substring(0,gameList.get(i).lastIndexOf(".")));
        }

        gameList.removeAll(CacheCache.gameQueue.keySet());

        int min = 0, max = gameList.size();
        int r = min + (int)(Math.random() * ((max - min)));

        return gameList.get(r);
    }
    
    public static List<Player> random(List<Player> list){
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

    public void setTeamSeekers(Player player) {
        if (seekers.hasMember(player)) {
            player.sendMessage(ChatColor.YELLOW + "Vous êtes déjà dans l'équipe" + ChatColor.BLUE + " seekers");
            return;
        }
        if (hiders.getMembers().size() >= seekers.getMembers().size() && seekers.getMembers().size() - hiders.getMembers().size() != 2) {
            if (hiders.hasMember(player)) { hiders.removePlayer(player); }
            seekers.addPlayer(player);
            player.sendMessage(ChatColor.YELLOW + "Vous avez rejoint la team " + ChatColor.BLUE + "seekers");

        } else
            player.sendMessage(ChatColor.RED + "Il y a trop de joueur dans cette équipe");
    }

    public void setTeamHiders(Player player) {
        if (hiders.hasMember(player)) {
            player.sendMessage(ChatColor.YELLOW + "Vous êtes déjà dans l'équipe" + ChatColor.RED + " hiders");
            return;
        }
        if (hiders.getMembers().size() <= seekers.getMembers().size() && hiders.getMembers().size() - seekers.getMembers().size() != 2) {
            if (seekers.hasMember(player)) { seekers.removePlayer(player); }
            hiders.addPlayer(player);
            player.sendMessage(ChatColor.YELLOW + "Vous avez rejoint la team " + ChatColor.RED + "hiders");
        } else
            player.sendMessage(ChatColor.RED + "Il y a trop de joueur dans cette équipe");
    }

    public void cancelGame() {
        //getHandlerList().unregisterAll(em);
        //HandlerList.unregisterAll(em);
        if(state == GameState.LOBBY) {
            lobbyTimer.cancel();
            if(this.lobbyTimer.isCancelled()){
                //CacheCache.gameQueue.replace(this.getName(),null);
                CacheCache.gameQueue.remove(this.getName());
            }
        } else if(state == GameState.GAME){
            gameTimer.cancel();
            if(this.gameTimer.isCancelled()){
                //CacheCache.gameQueue.get(this.getName());
                CacheCache.gameQueue.remove(this.getName());
            }
        }
    }

    public GameCycle getGameCycle() {
        return this;
    }

    public CacheCachePlayer getCacheCachePlayer(Player player){
        return this.registeredPlayer.get(player);
    }

    public HashMap<Player, CacheCachePlayer> getRegisteredPlayer() {
        return registeredPlayer;
    }

	public void printPlayerList(String gameName, Player p) {
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

    public void removePlayer(Player p) {
        playerList.remove(p);
        try{
            PlayerManager.restoreInventory(p);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void addPlayer(Player p) {
    	playerList.add(p);
        try {

            PlayerManager.saveInventory(p);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
