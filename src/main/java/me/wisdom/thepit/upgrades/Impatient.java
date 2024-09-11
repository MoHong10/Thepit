package me.wisdom.thepit.upgrades;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.SpawnManager;
import me.wisdom.thepit.controllers.UpgradeManager;
import me.wisdom.thepit.controllers.objects.TieredRenownUpgrade;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public class Impatient extends TieredRenownUpgrade {
    public static Impatient INSTANCE;

    static {
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    if(!UpgradeManager.hasUpgrade(player, INSTANCE) || !SpawnManager.isInSpawn(player)) continue;
                    Misc.applyPotionEffect(player, PotionEffectType.SPEED, 40,
                            UpgradeManager.getTier(player, INSTANCE) - 1, false, false);
                }
            }
        }.runTaskTimer(Thepit.INSTANCE, 0L, 20L);
    }

    public Impatient() {
        super("Impatient", "IMPATIENT", 6);
        INSTANCE = this;
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.CARROT_ITEM)
                .getItemStack();
    }

    @Override
    public String getEffectPerTier() {
        return "&7Gain an additional level of &eSpeed &7in spawn";
    }

    @Override
    public String getCurrentEffect(int tier) {
        return "&e速度 " + AUtil.toRoman(tier) + " &7在出生点";
    }

    @Override
    public String getSummary() {
        return "&a急躁&7 是一个 &e声望&7 升级，它在出生点为你提供 &e速度&7 增益";
    }

    @Override
    public List<Integer> getTierCosts() {
        return Arrays.asList(10, 25, 40);
    }
}