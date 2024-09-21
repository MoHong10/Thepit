package me.wisdom.thepit.enchants.tainted.chestplate;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.*;
import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.cosmetics.particles.FireworkSparkParticle;
import me.wisdom.thepit.cosmetics.particles.ParticleColor;
import me.wisdom.thepit.cosmetics.particles.RedstoneParticle;
import me.wisdom.thepit.darkzone.DarkzoneManager;
import me.wisdom.thepit.darkzone.PitMob;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.events.ManaRegenEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepit.misc.effects.PacketBlock;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.*;

public class Persephone extends PitEnchant {
    public static Persephone INSTANCE;
    public static Map<Player, List<FlowerBunch>> flowerMap = new HashMap<>();

    public static final int MIN_FLOWER_COUNT = 8;
    public static final int MAX_FLOWER_COUNT = 10;
    public static final double FLOWER_SPAWN_RADIUS = 5;
    public static final double EFFECT_RADIUS = 7;
    public static final int DEFAULT_EFFECT_DURATION = 20 * 15;

    public Persephone() {
        super("Persephone", true, ApplyType.CHESTPLATES,
                "persephone", "per", "pers", "perse", "perseph");
        isTainted = true;
        INSTANCE = this;

        if(!isEnabled()) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    int enchantLvl = EnchantManager.getEnchantLevel(player, INSTANCE);
                    if(enchantLvl != 0) continue;
                    if(flowerMap.containsKey(player)) {
                        List<FlowerBunch> flowerBunches = flowerMap.remove(player);
                        for(FlowerBunch flowerBunch : flowerBunches) flowerBunch.remove();
                    }
                }
            }
        }.runTaskTimer(Thepit.INSTANCE, 0L, 1L);

        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    int enchantLvl = EnchantManager.getEnchantLevel(player, INSTANCE);
                    if(enchantLvl == 0) continue;

                    PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);

                    List<FlowerType> flowerTypes = getFlowersInProximity(player);
                    if(flowerTypes.contains(FlowerType.POPPY)) pitPlayer.heal(getHealing());
                }
            }
        }.runTaskTimer(Thepit.INSTANCE, 0L, 20L);

        new BukkitRunnable() {
            int count = 0;
            @Override
            public void run() {
                for(Map.Entry<Player, List<FlowerBunch>> entry : flowerMap.entrySet()) {
                    for(FlowerBunch flowerBunch : entry.getValue()) {
                        if(flowerBunch.flowerType != FlowerType.AZURE_BLUET || Math.random() > getLightningPercent() / 100.0) continue;
                        List<LivingEntity> potentialTargets = new ArrayList<>();
                        for(Entity entity : flowerBunch.centerLocation.getWorld()
                                .getNearbyEntities(flowerBunch.centerLocation, EFFECT_RADIUS, EFFECT_RADIUS, EFFECT_RADIUS)) {
                            if(!(entity instanceof LivingEntity) || entity == flowerBunch.player) continue;
                            LivingEntity livingEntity = (LivingEntity) entity;
                            PitMob pitMob = DarkzoneManager.getPitMob(livingEntity);
                            if(pitMob == null && !PlayerManager.isRealPlayer(livingEntity)) continue;
                            potentialTargets.add(livingEntity);
                        }

                        if(potentialTargets.isEmpty()) continue;
                        if(count % 2 == 0 && potentialTargets.size() < 3) continue;
                        LivingEntity randomTarget = potentialTargets.get(new Random().nextInt(potentialTargets.size()));
                        randomTarget.getWorld().strikeLightningEffect(randomTarget.getLocation());

                        double damage = PlayerManager.isRealPlayer(randomTarget) ? getLightningPlayerDamage() : getLightningMobDamage();
                        DamageManager.createIndirectAttack(entry.getKey(), randomTarget, 0, null, null,
                                attackEvent -> attackEvent.trueDamage = damage);
                    }
                }
                count++;
            }
        }.runTaskTimer(Thepit.INSTANCE, 0L, 10L);
    }

    @EventHandler
    public void onManaRegen(ManaRegenEvent event) {
        Player player = event.getPlayer();

        int enchantLvl = EnchantManager.getEnchantLevel(player, this);
        if(enchantLvl == 0) return;

        List<FlowerType> flowerTypes = getFlowersInProximity(player);
        if(!flowerTypes.contains(FlowerType.ALLIUM)) return;

        event.multipliers.add(1 + (getManaIncreasePercent() / 100.0));
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!canApply(attackEvent)) return;

        int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
        if(enchantLvl == 0) return;

        List<FlowerType> flowerTypes = getFlowersInProximity(attackEvent.getAttackerPlayer());
        if(!flowerTypes.contains(FlowerType.ORANGE_TULIP)) return;

        attackEvent.increasePercent += getDamageIncreasePercent();
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if(player.isSneaking() || player.isFlying()) return;

        int enchantLvl = EnchantManager.getEnchantLevel(player, this);
        if(enchantLvl == 0) return;

        if(SpawnManager.isInSpawn(player)) {
            AOutput.error(player, "&c&lOOPS!&7 You cannot do this in spawn");
            return;
        }

        Cooldown cooldown = getCooldown(player, getCooldownSeconds(enchantLvl) * 20);
        if(cooldown.isOnCooldown()) return;
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        if(!pitPlayer.useManaForSpell(getManaCost(enchantLvl))) {
            Sounds.NO.play(event.getPlayer());
            return;
        }
        cooldown.restart();

        flowerMap.putIfAbsent(player, new ArrayList<>());
        List<FlowerBunch> flowerBunches = flowerMap.get(player);
        FlowerType flowerType = FlowerType.random();
        flowerBunches.add(new FlowerBunch(player, flowerType));
    }

    public static List<FlowerType> getFlowersInProximity(Player player) {
        List<FlowerType> flowerTypes = new ArrayList<>();
        if(!flowerMap.containsKey(player)) return flowerTypes;
        List<FlowerBunch> flowerBunches = flowerMap.get(player);

        for(FlowerBunch flowerBunch : flowerBunches) {
            if(player.getWorld() != flowerBunch.centerLocation.getWorld() ||
                    player.getLocation().distance(flowerBunch.centerLocation) > EFFECT_RADIUS) continue;
            if(flowerTypes.contains(flowerBunch.flowerType)) continue;
            flowerTypes.add(flowerBunch.flowerType);
        }

        return flowerTypes;
    }

    public static double getShieldRegenMultiplier(Player player) {
        int enchantLvl = EnchantManager.getEnchantLevel(player, INSTANCE);
        if(enchantLvl == 0) return 1;

        List<FlowerType> flowerTypes = getFlowersInProximity(player);
        if(!flowerTypes.contains(FlowerType.BLUE_ORCHID)) return 1;

        return getShieldRegenMultiplier();
    }

    public static boolean shouldPreventDeath(Player player) {
        if(!PlayerManager.isRealPlayer(player)) return false;

        int enchantLvl = EnchantManager.getEnchantLevel(player, INSTANCE);
        if(enchantLvl == 0) return false;

        List<FlowerType> flowerTypes = getFlowersInProximity(player);
        return flowerTypes.contains(FlowerType.DANDELION);
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Sneaking plants a handful of flower for &b" + getManaCost(enchantLvl) + " mana&7 (" +
                        getCooldownSeconds(enchantLvl) + "s cooldown). Each of the 6 different types of flowers " +
                        "gives an effect when in their proximity"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is a &5Darkzone &7enchant that " +
                "allows you to plan flowers when sneaking. Each type of flower has a unique effect!";
    }

    public static int getCooldownSeconds(int enchantLvl) {
        return 5;
    }

    public static int getManaCost(int enchantLvl) {
        return Math.max(30 - enchantLvl * 5, 0);
    }

    public static double getHealing() {
        return 1;
    }

    public static double getShieldRegenMultiplier() {
        return 2;
    }

    public static int getManaIncreasePercent() {
        return 50;
    }

    public static double getLightningPercent() {
        return 50;
    }

    public static double getLightningPlayerDamage() {
        return 2;
    }

    public static double getLightningMobDamage() {
        return 10;
    }

    public static int getDamageIncreasePercent() {
        return 50;
    }

    private static class FlowerBunch {
        public Player player;
        public FlowerType flowerType;
        public Location centerLocation;
        public int length = DEFAULT_EFFECT_DURATION;

        public List<PacketBlock> flowerBlocks = new ArrayList<>();

        public FlowerBunch(Player player, FlowerType flowerType) {
            this.player = player;
            this.flowerType = flowerType;
            this.centerLocation = player.getLocation();

            spawn();

            new BukkitRunnable() {
                @Override
                public void run() {
                    if(!flowerMap.containsKey(player)) return;
                    flowerMap.get(player).remove(FlowerBunch.this);
                }
            }.runTaskLater(Thepit.INSTANCE, length);
        }

        public void spawn() {
            List<Block> validBlocks = new ArrayList<>();
            for(double x = -FLOWER_SPAWN_RADIUS; x <= FLOWER_SPAWN_RADIUS; x++) {
                for(double z = -FLOWER_SPAWN_RADIUS; z <= FLOWER_SPAWN_RADIUS; z++) {
                    for(int y = -5; y <= 5; y++) {
                        Location testLocation = centerLocation.clone().add(x, y, z);
                        Block block = testLocation.getBlock();
                        if(block == null || block.getType() != Material.AIR) continue;
                        Block blockBelow = block.getRelative(0, -1, 0);
//						TODO: Add solid block
                        if(blockBelow == null || blockBelow.getType() == Material.AIR) continue;
                        validBlocks.add(block);
                        break;
                    }
                }
            }
            Collections.shuffle(validBlocks);
            int flowers = Math.min(new Random().nextInt(MAX_FLOWER_COUNT - MIN_FLOWER_COUNT + 1) + MIN_FLOWER_COUNT, validBlocks.size());
            for(int i = 0; i < flowers; i++) {
                Block block = validBlocks.get(i);
                PacketBlock flowerBlock = new PacketBlock(flowerType.material, flowerType.data, block.getLocation())
                        .setViewers(Misc.getNearbyRealPlayers(block.getLocation(), 50))
                        .spawnBlock()
                        .removeAfter(length + new Random().nextInt(21));
                flowerBlocks.add(flowerBlock);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(flowerBlock.isRemoved()) {
                            cancel();
                            return;
                        }
                        drawParticles(player, flowerBlock);
                    }
                }.runTaskTimer(Thepit.INSTANCE, new Random().nextInt(3), 3);
            }

            flowerType.sendPlantMessage(player);
        }

        public void remove() {
            for(PacketBlock flowerBlock : flowerBlocks) flowerBlock.removeBlock();
        }

        public void drawParticles(Player player, PacketBlock flowerBlock) {
            RedstoneParticle redstoneParticle = new RedstoneParticle();
            FireworkSparkParticle sparkParticle = new FireworkSparkParticle();
            if(flowerType == FlowerType.AZURE_BLUET) {
                Location location = createRandomParticleLocation(flowerBlock);
                sparkParticle.display(player, location);
            } else {
                for(int i = 0; i < 3; i++) {
                    Location location = createRandomParticleLocation(flowerBlock);
                    redstoneParticle.display(player, location, flowerType.particleColor);
                }
            }
        }

        public Location createRandomParticleLocation(PacketBlock flowerBlock) {
            return flowerBlock.getLocation().clone()
                    .add(Misc.randomOffset(5), Misc.randomOffsetPositive(3) + 0.5, Misc.randomOffset(5));
        }
    }

    private enum FlowerType {
        POPPY(Material.RED_ROSE, 1, 0, ParticleColor.RED), // healing
        BLUE_ORCHID(Material.RED_ROSE, 1, 1, ParticleColor.AQUA), // shield regeneration
        ALLIUM(Material.RED_ROSE, 1, 2, ParticleColor.DARK_PURPLE), // mana regeneration
        AZURE_BLUET(Material.RED_ROSE, 1, 3, ParticleColor.WHITE), // strikes entities with lightning
        ORANGE_TULIP(Material.RED_ROSE, 1, 5, ParticleColor.GOLD), // damage increase
        DANDELION(Material.YELLOW_FLOWER, 0.5, 0, ParticleColor.YELLOW); // cannot die

//		RED_TULIP(Material.RED_ROSE, 4, ParticleColor.RED),
//		WHITE_TULIP(Material.RED_ROSE, 6, ParticleColor.WHITE),
//		PINK_TULIP(Material.RED_ROSE, 7, ParticleColor.AQUA),
//		OXEYE_DAISY(Material.RED_ROSE, 8, ParticleColor.AQUA),

        public final Material material;
        public double weight;
        public final byte data;
        public final ParticleColor particleColor;

        FlowerType(Material material, double weight, int data, ParticleColor particleColor) {
            this.material = material;
            this.weight = weight;
            this.data = (byte) data;
            this.particleColor = particleColor;
        }

        public void sendPlantMessage(Player player) {
            DecimalFormat decimalFormat = new DecimalFormat("0.#");
            switch(this) {
                case POPPY:
                    AOutput.send(player, "&5&lPERSEPHONE!&7 You planted &cpoppies&7, granting &c" +
                            Misc.getHearts(getHealing()) + " &7every second when near them");
                    break;
                case BLUE_ORCHID:
                    AOutput.send(player, "&5&lPERSEPHONE!&7 You planted &bblue orchids&7, granting &9" +
                            decimalFormat.format(getShieldRegenMultiplier()) + "x &7faster shield regeneration when near them");
                    break;
                case ALLIUM:
                    AOutput.send(player, "&5&lPERSEPHONE!&7 You planted &5allium&7, granting &b+" +
                            decimalFormat.format(getManaIncreasePercent()) + "% &7faster mana regeneration when near them");
                    break;
                case AZURE_BLUET:
                    AOutput.send(player, "&5&lPERSEPHONE!&7 You planted &fazure bluets&7, creating a " +
                            "mini thunderstorm that will vanquish your enemies");
                    break;
                case ORANGE_TULIP:
                    AOutput.send(player, "&5&lPERSEPHONE!&7 You planted &6orange tulips&7, granting &c+" +
                            getDamageIncreasePercent() + "% &7damage when near them");
                    break;
                case DANDELION:
                    AOutput.send(player, "&5&lPERSEPHONE!&7 You planted &edandelions&7, making it so you " +
                            "&ecannot die &7when near them");
                    break;
            }
        }

        public static FlowerType random() {
            LinkedHashMap<FlowerType, Double> weightedMap = new LinkedHashMap<>();
            for(FlowerType value : values()) weightedMap.put(value, value.weight);
            return Misc.weightedRandom(weightedMap);
        }
    }
}
