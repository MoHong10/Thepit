package me.wisdom.thepit.megastreaks;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.battlepass.quests.daily.DailyMegastreakQuest;
import me.wisdom.thepit.controllers.DamageManager;
import me.wisdom.thepit.controllers.NonManager;
import me.wisdom.thepit.controllers.PlayerManager;
import me.wisdom.thepit.controllers.objects.Megastreak;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.events.HealEvent;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepit.misc.particles.HomeParticle;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.misc.AOutput;
import me.wisdom.thepitapi.misc.ASound;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.text.DecimalFormat;
import java.util.*;

public class RNGesus extends Megastreak {
    public static final int INSTABILITY_THRESHOLD = 1000;
    public static final double DAMAGE_PER_TICK = 0.75;

    public static RNGesus INSTANCE;
    private static final Map<Player, RNGesusInfo> rngesusInfoMap = new HashMap<>();

    public RNGesus() {
        super("&4RNGesus", "rngesus", 100, 50, 0);
        hasDailyLimit = true;
        INSTANCE = this;
    }

    @EventHandler
    public void onAttack(AttackEvent.Pre attackEvent) {
        if(!PlayerManager.isRealPlayer(attackEvent.getAttackerPlayer()) || !PlayerManager.isRealPlayer(attackEvent.getDefenderPlayer()) ||
                attackEvent.getAttacker() == attackEvent.getDefender()) return;
        if(hasMegastreak(attackEvent.getDefenderPlayer()) && attackEvent.getDefenderPitPlayer().getKills() >= INSTABILITY_THRESHOLD) attackEvent.setCancelled(true);
        if(hasMegastreak(attackEvent.getAttackerPlayer()) && attackEvent.getAttackerPitPlayer().getKills() >= INSTABILITY_THRESHOLD) attackEvent.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onHit(AttackEvent.Apply attackEvent) {
        if(!hasMegastreak(attackEvent.getAttackerPlayer())) return;
        PitPlayer pitPlayer = attackEvent.getAttackerPitPlayer();
        RNGesusInfo rngesusInfo = getRNGesusInfo(attackEvent.getAttackerPlayer());

        if(pitPlayer.getKills() + 1 >= INSTABILITY_THRESHOLD) {
            attackEvent.multipliers.clear();
            attackEvent.increaseCalcDecrease.clear();
            attackEvent.increase = 0;
            attackEvent.increasePercent = 0;
            attackEvent.decreasePercent = 0;
            double damage = getDamage(rngesusInfo.realityMap.get(Reality.DAMAGE).getLevel());
            attackEvent.increase += damage;

            if(attackEvent.getWrapperEvent().getDamager() instanceof Slime && Math.random() > 0.1) return;

            List<Entity> entities = attackEvent.getDefender().getNearbyEntities(20, 20, 20);
            Collections.shuffle(entities);
            int count = 0;
            for(Entity entity : entities) {
                if(count++ >= 10) break;
                if(!(entity instanceof Player)) continue;
                Player target = (Player) entity;
                if(NonManager.getNon(target) == null) continue;

                BukkitRunnable callback = new BukkitRunnable() {
                    @Override
                    public void run() {
                        double chance = damage / target.getMaxHealth();
                        if(Math.random() < chance) DamageManager.fakeKill(attackEvent.getAttacker(), target);
                    }
                };

                if(attackEvent.isAttackerPlayer()) new HomeParticle(attackEvent.getAttackerPlayer(),
                        attackEvent.getDefender().getLocation().add(0, 1, 0), target, 0.5, callback);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onKill(KillEvent killEvent) {
        if(!hasMegastreak(killEvent.getKillerPlayer())) return;
        PitPlayer pitPlayer = killEvent.getKillerPitPlayer();
        if(pitPlayer.getKills() < INSTABILITY_THRESHOLD) return;
        RNGesusInfo rngesusInfo = getRNGesusInfo(killEvent.getKillerPlayer());

        killEvent.xpMultipliers.clear();
        killEvent.maxXPMultipliers.clear();
        killEvent.xpReward = 0;
        killEvent.xpCap = 0;
        killEvent.xpReward += getXP(rngesusInfo.realityMap.get(Reality.XP).getLevel() * 2);
        killEvent.xpCap += getXP(rngesusInfo.realityMap.get(Reality.XP).getLevel());

        killEvent.goldMultipliers.clear();
        killEvent.goldReward = 0;
        killEvent.goldReward += getGold(rngesusInfo.realityMap.get(Reality.GOLD).getLevel());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onKill2(KillEvent killEvent) {
        if(!hasMegastreak(killEvent.getKillerPlayer())) return;
        PitPlayer pitPlayer = killEvent.getKillerPitPlayer();
        RNGesusInfo rngesusInfo = getRNGesusInfo(killEvent.getKillerPlayer());

        if((pitPlayer.getKills() + 1) % 100 == 0 && pitPlayer.getKills() + 1 < INSTABILITY_THRESHOLD)
            shiftReality(killEvent.getKillerPlayer());
        if(pitPlayer.getKills() + 1 == INSTABILITY_THRESHOLD) destabilize(killEvent.getKillerPlayer());

        if(rngesusInfo.reality == Reality.XP) {
            rngesusInfo.realityMap.get(rngesusInfo.reality).progression += killEvent.getFinalXp();
        } else if(rngesusInfo.reality == Reality.GOLD) {
            rngesusInfo.realityMap.get(rngesusInfo.reality).progression += killEvent.getFinalGold();
        }
        if(pitPlayer.getKills() + 1 < INSTABILITY_THRESHOLD) pitPlayer.updateXPBar();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void attack(AttackEvent.Apply attackEvent) {
        if(!hasMegastreak(attackEvent.getAttackerPlayer())) return;
        if(NonManager.getNon(attackEvent.getDefender()) == null) return;
        PitPlayer pitPlayer = attackEvent.getAttackerPitPlayer();
        if(!pitPlayer.isOnMega()) return;
        RNGesusInfo rngesusInfo = getRNGesusInfo(attackEvent.getAttackerPlayer());

        if(rngesusInfo.reality == Reality.DAMAGE) {
            rngesusInfo.realityMap.get(rngesusInfo.reality).progression += attackEvent.getFinalPitDamage();
        } else if(rngesusInfo.reality == Reality.ABSORPTION) {
            rngesusInfo.realityMap.get(rngesusInfo.reality).progression += attackEvent.trueDamage;
        }
        if(pitPlayer.getKills() + 1 < INSTABILITY_THRESHOLD) pitPlayer.updateXPBar();
    }

    @EventHandler
    public void onHeal(HealEvent event) {
        Player player = event.getPlayer();
        if(!hasMegastreak(player)) return;
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        if(pitPlayer.getKills() + 1 < INSTABILITY_THRESHOLD) return;
        event.multipliers.add(0.0);
    }

    @Override
    public void proc(Player player) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        RNGesusInfo rngesusInfo = getRNGesusInfo(player);

        Sounds.MEGA_RNGESUS.play(player.getLocation());

        rngesusInfo.runnable = new BukkitRunnable() {
            int count = 0;
            @Override
            public void run() {
                if(pitPlayer.getKills() > INSTABILITY_THRESHOLD) {
                    for(int i = 0; i < 3; i++) {
                        player.getWorld().playEffect(player.getLocation()
                                .add(Math.random() * 0.2 - 0.1, Math.random() * 0.2 + 2.1, Math.random() * 0.2 - 0.1), Effect.HAPPY_VILLAGER, 1);
                    }
                }
                if(count++ % 5 != 0) return;

                if (pitPlayer.getKills() < INSTABILITY_THRESHOLD && pitPlayer.isOnMega()) {
                    DecimalFormat decimalFormat = new DecimalFormat("#,####,##0");
                    switch (rngesusInfo.reality) {
                        case NONE:
                            String realityString = Misc.distortMessage("现实看起来正常", 0.2);
                            Misc.sendActionBar(player, "&7" + realityString);
                            break;
                        case XP:
                            double xp = rngesusInfo.realityMap.get(Reality.XP).progression;
                            Misc.sendActionBar(player, "&b经验: " + decimalFormat.format(xp));
                            break;
                        case GOLD:
                            double gold = rngesusInfo.realityMap.get(Reality.GOLD).progression;
                            Misc.sendActionBar(player, "&6金币: " + decimalFormat.format(gold));
                            break;
                        case DAMAGE:
                            double damage = rngesusInfo.realityMap.get(Reality.DAMAGE).progression;
                            Misc.sendActionBar(player, "&c伤害: " + decimalFormat.format(damage));
                            break;
                        case ABSORPTION:
                            double absorption = rngesusInfo.realityMap.get(Reality.ABSORPTION).progression;
                            Misc.sendActionBar(player, "&9真实伤害: " + decimalFormat.format(absorption));
                    }
                } else {
                    EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
                    if(nmsPlayer.getAbsorptionHearts() > 0) {
                        nmsPlayer.setAbsorptionHearts((float) Math.max(nmsPlayer.getAbsorptionHearts() - DAMAGE_PER_TICK, 0));
                    } else if(player.getHealth() > DAMAGE_PER_TICK) {
                        player.setHealth(player.getHealth() - DAMAGE_PER_TICK);
                    } else {
                        DamageManager.killPlayer(player);
                    }
                }
            }
        }.runTaskTimer(Thepit.INSTANCE, 0L, 4L);
    }

    @Override
    public void reset(Player player) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        RNGesusInfo rngesusInfo = getRNGesusInfo(player);
        rngesusInfoMap.remove(player);

        if(pitPlayer.getKills() >= INSTABILITY_THRESHOLD) {
            pitPlayer.stats.rngesusCompleted++;
            DailyMegastreakQuest.INSTANCE.onMegastreakComplete(pitPlayer);
        }

        if(!pitPlayer.isOnMega()) return;

//		Has to be after the isOnMega check because it resets the megastreak
        if(pitPlayer.getKills() >= INSTABILITY_THRESHOLD) {
            PitPlayer.MegastreakLimit limit = pitPlayer.getMegastreakCooldown(this);
            limit.completeStreak(pitPlayer);
        }

        int xp = getXP(rngesusInfo.realityMap.get(Reality.XP).getLevel());
        double gold = getGold(rngesusInfo.realityMap.get(Reality.GOLD).getLevel());
        double damage = getDamage(rngesusInfo.realityMap.get(Reality.DAMAGE).getLevel());
        float absorption = getAbsorption(rngesusInfo.realityMap.get(Reality.ABSORPTION).getLevel());
        new BukkitRunnable() {
            @Override
            public void run() {
                DecimalFormat decimalFormat = new DecimalFormat("#,##0");
                AOutput.send(pitPlayer.player, "&b经验 &7增加了 &b" + decimalFormat.format(xp));
                AOutput.send(pitPlayer.player, "&6金币 &7增加了 &6" + decimalFormat.format(gold));
                AOutput.send(pitPlayer.player, "&c伤害 &7增加了 &c" + decimalFormat.format(damage));
                AOutput.send(pitPlayer.player, "&6吸收 &7增加了 &9" + decimalFormat.format(absorption));
            }
        }.runTaskLater(Thepit.INSTANCE, 20L);

        rngesusInfo.generateRealityOrder();
        rngesusInfo.realityMap.clear();
        for(Reality value : Reality.values()) rngesusInfo.realityMap.put(value, new RealityInfo(value));
        rngesusInfo.reality = Reality.NONE;

        if(rngesusInfo.runnable != null) rngesusInfo.runnable.cancel();
    }

    @Override
    public int getMaxDailyStreaks(PitPlayer pitPlayer) {
        return pitPlayer.player.hasPermission("group.eternal") ? 2 : 1;
    }

    public void shiftReality(Player player) {
        RNGesusInfo rngesusInfo = getRNGesusInfo(player);
        rngesusInfo.reality = rngesusInfo.generatedRealityOrder.remove(0);
        if (rngesusInfo.reality == null) return;
        AOutput.send(player, getCapsDisplayName() + "!&7 现实扭曲: " + rngesusInfo.reality.displayName + "&7!");
        ASound.play(player, Sound.ENTITY_PLAYER_HURT_ON_FIRE, 1000, 0.5F);
        Misc.applyPotionEffect(player, PotionEffectType.BLINDNESS, 40, 0, true, false);
    }

    public void destabilize(Player player) {
        RNGesusInfo rngesusInfo = getRNGesusInfo(player);
        rngesusInfo.reality = Reality.NONE;
        String message = "现实不稳定。它会让你变得更强，还是让你陷入无尽的时间虚空";
        AOutput.send(player, getCapsDisplayName() + "!&7 " + message);
        Sounds.RNGESUS_DESTABILIZE.play(player);

        EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        nmsPlayer.setAbsorptionHearts(getAbsorption(rngesusInfo.realityMap.get(Reality.ABSORPTION).getLevel()));

        DecimalFormat decimalFormat = new DecimalFormat("#,####,##0");
        int xp = getXP(rngesusInfo.realityMap.get(Reality.XP).getLevel());
        double gold = getGold(rngesusInfo.realityMap.get(Reality.GOLD).getLevel());
        double damage = getDamage(rngesusInfo.realityMap.get(Reality.DAMAGE).getLevel());
        float absorption = getAbsorption(rngesusInfo.realityMap.get(Reality.ABSORPTION).getLevel());
        AOutput.send(player, "&b经验 &7增加了 &b" + decimalFormat.format(xp));
        AOutput.send(player, "&6金币 &7增加了 &6" + decimalFormat.format(gold));
        AOutput.send(player, "&c伤害 &7增加了 &c" + decimalFormat.format(damage));
        AOutput.send(player, "&6吸收 &7增加了 &9" + decimalFormat.format(absorption));
    }

    public static RNGesusInfo getRNGesusInfo(Player player) {
        rngesusInfoMap.putIfAbsent(player, new RNGesusInfo());
        return rngesusInfoMap.get(player);
    }

    public int getXP(double progression) {
        return (int) (progression);
    }

    public double getGold(double progression) {
        return progression;
    }

    public double getDamage(double progression) {
        return progression * 0.10;
    }

    public float getAbsorption(double progression) {
        return (float) progression;
    }

    @Override
    public String getPrefix(Player player) {
        return getRNGesusInfo(player).reality.prefix;
    }

    @Override
    public ItemStack getBaseDisplayStack(Player player) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        PitPlayer.MegastreakLimit limit = pitPlayer.getMegastreakCooldown(this);
        return new AItemStackBuilder(limit.isAtLimit(pitPlayer) ? Material.ENDER_PEARL : Material.EYE_OF_ENDER)
                .getItemStack();
    }

    @Override
    public void addBaseDescription(PitLoreBuilder loreBuilder, PitPlayer pitPlayer) {
        loreBuilder.addLore(
                "&7触发时:",
                "&a\u25a0 &7免疫于将你 &emove &7的附魔",
                "&a\u25a0 &e转变 &7到一个随机现实 (&bXP&7, &6金币&7,",
                "   &c伤害&7, &e吸收&7)",
                "&c\u25a0 &7为此连击开始 &f1小时 &7的冷却时间",
                "",
                "&7每100击:",
                "&a\u25a0 &e转变 &7到一个随机现实 (&bXP&7, &6金币&7,",
                "   &c伤害&7, &e吸收&7)",
                "&a\u25a0 &7在连击过程中为每个现实积累数据",
                "",
                "&7在1,000击时:",
                "&e&k\u25a0&7 现实 " + Misc.distortMessage("&f不稳定", 0.2),
                "&a\u25a0 &7将从每个现实中获得的统计数据用于",
                "   &7&c伤害&7, &9健康&7, &bXP&7 和 &6金币&7",
                "&c\u25a0 &7你将无法再治疗",
                "",
                "&7死亡时:",
                "&e\u25a0 &7查看你连击的回顾"
        );
    }

    @Override
    public String getSummary() {
        return getCapsDisplayName() + "&7 允许你进入四种不同的随机现实，&bXP&7, &6金币&7, &c伤害&7 和 &9吸收&7。 " +
                "在 &c1,000 连击&7时，获得你在各个现实中赚取的增益，但你将不再 &cheal&7。在整个 &c大连击&7 期间，你免疫于会 &emove&7 " +
                "你的附魔";
    }

    public enum Reality {
        NONE("&e异常", "&4&lRNGSUS", 1),
        XP("&bXP", "&b&lRNG&4&lSUS", 0.03),
        GOLD("&6金币", "&6&lRNG&4&lSUS", 0.2),
        DAMAGE("&c伤害", "&c&lRNG&4&lSUS", 1),
        ABSORPTION("&6吸收", "&9&lRNG&4&lSUS", 0.3);

        public final String displayName;
        public final String prefix;
        public final double baseMultiplier;

        Reality(String displayName, String prefix, double baseMultiplier) {
            this.displayName = displayName;
            this.prefix = prefix;
            this.baseMultiplier = baseMultiplier;
        }
    }

    public static class RealityInfo {
        public Reality reality;
        public double progression;

        public RealityInfo(Reality reality) {
            this.reality = reality;
        }

        public int getLevel() {
            double modifiableProgression = progression / reality.baseMultiplier;
            int level = 0;
            while(modifiableProgression >= level + 1) {
                modifiableProgression -= level + 1;
                level++;
            }
            return level;
        }

        public double getProgression(int level) {
            int progression = 0;
            for(int i = 0; i < level + 1; i++) {
                progression += i;
            }
            return progression * reality.baseMultiplier;
        }
    }

    public static class RNGesusInfo {
        public List<Reality> generatedRealityOrder = new ArrayList<>();
        public Map<Reality, RealityInfo> realityMap = new HashMap<>();
        public Reality reality = Reality.NONE;
        public BukkitTask runnable;

        public RNGesusInfo() {
            for(Reality value : Reality.values()) realityMap.put(value, new RealityInfo(value));
            generateRealityOrder();
        }

        public void generateRealityOrder() {
            generatedRealityOrder.clear();
            for(Reality value : Reality.values()) {
                if(value == Reality.NONE) continue;
                generatedRealityOrder.add(value);
                generatedRealityOrder.add(value);
            }
            for(int i = generatedRealityOrder.size(); i < 9; i++) {
                List<Reality> possibleRealities = new ArrayList<>(Arrays.asList(Reality.values()));
                possibleRealities.remove(Reality.NONE);
                generatedRealityOrder.add(possibleRealities.get(new Random().nextInt(possibleRealities.size())));
            }
            Collections.shuffle(generatedRealityOrder);
        }
    }
}
