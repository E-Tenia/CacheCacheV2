package fr.ethan.cachecache.GameElements;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Team {
	
	public static ArrayList<Player> members = new ArrayList<Player>();;
    public static String name;

    public Team(String initName) {
        name = initName;
    }

    public boolean hasMember(Player p){
        return members.contains(p);
    }
    
    public boolean isNameVisible() {
    	boolean a = false;
    	for(Player p : members) {
    		if(p.isCustomNameVisible()) {
    			a = true;
    		}
    	}
    	return a;
    }
    
    public boolean isEmpty() {
    	return members.isEmpty();
    }
    
    public void add(Player p) {
    	members.add(p);
    }
    
    public void remove(Player p) {
    	members.remove(p);
    }
    
    public void setNameVisible(boolean y) {
    	for(Player p : members) {
    		p.setCustomNameVisible(y);
    	}
    }
    
    public String getName() {
    	return name;
    }
    
    public int getSize() {
    	return members.size();
    }
}