package me.wisdom.thepit.enchants.tainted.uncommon.basic;

import me.wisdom.thepit.controllers.objects.BasicDarkzoneEnchant;
import me.wisdom.thepit.darkzone.PitMob;
import me.wisdom.thepit.darkzone.mobs.PitBlaze;
import me.wisdom.thepit.darkzone.mobs.PitWitherSkeleton;
import me.wisdom.thepit.darkzone.mobs.PitZombiePigman;
import me.wisdom.thepit.enums.ApplyType;

import java.util.Arrays;
import java.util.List;

public class EliteHunter extends BasicDarkzoneEnchant {
    public static EliteHunter INSTANCE;

    public EliteHunter() {
        super("Elite Hunter", false, ApplyType.SCYTHES,
                "elitehunter", "elite", "hunter2", "hunt2");
        isUncommonEnchant = true;
        isTainted = true;
        INSTANCE = this;
    }

    @Override
    public int getBaseStatPercent(int enchantLvl) {
        return enchantLvl * 11 + 12;
    }

    @Override
    public boolean isOffensive() {
        return true;
    }

    @Override
    public List<Class<? extends PitMob>> getApplicableMobs() {
        return Arrays.asList(PitBlaze.class, PitZombiePigman.class, PitWitherSkeleton.class);
    }
}
