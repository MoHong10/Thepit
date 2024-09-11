package me.wisdom.thepit.commands.admin;

import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.cosmetics.CosmeticManager;
import me.wisdom.thepit.cosmetics.PitCosmetic;
import me.wisdom.thepit.cosmetics.particles.ParticleColor;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.commands.ACommand;
import me.wisdom.thepitapi.commands.AMultiCommand;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class UnlockCosmeticCommand extends ACommand {
    public UnlockCosmeticCommand(AMultiCommand base, String executor) {
        super(base, executor);
    }

    @Override
    public void execute(CommandSender sender, Command command, String alias, List<String> args) {
        if(!(sender instanceof Player)) return;
        Player player = (Player) sender;

        if(!Misc.isKyro(player.getUniqueId())) {
            AOutput.error(player, "&c&l错误!&7 你需要是 &9Kyro &7才能执行此操作");
            return;
        }

        if(args.size() < 2) {
            AOutput.error(player, "&7用法: /unlockcosmetic <玩家> <装饰品> [颜色]");
            return;
        }

        Player target = null;
        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if(!onlinePlayer.getName().equalsIgnoreCase(args.get(0))) continue;
            target = onlinePlayer;
            break;
        }
        if(target == null) {
            AOutput.error(player, "&7找不到该玩家");
            return;
        }

        PitCosmetic pitCosmetic = CosmeticManager.getCosmetic(args.get(1));
        if(pitCosmetic == null) {
            AOutput.error(player, "&7找不到该装饰品");
            return;
        }

        if(pitCosmetic.isColorCosmetic && args.size() < 3) {
            AOutput.error(player, "&7用法: /unlockcosmetic <玩家> <装饰品> <颜色>");
            return;
        }
        ParticleColor particleColor = null;
        if(pitCosmetic.isColorCosmetic) {
            particleColor = ParticleColor.getParticleColor(args.get(2));
            if(particleColor == null) {
                AOutput.error(player, "&7该颜色不存在");
                return;
            }
        }

        PitPlayer pitTarget = PitPlayer.getPitPlayer(target);
        PitPlayer.UnlockedCosmeticData cosmeticData = pitTarget.unlockedCosmeticsMap.get(pitCosmetic.refName);
        if(cosmeticData != null) {
            if(!pitCosmetic.isColorCosmetic) {
                AOutput.error(player, "&7玩家已经拥有该装饰品");
                return;
            } else if(cosmeticData.unlockedColors.contains(particleColor)) {
                AOutput.error(player, "&7玩家已经拥有该颜色的装饰品");
                return;
            }
        }

        CosmeticManager.unlockCosmetic(pitTarget, pitCosmetic, particleColor);
        if(pitCosmetic.isColorCosmetic) {
            AOutput.send(player, "&7已为 " + target.getName() + " 解锁 " + pitCosmetic.getDisplayName() + "&7 的颜色 " + particleColor.displayName);
        } else {
            AOutput.send(player, "&7已为 " + target.getName() + " 解锁 " + pitCosmetic.getDisplayName());
        }
    }

    @Override
    public List<String> getTabComplete(Player player, String current, List<String> args) {
        return null;
    }
}
