package me.wisdom.thepit.commands;

import litebans.api.Database;
import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.EnchantManager;
import me.wisdom.thepit.controllers.ItemFactory;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.controllers.objects.PluginMessage;
import me.wisdom.thepit.items.PitItem;
import me.wisdom.thepit.misc.CustomSerializer;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.misc.AOutput;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ShowCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        if(!player.hasPermission("pitsim.show") && !player.isOp() && !Thepit.isDev()) {
            AOutput.error(player, "&c权限不足");
            return false;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                String ip = player.getAddress().getAddress().getHostAddress();
                boolean isMuted = Database.get().isPlayerMuted(player.getUniqueId(), ip);
                if(isMuted) {
                    AOutput.error(player, "&c你被禁言了！");
                    return;
                }

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        ItemStack itemStack = player.getItemInHand();
                        if(Misc.isAirOrNull(itemStack)) {
                            AOutput.error(player, "&c&l错误！&7 你没有拿着任何物品");
                            return;
                        }

                        PitItem pitItem = ItemFactory.getItem(itemStack);
                        if(pitItem != null) pitItem.updateItem(itemStack);
                        player.setItemInHand(itemStack);
                        player.updateInventory();

                        EnchantManager.setItemLore(itemStack, null, false, true);
                        sendShowMessage(Misc.getDisplayName(player), itemStack);

                        new PluginMessage()
                                .writeString("ITEMSHOW")
                                .writeString(Thepit.serverName)
                                .writeString(Misc.getDisplayName(player))
                                .writeString(CustomSerializer.serialize(itemStack))
                                .send();
                    }
                }.runTask(Thepit.INSTANCE);
            }
        }.runTaskAsynchronously(Thepit.INSTANCE);
        return false;
    }

    public static void sendShowMessage(String displayName, ItemStack itemStack) {
        TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&6炫耀！ " + displayName + " &7展示了他们的 "));
        message.addExtra(Misc.createItemHover(itemStack));

        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            PitPlayer pitPlayer = PitPlayer.getPitPlayer(onlinePlayer);
            if(!pitPlayer.playerChatDisabled) onlinePlayer.spigot().sendMessage(message);
        }
    }
}
