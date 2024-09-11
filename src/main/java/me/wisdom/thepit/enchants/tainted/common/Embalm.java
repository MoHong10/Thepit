package me.wisdom.thepit.enchants.tainted.common;

import me.wisdom.thepit.controllers.objects.BasicDarkzoneEnchant;
import me.wisdom.thepit.darkzone.PitMob;
import me.wisdom.thepit.darkzone.mobs.PitSkeleton;
import me.wisdom.thepit.darkzone.mobs.PitWitherSkeleton;
import me.wisdom.thepit.darkzone.mobs.PitZombie;
import me.wisdom.thepit.darkzone.mobs.PitZombiePigman;
import me.wisdom.thepit.enums.ApplyType;

import java.util.Arrays;
import java.util.List;

public class Embalm extends BasicDarkzoneEnchant {
    public static Embalm INSTANCE;

    public Embalm() {
        super("Embalm", false, ApplyType.CHESTPLATES,
                "embalm");
        isTainted = true;
        INSTANCE = this;
    }

    @Override
    public int getBaseStatPercent(int enchantLvl) {
        return enchantLvl * 4 + 4;
    }

    @Override
    public boolean isOffensive() {
        return false;
    }

    @Override
    public List<Class<? extends PitMob>> getApplicableMobs() {
        return Arrays.asList(PitZombie.class, PitSkeleton.class, PitZombiePigman.class, PitWitherSkeleton.class);
    }
}
