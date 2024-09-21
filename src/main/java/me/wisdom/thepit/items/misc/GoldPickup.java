package me.wisdom.thepit.items.misc;

import de.tr7zw.nbtapi.NBTItem;
import me.wisdom.thepit.controllers.LevelManager;
import me.wisdom.thepit.controllers.PlayerManager;
import me.wisdom.thepit.controllers.objects.Killstreak;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.NBTTag;
import me.wisdom.thepit.items.PitItem;
import me.wisdom.thepit.killstreaks.GoldNanoFactory;
import me.wisdom.thepit.misc.Formatter;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GoldPickup extends PitItem {

    public GoldPickup() {
        hasUUID = true;
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        if(!PlayerManager.isRealPlayer(player)) return;

        Item item = event.getItem();
        ItemStack itemStack = item.getItemStack();
        if(!isThisItem(itemStack)) return;

        event.setCancelled(true);
        int gold = getGold(itemStack);
        item.remove();

        LevelManager.addGold(player, gold);
        Misc.applyPotionEffect(player, PotionEffectType.REGENERATION, 20 * getRegenSeconds(), getRegenAmplifier(), true, false);

        if(Killstreak.hasKillstreak(player, GoldNanoFactory.INSTANCE)) {
            GoldNanoFactory.onGoldPickup(player);
            AOutput.send(player, "&6&lGOLD PICKUP!&7 Gain &6+" + gold + "g&7. &6+25% gold &7on your next kill.");
        } else {
            AOutput.send(player, "&6&lGOLD PICKUP!&7 Gain &6+" + gold + "g&7");
        }
        Sounds.SUCCESS.play(player);
    }

    @Override
    public String getNBTID() {
        return "gold-pickup";
    }

    @Override
    public List<String> getRefNames() {
        return new ArrayList<>(Arrays.asList("goldpickup"));
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public ItemStack getReplacementItem(PitPlayer pitPlayer, ItemStack itemStack, NBTItem nbtItem) {
        return null;
    }

    public Material getMaterial() {
        return Material.GOLD_INGOT;
    }

    public String getName(int amount) {
        return Formatter.formatGoldFull(amount);
    }

    public List<String> getLore() {
        return new ALoreBuilder(
                "&7You shouldn't be able to read this lol"
        ).getLore();
    }

    public ItemStack getItem(int amount) {
        ItemStack itemStack = new ItemStack(getMaterial());
        itemStack = buildItem(itemStack);

        NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.setInteger(NBTTag.GOLD_PICKUP_AMOUNT.getRef(), amount);
        itemStack = nbtItem.getItem();

        return new AItemStackBuilder(itemStack)
                .setName(getName(amount))
                .setLore(getLore())
                .getItemStack();
    }

    public int getGold(ItemStack itemStack) {
        if(!isThisItem(itemStack)) return 0;
        NBTItem nbtItem = new NBTItem(itemStack);
        return nbtItem.getInteger(NBTTag.GOLD_PICKUP_AMOUNT.getRef());
    }

    @Override
    public boolean isLegacyItem(ItemStack itemStack, NBTItem nbtItem) {
        return false;
    }

    public static int getRegenAmplifier() {
        return 3;
    }

    public static int getRegenSeconds() {
        return 2;
    }

    public static int getPickupGold() {
        return 123;
    }
}
