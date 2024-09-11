package me.wisdom.thepit.enchants.tainted.common;

import me.wisdom.thepit.controllers.objects.BasicDarkzoneEnchant;
import me.wisdom.thepit.darkzone.PitMob;
import me.wisdom.thepit.darkzone.mobs.PitBlaze;
import me.wisdom.thepit.darkzone.mobs.PitSkeleton;
import me.wisdom.thepit.enums.ApplyType;

import java.util.Arrays;
import java.util.List;

public class PinCushion extends BasicDarkzoneEnchant {
    public static PinCushion INSTANCE;

    public PinCushion() {
        super("Pin Cushion", false, ApplyType.CHESTPLATES,
                "pincushion", "cushion");
        isTainted = true;
        INSTANCE = this;
    }

    @Override
    public int getBaseStatPercent(int enchantLvl) {
        return enchantLvl * 5 + 3;
    }

    @Override
    public boolean isOffensive() {
        return false;
    }

    @Override
    public List<Class<? extends PitMob>> getApplicableMobs() {
        return Arrays.asList(PitSkeleton.class, PitBlaze.class);
    }
}
