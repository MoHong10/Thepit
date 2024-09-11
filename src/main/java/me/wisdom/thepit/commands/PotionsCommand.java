package me.wisdom.thepit.commands;

import me.wisdom.thepit.brewing.PotionManager;
import me.wisdom.thepit.brewing.objects.PotionEffect;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.misc.AOutput;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PotionsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (PotionManager.getPotionEffects(player).isEmpty()) {
            AOutput.send(player, "&5&l药水!&7 没有活跃的药水效果!");
            return false;
        }

        AOutput.send(player, "&d&m--------------------&d<&5&l药水&d>&m--------------------");
        for (PotionEffect potionEffect : PotionManager.getPotionEffects(player)) {
            AOutput.send(player, "&d * &5" + potionEffect.potionType.color + potionEffect.potionType.name + " " +
                    AUtil.toRoman(potionEffect.potency.tier) + ": &f" + Misc.ticksToTime(potionEffect.ticksLeft));
        }
        AOutput.send(player, "&d&m--------------------&d<&5&l药水&d>&m--------------------");

        return false;
    }
}

