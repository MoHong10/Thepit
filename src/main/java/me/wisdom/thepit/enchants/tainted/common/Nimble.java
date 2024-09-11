package me.wisdom.thepit.enchants.tainted.common;

import me.wisdom.thepit.controllers.objects.BasicDarkzoneEnchant;
import me.wisdom.thepit.darkzone.PitMob;
import me.wisdom.thepit.darkzone.mobs.PitIronGolem;
import me.wisdom.thepit.darkzone.mobs.PitZombie;
import me.wisdom.thepit.enums.ApplyType;

import java.util.Arrays;
import java.util.List;

public class Nimble extends BasicDarkzoneEnchant {
    public static Nimble INSTANCE;

    public Nimble() {
        super("Nimble", false, ApplyType.SCYTHES,
                "nimble");
        isTainted = true;
        INSTANCE = this;
    }

    @Override
    public int getBaseStatPercent(int enchantLvl) {
        return enchantLvl * 11 + 10;
    }

    @Override
    public boolean isOffensive() {
        return true;
    }

    @Override
    public List<Class<? extends PitMob>> getApplicableMobs() {
        return Arrays.asList(PitZombie.class, PitIronGolem.class);
    }
}
