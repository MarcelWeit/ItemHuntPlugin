package weitma.itemHuntPlugin.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class TeamManager {

    private static ArrayList<Team> teamList;

    public static final String[] availableTeams = {
            ChatColor.RED + "Team Red",
            ChatColor.BLUE + "Team Blue",
            ChatColor.GREEN + "Team Green",
            ChatColor.YELLOW + "Team Yellow",
            ChatColor.BLACK + "Team Black",
            ChatColor.DARK_PURPLE + "Team Purple",
            ChatColor.GOLD + "Team Orange",
            ChatColor.LIGHT_PURPLE + "Team Pink",
            ChatColor.WHITE + "Team White"
    };

    public static final ChatColor[] colors = {
            ChatColor.RED,
            ChatColor.BLUE,
            ChatColor.GREEN,
            ChatColor.YELLOW,
            ChatColor.BLACK,
            ChatColor.DARK_PURPLE,
            ChatColor.GOLD,
            ChatColor.LIGHT_PURPLE,
            ChatColor.WHITE
    };

    private static TeamManager instance;

    private TeamManager(){
        teamList = new ArrayList<>();
        for(int i=0; i<availableTeams.length; i++){
            teamList.add(new Team(availableTeams[i], colors[i]));
        }
    }

    public static TeamManager getInstance(){
        if(instance == null){
            instance = new TeamManager();
        }
        return instance;

    }

    public Team getTeamOfPlayer(UUID playerID){
        for(Team team : teamList){
            if(team.getTeamMembers().contains(playerID)){
                return team;
            }
        }
        return null;
    }

    public void joinTeam(UUID playerID, Team team){
        teamList.get(teamList.indexOf(team)).addPlayer(playerID);
    }

    public boolean isEveryoneInATeam(){
        int numberOfPlayersInTeams = 0;
        for(Team team : teamList){
            numberOfPlayersInTeams += team.getPlayerCount();
            Bukkit.getLogger().info("Number of players in teams: " + numberOfPlayersInTeams);
        }
        return numberOfPlayersInTeams == Bukkit.getOnlinePlayers().size();
    }

    public void resetTeams(){
        for(Team team : teamList){
            team.resetTeam();
        }
    }

    public ArrayList<Team> getTeamsWithPlayers(){
        ArrayList<Team> teamsWithPlayers = new ArrayList<>();
        for(Team team : teamList){
            Bukkit.getLogger().info("Team: " + team + " - Player Count " + team.getPlayerCount());
            if(team.getPlayerCount() > 0){
                teamsWithPlayers.add(team);
            }
        }
        Bukkit.getLogger().info("Teams with players: " + teamsWithPlayers);
        return teamsWithPlayers;
    }

    public HashSet<UUID> getPlayersInTeam(Team team){
        return teamList.get(teamList.indexOf(team)).getTeamMembers();
    }

    public Team getTeam(int index){
        return teamList.get(index);
    }

    public int getTeamIndex(String teamName) {
        for(int i=0; i<availableTeams.length; i++){
            if(teamName.equalsIgnoreCase(availableTeams[i])){
                return i;
            }
        }
        return -1;
    }
}
