package me.wisdom.thepit.commands;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.megastreaks.StashStreaker;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepit.storage.EnderchestGUI;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WardrobeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);

        if(!player.isOp() && !Thepit.isDev()) {
            if(!(pitPlayer.getMegastreak() instanceof StashStreaker) || !pitPlayer.isOnMega()) {
                AOutput.error(player, "&c&l错误!&7 你必须处于 " + StashStreaker.INSTANCE.getCapsDisplayName() + "&7 才能使用这个功能!");
                Sounds.NO.play(player);
                return false;
            }
        }

        EnderchestGUI enderchestGUI = new EnderchestGUI(player, player.getUniqueId());
        enderchestGUI.enderchestPanel.openPanel(enderchestGUI.wardrobePanel);
        return false;
    }
}
