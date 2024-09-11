package me.wisdom.thepit.darkzone.progression;

import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.UnlockState;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MainProgressionStart extends MainProgressionUnlock {

    public MainProgressionStart(String id, int guiXPos, int guiYPos) {
        super(id, guiXPos, guiYPos);
    }

    @Override
    public String getDisplayName() {
        return "&5Progression Start";
    }

    @Override
    public ItemStack getDisplayStack(PitPlayer pitPlayer, UnlockState unlockState) {
        return new AItemStackBuilder(Material.BEACON, 1)
                .setName("&5Progression Start")
                .setLore(new ALoreBuilder(
                        "&7Start your journey here and",
                        "&7build your skillset"
                ))
                .getItemStack();
    }
}
