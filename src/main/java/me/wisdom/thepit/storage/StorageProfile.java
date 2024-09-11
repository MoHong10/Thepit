package me.wisdom.thepit.storage;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.EnchantManager;
import me.wisdom.thepit.controllers.ItemFactory;
import me.wisdom.thepit.controllers.objects.PluginMessage;
import me.wisdom.thepit.exceptions.DataNotLoadedException;
import me.wisdom.thepit.logging.LogManager;
import me.wisdom.thepit.misc.CustomSerializer;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PlayerItemLocation;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StorageProfile implements Cloneable {
    private ItemStack[] inventory = new ItemStack[StorageManager.ENDERCHEST_ITEM_SLOTS];
    private ItemStack[] armor = new ItemStack[4];
    private final EnderchestPage[] enderchestPages = new EnderchestPage[StorageManager.MAX_ENDERCHEST_PAGES];

    private int defaultOverworldSet = -1;
    private int defaultDarkzoneSet = -1;
    private final Outfit[] outfits = new Outfit[9];

    private final UUID uuid;
    private BukkitTask saveTask;
    private BukkitRunnable saveRunnable;
    private boolean isLoaded;
    private boolean saving;

    public StorageProfile(UUID uuid) {
        this.uuid = uuid;
    }

    public void saveData(BukkitRunnable runnable, boolean logout) {
        saveRunnable = runnable;
        if(saving) return;
        saveData(logout);
    }

    @Override
    public StorageProfile clone() throws CloneNotSupportedException {
        return (StorageProfile) super.clone();
    }

    public void loadData(PluginMessage message, boolean view) {
        List<String> strings = message.getStrings();
        List<Integer> integers = message.getIntegers();

        defaultOverworldSet = integers.remove(0);
        defaultDarkzoneSet = integers.remove(0);
        for(int i = 0; i < 36; i++) inventory[i] = deserialize(strings.remove(0), uuid);
        for(int i = 0; i < 4; i++) armor[i] = deserialize(strings.remove(0), uuid);
        for(int i = 0; i < enderchestPages.length; i++) enderchestPages[i] = new EnderchestPage(this, message);
        for(int i = 0; i < StorageManager.OUTFITS; i++) outfits[i] = new Outfit(this, message);
        isLoaded = true;

        if(view) return;

        Player player = getOnlinePlayer();
        if(player == null || !player.isOnline()) return;
        AOutput.log("加载 " + player.getName() + " 的在线数据");
        AOutput.send(player, "&9&l重新加载！&7 你所在的服务器已重新加载，你的数据已被恢复！");
        Sounds.BOOSTER_REMIND.play(player);

        initializePlayerInventory(player);
    }

    public void saveData(boolean isLogout) {
        if(!isLoaded) throw new DataNotLoadedException();

        PluginMessage message = new PluginMessage()
                .writeString("物品数据保存")
                .writeString(Thepit.serverName)
                .writeString(uuid.toString())
                .writeBoolean(isLogout)
                .writeInt(defaultOverworldSet)
                .writeInt(defaultDarkzoneSet);

        if(StorageManager.isEditing(uuid) && Bukkit.getPlayer(uuid) != null) return;
        if(StorageManager.isBeingEdited(uuid) && Bukkit.getPlayer(uuid) != null) return;

        if(getOnlinePlayer() != null) {
            for(ItemStack itemStack : getOnlinePlayer().getInventory()) message.writeString(serialize(getOfflinePlayer(), itemStack, isLogout));
            for(ItemStack itemStack : getOnlinePlayer().getInventory().getArmorContents()) message.writeString(serialize(getOfflinePlayer(), itemStack, isLogout));
        } else if(inventory != null) {
            for(ItemStack itemStack : inventory) message.writeString(serialize(getOfflinePlayer(), itemStack, isLogout));
            for(ItemStack itemStack : armor) message.writeString(serialize(getOfflinePlayer(), itemStack, isLogout));
        }
        for(EnderchestPage enderchestPage : enderchestPages) enderchestPage.writeData(message, isLogout);

        for(Outfit outfit : outfits) outfit.writeData(message);

        saving = true;
        saveTask = isLogout ? null : new BukkitRunnable() {
            @Override
            public void run() {
                OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
                if(!player.isOnline()) return;
                player.getPlayer().kickPlayer(ChatColor.RED + "你的玩家数据保存失败。请报告此问题");
                Misc.alertDiscord("@everyone 玩家数据保存确认失败: " + uuid + " 在服务器: " + Thepit.serverName);
            }
        }.runTaskLater(Thepit.INSTANCE, 40);

        message.send();
    }

    public boolean storeInvAndArmor(Map<PlayerItemLocation, ItemStack> proposedChanges, List<PlayerItemLocation> emptySlots,
                                    boolean useOnlinePlayer) {
        Player player = getOnlinePlayer();
        loop:
        for (PlayerItemLocation itemLocation : PlayerItemLocation
                .getLocations(PlayerItemLocation.Location.INVENTORY, PlayerItemLocation.Location.ARMOR)) {
            ItemStack itemStack = proposedChanges.containsKey(itemLocation) ? proposedChanges.get(itemLocation) :
                    itemLocation.getItem(getUniqueID(), useOnlinePlayer).clone();
            proposedChanges.put(itemLocation, new ItemStack(Material.AIR));
            if (Misc.isAirOrNull(itemStack)) continue;
            System.out.println("检查要存储的物品: " + itemLocation.getIdentifier());
            if (emptySlots.contains(itemLocation)) continue;
            System.out.println("尝试存储物品: " + itemLocation.getIdentifier());

            int maxStackSize = itemStack.getMaxStackSize();

            boolean foundAnyEnabled = false;
            for (EnderchestPage enderchestPage : getEnderchestPages()) {
                if (!enderchestPage.isWardrobeEnabled()) continue;
                foundAnyEnabled = true;
                for (PlayerItemLocation testLocation : PlayerItemLocation.enderchest(enderchestPage.getIndex())) {
                    ItemStack testStack = proposedChanges.containsKey(testLocation) ? proposedChanges.get(testLocation) :
                            testLocation.getItem(getUniqueID(), useOnlinePlayer).clone();
                    if (!testStack.isSimilar(itemStack)) continue;
                    int amountToAdd = Math.min(itemStack.getAmount(), maxStackSize - testStack.getAmount());
                    testStack.setAmount(testStack.getAmount() + amountToAdd);
                    itemStack.setAmount(Math.max(itemStack.getAmount() - amountToAdd, 0));
                    proposedChanges.put(testLocation, testStack);
                    System.out.println("找到相似的物品堆: " + testLocation.getIdentifier());
                    if (itemStack.getAmount() == 0) continue loop;
                }
            }

            for (EnderchestPage enderchestPage : getEnderchestPages()) {
                if (!enderchestPage.isWardrobeEnabled()) continue;
                for (PlayerItemLocation testLocation : PlayerItemLocation.enderchest(enderchestPage.getIndex())) {
                    ItemStack testStack = proposedChanges.containsKey(testLocation) ? proposedChanges.get(testLocation) :
                            testLocation.getItem(getUniqueID(), useOnlinePlayer).clone();
                    if (!Misc.isAirOrNull(testStack)) continue;
                    proposedChanges.put(testLocation, itemStack);
                    System.out.println("找到空槽位: " + testLocation.getIdentifier());
                    continue loop;
                }
            }

            if (player != null) {
                if (foundAnyEnabled) {
                    AOutput.error(player, "&c&l错误！&7 当前物品栏和盔甲没有足够的空间");
                } else {
                    AOutput.error(player, "&c&l错误！&7 你没有任何启用衣柜的末影箱");
                }
                Sounds.NO.play(player);
            }
            return false;
        }
        return true;
    }

    public UUID getUniqueID() {
        return uuid;
    }

    public Player getOnlinePlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }

    public ItemStack[] getInventory() {
        if(!isLoaded) throw new DataNotLoadedException();
        return inventory;
    }

    public void setInventory(ItemStack[] inventory) {
        if(!isLoaded) throw new DataNotLoadedException();
        this.inventory = inventory;
    }

    public ItemStack[] getArmor() {
        if(!isLoaded) throw new DataNotLoadedException();
        return armor;
    }

    public void setArmor(ItemStack[] armor) {
        if(!isLoaded) throw new DataNotLoadedException();
        this.armor = armor;
    }

    public EnderchestPage[] getEnderchestPages() {
        if(!isLoaded) throw new DataNotLoadedException();
        return enderchestPages;
    }

    public EnderchestPage getEnderchestPage(int index) {
        if(!isLoaded) throw new DataNotLoadedException();
        return enderchestPages[index];
    }

    public int getDefaultOverworldSet() {
        return defaultOverworldSet;
    }

    public void setDefaultOverworldSet(int defaultOverworldSet) {
        this.defaultOverworldSet = defaultOverworldSet;
    }

    public int getDefaultDarkzoneSet() {
        return defaultDarkzoneSet;
    }

    public void setDefaultDarkzoneSet(int defaultDarkzoneSet) {
        this.defaultDarkzoneSet = defaultDarkzoneSet;
    }

    public Outfit[] getOutfits() {
        return outfits;
    }

    public static ItemStack deserialize(String string, UUID informUUID) {
        if(string.isEmpty()) return new ItemStack(Material.AIR);
        return CustomSerializer.deserializeFromPlayerData(string, informUUID);
    }

    public static String serialize(OfflinePlayer player, ItemStack itemStack, boolean isLogout) {
        if(Misc.isAirOrNull(itemStack)) return "";
//		return Base64.itemTo64(itemStack);

        if(isLogout && ItemFactory.isTutorialItem(itemStack)) {
            AOutput.log("未保存教程物品: " + Misc.stringifyItem(itemStack));
            return "";
        }

        if(isLogout && EnchantManager.isIllegalItem(itemStack) && !player.isOp()) {
            AOutput.log("未保存非法物品: " + Misc.stringifyItem(itemStack));
            LogManager.onIllegalItemRemoved(player, itemStack);
            return "";
        }
        return CustomSerializer.serialize(itemStack);
    }

    public boolean isSaving() {
        return saving;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    protected void receiveSaveConfirmation(PluginMessage message) {
        saving = false;

        if(message.getStrings().get(0).equals("保存配置")) {
            if(saveRunnable != null) saveRunnable.run();
            saveRunnable = null;
            if(saveTask != null) saveTask.cancel();
        }
    }

    public void initializePlayerInventory(Player player) {
        player.setItemOnCursor(null);
        player.getInventory().setContents(getInventory());
        player.getInventory().setArmorContents(getArmor());
        player.updateInventory();
    }

}
