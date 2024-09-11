package me.wisdom.thepit.settings.scoreboard;

import me.wisdom.thepit.controllers.Cooldown;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enchants.overworld.BulletTime;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BulletTimeScoreboard extends ScoreboardOption {

    @Override
    public String getDisplayName() {
        return "&9Bullet Time";
    }

    @Override
    public String getRefName() {
        return "bullettime";
    }

    @Override
    public String getValue(PitPlayer pitPlayer) {
        if (!BulletTime.INSTANCE.cooldowns.containsKey(pitPlayer.player.getUniqueId())) return null;
        Cooldown cooldown = BulletTime.INSTANCE.cooldowns.get(pitPlayer.player.getUniqueId());
        if (!cooldown.isOnCooldown()) return null;
        int seconds = (int) Math.ceil(cooldown.getTicksLeft() / 20.0);
        return "&6子弹时间: &e" + seconds + "秒";
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        ItemStack itemStack = new AItemStackBuilder(Material.ARROW)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7显示当前的冷却时间",
                        "&7剩余时间在" + BulletTime.INSTANCE.getDisplayName(),
                        "&7当适用时"
                )).getItemStack();
        return itemStack;
    }
}
