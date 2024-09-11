package me.wisdom.thepit.enchants.tainted.uncommon;

import me.wisdom.thepit.controllers.PlayerManager;
import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PitLoreBuilder;
import org.bukkit.event.EventHandler;

import java.util.List;

public class Hoarder extends PitEnchant {
    public static Hoarder INSTANCE;

    public Hoarder() {
        super("Hoarder", false, ApplyType.CHESTPLATES,
                "hoarder", "hoard");
        isUncommonEnchant = true;
        isTainted = true;
        INSTANCE = this;
    }

    @EventHandler
    public void onKill(KillEvent killEvent) {
        if(!PlayerManager.isRealPlayer(killEvent.getDeadPlayer())) return;

        int enchantLvl = killEvent.getKillerEnchantLevel(this);
        if(enchantLvl == 0) return;

        killEvent.soulMultipliers.add(Misc.getReductionMultiplier(getDecreasePercent(enchantLvl)));
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Lose &f-" + getDecreasePercent(enchantLvl) + "% &7souls on death"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is a &5Darkzone &7enchant that " +
                "decreases the amount of &fsouls &7that you lose on death";
    }

    public int getDecreasePercent(int enchantLvl) {
        return enchantLvl * 10;
    }
}
