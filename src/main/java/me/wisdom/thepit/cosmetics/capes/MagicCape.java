package me.wisdom.thepit.cosmetics.capes;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.cosmetics.CosmeticType;
import me.wisdom.thepit.cosmetics.PitCosmetic;
import me.wisdom.thepit.cosmetics.collections.CapeCollection;
import me.wisdom.thepit.cosmetics.particles.EnchantmentTableParticle;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class MagicCape extends PitCosmetic {
    public CapeCollection cape;

    public MagicCape() {
        super("&5Enchanted Cloak", "magiccape", CosmeticType.CAPE);
        accountForPitch = false;

        cape = new CapeCollection(new EnchantmentTableParticle(accountForPitch, accountForYaw));
    }

    @Override
    public void onEnable(PitPlayer pitPlayer) {
        runnableMap.put(pitPlayer.player.getUniqueId(), new BukkitRunnable() {
            @Override
            public void run() {
                cape.draw(MagicCape.this, pitPlayer, null);
            }
        }.runTaskTimer(Thepit.INSTANCE, 0L, 10L));
    }

    @Override
    public void onDisable(PitPlayer pitPlayer) {
        if(runnableMap.containsKey(pitPlayer.player.getUniqueId()))
            runnableMap.get(pitPlayer.player.getUniqueId()).cancel();
    }

    @Override
    public ItemStack getRawDisplayItem() {
        ItemStack itemStack = new AItemStackBuilder(Material.EXP_BOTTLE)
                .setName(getDisplayName())
                .setLore(new ALoreBuilder(
                        "&7Show off your &5magical &7style",
                        "&7with this mythical cape!"
                ))
                .getItemStack();
        return itemStack;
    }
}
