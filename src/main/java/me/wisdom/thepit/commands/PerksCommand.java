package me.wisdom.thepit.commands;

import me.wisdom.thepit.inventories.PerkGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PerksCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if(!player.isOp()) return false;

        PerkGUI perkGUI = new PerkGUI(player);
        perkGUI.open();
        return false;
    }
}
