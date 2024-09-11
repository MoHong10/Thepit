package me.wisdom.thepit.brewing.objects;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.brewing.PotionManager;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.misc.AOutput;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class PotionEffect implements Listener {
    public Player player;
    public BrewingIngredient potionType;
    public BrewingIngredient potency;
    public BrewingIngredient duration;
    public int durationOverride;
    public long creationTime;

    public int ticksLeft;

    public PotionEffect(Player player, BrewingIngredient potionType, BrewingIngredient potency, BrewingIngredient duration) {
        this.player = player;
        this.potionType = potionType;
        this.potency = potency;
        this.duration = duration;
        this.creationTime = System.currentTimeMillis();

        ticksLeft = potionType.getDuration(duration);
        AOutput.send(player, "&5&l药水！&7 施加了 " + potionType.color + potionType.name + " " +
                AUtil.toRoman(potency.tier) + " &7，持续时间为 &f" + Misc.ticksToTime(ticksLeft));
        potionType.administerEffect(player, potency, getTimeLeft());
    }

    public PotionEffect(Player player, BrewingIngredient potionType, BrewingIngredient potency, int durationOverride) {
        this.player = player;
        this.potionType = potionType;
        this.potency = potency;
        this.durationOverride = durationOverride;

        ticksLeft = durationOverride;
        AOutput.send(player, "&5&l药水！&7 你受到 " + potionType.color + potionType.name + " " +
                AUtil.toRoman(potency.tier) + " 的影响，&7持续时间为 &f" + Misc.ticksToTime(ticksLeft));
        potionType.administerEffect(player, potency, getTimeLeft());
    }

    public void tick() {
        ticksLeft--;
        potionType.administerEffect(player, potency, getTimeLeft());
        if(getTimeLeft() == 0) onExpire(false);
    }

    public int getTimeLeft() {
        return ticksLeft;
    }

    public int getOriginalTime() {
        return durationOverride == 0 ? potionType.getDuration(duration) : durationOverride;
    }

    public void onExpire(boolean hideMessage) {
        if (!hideMessage) AOutput.send(player, "&5&l药水！ " + potionType.color + potionType.name + " &7已过期");
        PotionManager.potionEffectList.remove(this);
        if(PotionManager.playerIndex.containsKey(player) && PotionManager.playerIndex.get(player) > 0)
            PotionManager.playerIndex.put(player, PotionManager.playerIndex.get(player) - 1);
        else PotionManager.playerIndex.remove(player);
        if(PotionManager.getPotionEffects(player).size() == 0)
            PotionManager.hideActiveBossBar(Thepit.adventure.player(player), player);
    }
}
