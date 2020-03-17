package fr.ethan.cachecache.GameElements;

import fr.ethan.cachecache.Mains.CacheCache;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;

public class EventManager implements Listener {
    private CacheCache plugin = CacheCache.plugin;
    public GameCycle cycle;
    public Team hiders;
    public Team seekers;
    public List<Player> players;
    
    public EventManager(GameCycle cycle, Team hideteam, Team seekteam, List<Player> playerList){
       hiders = hideteam;
       seekers = seekteam;
       players = playerList;

       this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    //GESTION DES EVENEMENTS BASIQUES DE GAMEPLAY
    @EventHandler
    public void onRegainHealth(EntityRegainHealthEvent event) {
    	Player player = null;
    	RegainReason reason = event.getRegainReason();
    	if(event.getEntity() instanceof Player) {
    		player = Bukkit.getPlayerExact(event.getEntity().getName());
    	}
    	else {
    		return;
    	}
    	//si joueur est en jeu et raison est sati�t�
    	if(reason == RegainReason.SATIATED /*&& joueur est en jeu*/) {
    		event.setCancelled(true);
    	}
    }

    @EventHandler
    public void onFoodLvlChange(FoodLevelChangeEvent event){
    	Player player = null;
    	if(event.getEntity() instanceof Player) {
    		player = Bukkit.getPlayerExact(event.getEntity().getName());
    	}
    	else {
    		return;
    	}
    	//si joueur est en jeu et foodlevel est au max
    	if(player.getFoodLevel() == 20 /*&& joueur est en jeu*/) {
    		event.setCancelled(true);
    	}
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
    	Player player = event.getPlayer();
    	//si joueur est en jeu
    	if(true/*joueur est en jeu*/) {
    		event.setCancelled(true);
    	}
    }

    @EventHandler
    public void onPickupItem(EntityPickupItemEvent event) {
    	Player player = null;
    	if(event.getEntity() instanceof Player) {
    		player = Bukkit.getPlayerExact(event.getEntity().getName());
    	}
    	else {
    		return;
    	}
    	//si joueur est en jeu
    	if(true/*joueur est en jeu*/) {
    		event.setCancelled(true);
    	}
    }
    
    //GESTION DES INTERRACTIONS JOUEURS
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        Entity damager = event.getDamager();//entité ayant frappé
        String name1 = event.getDamager().getName();
        Entity ent = event.getEntity();//entité ayant été frappée
        String name2 = event.getEntity().getName();

        if(damager instanceof Player && ent instanceof Player) {//si les deux entités sont des joeurs on vérifie qu'elles sont dans une partie
            if(seekers.hasMember(Bukkit.getPlayerExact(name1)) && hiders.hasMember(Bukkit.getPlayerExact(name2))){//si c'est le cas on notifie au joueur frappé qu'il a été trouvé et au joueur frappant qu'il l'a trouvé ainsi qu'au reste des joueurs
                Bukkit.getPlayerExact(name2).sendMessage(ChatColor.RED + "Tu as été trouvé par "+ ChatColor.GOLD + name1);
                Bukkit.getPlayerExact(name1).sendMessage(ChatColor.GREEN + "Tu as trouvé "+ ChatColor.GOLD + name2);
                for(Player p : players) {
                    p.sendMessage("[" + ChatColor.DARK_AQUA + "CacheCache" + ChatColor.RESET + "] : " + ChatColor.RED + "" + Bukkit.getPlayerExact(name2).getName() + ChatColor.GOLD +  " as été trouvé par " + ChatColor.BLUE + Bukkit.getPlayerExact(name1).getName());
                }
                hiders.removePlayer(Bukkit.getPlayerExact(ent.getName()));
                if(hiders.isEmpty()) {//si tous les hiders on été trouvés on le notifie aux joueurs, on vide les listes restantes et on arrete le chronomètre
                    for (Player p : players) {
                        p.setGameMode(GameMode.SURVIVAL);
                        p.sendMessage("[" + ChatColor.DARK_AQUA + "CacheCache" + ChatColor.RESET + "] :" + ChatColor.GOLD + " Tous les hiders ont été trouvé, fin de la partie !");
                    }
                    cycle.cancelGame();
                }
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        String msg = event.getMessage();
        System.out.println(hiders.getMembers());
        System.out.println(seekers.getMembers());
        System.out.println(players);
        if(hiders.hasMember(player)){
            event.setCancelled(true);
            for(Player p : hiders.getMembers()){
                p.sendMessage(ChatColor.RED + player.getName() + ChatColor.RESET + " : " + msg);
            }
        }
        else if(seekers.hasMember(player)){
            event.setCancelled(true);
            for(Player p : seekers.getMembers()){
                p.sendMessage(ChatColor.BLUE + player.getName() + ChatColor.RESET + " : " + msg);
            }
        }
        else if(players.contains(player)){
            event.setCancelled(true);
            for(Player p : players){
                p.sendMessage(player.getName() + " : " + msg);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        event.getPlayer().sendMessage("EventManager active");
    }
    
    //GESTION DES MOUVEMENTS ET DES POSITIONS
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
    	
    }
    
    //GESTION DES MORTS ET DES RESPAWN
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
    	
    }

    @EventHandler
    public void onRespawn(EntityResurrectEvent event) {
    	
    }
}
