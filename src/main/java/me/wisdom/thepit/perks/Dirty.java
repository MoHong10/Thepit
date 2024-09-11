package me.wisdom.thepit.perks;

import me.wisdom.thepit.controllers.objects.PitPerk;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class Dirty extends PitPerk {
    public static Dirty INSTANCE;

    public Dirty() {
        super("Dirty", "dirty");
        INSTANCE = this;
    }

    @EventHandler
    public void onKill(KillEvent killEvent) {
        if (!hasPerk(killEvent.getKiller()) || !killEvent.isDeadPlayer()) return;
        ;
        Misc.applyPotionEffect(killEvent.getKiller(), PotionEffectType.DAMAGE_RESISTANCE, 4 * 20, 1, true, false);
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.DIRT, 1, 1)
                .getItemStack();
    }

    @Override
    public void addBaseDescription(PitLoreBuilder loreBuilder, Player player) {
        loreBuilder.addLongLine(
                "&7在击杀玩家或机器人时获得 &9抗性 II &7（4秒）"
        );
    }

    @Override
    public String getSummary() {
        return "&aDirty &7是一个技能，在击杀玩家或机器人时为你提供 &9抗性 II &7。";
    }
}
