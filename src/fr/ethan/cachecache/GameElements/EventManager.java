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
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;

public class EventManager implements Listener {
    private CacheCache plugin = CacheCache.plugin;
    public static boolean cancel;
    public static boolean playable;
    public Team hiders;
    public Team seekers;
    public ArrayList<Player> players = new ArrayList<Player>();
    
    public EventManager(Team hideteam, Team seekteam){
       hiders = hideteam;
       seekers = seekteam;
       for(Player p : hiders.members) {
           players.add(p);
       }
       for(Player p2 : seekers.members) {
           players.add(p2);
       }
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
        if(playable) {
            if(damager instanceof Player && ent instanceof Player) {//si les deux entités sont des joeurs on vérifie qu'elles sont dans une partie
                if(seekers.hasMember(Bukkit.getPlayerExact(name1)) && hiders.hasMember(Bukkit.getPlayerExact(name2))){//si c'est le cas on notifie au joueur frappé qu'il a été trouvé et au joueur frappant qu'il l'a trouvé ainsi qu'au reste des joueurs
                    Bukkit.getPlayerExact(name2).sendMessage(ChatColor.YELLOW + "You got found by " + name1);
                    Bukkit.getPlayerExact(name1).sendMessage(ChatColor.GREEN + "You found " + name2);
                    for(Player p : players) {
                        p.sendMessage(ChatColor.RED + "" + Bukkit.getPlayerExact(name2).getName() + " has been found by " + Bukkit.getPlayerExact(name1).getName());
                    }
                    hiders.remove(Bukkit.getPlayerExact(ent.getName()));
                }
            }
            if(hiders.isEmpty()) {//si tous les hiders on été trouvés on le notifie aux joueurs, on vide les listes restantes et on arrete le chronomètre
                for (Player p : players) {
                    p.setGameMode(GameMode.SURVIVAL);
                    p.sendMessage("All the hiders have been found, game over !");
                }
                cancel = true;
            }
        }
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
    	
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
