package weitma.itemHuntPlugin.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import weitma.itemHuntPlugin.ItemHuntPlugin;

import java.util.*;
import java.util.stream.Collectors;

public class ShowResultsCommand implements CommandExecutor {

    private final ItemHuntPlugin plugin;

    public ShowResultsCommand(ItemHuntPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        HashMap<UUID, ArrayList<Material>> itemsCollectedByPlayers = plugin.getItemsCollectedByPlayers();

        HashMap<UUID, Integer> scores = new HashMap<>();

        itemsCollectedByPlayers.forEach((uuid, materials) -> {
            scores.put(uuid, materials.size());
        });

        List<Map.Entry<UUID, Integer>> sortedScores = scores.entrySet().stream()
                .sorted(Map.Entry.<UUID, Integer>comparingByValue().reversed())
                .toList();

        // Display the sorted results
        sortedScores.forEach(entry -> {
            UUID playerUUID = entry.getKey();
            int score = entry.getValue();
            Player player = Bukkit.getPlayer(playerUUID);
            if (player != null) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.sendMessage(player.getName() + ": " + score);
                }
            }
        });

        return true;
    }
}
