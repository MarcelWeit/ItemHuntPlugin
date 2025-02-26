package weitma.itemHuntPlugin.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerVoteSkipNightTrueCommand implements CommandExecutor {

    NightSkipVoteCommand nightSkipVoteCommand;

    public PlayerVoteSkipNightTrueCommand(NightSkipVoteCommand nightSkipVoteCommand) {
        this.nightSkipVoteCommand = nightSkipVoteCommand;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player player){
            nightSkipVoteCommand.vote(player, true);
        }

        return true;
    }
}
