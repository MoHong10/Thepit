package me.wisdom.thepit.inventories;

import me.wisdom.thepit.controllers.HelmetSystem;
import me.wisdom.thepit.controllers.ItemFactory;
import me.wisdom.thepit.controllers.objects.HelmetAbility;
import me.wisdom.thepit.controllers.objects.HelmetManager;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.items.misc.GoldenHelmet;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.gui.AGUI;
import me.wisdom.thepitapi.gui.AGUIPanel;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HelmetPanel extends AGUIPanel {

    public PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
    public ItemStack goldenHelmet = null;
    public List<List<ItemStack>> columns = new ArrayList<>();
    public HelmetGUI helmetGUI;

    public HelmetPanel(AGUI gui) {
        super(gui);
        helmetGUI = (HelmetGUI) gui;

    }

    @Override
    public String getName() {
        return "Modify Helmet";
    }

    @Override
    public int getRows() {
        return 5;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        if(event.getClickedInventory().getHolder() == this) {
            if(slot == 4) {
                openPanel(helmetGUI.helmetAbilityPanel);
            }
            if(slot == 7) {
                HelmetGUI.deposit(player, goldenHelmet);
                player.closeInventory();

            }

        }
        updateInventory();
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        goldenHelmet = getHelm();
        if(goldenHelmet.getItemMeta().hasLore()) {
            List<String> helmetLore = goldenHelmet.getItemMeta().getLore();
            helmetLore.remove(helmetLore.size() - 1);
            ItemStack helmetDisplay = goldenHelmet.clone();
            ItemMeta helmetMeta = helmetDisplay.getItemMeta();
            helmetMeta.setLore(helmetLore);
            helmetDisplay.setItemMeta(helmetMeta);
            getInventory().setItem(1, helmetDisplay);
        }
        AItemStackBuilder abilityBuilder = new AItemStackBuilder(Material.EYE_OF_ENDER);
        abilityBuilder.setName("&eAbility");
        ALoreBuilder abilityLoreBuilder = new ALoreBuilder();
        HelmetAbility ability = getAbility(goldenHelmet);
        if(ability != null) {
            abilityLoreBuilder.addLore("&7Selected: &9" + ability.name);
            abilityBuilder.getItemStack().setType(ability.getDisplayStack().getType());
            abilityLoreBuilder.addLore(ability.getDescription());
            abilityLoreBuilder.addLore("", "&eClick to change ability!");
        } else {
            abilityLoreBuilder.addLore("&7Selected: &cNONE", "&7Abilities let you spend the", "&6gold &7in your helmet on", "&7various buffs.");
            abilityLoreBuilder.addLore("", "&eClick to choose an ability!");
        }
        abilityBuilder.setLore(abilityLoreBuilder);
        getInventory().setItem(4, abilityBuilder.getItemStack());

        ItemStack deposit = new ItemStack(Material.PAPER);
        ItemMeta depositMeta = deposit.getItemMeta();
        depositMeta.setDisplayName(ChatColor.YELLOW + "Deposit Gold");
        List<String> depositLore = new ArrayList<>();
        depositLore.add(ChatColor.translateAlternateColorCodes('&', "&7Deposit &6gold &7into the helmet that"));
        depositLore.add(ChatColor.translateAlternateColorCodes('&', "&7can be spent to use abilities and"));
        depositLore.add(ChatColor.translateAlternateColorCodes('&', "&7builds up passives the more that's"));
        depositLore.add(ChatColor.translateAlternateColorCodes('&', "&7in it."));
        depositLore.add("");
        DecimalFormat formatter = new DecimalFormat("#,###.#");
        depositLore.add(ChatColor.GRAY + "Current gold: " + ChatColor.GOLD + formatter.format(HelmetManager.getHelmetGold(goldenHelmet)) + "g");
        depositLore.add("");
        depositLore.add(ChatColor.YELLOW + "Click to deposit gold!");

        depositMeta.setLore(depositLore);
        deposit.setItemMeta(depositMeta);
        getInventory().setItem(7, deposit);

        List<ItemStack> column1 = new ArrayList<>();
        column1.add(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        column1.add(new ItemStack(Material.GOLD_NUGGET));
        column1.add(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        columns.add(column1);

        List<ItemStack> column2 = new ArrayList<>();
        column2.add(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        column2.add(new ItemStack(Material.GOLD_NUGGET));
        column2.add(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        columns.add(column2);

        List<ItemStack> column3 = new ArrayList<>();
        column3.add(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        column3.add(new ItemStack(Material.GOLD_NUGGET));
        column3.add(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        columns.add(column3);

        List<ItemStack> column4 = new ArrayList<>();
        column4.add(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        column4.add(new ItemStack(Material.GOLD_NUGGET));
        column4.add(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        columns.add(column4);

        List<ItemStack> column5 = new ArrayList<>();
        column5.add(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        column5.add(new ItemStack(Material.GOLD_NUGGET));
        column5.add(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        columns.add(column5);

        List<ItemStack> column6 = new ArrayList<>();
        column6.add(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        column6.add(new ItemStack(Material.GOLD_NUGGET));
        column6.add(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        columns.add(column6);

        List<ItemStack> column7 = new ArrayList<>();
        column7.add(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        column7.add(new ItemStack(Material.GOLD_NUGGET));
        column7.add(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        columns.add(column7);

        List<ItemStack> column8 = new ArrayList<>();
        column8.add(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        column8.add(new ItemStack(Material.GOLD_NUGGET));
        column8.add(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        columns.add(column8);

        List<ItemStack> column9 = new ArrayList<>();
        column9.add(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        column9.add(new ItemStack(Material.GOLD_NUGGET));
        column9.add(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        columns.add(column9);

        int level = HelmetSystem.getLevel(HelmetManager.getHelmetGold(goldenHelmet));

        int i;
        if(level < 5) i = 1;
        else if(level > 94) i = 92;
        else i = level - 4;

        for(int j = 0; j < 9; j++) {
            setColumn(j, i);
            i++;
        }

        int k = 37;
        for(HelmetSystem.Passive passive : HelmetSystem.Passive.values()) {
            AItemStackBuilder builder = new AItemStackBuilder(Material.INK_SACK);
            ALoreBuilder loreBuilder = new ALoreBuilder();
            builder.getItemStack().setDurability(passive.data);
            int goldLevel = HelmetSystem.getLevel(HelmetManager.getHelmetGold(goldenHelmet));
            int passiveLevel = HelmetSystem.getTotalStacks(passive, goldLevel - 1);
            builder.getItemStack().setAmount(passiveLevel);
            builder.setName(passive.refName);
            String percent;
            if(passiveLevel != 0) {
                if(passive == HelmetSystem.Passive.DAMAGE_REDUCTION) {
                    percent = "&7Current: " + passive.color + "-" + passiveLevel * passive.baseUnit + "%";
                } else percent = "&7Current: " + passive.color + "+" + passiveLevel * passive.baseUnit + "%";
                String tier = "&7Tier: &a" + AUtil.toRoman(passiveLevel);
                if(passiveLevel > 0) loreBuilder.addLore(percent, tier);
            }
            loreBuilder.addLore("");
            if(passive == HelmetSystem.Passive.DAMAGE_REDUCTION) {
                loreBuilder.addLore("&7Each Tier:", passive.color + "-" + passive.baseUnit + "% " + passive.refName, "", "&eLevel up helmet to upgrade!");
            } else
                loreBuilder.addLore("&7Each Tier:", passive.color + "+" + passive.baseUnit + "% " + passive.refName, "", "&eLevel up helmet to upgrade!");
            builder.setLore(loreBuilder);
            getInventory().setItem(k, builder.getItemStack());
            k += 2;
        }
    }

    public void setColumn(int column, int level) {
        List<ItemStack> columnList = columns.get(column);
        if(HelmetSystem.getTotalGoldAtLevel(level) <= HelmetManager.getHelmetGold(goldenHelmet)) {
            columnList.get(0).setDurability((short) 5);
            removeName(columnList.get(0));
            getInventory().setItem(column + 9, removeName(columnList.get(0)));
            columnList.get(2).setDurability((short) 5);
            removeName(columnList.get(2));
            getInventory().setItem(column + 27, removeName(columnList.get(2)));

        } else if(HelmetSystem.getLevel(HelmetManager.getHelmetGold(goldenHelmet)) > 1 && HelmetSystem.getTotalGoldAtLevel(level - 1)
                <= HelmetManager.getHelmetGold(goldenHelmet)) {
            columnList.get(0).setDurability((short) 1);
            removeName(columnList.get(0));
            getInventory().setItem(column + 9, removeName(columnList.get(0)));
            columnList.get(2).setDurability((short) 1);
            getInventory().setItem(column + 27, removeName(columnList.get(2)));
        } else {
            if(HelmetSystem.getLevel(HelmetManager.getHelmetGold(goldenHelmet)) == 1 && column == 0) {
                columnList.get(0).setDurability((short) 1);
                removeName(columnList.get(0));
                columnList.get(2).setDurability((short) 1);
                removeName(columnList.get(2));
            }
            getInventory().setItem(column + 9, removeName(columnList.get(0)));
            getInventory().setItem(column + 27, removeName(columnList.get(2)));
        }

        List<HelmetSystem.Passive> passives;
        if(HelmetSystem.getLevel(HelmetManager.getHelmetGold(goldenHelmet)) == 1)
            passives = HelmetSystem.getLevelData(level);
        else passives = HelmetSystem.getLevelData(level);

        DecimalFormat formatter = new DecimalFormat("#,###.#");
        ALoreBuilder loreBuilder = new ALoreBuilder("&7Unlocked at: &6" + formatter.format(HelmetSystem.getTotalGoldAtLevel(level)) + "g", "", "&7Passives:");

        AItemStackBuilder builder = new AItemStackBuilder(Material.GOLD_NUGGET);

        for(HelmetSystem.Passive passive : passives) {
            if(passive == HelmetSystem.Passive.DAMAGE_REDUCTION) {
                loreBuilder.addLore(passive.color + "-" + passive.baseUnit + "% " + passive.refName);
                continue;
            }
            loreBuilder.addLore(passive.color + "+" + passive.baseUnit + "% " + passive.refName);
        }
        if(passives.size() > 1) builder.getItemStack().setType(Material.BEACON);
        else if(passives.size() != 0) {
            builder.getItemStack().setType(Material.INK_SACK);
            builder.getItemStack().setDurability(passives.get(0).data);
        } else loreBuilder.addLore("&cNONE");
        loreBuilder.addLore("");
        if(HelmetSystem.getTotalGoldAtLevel(level) <= HelmetManager.getHelmetGold(goldenHelmet)) {
            loreBuilder.addLore(ColumnStatus.UNLOCKED.color + "Unlocked!");
            builder.setName(ColumnStatus.UNLOCKED.color + "Level " + level);
        } else if(HelmetSystem.getLevel(HelmetManager.getHelmetGold(goldenHelmet)) > 1 && HelmetSystem.getTotalGoldAtLevel(level - 1)
                <= HelmetManager.getHelmetGold(goldenHelmet)) {
            loreBuilder.addLore(ColumnStatus.UNLOCKING.color + "Locked!");
            builder.setName(ColumnStatus.UNLOCKING.color + "Level " + level);
        } else {
            loreBuilder.addLore(ColumnStatus.LOCKED.color + "Locked!");
            builder.setName(ColumnStatus.LOCKED.color + "Level " + level);
        }

        builder.setLore(loreBuilder);

        if(passives.size() > 1) {

            getInventory().setItem(column + 18, builder.getItemStack());
        } else if(passives.size() == 1) {
            columnList.get(1).setType(Material.INK_SACK);
            columnList.get(1).setDurability(passives.get(0).data);
            getInventory().setItem(column + 18, builder.getItemStack());
        } else getInventory().setItem(column + 18, builder.getItemStack());

    }

    enum ColumnStatus {
        UNLOCKED(ChatColor.GREEN),
        UNLOCKING(ChatColor.GOLD),
        LOCKED(ChatColor.RED);

        public ChatColor color;

        ColumnStatus(ChatColor color) {
            this.color = color;
        }
    }

    public ItemStack removeName(ItemStack item) {
        ItemStack cItem = item.clone();
        ItemMeta meta = cItem.getItemMeta();
        meta.setDisplayName(" ");
        cItem.setItemMeta(meta);
        return cItem;
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
    }

    public ItemStack getHelm() {
        GoldenHelmet pitItem = ItemFactory.getItem(GoldenHelmet.class);
        if(!pitItem.isThisItem(player.getItemInHand())) return null;
        return player.getItemInHand();
    }

    public HelmetAbility getAbility(ItemStack helmet) {
        return HelmetManager.getAbility(helmet);
    }
}
