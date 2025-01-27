package weitma.itemHuntPlugin.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import weitma.itemHuntPlugin.ItemHuntPlugin;

import java.io.File;

public class ResetWorldCommand implements CommandExecutor {

    private final ItemHuntPlugin plugin;

    public ResetWorldCommand(ItemHuntPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /createworld <newWorldName>");
            return false;
        }

        String newWorldName = args[0];
        World newWorld = Bukkit.createWorld(new WorldCreator(newWorldName));
        if (newWorld == null) {
            sender.sendMessage(ChatColor.RED + "Failed to create the new world.");
            return false;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.teleport(newWorld.getSpawnLocation());
        }

        World oldWorld = Bukkit.getWorlds().getFirst(); // Assuming the first world is the old one
        if (oldWorld != null) {
            Bukkit.unloadWorld(oldWorld, false);
            deleteWorld(oldWorld.getWorldFolder());
        }

        sender.sendMessage(ChatColor.GREEN + "New world created and players teleported. Old world deleted.");
        return true;
    }

    private void deleteWorld(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteWorld(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }
        path.delete();
    }
}
