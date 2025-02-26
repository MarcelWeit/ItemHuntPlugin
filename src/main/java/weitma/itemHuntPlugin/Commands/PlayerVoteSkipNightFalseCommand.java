package weitma.itemHuntPlugin.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerVoteSkipNightFalseCommand implements CommandExecutor {

    NightSkipVoteCommand nightSkipVoteCommand;

    public PlayerVoteSkipNightFalseCommand(NightSkipVoteCommand nightSkipVoteCommand) {
        this.nightSkipVoteCommand = nightSkipVoteCommand;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player player){
            nightSkipVoteCommand.vote(player, false);
        }

        return true;
    }
}
