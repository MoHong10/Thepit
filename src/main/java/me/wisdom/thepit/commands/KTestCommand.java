package me.wisdom.thepit.commands;

import de.tr7zw.nbtapi.NBTItem;
import me.clip.placeholderapi.PlaceholderAPI;
import me.wisdom.thepit.controllers.PrestigeValues;
import me.wisdom.thepit.controllers.UpgradeManager;
import me.wisdom.thepit.controllers.objects.RenownUpgrade;
import me.wisdom.thepit.enums.NBTTag;
import me.wisdom.thepit.misc.Formatter;
import me.wisdom.thepitapi.misc.AOutput;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class KTestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if (!player.isOp()) return false;

        for (int i = 1; i <= 60; i++) {
            int renown = 0;
            for (int j = 0; j < i; j++) renown += PrestigeValues.getPrestigeInfo(j).getRenownReward();
            int shopRenown = 0;
            for (RenownUpgrade upgrade : UpgradeManager.upgrades) {
                if (upgrade.prestigeReq > i) continue;
                if (upgrade.isTiered()) {
                    shopRenown += upgrade.getTierCosts().stream().mapToInt(Integer::intValue).sum();
                } else {
                    shopRenown += upgrade.getUnlockCost();
                }
            }
            AOutput.broadcast("&7声望: " + i + ", 声望: &e" + Formatter.commaFormat.format(renown) + "&7, 商店: &e" +
                    Formatter.commaFormat.format(shopRenown) + "&7, 比例: &e" + Formatter.commaFormat.format((double) renown * 100 / shopRenown) + "%");
        }
        return false;
    }

//		for(PitEnchant pitEnchant : EnchantManager.pitEnchants) {
//			System.out.println(pitEnchant.name.toUpperCase().replaceAll(" ", "_")
//					.replaceAll(":", "").replaceAll("\"", "").replaceAll("-", "") +
//					"(\"" + pitEnchant.getDisplayName(false, true).replaceAll("\u00A7", "&")
//					.replaceAll(":", "").replaceAll("\"", "").replaceAll("-", "") +
//					"\", \"" + pitEnchant.name.replaceAll(":", "").replaceAll("\"", "")
//					.replaceAll("-", "") + "\", \"" + pitEnchant.refNames.get(0) + "\", " + pitEnchant.isRare + ", " +
//					pitEnchant.isUncommonEnchant + ", " + pitEnchant.isTainted + "),");
//		}

//		for(Block block : getNearbyBlocks(player.getLocation(), 100)) {
//			Block blockBelow = block.getRelative(0, -1, 0);
//			if(blockBelow == null || blockBelow.getType() != Material.AIR) continue;
//			if(Math.random() > 0.5) continue;
//
//			new BukkitRunnable() {
//				@Override
//				public void run() {
//					new PacketBlock(Material.AIR, (byte) 0, block.getLocation())
//							.setViewers(player)
//							.spawnBlock()
//							.removeAfter(30 * 20);
//
//					Location spawnLocation = block.getLocation().add(0.5, 0.5, 0.5);
//					new FallingBlock(block.getType(), block.getData(), spawnLocation)
//							.setViewers(player)
//							.spawnBlock()
//							.removeAfter(40 + new Random().nextInt(41));
//				}
//			}.runTaskLater(PitSim.INSTANCE, new Random().nextInt(30 * 20));
//		}

    public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<>();
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    Block block = location.getWorld().getBlockAt(x, y, z);
                    if (block == null || block.getType() == Material.AIR) continue;
                    blocks.add(block);
                }
            }
        }
        return blocks;
    }

    public static void giveToken(Player player, int amount) {
        ItemStack vile = new ItemStack(Material.MAGMA_CREAM);
        ItemMeta meta = vile.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "感谢令牌");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW + "特殊物品");
        lore.add(ChatColor.GRAY + "对理解我们为什么要重置 PitSim 经济的人的一份谢意。");
        lore.add(ChatColor.GRAY + "谁知道，这可能将来会有用。");
        lore.add("");
        String loresMessage = ChatColor.translateAlternateColorCodes('&',
                "&7至: &8[%luckperms_primary_group_name%&8] %luckperms_prefix%" + player.getName());
        lore.add(PlaceholderAPI.setPlaceholders(player, loresMessage));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7从: &8[&9开发者&8] &9wiji1"));
        meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setLore(lore);
        vile.setItemMeta(meta);
        vile.setAmount(amount);

        NBTItem nbtItem = new NBTItem(vile);
        nbtItem.setBoolean(NBTTag.IS_TOKEN.getRef(), true);
        AUtil.giveItemSafely(player, nbtItem.getItem());
    }
}

