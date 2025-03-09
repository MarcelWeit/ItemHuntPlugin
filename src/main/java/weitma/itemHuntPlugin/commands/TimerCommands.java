package weitma.itemHuntPlugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import weitma.itemHuntPlugin.Utils.InGameTimer;

import java.util.List;

public class TimerCommands implements CommandExecutor, TabCompleter {

    private InGameTimer inGameTimer;

    public TimerCommands(InGameTimer inGameTimer) {
        this.inGameTimer = inGameTimer;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (sender instanceof Player player) {
            String usage = ChatColor.RED + "Usage: /timer <resume/pause>";
            if (args.length != 1) {
                player.sendMessage(usage);
                return false;
            }

            String arg = args[0];

            if (arg.equalsIgnoreCase("resume")) {
                inGameTimer.resumeTimer();
            } else if (arg.equalsIgnoreCase("pause")) {
                inGameTimer.pauseTimer();
            } else {
                player.sendMessage(usage);
                return false;
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        int length = args.length;

        if (length == 1) {
            return List.of("pause", "resume");
        }
        return null;
    }
}
