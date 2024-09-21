package me.wisdom.thepit.controllers.objects;

import de.myzelyam.api.vanish.VanishAPI;
import de.tr7zw.nbtapi.NBTItem;
import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.battlepass.quests.UseHelmetGoldQuest;
import me.wisdom.thepit.controllers.*;
import me.wisdom.thepit.enchants.overworld.ComboVenom;
import me.wisdom.thepit.enums.NBTTag;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.events.DoubleSneakEvent;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.events.PitQuitEvent;
import me.wisdom.thepit.helmetabilities.*;
import me.wisdom.thepit.inventories.HelmetGUI;
import me.wisdom.thepit.items.misc.GoldenHelmet;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepit.upgrades.Helmetry;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.*;

public class HelmetManager implements Listener {
    public static Map<LivingEntity, HelmetAbility> abilities = new HashMap<>();
    public static List<LivingEntity> toggledPlayers = new ArrayList<>();
    public static DecimalFormat formatter = new DecimalFormat("#,###.#");
    private static final List<Material> armorMaterials = Collections.singletonList(Material.GOLD_HELMET);

    @EventHandler
    public void onDoubleSneak(DoubleSneakEvent event) {
        Player player = event.getPlayer();
        if(getHelmet(player) == null) return;
        if(VanishAPI.isInvisible(player)) return;

        if(!abilities.containsKey(player)) generateAbility(player);

        if(abilities.get(player) == null) {
            AOutput.error(player, "&6&lGOLDEN HELMET! &cNo ability selected!");
            Sounds.NO.play(player);
            return;
        }

        if(!abilities.get(player).refName.equals(Objects.requireNonNull(getAbility(getHelmet(player))).refName))
            generateAbility(player);

        HelmetAbility ability = abilities.get(player);

        if(ability.isTogglable) {
            if(!ability.shouldActivate()) return;
            if(toggledPlayers.contains(player)) {
                ability.onDeactivate();
                ability.isActive = false;
                toggledPlayers.remove(player);
            } else {
                if(SpawnManager.isInSpawn(player)) {
                    AOutput.error(player, "&c&lOOPS!&7 You cannot do this in spawn");
                    return;
                }
                ability.onActivate();
                ability.isActive = true;
                toggledPlayers.add(player);
            }

        } else {
            if(SpawnManager.isInSpawn(player)) {
                AOutput.error(player, "&c&lOOPS!&7 You cannot do this in spawn");
                return;
            }
            ability.onProc();
        }
    }

    public static ItemStack getHelmet(LivingEntity checkPlayer) {
        if(!(checkPlayer instanceof Player)) return null;
        Player player = (Player) checkPlayer;

        if(Misc.isAirOrNull(player.getInventory().getHelmet())) return null;
        GoldenHelmet pitItem = ItemFactory.getItem(GoldenHelmet.class);
        if(!pitItem.isThisItem(player.getInventory().getHelmet())) return null;

        return player.getInventory().getHelmet();
    }

    public static long getUsedHelmetGold(LivingEntity checkPlayer) {
        if(!(checkPlayer instanceof Player)) return 0;
        Player player = (Player) checkPlayer;

        ItemStack helmet = getHelmet(player);
        if(helmet == null) return 0;

        NBTItem nbtItem = new NBTItem(helmet);
        return nbtItem.getLong(NBTTag.GHELMET_GOLD.getRef());
    }

    public static long getHelmetGold(ItemStack helmet) {
        if(helmet == null) return 0;

        NBTItem nbtItem = new NBTItem(helmet);
        return nbtItem.getLong(NBTTag.GHELMET_GOLD.getRef());
    }

    public static void generateAbility(Player player) {
        ItemStack helmet = getHelmet(player);
        if(helmet == null) return;

        NBTItem nbtItem = new NBTItem(helmet);

        if(abilities.containsKey(player)) abilities.get(player).unload();
        abilities.put(player, generateInstance(player, nbtItem.getString(NBTTag.GHELMET_ABILITY.getRef())));
    }

    public static HelmetAbility getAbility(ItemStack helmet) {
        if(helmet == null) return null;

        NBTItem nbtItem = new NBTItem(helmet);
        return generateInstance(null, nbtItem.getString(NBTTag.GHELMET_ABILITY.getRef()));
    }

    public static void setAndDepositGold(Player player, ItemStack helmet, int gold) {
        player.getInventory().setItemInHand(depositGold(helmet, gold));
        player.updateInventory();
    }

    public static ItemStack depositGold(ItemStack helmet, int gold) {
        NBTItem nbtItem = new NBTItem(helmet);
        nbtItem.setLong(NBTTag.GHELMET_GOLD.getRef(), (getHelmetGold(helmet) + gold));
        helmet = nbtItem.getItem();

        GoldenHelmet pitItem = ItemFactory.getItem(GoldenHelmet.class);
        pitItem.updateItem(helmet);

        return helmet;
    }

    public static boolean withdrawGold(Player player, ItemStack helmet, int gold) {
        NBTItem nbtItem = new NBTItem(helmet);
        long helmetGold = nbtItem.getLong(NBTTag.GHELMET_GOLD.getRef());

        if(helmetGold < gold) return false;
        else {
            if(HelmetSystem.getLevel(helmetGold - gold) < HelmetSystem.getLevel(helmetGold)) {
                AOutput.send(player, "&6&lGOLDEN HELMET!&7 Helmet level reduced to &f" +
                        HelmetSystem.getLevel(helmetGold - gold) + "&7. (&6" + formatter.format(helmetGold - gold) + "g&7)");
                Sounds.HELMET_DOWNGRADE.play(player);
            }
            helmetGold -= gold;
            nbtItem.setLong(NBTTag.GHELMET_GOLD.getRef(), helmetGold);
            helmet = nbtItem.getItem();

            GoldenHelmet pitItem = ItemFactory.getItem(GoldenHelmet.class);
            pitItem.updateItem(helmet);
            player.getInventory().setHelmet(helmet);
            player.updateInventory();
        }
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        UseHelmetGoldQuest.INSTANCE.spendGold(pitPlayer, gold);
        return true;
    }

    public static void deactivate(LivingEntity checkPlayer) {
        if(!(checkPlayer instanceof Player)) return;
        Player player = (Player) checkPlayer;

        if(!abilities.containsKey(player)) return;
        HelmetAbility ability = abilities.get(player);
        if(!ability.isActive) return;

        ability.onDeactivate();
        ability.isActive = false;
        toggledPlayers.remove(player);
        abilities.remove(player);
    }

    public static HelmetAbility generateInstance(Player player, String refName) {
        if(refName.equals("leap")) return new LeapAbility(player);
        if(refName.equals("pitblob")) return new BlobAbility(player);
        if(refName.equals("goldrush")) return new GoldRushAbility(player);
        if(refName.equals("hermit")) return new HermitAbility(player);
        if(refName.equals("judgement")) return new JudgementAbility(player);
        if(refName.equals("phoenix")) return new PhoenixAbility(player);
        return null;
    }

    public List<Player> crouchPlayers = new ArrayList<>();

    @EventHandler
    public void onCrouch(PlayerToggleSneakEvent event) {
        if(!Thepit.status.isOverworld()) return;
        Player player = event.getPlayer();
        if(!event.isSneaking()) return;
        if(ComboVenom.isVenomed(player)) return;

        if(!crouchPlayers.contains(player)) {
            crouchPlayers.add(player);
            new BukkitRunnable() {
                @Override
                public void run() {
                    crouchPlayers.remove(player);
                }
            }.runTaskLater(Thepit.INSTANCE, 7L);
            return;
        }
        crouchPlayers.remove(player);
        DoubleSneakEvent newEvent = new DoubleSneakEvent(player);
        Bukkit.getPluginManager().callEvent(newEvent);
    }

    @EventHandler
    public void onQuit(PitQuitEvent event) {
        crouchPlayers.remove(event.getPlayer());

        if(abilities.get(event.getPlayer()) != null) {
            HelmetManager.deactivate(event.getPlayer());
        }
        if(abilities.get(event.getPlayer()) == null) return;
        if(abilities.containsKey(event.getPlayer())) abilities.get(event.getPlayer()).unload();
        toggledPlayers.remove(event.getPlayer());
    }

    @EventHandler
    public void onRemove(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if(event.getClickedInventory() == null || event.getClickedInventory().getType() != InventoryType.PLAYER) return;
        if(Misc.isAirOrNull(player.getInventory().getHelmet())) return;
        if(event.getSlot() == 39 && player.getInventory().getHelmet().getType() == Material.GOLD_HELMET) {
            if(abilities.get(player) != null) {
                HelmetManager.deactivate(player);
            }
            toggledPlayers.remove(player);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if(event.getItemDrop().getItemStack().getType() != Material.GOLD_HELMET) return;
        if(abilities.get(event.getPlayer()) != null) {
            HelmetManager.deactivate(event.getPlayer());
        }
        toggledPlayers.remove(event.getPlayer());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        if(HelmetGUI.depositPlayers.containsKey(player)) {
            event.setCancelled(true);
            ItemStack helmet = HelmetGUI.depositPlayers.get(player);

            if(Misc.isAirOrNull(helmet)) {
                HelmetGUI.depositPlayers.remove(player);
                return;
            }

            int gold;
            try {
                gold = Integer.parseInt(ChatColor.stripColor(event.getMessage()));
                if(gold <= 0) throw new Exception();
            } catch(Exception e) {
                AOutput.send(player, "&cThat is not a valid number!");
                HelmetGUI.depositPlayers.remove(player);
                Sounds.NO.play(player);
                return;
            }

            double finalBalance = pitPlayer.gold - gold;
            if(finalBalance < 0) {
                AOutput.send(player, "&cYou do not have enough gold!");
                HelmetGUI.depositPlayers.remove(player);
                Sounds.NO.play(player);
                return;
            }
            pitPlayer.gold -= gold;

            if(Misc.isAirOrNull(player.getItemInHand())) return;

            GoldenHelmet pitItem = ItemFactory.getItem(GoldenHelmet.class);
            if(!pitItem.isThisItem(player.getItemInHand())) {
                AOutput.send(player, "&cUnable to find helmet!");
                HelmetGUI.depositPlayers.remove(player);
                Sounds.NO.play(player);
                return;
            }

            setAndDepositGold(player, helmet, gold);

            AOutput.send(player, "&aSuccessfully deposited gold!");
            HelmetGUI.depositPlayers.remove(player);
            Sounds.HELMET_DEPOSIT_GOLD.play(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(!event.getPlayer().isSneaking()) return;
        Player player = event.getPlayer();
        Action action = event.getAction();
        if((action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_AIR)) && armorMaterials.contains(player.getItemInHand().getType())) {
            event.setCancelled(true);
            player.updateInventory();
        }

        GoldenHelmet pitItem = ItemFactory.getItem(GoldenHelmet.class);
        if(!pitItem.isThisItem(player.getItemInHand())) return;

        if(!UpgradeManager.hasUpgrade(event.getPlayer(), Helmetry.INSTANCE)) {
            AOutput.error(event.getPlayer(), "&cYou must first unlock &6Helmetry &cfrom the renown shop before using this item!");
            Sounds.NO.play(event.getPlayer());
            return;
        }

        Sounds.HELMET_GUI_OPEN.play(event.getPlayer());
        HelmetGUI helmetGUI = new HelmetGUI(event.getPlayer());
        helmetGUI.open();
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(NonManager.getNon(attackEvent.getAttacker()) != null || NonManager.getNon(attackEvent.getDefender()) != null)
            return;
        ItemStack attackerHelmet = getHelmet(attackEvent.getAttacker());
        ItemStack defenderHelmet = getHelmet(attackEvent.getDefender());

        int attackLevel = 0;
        if(attackerHelmet != null) attackLevel = HelmetSystem.getLevel(getUsedHelmetGold(attackEvent.getAttacker()));
        if(attackerHelmet != null)
            attackEvent.increasePercent += HelmetSystem.getTotalStacks(HelmetSystem.Passive.DAMAGE, attackLevel - 1);

        int defenderLevel = 0;
        if(defenderHelmet != null) defenderLevel = HelmetSystem.getLevel(getUsedHelmetGold(attackEvent.getDefender()));
        if(defenderHelmet != null)
            attackEvent.multipliers.add(Misc.getReductionMultiplier(HelmetSystem.getTotalStacks(HelmetSystem.Passive.DAMAGE_REDUCTION, defenderLevel - 1)));
    }

    @EventHandler
    public void onKill(KillEvent killEvent) {
        if(killEvent.isDeadPlayer()) {
            LivingEntity dead = killEvent.getDead();
            if(abilities.get(dead) != null) {
                HelmetManager.deactivate(killEvent.getDead());
            }
            toggledPlayers.remove(dead);
        }
        if(killEvent.isKillerPlayer()) {
            if(NonManager.getNon(killEvent.getKiller()) != null) return;
            if(Misc.isAirOrNull(killEvent.getKillerPlayer().getInventory().getHelmet())) return;
            if(killEvent.getKillerPlayer().getInventory().getHelmet().getType() != Material.GOLD_HELMET) return;

            ItemStack helmet = getHelmet(killEvent.getKiller());
            if(helmet == null) return;

            int level = HelmetSystem.getLevel(getUsedHelmetGold(killEvent.getKiller()));

            killEvent.goldMultipliers.add(1 + HelmetSystem.getTotalStacks(HelmetSystem.Passive.GOLD_BOOST, level - 1) / 100D);
            killEvent.xpMultipliers.add(1 + HelmetSystem.getTotalStacks(HelmetSystem.Passive.XP_BOOST, level - 1) / 100D);
        }
    }
}
