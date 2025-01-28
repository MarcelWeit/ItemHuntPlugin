package weitma.itemHuntPlugin.Utils;

import java.util.ArrayList;
import java.util.UUID;

public class Team {

    private ArrayList<UUID> teamMembers;
    private String teamName;

    public Team(String teamName){
        this.teamName = teamName;
        teamMembers = new ArrayList<>();
    }

    public void addPlayer(UUID player){
        teamMembers.add(player);
    }

    public void removePlayer(UUID player){
        teamMembers.remove(player);
    }

    public ArrayList<UUID> getTeammembers(){
        return teamMembers;
    }

    public String getTeamName(){
        return teamName;
    }

    public void setTeamName(String teamName){
        this.teamName = teamName;
    }

    public boolean containsPlayer(UUID player){
        return teamMembers.contains(player);
    }

}
