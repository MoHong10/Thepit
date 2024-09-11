package me.wisdom.thepit.cosmetics.capes;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.cosmetics.CosmeticType;
import me.wisdom.thepit.cosmetics.PitCosmetic;
import me.wisdom.thepit.cosmetics.collections.CapeCollection;
import me.wisdom.thepit.cosmetics.particles.CritParticle;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class CritCape extends PitCosmetic {
    public CapeCollection cape;

    public CritCape() {
        super("&4&lCritical &c&lHit!", "critcape", CosmeticType.CAPE);
        accountForPitch = false;

        cape = new CapeCollection(new CritParticle(accountForPitch, accountForYaw));
    }

    @Override
    public void onEnable(PitPlayer pitPlayer) {
        runnableMap.put(pitPlayer.player.getUniqueId(), new BukkitRunnable() {
            @Override
            public void run() {
                cape.draw(CritCape.this, pitPlayer, null);
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
        ItemStack itemStack = new AItemStackBuilder(Material.DIAMOND_SWORD)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7Wear the marks of combat proudly",
                        "&7on your back"
                ))
                .getItemStack();
        return itemStack;
    }
}
