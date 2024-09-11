package me.wisdom.thepit.settings.scoreboard;

import me.wisdom.thepit.controllers.Cooldown;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enchants.overworld.Telebow;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TelebowScoreboard extends ScoreboardOption {

    @Override
    public String getDisplayName() {
        return "&9传送弓";
    }

    @Override
    public String getRefName() {
        return "telebow";
    }

    @Override
    public String getValue(PitPlayer pitPlayer) {
        if (!Telebow.INSTANCE.cooldowns.containsKey(pitPlayer.player.getUniqueId())) return null;
        Cooldown cooldown = Telebow.INSTANCE.cooldowns.get(pitPlayer.player.getUniqueId());
        if (!cooldown.isOnCooldown()) return null;
        int seconds = (int) Math.ceil(cooldown.getTicksLeft() / 20.0);
        return "&6传送弓: &e" + seconds + "秒";
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        ItemStack itemStack = new AItemStackBuilder(Material.ENDER_PEARL)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7显示当前的冷却时间",
                        "&7剩余的 " + Telebow.INSTANCE.getDisplayName(),
                        "&7在适用时"
                )).getItemStack();
        return itemStack;
    }
}
