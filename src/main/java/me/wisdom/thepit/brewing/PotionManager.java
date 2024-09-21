package me.wisdom.thepit.brewing;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.brewing.objects.BrewingIngredient;
import me.wisdom.thepit.brewing.objects.PotionEffect;
import de.tr7zw.nbtapi.NBTItem;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.darkzone.progression.ProgressionManager;
import me.wisdom.thepit.darkzone.progression.skillbranches.BrewingBranch;
import me.wisdom.thepit.enums.NBTTag;
import me.wisdom.thepit.enums.PitEntityType;
import me.wisdom.thepit.events.PitJoinEvent;
import me.wisdom.thepit.items.mobdrops.SpiderEye;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.misc.AOutput;
import me.wisdom.thepitapi.misc.AUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.*;

public class PotionManager implements Listener {

    public static List<PotionEffect> potionEffectList = new ArrayList<>();
    public static Map<Player, Integer> playerIndex = new HashMap<>();
    public static Map<Player, BossBar> bossBars = new HashMap<>();
    public static List<Entity> potions = new ArrayList<>();
    public static int i = 0;

    static {
        new BukkitRunnable() {
            @Override
            public void run() {
                for(PotionEffect potionEffect : new ArrayList<>(potionEffectList)) potionEffect.tick();
            }
        }.runTaskTimer(Thepit.INSTANCE, 1, 1);

        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    List<PotionEffect> effects = getPotionEffects(player);
                    if(effects.size() == 0) continue;
//					TODO: Readd
//					if(OldBossManager.activePlayers.contains(player)) {
//						hideActiveBossBar(PitSim.adventure.player(player), player);
//						hideActiveBossBar(PitSim.adventure.player(player), player);
//						continue;
//					}

                    playerIndex.putIfAbsent(player, 0);
                    int index = playerIndex.get(player);

                    while(index >= effects.size()) {
                        index--;
                    }

                    StringBuilder builder = new StringBuilder();
                    for(PotionEffect effect : effects) {
                        if(effect == effects.get(index))
                            builder.append(effect.potionType.color + "" + ChatColor.BOLD + effect.potionType.name.toUpperCase(Locale.ROOT)).append(" ").append(AUtil.toRoman(effect.potency.tier));
                        else
                            builder.append(effect.potionType.color + effect.potionType.name).append(" ").append(AUtil.toRoman(effect.potency.tier));
                        if(effect != effects.get(effects.size() - 1)) builder.append(ChatColor.GRAY + ", ");
                    }
                    float progress = (float) effects.get(index).getTimeLeft() / (float) effects.get(index).getOriginalTime();

                    int maxI = effects.size() - 1;
                    if(i == 60) {
                        if(playerIndex.get(player) + 1 > maxI) {
                            playerIndex.put(player, 0);
                        } else playerIndex.put(player, playerIndex.get(player) + 1);
                    }

                    if(!bossBars.containsKey(player))
                        showMyBossBar(Thepit.adventure.player(player), player, builder.toString(), progress);
                    else {
                        bossBars.get(player).name(Component.text(builder.toString()));
                        bossBars.get(player).progress(progress);
                    }
                    i++;
                    if(i > 20 * 3) i = 0;
                }
            }
        }.runTaskTimer(Thepit.INSTANCE, 2, 2);
    }

    public static ItemStack createPotion(BrewingIngredient identifier, BrewingIngredient potency, BrewingIngredient duration) {
        Potion rawPotion = new Potion(identifier.potionType);
        ItemStack potionStack = rawPotion.toItemStack(1);
        PotionMeta meta = (PotionMeta) potionStack.getItemMeta();
        meta.addCustomEffect(new org.bukkit.potion.PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1, 0, false, false), true);
        meta.setDisplayName(identifier.color + "等级 " + AUtil.toRoman(potency.tier) + " " + identifier.name + " 药水");
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        List<String> lore = new ArrayList<>(identifier.getPotencyLore(potency));
        lore.add("");
        if (identifier instanceof SpiderEye)
            lore.add(ChatColor.GRAY + "持续时间: " + ChatColor.WHITE + "瞬间！");
        else
            lore.add(ChatColor.GRAY + "持续时间: " + ChatColor.WHITE + Misc.ticksToTime(identifier.getDuration(duration)));
        lore.add("");
        lore.add(identifier.color + "被污染的药水");
        meta.setLore(lore);
        potionStack.setItemMeta(meta);
        NBTItem nbtItem = new NBTItem(potionStack, true);
        nbtItem.setInteger(NBTTag.POTION_IDENTIFIER.getRef(), identifier.tier);
        nbtItem.setInteger(NBTTag.POTION_POTENCY.getRef(), potency.tier);
        nbtItem.setInteger(NBTTag.POTION_DURATION.getRef(), duration.tier);
//		nbtItem.setBoolean(NBTTag.DROP_CONFIRM.getRef(), true);
        me.wisdom.thepit.items.misc.Potion potion = new me.wisdom.thepit.items.misc.Potion();
        potion.updateItem(nbtItem.getItem());
//		return potionStack; //temp
        return potion.getItem(nbtItem.getItem());
    }

    public static ItemStack createSplashPotion(BrewingIngredient identifier, BrewingIngredient potency, BrewingIngredient duration) {
        return createSplashPotion(createPotion(identifier, potency, duration));
    }

    public static ItemStack createSplashPotion(ItemStack potionStack) {
        NBTItem nbtItem = new NBTItem(potionStack);
        nbtItem.getItem().setDurability((short) (nbtItem.getItem().getDurability() + 16384));
        nbtItem.setBoolean(NBTTag.IS_SPLASH_POTION.getRef(), true);
        return nbtItem.getItem();
    }

    @EventHandler
    public void onPotionDrink(PlayerItemConsumeEvent event) {
        if(event.getItem().getType() != Material.POTION) return;
        Player player = event.getPlayer();

        event.setCancelled(true);

        ItemStack potion = event.getItem();
        NBTItem nbtItem = new NBTItem(potion);
        if(!nbtItem.hasKey(NBTTag.POTION_IDENTIFIER.getRef())) return;

        BrewingIngredient identifier = BrewingIngredient.getIngredientFromTier(nbtItem.getInteger(NBTTag.POTION_IDENTIFIER.getRef()));
        BrewingIngredient potency = BrewingIngredient.getIngredientFromTier(nbtItem.getInteger(NBTTag.POTION_POTENCY.getRef()));
        BrewingIngredient duration = BrewingIngredient.getIngredientFromTier(nbtItem.getInteger(NBTTag.POTION_DURATION.getRef()));
        assert identifier != null;
        assert potency != null;

        if (hasStrongerEffect(player, identifier, potency)) {
            AOutput.send(player, "&5&l药水！&7你已经有了更强等级的效果！");
            return;
        }

        if (potency.tier > getMaxPotionTier(player) && identifier.isPositive) {
            AOutput.send(player, "&5&l药水！&7 你无法处理如此强大的效果！");
            return;
        }

        replaceWeakerEffects(player, identifier, potency);
        player.setItemInHand(new ItemStack(Material.AIR));

        if(identifier instanceof SpiderEye) {
            identifier.administerEffect(player, potency, 0);
        } else {
            potionEffectList.add(new PotionEffect(player, identifier, potency, duration));
        }
    }

    @EventHandler
    public void onSplash(PotionSplashEvent event) {
        ItemStack potion = event.getPotion().getItem();
        NBTItem nbtItem = new NBTItem(potion);
        if(!nbtItem.hasKey(NBTTag.POTION_IDENTIFIER.getRef())) return;

        BrewingIngredient identifier = BrewingIngredient.getIngredientFromTier(nbtItem.getInteger(NBTTag.POTION_IDENTIFIER.getRef()));
        BrewingIngredient potency = BrewingIngredient.getIngredientFromTier(nbtItem.getInteger(NBTTag.POTION_POTENCY.getRef()));
        BrewingIngredient duration = BrewingIngredient.getIngredientFromTier(nbtItem.getInteger(NBTTag.POTION_DURATION.getRef()));
        assert identifier != null;
        assert potency != null;

        int initialDuration = identifier.getDuration(duration);

        List<Player> affectedPlayers = new ArrayList<>();
        for(LivingEntity affectedEntity : event.getAffectedEntities()) {
            if(!Misc.isEntity(affectedEntity, PitEntityType.REAL_PLAYER)) continue;
            Player player = (Player) affectedEntity;

            if (hasStrongerEffect(player, identifier, potency)) {
                AOutput.send(player, "&5&l药水！&7 你已经有了更强等级的效果！");
                continue;
            }

            if (potency.tier > getMaxPotionTier(player) && identifier.isPositive) {
                AOutput.send(player, "&5&l药水！&7 你无法处理如此强大的效果！");
                continue;
            }

            affectedPlayers.add(player);
        }

        int effectiveDuration = (int) (Math.max(1 - 0.2 * (affectedPlayers.size() - 1), 0.2) * initialDuration);

        for(Player player : affectedPlayers) {
            replaceWeakerEffects(player, identifier, potency);

            if(identifier instanceof SpiderEye) {
                identifier.administerEffect(player, potency, 0);
            } else {
                potionEffectList.add(new PotionEffect(player, identifier, potency, effectiveDuration));
            }
        }
    }

    public static void savePotions(PitPlayer pitPlayer, boolean logout) {
        List<PotionEffect> toExpire = new ArrayList<>();
        for(PotionEffect potionEffect : potionEffectList) {
            if(potionEffect.player.getUniqueId().equals(pitPlayer.uuid)) toExpire.add(potionEffect);
        }

        for(PotionEffect potionEffect : toExpire) {

            if(logout) potionEffect.onExpire(true);

            String time = String.valueOf(System.currentTimeMillis());

            pitPlayer.potionStrings.add(potionEffect.potionType.name + ":" + potionEffect.potency.tier + ":" + potionEffect.getTimeLeft() + ":" + time);
        }
    }

    @EventHandler
    public void onJoin(PitJoinEvent event) {
        Player player = event.getPlayer();

        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        List<String> potionStrings = pitPlayer.potionStrings;
        if(potionStrings == null || potionStrings.isEmpty()) return;

        main:
        for(String potionString : potionStrings) {
            String[] split = potionString.split(":");
            if(split.length != 4) continue;

            BrewingIngredient potionType = BrewingIngredient.getIngredientFromName(split[0]);
            if(potionType == null) continue;

            int tier = Integer.parseInt(split[1]);
            int timeLeft = Integer.parseInt(split[2]);
            long time = Long.parseLong(split[3]);

            long passedTicks = ((System.currentTimeMillis() - time) / 1000) * 20;
            if(passedTicks > timeLeft) continue;

            for(PotionEffect potionEffect : getPotionEffects(player)) {
                if(potionEffect.potionType == potionType) continue main;
            }

            PotionEffect potionEffect = new PotionEffect(player, potionType, BrewingIngredient.getIngredientFromTier(tier), (int) (timeLeft - passedTicks));
            potionEffectList.add(potionEffect);
        }

        pitPlayer.potionStrings.clear();
    }

    public boolean hasStrongerEffect(Player player, BrewingIngredient identifier, BrewingIngredient potency) {
        for(PotionEffect potionEffect : potionEffectList) {
            if(potionEffect.player != player) continue;
            if(potionEffect.potionType != identifier) continue;

            if(potionEffect.potency.tier > potency.tier) return true;
        }
        return false;
    }

    public void replaceWeakerEffects(Player player, BrewingIngredient identifier, BrewingIngredient potency) {
        for(PotionEffect potionEffect : potionEffectList) {
            if(potionEffect.player != player) continue;
            if(potionEffect.potionType != identifier) continue;

            if(potionEffect.potency.tier <= potency.tier) {
                potionEffect.onExpire(false);
                return;
            }
        }
    }

    public static List<PotionEffect> getPotionEffects(Player player) {
        List<PotionEffect> effects = new ArrayList<>();
        for(PotionEffect potionEffect : potionEffectList) {
            if(potionEffect.player == player) effects.add(potionEffect);
        }
        return effects;
    }

    public static PotionEffect getEffect(LivingEntity player, BrewingIngredient type) {
        if(!(player instanceof Player)) return null;
        for(PotionEffect potionEffect : potionEffectList) {
            if(potionEffect.player == player && potionEffect.potionType == type) return potionEffect;
        }
        return null;
    }

    public static void showMyBossBar(final @NonNull Audience player, Player realPlayer, String text, float progress) {
        final Component name = Component.text(text);
        final BossBar fullBar = BossBar.bossBar(name, progress, BossBar.Color.PINK, BossBar.Overlay.PROGRESS);

        player.showBossBar(fullBar);
        bossBars.put(realPlayer, fullBar);
    }

    public static void hideActiveBossBar(final @NonNull Audience player, Player realPlayer) {
        player.hideBossBar(bossBars.get(realPlayer));
        bossBars.remove(realPlayer);
    }

    public static int getMaxPotionTier(Player player) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        return 4 + ProgressionManager.getUnlockedLevel(pitPlayer, BrewingBranch.INSTANCE.firstPath);
    }
}