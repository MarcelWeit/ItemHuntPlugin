package weitma.itemHuntPlugin.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public class Team {

    private final HashSet<UUID> teamMembers;
    private final String teamName;
    private final ChatColor chatColor;

    public Team(String teamName, ChatColor chatColor){
        this.teamName = teamName;
        teamMembers = new HashSet<>();
        this.chatColor = chatColor;
    }

    public void addPlayer(UUID player){
        Bukkit.getLogger().info("[InTeamClass] Adding player " + player + " to team " + teamName);
        teamMembers.add(player);
    }

    public void removePlayer(UUID player){
        teamMembers.remove(player);
    }

    public HashSet<UUID> getTeamMembers(){
        return teamMembers;
    }

    public String getTeamName(){
        return teamName;
    }

    public int getPlayerCount(){
        Bukkit.getLogger().info("[InTeamClass] Number of players in team " + teamName + ": " + teamMembers.size());
        return teamMembers.size();
    }

    public void resetTeam() {
        teamMembers.clear();
    }

    public String toString(){
        return teamName;
    }

    public ChatColor getChatColor(){
        return chatColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Team team)) return false;
        return Objects.equals(teamMembers, team.teamMembers) && Objects.equals(teamName, team.teamName) && chatColor == team.chatColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamMembers, teamName, chatColor);
    }
}
