package me.wisdom.thepit.cosmetics.capes;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.cosmetics.CosmeticType;
import me.wisdom.thepit.cosmetics.PitCosmetic;
import me.wisdom.thepit.cosmetics.collections.CapeCollection;
import me.wisdom.thepit.cosmetics.particles.CritMagicParticle;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class CritMagicCape extends PitCosmetic {
    public CapeCollection cape;

    public CritMagicCape() {
        super("&3Crit Magic Cape", "critmagiccape", CosmeticType.CAPE);
        accountForPitch = false;

        cape = new CapeCollection(new CritMagicParticle(accountForPitch, accountForYaw));
    }

    @Override
    public void onEnable(PitPlayer pitPlayer) {
        runnableMap.put(pitPlayer.player.getUniqueId(), new BukkitRunnable() {
            @Override
            public void run() {
                cape.draw(CritMagicCape.this, pitPlayer, null);
            }
        }.runTaskTimer(Thepit.INSTANCE, 0L, 4L));
    }

    @Override
    public void onDisable(PitPlayer pitPlayer) {
        if(runnableMap.containsKey(pitPlayer.player.getUniqueId()))
            runnableMap.get(pitPlayer.player.getUniqueId()).cancel();
    }

    @Override
    public ItemStack getRawDisplayItem() {
        ItemStack itemStack = new AItemStackBuilder(Material.ENCHANTMENT_TABLE)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7nice"
                ))
                .getItemStack();
        return itemStack;
    }
}
