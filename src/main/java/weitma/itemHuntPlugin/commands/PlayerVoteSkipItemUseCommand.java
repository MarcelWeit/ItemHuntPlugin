package weitma.itemHuntPlugin.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import weitma.itemHuntPlugin.ItemHuntPlugin;
import weitma.itemHuntPlugin.Utils.Team;
import weitma.itemHuntPlugin.Utils.TeamManager;

import java.util.HashMap;
import java.util.Map;

public class PlayerVoteSkipItemUseCommand implements CommandExecutor {

    private final ItemHuntPlugin plugin;

    private HashMap<Team, HashMap<Player, Boolean>> votes = new HashMap<>();
    private HashMap<Team, Player> voteInitiators = new HashMap<>();

    public PlayerVoteSkipItemUseCommand(ItemHuntPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player player) {
            Team team = TeamManager.getInstance().getTeamOfPlayer(player.getUniqueId());
            switch (command.getName().toLowerCase()) {
                case "playervoteskipitem":
                    HashMap<Player, Boolean> map = new HashMap<>();
                    map.put(player, true);
                    votes.put(team, map);
                    voteInitiators.put(team, player);

                    team.getTeamMembers().forEach(teamMember -> {
                        Player teamPlayer = plugin.getServer().getPlayer(teamMember);
                        assert teamPlayer != null;

                        Material itemToCollect = plugin.getItemToCollect(team);
                        String itemName = ItemHuntPlugin.beautifyMaterialName(itemToCollect.name());

                        ChatColor teamColor = team.getChatColor();
                        teamPlayer.sendMessage(teamColor + player.getName() + ChatColor.RESET + " wants to skip " + ChatColor.DARK_RED + itemName);

                        if (teamMember != voteInitiators.get(team).getUniqueId()) {
                            TextComponent messsageSkipItem = new TextComponent("Skip " + itemName + "?");
                            TextComponent messageYes = new TextComponent(ChatColor.GREEN + " [Yes]");
                            TextComponent messageNo = new TextComponent(ChatColor.RED + " [No]");

                            messageYes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                    "/voteskipitemyes"));
                            messageNo.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                    "/voteskipitemno"));

                            messsageSkipItem.addExtra(messageYes);
                            messsageSkipItem.addExtra(messageNo);
                            teamPlayer.spigot().sendMessage(messsageSkipItem);
                        }
                    });
                    checkIfEveryoneVoted(team);
                    break;
                case "voteskipitemyes":
                    votes.get(team).put(player, true);
                    team.getTeamMembers().forEach(teamMember -> {
                        plugin.getServer().getPlayer(teamMember).sendMessage(player.getName() + " voted Yes!");
                    });
                    checkIfEveryoneVoted(team);
                    break;
                case "voteskipitemno":
                    votes.get(team).put(player, false);
                    team.getTeamMembers().forEach(teamMember -> {
                        plugin.getServer().getPlayer(teamMember).sendMessage(player.getName() + " voted No!");
                    });
                    checkIfEveryoneVoted(team);
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    private void checkIfEveryoneVoted(Team team) {
        int amountOfPlayersInTeam = TeamManager.getInstance().getPlayersInTeam(team).size();
        int amountOfVotesInTeam = votes.get(team).size();

        if (amountOfPlayersInTeam == amountOfVotesInTeam) {
            String itemToSkip = ItemHuntPlugin.beautifyMaterialName(plugin.getItemToCollect(team).name());
            StringBuilder messageNoSkip = new StringBuilder(itemToSkip + " was " + ChatColor.RED + "not skipped. The following players voted No: ");
            String messageSkip = itemToSkip + " was " + ChatColor.GREEN + "skipped!";
            boolean allVotedYes = true;

            for (Map.Entry<Player, Boolean> entry : votes.get(team).entrySet()) {
                if (!entry.getValue()) {
                    messageNoSkip.append(entry.getKey().getName()).append(" ");
                    allVotedYes = false;
                }
            }

            if (allVotedYes) {
                Player voteInitiator = voteInitiators.get(team);
                team.getTeamMembers().forEach(teamMember -> {
                    Player teamPlayer = plugin.getServer().getPlayer(teamMember);
                    assert teamPlayer != null;
                    teamPlayer.sendMessage(messageSkip);
                });

                // Reduce the amount of skip items in the inventory of the vote initiator by one
                ItemStack[] items = voteInitiator.getInventory().getContents();
                for (ItemStack item : items) {
                    if (item != null && item.getItemMeta() != null && item.getItemMeta().hasCustomModelData() &&
                            item.getItemMeta().getCustomModelData() == ItemHuntPlugin.SKIPITEM_ID) {
                        item.setAmount(item.getAmount() - 1);
                        break;
                    }
                }
                plugin.skipItemToCollect(voteInitiator.getUniqueId());

            } else {
                team.getTeamMembers().forEach(teamMember -> {
                    Player teamPlayer = plugin.getServer().getPlayer(teamMember);
                    assert teamPlayer != null;
                    teamPlayer.sendMessage(messageNoSkip.toString());
                });
            }

            voteInitiators.remove(team);
            votes.get(team).clear();
        }
    }
}
