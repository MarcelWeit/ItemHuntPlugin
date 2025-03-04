package weitma.itemHuntPlugin.Commands.nightskip;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import weitma.itemHuntPlugin.Utils.TeamManager;

public class PlayerVoteSkipNightTrueCommand implements CommandExecutor {

    NightSkipVoteCommand nightSkipVoteCommand;

    public PlayerVoteSkipNightTrueCommand(NightSkipVoteCommand nightSkipVoteCommand) {
        this.nightSkipVoteCommand = nightSkipVoteCommand;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player player) {
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                ChatColor teamColor = TeamManager.getInstance().getTeamOfPlayer(player.getUniqueId()).getChatColor();
                onlinePlayer.sendMessage(teamColor + onlinePlayer.getName() + ChatColor.RESET + " has voted to skip the night!");
            });
            nightSkipVoteCommand.vote(player, true);
        }

        return true;
    }
}
