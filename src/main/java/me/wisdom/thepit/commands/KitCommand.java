package me.wisdom.thepit.commands;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.SpawnManager;
import me.wisdom.thepit.inventories.help.KitGUI;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (!player.isOp() && !Thepit.isDev()) {
            if (!SpawnManager.isInSpawn(player)) {
                AOutput.error(player, "您只能在出生点使用此命令！");
                return false;
            }
        }

        KitGUI kitGUI = new KitGUI(player);
        kitGUI.kitPanel.openPanel(kitGUI.kitPanel);

        return false;
    }
}
