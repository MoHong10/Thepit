package me.wisdom.thepit.items.misc;

import de.tr7zw.nbtapi.NBTItem;
import me.wisdom.thepit.controllers.GuildIntegrationManager;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.MarketCategory;
import me.wisdom.thepit.enums.NBTTag;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.items.StaticPitItem;
import me.wisdom.thepit.misc.PlayerItemLocation;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CorruptedFeather extends StaticPitItem {

    public CorruptedFeather() {
        hasDropConfirm = true;
        marketCategory = MarketCategory.PURE_RELATED;
    }

    @Override
    public String getNBTID() {
        return "corrupted-feather";
    }

    @Override
    public List<String> getRefNames() {
        return new ArrayList<>(Arrays.asList("corruptfeather"));
    }

    @Override
    public Material getMaterial() {
        return Material.INK_SACK;
    }

    @Override
    public String getName() {
        return "&5Corrupted Feather";
    }

    @Override
    public List<String> getLore() {
        return new ALoreBuilder(
                "&eSpecial item",
                "&7Protects your inventory but",
                "&7gets consumed on death if",
                "&7in your hotbar (&5Darkzone&7",
                "&7only)"
        ).getLore();
    }

    public boolean useCorruptedFeather(KillEvent killEvent) {
        LivingEntity killer = killEvent.getKiller();
        Player dead = killEvent.getDeadPlayer();

        for(int i = 0; i < 9; i++) {
            ItemStack itemStack = dead.getInventory().getItem(i);
            if(!isThisItem(itemStack)) continue;

            AOutput.send(dead, "&5&lCORRUPTED FEATHER!&7 Inventory protected!");
            if(itemStack.getAmount() > 1) itemStack.setAmount(itemStack.getAmount() - 1);
            else {
                dead.getInventory().setItem(i, null);
                killEvent.getDeadInventoryWrapper().removeItem(PlayerItemLocation.inventory(i));
            }
            dead.updateInventory();
            Sounds.FUNKY_FEATHER.play(dead);

            GuildIntegrationManager.handleFeather(killer, dead);

            // Remove this if you don't want them counting for lb
            PitPlayer pitPlayer = PitPlayer.getPitPlayer(dead);
            pitPlayer.stats.feathersLost++;
            return true;
        }
        return false;
    }

    @Override
    public boolean isLegacyItem(ItemStack itemStack, NBTItem nbtItem) {
        return nbtItem.hasKey(NBTTag.IS_CORRUPTED_FEATHER.getRef());
    }
}
