package me.wisdom.thepit.enchants.tainted.common;

import me.wisdom.thepit.controllers.objects.BasicDarkzoneEnchant;
import me.wisdom.thepit.darkzone.PitMob;
import me.wisdom.thepit.darkzone.mobs.PitIronGolem;
import me.wisdom.thepit.darkzone.mobs.PitWolf;
import me.wisdom.thepit.enums.ApplyType;

import java.util.Arrays;
import java.util.List;

public class Antagonist extends BasicDarkzoneEnchant {
    public static Antagonist INSTANCE;

    public Antagonist() {
        super("Antagonist", false, ApplyType.SCYTHES,
                "antagonist");
        isTainted = true;
        INSTANCE = this;
    }

    @Override
    public int getBaseStatPercent(int enchantLvl) {
        return enchantLvl * 10 + 6;
    }

    @Override
    public boolean isOffensive() {
        return true;
    }

    @Override
    public List<Class<? extends PitMob>> getApplicableMobs() {
        return Arrays.asList(PitWolf.class, PitIronGolem.class);
    }
}
