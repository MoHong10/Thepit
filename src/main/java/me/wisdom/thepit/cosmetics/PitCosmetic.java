package me.wisdom.thepit.cosmetics;

import de.tr7zw.nbtapi.NBTItem;
import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.MapManager;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.cosmetics.particles.ParticleColor;
import me.wisdom.thepit.enums.NBTTag;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public abstract class PitCosmetic implements Listener {
    private String displayName;
    public String refName;
    public CosmeticType cosmeticType;

    public boolean isColorCosmetic;
    public boolean accountForYaw = true;
    public boolean accountForPitch = true;
    public boolean isPermissionRequired = false;

    public boolean preventKillSound = false;
    public final int SOUND_RANGE = 10;
    public final int MID_RANGE = 10;

    public Map<UUID, BukkitTask> runnableMap = new HashMap<>();

    public PitCosmetic(String displayName, String refName, CosmeticType cosmeticType) {
        this.displayName = displayName;
        this.refName = refName;
        this.cosmeticType = cosmeticType;
        this.isColorCosmetic = this instanceof ColorableCosmetic;

        Bukkit.getServer().getPluginManager().registerEvents(this, Thepit.INSTANCE);
    }

    public abstract ItemStack getRawDisplayItem();

    public void onEnable(PitPlayer pitPlayer) {}

    public void onDisable(PitPlayer pitPlayer) {}

    public void enable(PitPlayer pitPlayer) {
        loadColor(pitPlayer);
        onEnable(pitPlayer);
        CosmeticManager.sendEnableMessage(pitPlayer, this, getColor(pitPlayer));
    }

    public void disable(PitPlayer pitPlayer) {
        onDisable(pitPlayer);
        CosmeticManager.sendDisableMessage(pitPlayer, this);
    }

    public void loadColor(PitPlayer pitPlayer) {
        if(!(this instanceof ColorableCosmetic)) return;
        ColorableCosmetic colorableCosmetic = (ColorableCosmetic) this;
        colorableCosmetic.setParticleColor(pitPlayer.player, getColor(pitPlayer));
    }

    public ItemStack getDisplayStack(boolean equipped) {
        ItemStack itemStack = getRawDisplayItem();
        ALoreBuilder loreBuilder = new ALoreBuilder(itemStack.getItemMeta().getLore());
        if(equipped) {
            Misc.addEnchantGlint(itemStack);
            loreBuilder.addLore("", "&a这个化妆品很有活性!");
        } else {
            loreBuilder.addLore("", "&e点击装备!");
        }
        new AItemStackBuilder(itemStack).setLore(loreBuilder);
        return itemStack;
    }

    private boolean hasPermission(PitPlayer pitPlayer, ParticleColor particleColor) {
        String permission = "pitsim.cosmetic." + refName;
        if(particleColor == null) {
            for(ParticleColor testColor : ParticleColor.values()) {
                if(pitPlayer.player.hasPermission(permission + "." +
                        testColor.name().toLowerCase().replaceAll("_", ""))) return true;
            }
            return false;
        }
        permission += "." + particleColor.name().toLowerCase().replaceAll("_", "");
        return pitPlayer.player.hasPermission(permission);
    }

    public boolean isUnlocked(PitPlayer pitPlayer) {
        return isUnlocked(pitPlayer, null);
    }

    public boolean isUnlocked(PitPlayer pitPlayer, ParticleColor particleColor) {
        if(refName.contains("kyro")) return Misc.isKyro(pitPlayer.player.getUniqueId());
        if(Misc.isKyro(pitPlayer.player.getUniqueId()) || Misc.isWiji(pitPlayer.player.getUniqueId())) return true;
        if(isPermissionRequired) return hasPermission(pitPlayer, particleColor);
        PitPlayer.UnlockedCosmeticData unlockedCosmeticData = pitPlayer.unlockedCosmeticsMap.get(refName);
        if(unlockedCosmeticData == null) return false;
        if(isColorCosmetic && particleColor != null) {
            return unlockedCosmeticData.unlockedColors.contains(particleColor);
        }
        return true;
    }

    public List<ParticleColor> getUnlockedColors(PitPlayer pitPlayer) {
        List<ParticleColor> particleColors = new ArrayList<>();
        if(!isUnlocked(pitPlayer)) return particleColors;

        if(Misc.isKyro(pitPlayer.player.getUniqueId()) || Misc.isWiji(pitPlayer.player.getUniqueId())) {
            particleColors.addAll(Arrays.asList(ParticleColor.values()));
            return particleColors;
        }

        if(isPermissionRequired) {
            for(ParticleColor particleColor : ParticleColor.values()) {
                if(hasPermission(pitPlayer, particleColor)) particleColors.add(particleColor);
            }
            return particleColors;
        }

        List<ParticleColor> unorderedColors = pitPlayer.unlockedCosmeticsMap.get(refName).unlockedColors;
        for(ParticleColor particleColor : ParticleColor.values()) {
            if(unorderedColors.contains(particleColor)) particleColors.add(particleColor);
        }
        return particleColors;
    }

    //	Returns true if color cosmetic if any color is equipped
    public boolean isEnabled(PitPlayer pitPlayer) {
        if(pitPlayer == null || !isUnlocked(pitPlayer)) return false;
        if(!pitPlayer.equippedCosmeticMap.containsKey(cosmeticType.name())) return false;
        PitPlayer.EquippedCosmeticData cosmeticData = pitPlayer.equippedCosmeticMap.get(cosmeticType.name());
        if(cosmeticData == null) return false;
        return cosmeticData.refName.equals(refName);
    }

    public boolean isEnabled(PitPlayer pitPlayer, ParticleColor particleColor) {
        if(!isEnabled(pitPlayer) || !isUnlocked(pitPlayer, particleColor)) return false;
        PitPlayer.EquippedCosmeticData cosmeticData = pitPlayer.equippedCosmeticMap.get(cosmeticType.name());
        return cosmeticData.particleColor == particleColor;
    }

    public ParticleColor getColor(PitPlayer pitPlayer) {
        if(!isEnabled(pitPlayer)) return null;
        PitPlayer.EquippedCosmeticData cosmeticData = pitPlayer.equippedCosmeticMap.get(cosmeticType.name());
        if(cosmeticData == null) return null;
        return cosmeticData.particleColor;
    }

    public String getDisplayName() {
        return ChatColor.translateAlternateColorCodes('&', displayName);
    }

    public double random(double variance) {
        return Math.random() * variance - variance / 2;
    }

    public String getBountyClaimMessage(String killerName, String deadName, String bounty) {
        return null;
    }

    public boolean nearMid(Player player) {
        return !MapManager.inDarkzone(player) && MapManager.currentMap.getMid().distance(player.getLocation()) < MID_RANGE;
    }

    public void dropItem(ItemStack itemStack, Location location, double randomX, double randomY, double randomZ) {
        NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.setString(NBTTag.DROPPED_ITEM_UUID.getRef(), UUID.randomUUID().toString());
        nbtItem.setBoolean(NBTTag.CANNOT_PICKUP.getRef(), true);
        itemStack = nbtItem.getItem();

        location.clone().add(Misc.randomOffset(randomX), Misc.randomOffsetPositive(randomY), Misc.randomOffset(randomZ));
        Item item = location.getWorld().dropItemNaturally(location, itemStack);
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!item.isDead()) item.remove();
            }
        }.runTaskLater(Thepit.INSTANCE, 40);
    }
}
