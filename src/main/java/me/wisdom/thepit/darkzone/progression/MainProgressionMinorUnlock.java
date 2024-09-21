package me.wisdom.thepit.darkzone.progression;

import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.UnlockState;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MainProgressionMinorUnlock extends MainProgressionUnlock {

    public MainProgressionMinorUnlock(String id, int guiXPos, int guiYPos) {
        super(id, guiXPos, guiYPos);
    }

    @Override
    public String getDisplayName() {
        return "&5Progression Path";
    }

    @Override
    public ItemStack getDisplayStack(PitPlayer pitPlayer, UnlockState unlockState) {
        int cost = ProgressionManager.getUnlockCost(pitPlayer, this);
        ItemStack baseStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) unlockState.data);
        ALoreBuilder loreBuilder = new ALoreBuilder();

        ProgressionManager.addPurchaseCostLore(this, loreBuilder, unlockState, pitPlayer.taintedSouls, cost, false);
        if(unlockState == UnlockState.UNLOCKED) Misc.addEnchantGlint(baseStack);

        return new AItemStackBuilder(baseStack)
                .setName(unlockState.chatColor + "Path Unlock")
                .setLore(loreBuilder)
                .getItemStack();
    }
}
