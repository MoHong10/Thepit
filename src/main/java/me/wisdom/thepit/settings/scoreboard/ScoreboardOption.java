package me.wisdom.thepit.settings.scoreboard;

import me.wisdom.thepit.controllers.ScoreboardManager;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class ScoreboardOption {

    public abstract String getDisplayName();
    public abstract String getRefName();
    public abstract String getValue(PitPlayer pitPlayer);
    public abstract ItemStack getBaseDisplayStack();

    public int getCurrentPosition(PitPlayer pitPlayer) {
        int count = 0;
        for (String testRefName : pitPlayer.scoreboardData.getPriorityList()) {
            if (getRefName().equals(testRefName)) return count;
            count++;
        }
        throw new RuntimeException();
    }

    public ItemStack getDisplayStack(int position, boolean isEnabled) {
        ItemStack itemStack = getBaseDisplayStack();
        ItemMeta itemMeta = itemStack.getItemMeta();
        ALoreBuilder loreBuilder = new ALoreBuilder(itemMeta.getLore()).addLore(
                "", "&7状态: " + (isEnabled ? "&a启用" : "&c禁用"), ""
        );
        if (position != 0) loreBuilder.addLore("&e左键点击以提高优先级");
        if (position != ScoreboardManager.scoreboardOptions.size() - 1) loreBuilder.addLore("&e右键点击以降低优先级");
        loreBuilder.addLore("&e中键/Shift-点击以 " + (isEnabled ? "禁用" : "启用"));

        if (isEnabled) Misc.addEnchantGlint(itemStack);

        return new AItemStackBuilder(itemStack)
                .setLore(loreBuilder)
                .getItemStack();
    }
}
