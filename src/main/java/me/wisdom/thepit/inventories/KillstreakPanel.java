package me.wisdom.thepit.inventories;

import me.wisdom.thepit.controllers.ChatTriggerManager;
import me.wisdom.thepit.controllers.PerkManager;
import me.wisdom.thepit.controllers.objects.Killstreak;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.killstreaks.NoKillstreak;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.gui.AGUI;
import me.wisdom.thepitapi.gui.AGUIPanel;
import me.wisdom.thepitapi.misc.AOutput;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class KillstreakPanel extends AGUIPanel {
    public static Map<Killstreak, Integer> killstreakSlots = new HashMap<>();

    public PerkGUI perkGUI;

    public KillstreakPanel(AGUI gui) {
        super(gui);
        perkGUI = (PerkGUI) gui;
    }

    @Override
    public String getName() {
        return "&7Choose a &eKillstreak";
    }

    @Override
    public int getRows() {
        return 6;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        int killstreakSlot = perkGUI.killstreakSlot;

        int slot = event.getSlot();
        if(event.getClickedInventory().getHolder() == this) {

            if(slot == 49) {
                openPreviousGUI();
                return;
            }

            for(Map.Entry<Killstreak, Integer> entry : killstreakSlots.entrySet()) {

                if(entry.getValue() == slot) {
                    Killstreak killstreak = entry.getKey();

                    if(!killstreak.refName.equals("NoKillstreak")) {

                        if(hasKillstreakEquipped(player, killstreak)) {
                            AOutput.error(player, "&cThat killstreak is already equipped");
                            Sounds.ERROR.play(player);
                            return;
                        }
                        if(pitPlayer.prestige < killstreak.prestigeReq) {
                            AOutput.error(player, "&cYou aren't high enough prestige to use this!");
                            Sounds.ERROR.play(player);
                            return;
                        }
                    }

                    Killstreak previousKillstreak = getKillstreakFromInterval(player, killstreak.killInterval);
                    for(int i = 0; i < pitPlayer.killstreaks.size(); i++) {
                        if(previousKillstreak != null && pitPlayer.killstreaks.get(i).refName.equals(previousKillstreak.refName)) {
                            if(i == killstreakSlot - 1) continue;
                            if(previousKillstreak.refName.equals("NoKillstreak")) continue;
                            pitPlayer.killstreaks.set(i, NoKillstreak.INSTANCE);
                            AOutput.error(player, "&c&lDISABLED!&7 Disabled &a" + previousKillstreak.displayName + " &7because you cannot have two killstreaks with the same kill interval!");
                        }
                    }

                    pitPlayer.killstreaks.set(killstreakSlot - 1, killstreak);
                    Sounds.SUCCESS.play(player);
                    ChatTriggerManager.sendPerksInfo(pitPlayer);
                    openPreviousGUI();
                }
            }
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        inventoryBuilder.createBorder(Material.STAINED_GLASS_PANE, 7);

        AItemStackBuilder builder3Kills = new AItemStackBuilder(Material.ITEM_FRAME);
        builder3Kills.setName("&c3 Kills");
        if(getKillstreakFromInterval(player, 3) != null) {
            builder3Kills.setLore(new ALoreBuilder("&7Selected: &e" + Objects.requireNonNull(getKillstreakFromInterval(player, 3)).displayName));
            Misc.addEnchantGlint(builder3Kills.getItemStack());
        }
        getInventory().setItem(10, builder3Kills.getItemStack());

        AItemStackBuilder builder7Kills = new AItemStackBuilder(Material.ITEM_FRAME);
        builder7Kills.setName("&c7 Kills");
        if(getKillstreakFromInterval(player, 7) != null) {
            builder7Kills.setLore(new ALoreBuilder("&7Selected: &e" + Objects.requireNonNull(getKillstreakFromInterval(player, 7)).displayName));
            Misc.addEnchantGlint(builder7Kills.getItemStack());
        }
        getInventory().setItem(19, builder7Kills.getItemStack());

        AItemStackBuilder builder15Kills = new AItemStackBuilder(Material.ITEM_FRAME);
        builder15Kills.setName("&c15 Kills");
        if(getKillstreakFromInterval(player, 15) != null) {
            builder15Kills.setLore(new ALoreBuilder("&7Selected: &e" + Objects.requireNonNull(getKillstreakFromInterval(player, 15)).displayName));
            Misc.addEnchantGlint(builder15Kills.getItemStack());
        }
        getInventory().setItem(28, builder15Kills.getItemStack());

        AItemStackBuilder builder40Kills = new AItemStackBuilder(Material.ITEM_FRAME);
        builder40Kills.setName("&c40 Kills");
        if(getKillstreakFromInterval(player, 40) != null) {
            builder40Kills.setLore(new ALoreBuilder("&7Selected: &e" + Objects.requireNonNull(getKillstreakFromInterval(player, 40)).displayName));
            Misc.addEnchantGlint(builder40Kills.getItemStack());
        }
        getInventory().setItem(37, builder40Kills.getItemStack());

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta meta = back.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Go Back");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "To Perks & Killstreaks");
        meta.setLore(lore);
        back.setItemMeta(meta);

        getInventory().setItem(49, back);

        for(Killstreak killstreak : PerkManager.killstreaks) {
            PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);

            if(killstreak.refName.equals("NoKillstreak")) {
                AItemStackBuilder builder = new AItemStackBuilder(killstreak.getDisplayStack(player));
                builder.setLore(new ALoreBuilder(builder.getItemStack().getItemMeta().getLore()).addLore("", "&eClick to remove killstreak!"));
                getInventory().setItem(50, builder.getItemStack());
                killstreakSlots.put(killstreak, 50);
                continue;
            }

            int slot = getIntervalStartingSlot(killstreak.killInterval);

            while(getInventory().getItem(slot) != null) {
                slot++;
            }

            AItemStackBuilder builder = new AItemStackBuilder(killstreak.getDisplayStack(player));
            ALoreBuilder loreBuilder = new ALoreBuilder(builder.getItemStack().getItemMeta().getLore()).addLore("");
            if(hasKillstreakEquipped(player, killstreak)) {
                builder.setName("&a" + killstreak.displayName);
                loreBuilder.addLore("&aAlready selected!");
                Misc.addEnchantGlint(builder.getItemStack());
            } else if(pitPlayer.prestige < killstreak.prestigeReq) {
                builder.setName("&c" + killstreak.displayName);
                loreBuilder.addLore("&cUnlocked at prestige &e" + AUtil.toRoman(killstreak.prestigeReq));
                builder.getItemStack().setType(Material.BEDROCK);
            } else {
                builder.setName("&e" + killstreak.displayName);
                loreBuilder.addLore("&eClick to select!");
            }

            builder.setLore(loreBuilder);
            getInventory().setItem(slot, builder.getItemStack());
            killstreakSlots.put(killstreak, slot);
        }

    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        for(int i = 0; i < getInventory().getSize(); i++) {
            getInventory().setItem(i, new ItemStack(Material.AIR));
        }
    }

    public static Killstreak getKillstreakFromInterval(Player player, int interval) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);

        for(Killstreak killstreak : pitPlayer.killstreaks) {
            if(killstreak.killInterval == interval) return killstreak;
        }

        return null;
    }

    public static int getIntervalStartingSlot(int interval) {
        if(interval == 3) return 10;
        if(interval == 7) return 19;
        if(interval == 15) return 28;
        return 37;
    }

    public static boolean hasKillstreakEquipped(Player player, Killstreak killstreak) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);

        for(Killstreak equippedKillstreak : pitPlayer.killstreaks) {
            if(equippedKillstreak.refName.equals(killstreak.refName)) return true;
        }
        return false;
    }
}
