package me.wisdom.thepit.commands;

import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LightningCommand implements CommandExecutor {
    public static List<Player> lightningPlayers = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);

        boolean lightningDisabled = pitPlayer.lightingDisabled;
        pitPlayer.lightingDisabled = !lightningDisabled;

        if (lightningDisabled) {
            AOutput.send(player, "闪电已启用");
            lightningPlayers.remove(player);
            return false;
        } else {
            AOutput.send(player, "闪电已禁用");
            lightningPlayers.add(player);
            return false;
        }
    }
}
