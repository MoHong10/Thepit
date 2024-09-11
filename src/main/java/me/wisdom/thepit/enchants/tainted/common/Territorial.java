package me.wisdom.thepit.enchants.tainted.common;

import me.wisdom.thepit.controllers.objects.BasicDarkzoneEnchant;
import me.wisdom.thepit.darkzone.PitMob;
import me.wisdom.thepit.darkzone.mobs.PitIronGolem;
import me.wisdom.thepit.darkzone.mobs.PitWolf;
import me.wisdom.thepit.enums.ApplyType;

import java.util.Arrays;
import java.util.List;

public class Territorial extends BasicDarkzoneEnchant {
    public static Territorial INSTANCE;

    public Territorial() {
        super("Territorial", false, ApplyType.CHESTPLATES,
                "territorial", "territory");
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
        return Arrays.asList(PitWolf.class, PitIronGolem.class);
    }
}
