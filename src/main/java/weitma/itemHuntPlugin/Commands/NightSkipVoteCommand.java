package weitma.itemHuntPlugin.Commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class NightSkipVoteCommand implements CommandExecutor {

    private final HashMap<Player, Boolean> nightSkipVotes = new HashMap<>();
    private boolean voteRunning;
    private boolean playersVotedNo;

    public NightSkipVoteCommand() {
        voteRunning = false;
        playersVotedNo = false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        voteRunning = true;
        playersVotedNo = false;

        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
            TextComponent messsageSkipNight = new TextComponent("Skip the Night?");
            TextComponent messageYes = new TextComponent(ChatColor.GREEN + " [Yes]");
            TextComponent messageNo = new TextComponent(ChatColor.RED + " [No]");

            messageYes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                    "/voteskipnighttrue"));
            messageNo.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                    "/voteskipnightfalse"));

            messsageSkipNight.addExtra(messageYes);
            messsageSkipNight.addExtra(messageNo);
            onlinePlayer.spigot().sendMessage(messsageSkipNight);
        });

        return true;
    }

    private void checkIfEveryoneVoted() {
        if (nightSkipVotes.size() == Bukkit.getOnlinePlayers().size()) {
            ArrayList<Player> votedNo = new ArrayList<>();
            for (Player player : nightSkipVotes.keySet()) {
                if (!nightSkipVotes.get(player)) { // voted no
                    votedNo.add(player);
                    playersVotedNo = true;
                }
            }
            ;
            if (playersVotedNo) {
                StringBuilder message = new StringBuilder(ChatColor.RED + "The Night was not skipped. The following players voted No: ");
                for (int i = 0; i < votedNo.size(); i++) {
                    message.append(votedNo.get(i).getName());
                    if (i < votedNo.size() - 1) {
                        message.append(", ");
                    }
                }
                message.append(". Buuuuh!");
                Bukkit.getOnlinePlayers().forEach(
                        onlinePlayer -> onlinePlayer.sendMessage(message.toString()));
                nightSkipVotes.clear();
            } else {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.sendMessage("Everyone voted to skip the Night!");
                    onlinePlayer.sendMessage(ChatColor.GREEN + "The Night has been skipped!");
                }
                for (World world : Bukkit.getWorlds()) {
                    world.setTime(0);
                }
            }

            voteRunning = false;

        }
    }

    public void vote(Player player, boolean vote) {
        nightSkipVotes.put(player, vote);
        checkIfEveryoneVoted();
    }

    public boolean isVoteRunning() {
        return voteRunning;
    }

    public void setVoteRunning(boolean voting) {
        voteRunning = voting;
    }

    public boolean didPlayersVoteNo() {
        return playersVotedNo;
    }

    public void setPlayersVotedNo(boolean votedNo) {
        playersVotedNo = votedNo;
    }
}
