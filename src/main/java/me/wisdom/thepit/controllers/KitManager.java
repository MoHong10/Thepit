package me.wisdom.thepit.controllers;

import de.tr7zw.nbtapi.NBTItem;
import me.wisdom.thepit.controllers.objects.Kit;
import me.wisdom.thepit.enums.KitItem;
import me.wisdom.thepit.enums.MysticType;
import me.wisdom.thepit.enums.NBTTag;
import me.wisdom.thepit.enums.PantColor;
import me.wisdom.thepit.items.MysticFactory;
import me.wisdom.thepit.items.PitItem;
import me.wisdom.thepit.items.diamond.DiamondBoots;
import me.wisdom.thepit.items.diamond.DiamondChestplate;
import me.wisdom.thepit.items.diamond.DiamondHelmet;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class KitManager implements Listener {
    private static final Map<KitItem, Supplier<ItemStack>> kitItemMap = new HashMap<>();
    public static List<Kit> kits = new ArrayList<>();

    public static void registerKit(Kit kit) {
        kits.add(kit);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onDrop(PlayerDropItemEvent event) {
        ItemStack dropped = event.getItemDrop().getItemStack();
        NBTItem nbtItem = new NBTItem(dropped);
        if(!nbtItem.hasKey(NBTTag.IS_PREMADE.getRef())) return;
        event.getItemDrop().remove();
        Sounds.NO.play(event.getPlayer());
        AOutput.send(event.getPlayer(), "&c&l物品已删除!&7 丢弃了一个预设物品（使用 /kit 重新获得）");
    }

    public static ItemStack getItem(KitItem kitItem) {
        ItemStack itemStack = kitItemMap.get(kitItem).get();
        PitItem pitItem = ItemFactory.getItem(itemStack);
        if(pitItem != null && pitItem.hasUUID) itemStack = pitItem.randomizeUUID(itemStack);
        return itemStack;
    }

    static {
        try {
            kitItemMap.put(KitItem.DIAMOND_HELMET, () -> ItemFactory.getItem(DiamondHelmet.class).getItem());
            kitItemMap.put(KitItem.DIAMOND_CHESTPLATE, () -> ItemFactory.getItem(DiamondChestplate.class).getItem());
            kitItemMap.put(KitItem.DIAMOND_BOOTS, () -> ItemFactory.getItem(DiamondBoots.class).getItem());

            kitItemMap.put(KitItem.EXE_SWEATY, () -> {
                try {
                    ItemStack itemStack = MysticFactory.getFreshItem(MysticType.SWORD, null);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("exe"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("sweaty"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("shark"), 2, false);
                    return new AItemStackBuilder(itemStack).setName("&bXP 剑").getItemStack();
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            });

            kitItemMap.put(KitItem.SWEATY_GHEART, () -> {
                try {
                    ItemStack itemStack = MysticFactory.getFreshItem(MysticType.PANTS, PantColor.BLUE);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("sweaty"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("gheart"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("ng"), 2, false);
                    return new AItemStackBuilder(itemStack).setName("&bXP 裤子 &7（防御）").getItemStack();
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            });

            kitItemMap.put(KitItem.SWEATY_ELEC, () -> {
                try {
                    ItemStack itemStack = MysticFactory.getFreshItem(MysticType.PANTS, PantColor.GREEN);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("sweaty"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("elec"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("ng"), 2, false);
                    return new AItemStackBuilder(itemStack).setName("&bXP 裤子 &7（机动性）").getItemStack();
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            });

            kitItemMap.put(KitItem.EXE_MOCT_BOOST, () -> {
                try {
                    ItemStack itemStack = MysticFactory.getFreshItem(MysticType.SWORD, null);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("exe"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("moct"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("boost"), 2, false);
                    return new AItemStackBuilder(itemStack).setName("&6金币剑 &7（效率）").getItemStack();
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            });

            kitItemMap.put(KitItem.EXE_MOCT_SHARK, () -> {
                try {
                    ItemStack itemStack = MysticFactory.getFreshItem(MysticType.SWORD, null);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("exe"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("moct"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("shark"), 2, false);
                    return new AItemStackBuilder(itemStack).setName("&6金币剑 &7(伤害)").getItemStack();
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            });

            kitItemMap.put(KitItem.MOCT_BOOST_BUMP, () -> {
                try {
                    ItemStack itemStack = MysticFactory.getFreshItem(MysticType.PANTS, PantColor.ORANGE);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("moct"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("boost"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("bump"), 2, false);
                    return new AItemStackBuilder(itemStack).setName("&6金币裤子 &7（效率）").getItemStack();
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            });

            kitItemMap.put(KitItem.MOCT_BOOST_ELEC, () -> {
                try {
                    ItemStack itemStack = MysticFactory.getFreshItem(MysticType.PANTS, PantColor.YELLOW);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("moct"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("boost"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("elec"), 2, false);
                    return new AItemStackBuilder(itemStack).setName("&6金币裤子 &7（机动性）").getItemStack();
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            });

            kitItemMap.put(KitItem.BILL_STOMP_PUN, () -> {
                try {
                    ItemStack itemStack = MysticFactory.getFreshItem(MysticType.SWORD, null);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("bill"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("stomp"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("pun"), 2, false);
                    return new AItemStackBuilder(itemStack).setName("&c伤害富翁 &7（附带 RGM）").getItemStack();
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            });

            kitItemMap.put(KitItem.BILL_LS_CD, () -> {
                try {
                    ItemStack itemStack = MysticFactory.getFreshItem(MysticType.SWORD, null);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("bill"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("ls"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("cd"), 2, false);
                    return new AItemStackBuilder(itemStack).setName("&cPvP 吸血 &7（附带 RGM）").getItemStack();
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            });

            kitItemMap.put(KitItem.CH_LS_GAB, () -> {
                try {
                    ItemStack itemStack = MysticFactory.getFreshItem(MysticType.SWORD, null);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("ch"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("ls"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("gab"), 2, false);
                    return new AItemStackBuilder(itemStack).setName("&cPvP 吸血 &7（附带规则性）").getItemStack();
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            });

            kitItemMap.put(KitItem.PERUN_GAMBLE_PUN, () -> {
                try {
                    ItemStack itemStack = MysticFactory.getFreshItem(MysticType.SWORD, null);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("perun"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("gamble"), 1, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("pun"), 3, false);
                    return new AItemStackBuilder(itemStack).setName("&b真实伤害 &7（附带规则性）").getItemStack();
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            });

            kitItemMap.put(KitItem.PERUN_CHEAL_GAB, () -> {
                try {
                    ItemStack itemStack = MysticFactory.getFreshItem(MysticType.SWORD, null);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("perun"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("ch"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("gab"), 2, false);
                    return new AItemStackBuilder(itemStack).setName("&b真实伤害 &7（附带规则性）").getItemStack();
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            });

            kitItemMap.put(KitItem.RGM_MIRROR_PROT, () -> {
                try {
                    ItemStack itemStack = MysticFactory.getFreshItem(MysticType.PANTS, PantColor.BLUE);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("rgm"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("mirror"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("prot"), 2, false);
                    return new AItemStackBuilder(itemStack).setName("&9RGM 镜像 &7（附带富翁）").getItemStack();
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            });

            kitItemMap.put(KitItem.RGM_CF_PROT, () -> {
                try {
                    ItemStack itemStack = MysticFactory.getFreshItem(MysticType.PANTS, PantColor.RED);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("rgm"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("cf"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("prot"), 2, false);
                    return new AItemStackBuilder(itemStack).setName("&9RGM CF &7（附带富翁）").getItemStack();
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            });

            kitItemMap.put(KitItem.REG_MIRROR_PROT, () -> {
                try {
                    ItemStack itemStack = MysticFactory.getFreshItem(MysticType.PANTS, PantColor.GREEN);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("reg"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("mirror"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("prot"), 2, false);
                    return new AItemStackBuilder(itemStack).setName("&aRegularity Mirror &7(w/Perun)").getItemStack();
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            });

            kitItemMap.put(KitItem.REG_SOLI_PROT, () -> {
                try {
                    ItemStack itemStack = MysticFactory.getFreshItem(MysticType.PANTS, PantColor.ORANGE);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("reg"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("soli"), 1, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("prot"), 3, false);
                    return new AItemStackBuilder(itemStack).setName("&a规则性孤寂 &7（附带佩尔恩）").getItemStack();
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            });

            kitItemMap.put(KitItem.MLB_DRAIN, () -> {
                try {
                    ItemStack itemStack = MysticFactory.getFreshItem(MysticType.BOW, null);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("mlb"), 1, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("drain"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("fletching"), 3, false);
                    return new AItemStackBuilder(itemStack).setName("&2MLB 排水 &7（连击）").getItemStack();
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            });

            kitItemMap.put(KitItem.MLB_PIN, () -> {
                try {
                    ItemStack itemStack = MysticFactory.getFreshItem(MysticType.BOW, null);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("mlb"), 1, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("pin"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("fletching"), 3, false);
                    return new AItemStackBuilder(itemStack).setName("&2MLB 钉子 &7（反连击）").getItemStack();
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            });

            kitItemMap.put(KitItem.MLB_WASP, () -> {
                try {
                    ItemStack itemStack = MysticFactory.getFreshItem(MysticType.BOW, null);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("mlb"), 1, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("wasp"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("fletching"), 3, false);
                    return new AItemStackBuilder(itemStack).setName("&2MLB 黄蜂 &7（坦克）").getItemStack();
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            });

            kitItemMap.put(KitItem.MLB_TELE, () -> {
                try {
                    ItemStack itemStack = MysticFactory.getFreshItem(MysticType.BOW, null);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("mlb"), 1, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("tele"), 3, false);
                    return new AItemStackBuilder(itemStack).setName("&eMLB 传送弓 &7（机动性）").getItemStack();
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            });

            kitItemMap.put(KitItem.STREAKING_BILL_LS, () -> {
                try {
                    ItemStack itemStack = MysticFactory.getFreshItem(MysticType.SWORD, null);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("bill"), 2, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("ls"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("shark"), 3, false);
                    return new AItemStackBuilder(itemStack).setName("&c连击吸血 &7（消耗金币）").getItemStack();
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            });

            kitItemMap.put(KitItem.STREAKING_CH_LS, () -> {
                try {
                    ItemStack itemStack = MysticFactory.getFreshItem(MysticType.SWORD, null);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("ch"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("ls"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("shark"), 2, false);
                    return new AItemStackBuilder(itemStack).setName("&c连击吸血 &7（无消耗）").getItemStack();
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            });

            kitItemMap.put(KitItem.VOLLEY_FTTS, () -> {
                try {
                    ItemStack itemStack = MysticFactory.getFreshItem(MysticType.BOW, null);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("volley"), 3, false);
                    itemStack = EnchantManager.addEnchant(itemStack, EnchantManager.getEnchant("ftts"), 3, false);
                    return new AItemStackBuilder(itemStack).setName("&b弹雨 FTTS &7（附带电解质）").getItemStack();
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            });

        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }
}
