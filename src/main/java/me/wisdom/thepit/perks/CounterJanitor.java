package me.wisdom.thepit.perks;

import me.wisdom.thepit.controllers.NonManager;
import me.wisdom.thepit.controllers.objects.PitPerk;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepit.upgrades.UnlockCounterJanitor;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

public class CounterJanitor extends PitPerk {
    public static CounterJanitor INSTANCE;

    public CounterJanitor() {
        super("Counter-Janitor", "counter-janitor");
        renownUpgradeClass = UnlockCounterJanitor.class;
        INSTANCE = this;
    }

    @EventHandler
    public void onKill(KillEvent killEvent) {
        if(!hasPerk(killEvent.getKiller())) return;
        if(killEvent.isKillerPlayer() && NonManager.getNon(killEvent.getDead()) == null) {
            PitPlayer pitPlayer = killEvent.getKillerPitPlayer();
            double missingHealth = killEvent.getKiller().getMaxHealth() - killEvent.getKiller().getHealth();
            pitPlayer.heal(missingHealth);
        }
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.SPONGE)
                .getItemStack();
    }

    @Override
    public void addBaseDescription(PitLoreBuilder loreBuilder, Player player) {
        loreBuilder.addLongLine(
                "&7在击杀玩家时&c立即恢复满血&7"
        );
    }

    @Override
    public String getSummary() {
        return "&eCounter-Janitor 是一个在 &erenown 商店&7 中解锁的技能，可以在击杀玩家时 &c恢复大量生命值&7。此技能与 &cVampire&7 不兼容。";
    }
}
