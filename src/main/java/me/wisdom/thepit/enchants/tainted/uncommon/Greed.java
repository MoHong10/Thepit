package me.wisdom.thepit.enchants.tainted.uncommon;

import me.wisdom.thepit.controllers.PlayerManager;
import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.misc.PitLoreBuilder;
import org.bukkit.event.EventHandler;

import java.util.List;

public class Greed extends PitEnchant {
    public static Greed INSTANCE;

    public Greed() {
        super("Greed", false, ApplyType.SCYTHES,
                "greed", "greedy");
        isUncommonEnchant = true;
        isTainted = true;
        INSTANCE = this;
    }

    @EventHandler
    public void onKill(KillEvent killEvent) {
        if(!PlayerManager.isRealPlayer(killEvent.getKillerPlayer()) || !PlayerManager.isRealPlayer(killEvent.getDeadPlayer())) return;

        int enchantLvl = killEvent.getKillerEnchantLevel(this);
        if(enchantLvl == 0) return;

        killEvent.soulMultipliers.add(1 + (getIncreasePercent(enchantLvl) / 100.0));
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Gain &f+" + getIncreasePercent(enchantLvl) + "% &7souls &7from kills on other players"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is a &5Darkzone &7enchant that " +
                "increases the amount of &fsouls &7that you get from killing other players";
    }

    public int getIncreasePercent(int enchantLvl) {
        return enchantLvl * 8 + 10;
    }
}
