package me.wisdom.thepit.perks;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitPerk;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.events.PerkEquipEvent;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Thick extends PitPerk {
    public static Thick INSTANCE;

    public Thick() {
        super("Thick", "thick");
        INSTANCE = this;
    }

    @EventHandler
    public void onPerkEquip(PerkEquipEvent event) {
        Player player = event.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
                pitPlayer.updateMaxHealth();
            }
        }.runTaskLater(Thepit.INSTANCE, 1L);
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.APPLE)
                .getItemStack();
    }

    @Override
    public void addBaseDescription(PitLoreBuilder loreBuilder, Player player) {
        loreBuilder.addLongLine(
                "&7你拥有 &c+2 最大 \u2764"
        );
    }

    @Override
    public String getSummary() {
        return "&aThick &7是一个赋予你 &c+2 最大 \u2764 的 perk";
    }
}
