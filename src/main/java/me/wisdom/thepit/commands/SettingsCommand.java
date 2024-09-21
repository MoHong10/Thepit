package me.wisdom.thepit.commands;

import me.wisdom.thepit.settings.SettingsGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SettingsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        SettingsGUI settingsGUI = new SettingsGUI(player);
        settingsGUI.open();
        return false;
    }
}
