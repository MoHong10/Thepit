package me.wisdom.thepit.controllers;

import me.clip.deluxechat.events.PrivateMessageEvent;
import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.commands.essentials.GamemodeCommand;
import me.wisdom.thepit.commands.essentials.TeleportCommand;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.help.HelpManager;
import me.wisdom.thepit.inventories.ChatColorPanel;
import me.wisdom.thepit.items.PitItem;
import me.wisdom.thepit.misc.ItemRename;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ChatManager implements Listener {
    public static List<String> illegalPhrases = new ArrayList<>();
    public static Map<Player, Integer> helpCooldownMap = new HashMap<>();

    static {
        illegalPhrases.add("kyro");
        illegalPhrases.add("wiji");
    }

    @EventHandler
    public void onPrivateMessage(PrivateMessageEvent event) {
        Player sender = event.getSender();
        Player recipient = event.getRecipient();
        PitPlayer pitSender = PitPlayer.getPitPlayer(sender);
        PitPlayer pitRecipient = PitPlayer.getPitPlayer(recipient);

        if(pitRecipient.uuidIgnoreList.contains(sender.getUniqueId().toString())) {
            event.setCancelled(true);
            AOutput.error(sender, "&c&lERROR!&7 That player has you ignored");
        } else if(pitSender.uuidIgnoreList.contains(recipient.getUniqueId().toString())) {
            event.setCancelled(true);
            AOutput.error(sender, "&c&lERROR!&7 You have that player ignored");
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (message.contains("我通过 ChatCraft 加入了")) {
            AOutput.send(player, "&c&lCOPE!&7 我们真的不在乎! :)");
            event.setCancelled(true);
            return;
        }

        for (Player recipient : new ArrayList<>(event.getRecipients())) {
            PitPlayer recipientPitPlayer = PitPlayer.getPitPlayer(recipient);

            if (player == recipient && recipientPitPlayer.playerChatDisabled) {
                AOutput.error(player, "&c你当前已静音聊天。要禁用此功能，" +
                        "请前往 &f/donator &c菜单中的聊天选项。");
                event.setCancelled(true);
                return;
            }
            if (recipientPitPlayer.playerChatDisabled || recipientPitPlayer.uuidIgnoreList.contains(player.getUniqueId().toString())) {
                event.getRecipients().remove(recipient);
            }
        }

        if (ItemRename.renamePlayers.containsKey(player)) {
            String name = ChatColor.translateAlternateColorCodes('&', message);
            String strippedName = ChatColor.stripColor(name);
            ItemStack heldItem = ItemRename.renamePlayers.get(player);
            event.setCancelled(true);

            if (Misc.isAirOrNull(heldItem)) {
                ItemRename.renamePlayers.remove(player);
                return;
            }
            PitItem pitItem = ItemFactory.getItem(heldItem);
            if (pitItem == null || !pitItem.isMystic) {
                AOutput.error(player, "&c你只能命名神秘物品！");
                return;
            }
            if (!strippedName.matches("[\\w\\s!@#$%^&*()\\-=+\\[\\]{}|\\\\;':\",./<>?`~]+")) {
                AOutput.error(player, "&c&l错误!&7 只能使用常规字符");
                return;
            }
            if (!player.isOp()) {
                for (String illegalPhrase : illegalPhrases) {
                    if (!strippedName.toLowerCase().contains(illegalPhrase)) continue;
                    AOutput.error(player, "&c&l错误!&7 名称包含非法短语 \"" + illegalPhrase + "\"");
                    return;
                }
            }
            if (!player.isOp() && strippedName.length() > 32) {
                AOutput.error(player, "&c&l错误!&7 物品名称不能超过32个字符");
                return;
            }
            ItemMeta meta = heldItem.getItemMeta();
            meta.setDisplayName(name);
            heldItem.setItemMeta(meta);
            AOutput.send(player, "&a物品重命名成功！");
            ItemRename.renamePlayers.remove(player);
        }

        if(!player.isOp()) message = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', message));
        if(ChatColorPanel.playerChatColors.containsKey(player) && player.hasPermission("pitsim.chatcolor")) {
            message = ChatColorPanel.playerChatColors.get(player).chatColor + message;
        }

        message = message.replaceAll("pitsandbox", "shitsandbox")
                .replaceAll("Pitsandbox", "Shitsandbox")
                .replaceAll("PitSandbox", "ShitSandbox")
                .replaceAll("pit sandbox", "shit sandbox")
                .replaceAll("Pit sandbox", "Shit sandbox")
                .replaceAll("Pit sandbox", "Shit sandbox")
                .replaceAll("Harry", "Hairy")
                .replaceAll("harry", "hairy")
                .replaceAll("(?i)pitsandbox", "shitsandbox")
                .replaceAll("(?i)pit sandbox", "shit sandbox")
                .replaceAll("(?i)harry", "hairy");

        event.setMessage(message);

        if(!event.isCancelled()) handleQuestion(player, message);
    }

    public static void handleQuestion(Player player, String message) {
        message = ChatColor.stripColor(message);
        if(message.length() > 250) return;

        HelpManager.HelperAgent helperAgent = HelpManager.getAgent(player);
        final boolean shouldStoreRequest;
        if(message.endsWith("?") && !helperAgent.isWaitingForResponse()) {
            HelpManager.StoredRequest storedRequest = HelpManager.getStoredRequest(message);
            if(storedRequest != null) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        helperAgent.executeIntent(storedRequest.getIntent());
                    }
                }.runTaskLater(Thepit.INSTANCE, 1L);
                return;
            } else {
                shouldStoreRequest = true;
            }
        } else {
            shouldStoreRequest = false;
        }

        if (message.endsWith("?") && !player.isOp()) {
            int recentMessages = helpCooldownMap.getOrDefault(player, 0);
            if (recentMessages > 3) {
                sendMessageDelayed(player, "&9&lAI!&7 请慢一点发请求");
                return;
            }
            helpCooldownMap.put(player, ++recentMessages);
            new BukkitRunnable() {
                @Override
                public void run() {
                    int recentMessages = helpCooldownMap.getOrDefault(player, 0);
                    if(recentMessages <= 1) {
                        helpCooldownMap.remove(player);
                    } else {
                        helpCooldownMap.put(player, --recentMessages);
                    }
                }
            }.runTaskLater(Thepit.INSTANCE, 20 * 60);
        }

        String finalMessage = message;
        new Thread(() -> {
            if(finalMessage.endsWith("?") || helperAgent.isWaitingForResponse()) {
                String intent = helperAgent.detectIntent(finalMessage);
                helperAgent.executeIntent(intent);
                if(shouldStoreRequest) {
                    HelpManager.StoredRequest storedRequest = new HelpManager.StoredRequest(finalMessage, intent);
                    HelpManager.writeStoredRequest(storedRequest);
                }
            }
        }).start();
    }

    public static void sendMessageDelayed(Player player, String message) {
        new BukkitRunnable() {
            @Override
            public void run() {
                AOutput.error(player, message);
            }
        }.runTaskLater(Thepit.INSTANCE, 1L);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage().toLowerCase();
        List<String> stringArgs = new ArrayList<>(Arrays.asList(message.split(" ")));
        String command = stringArgs.remove(0);
        String[] args = stringArgs.toArray(new String[0]);

        if(command.equalsIgnoreCase("/gamemode")) {
            event.setCancelled(true);
            GamemodeCommand.INSTANCE.onCommand(player, null, "gamemode", args);
        } else if(command.equalsIgnoreCase("/tp")) {
            event.setCancelled(true);
            TeleportCommand.INSTANCE.onCommand(player, null, "tp", args);
        }
    }
}
