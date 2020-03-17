package fr.ethan.cachecache.GameElements;


import fr.ethan.cachecache.Mains.CacheCache;
import org.bukkit.entity.Player;


public class CacheCachePlayer {
    private Team team;
    private CacheCache plugin;
    private Player player;
    private boolean isInGame;

    public CacheCachePlayer(Player player, CacheCache plugin) {
        this.player = player;
        this.plugin = plugin;
    }

    public Player getPlayer() {
        return player;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public boolean hasTeam(){
        return team != null;
    }
}