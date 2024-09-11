package me.wisdom.thepit.commands.beta;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.CombatManager;
import me.wisdom.thepit.darkzone.FastTravelGUI;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.commands.ACommand;
import me.wisdom.thepitapi.commands.AMultiCommand;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class FastTravelCommand extends ACommand {
    public FastTravelCommand(AMultiCommand base, String executor) {
        super(base, executor);
    }

    @Override
    public void execute(CommandSender sender, Command command, String alias, List<String> args) {
        if(!(sender instanceof Player)) return;
        Player player = (Player) sender;

        if(!player.isOp() && !Thepit.isDev() && CombatManager.isInCombat(player)) {
            AOutput.error(player, "&c&l错误!&7 你在战斗中无法使用此功能!");
            Sounds.NO.play(player);
            return;
        }

        FastTravelGUI fastTravelGUI = new FastTravelGUI(player);
        fastTravelGUI.open();
    }

    @Override
    public List<String> getTabComplete(Player player, String current, List<String> args) {
        return null;
    }
}
