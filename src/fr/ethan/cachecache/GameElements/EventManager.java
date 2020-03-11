package fr.ethan.cachecache.GameElements;

import fr.ethan.cachecache.Mains.CacheCache;

import org.bukkit.Bukkit;
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

public class EventManager implements Listener {
    private CacheCache plugin;// = CacheCache.plugin;
    public boolean cancel;
    
    public EventManager(){
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
    	//si joueur est en jeu et raison est satiété
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
