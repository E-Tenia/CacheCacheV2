package fr.ethan.cachecache.Listeners;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Team {
    public static String name;

    public Team(String initName) {
        name = initName;
        ArrayList<Player> members = new ArrayList<Player>();
    }

    public static boolean isMember(){
        return false;
    }
}