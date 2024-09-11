package me.wisdom.thepit.settings.scoreboard;

import me.wisdom.thepit.controllers.objects.HelmetAbility;
import me.wisdom.thepit.controllers.objects.HelmetManager;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.helmetabilities.JudgementAbility;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class JudgementScoreboard extends ScoreboardOption {

    @Override
    public String getDisplayName() {
        return "&9审判";
    }

    @Override
    public String getRefName() {
        return "judgement";
    }

    @Override
    public String getValue(PitPlayer pitPlayer) {
        ItemStack helmet = HelmetManager.getHelmet(pitPlayer.player);
        HelmetAbility deadAbility = HelmetManager.getAbility(helmet);
        HelmetAbility liveAbility = HelmetManager.abilities.get(pitPlayer.player);
        if (!(deadAbility instanceof JudgementAbility)) return null;

        if (liveAbility != null && liveAbility.isActive) {
            int remainingSeconds = (int) Math.ceil(JudgementAbility.maxActivationMap.getOrDefault(pitPlayer.player, 0) / 20.0);
            return "&6审判: &e(" + remainingSeconds + "秒)";
        } else {
            if (!JudgementAbility.cooldownMap.containsKey(pitPlayer.player.getUniqueId())) return "&6审判: &a准备好了";
            int remainingSeconds = (int) Math.ceil(JudgementAbility.cooldownMap.getOrDefault(pitPlayer.player.getUniqueId(), 0) / 20.0);
            return "&6审判: &7(" + remainingSeconds + "秒)";
        }
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        ItemStack itemStack = new AItemStackBuilder(Material.BEACON)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7显示剩余时间",
                        "&9" + JudgementAbility.INSTANCE.name + " &7正在激活或冷却中",
                        "&7当适用时"
                )).getItemStack();
        return itemStack;
    }
}
