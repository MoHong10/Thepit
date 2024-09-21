package me.wisdom.thepit.storage;

import de.tr7zw.nbtapi.NBTItem;
import me.wisdom.thepit.controllers.ItemFactory;
import me.wisdom.thepit.controllers.objects.PluginMessage;
import me.wisdom.thepit.enums.NBTTag;
import me.wisdom.thepit.enums.RankInformation;
import me.wisdom.thepit.exceptions.PitException;
import me.wisdom.thepit.items.PitItem;
import me.wisdom.thepit.items.StaticPitItem;
import me.wisdom.thepit.misc.CustomSerializer;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepit.misc.PlayerItemLocation;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Outfit {
    private final StorageProfile profile;
    private final int index;

    private ItemStack displayItem;
    private final Map<PlayerItemLocation, String> itemLocationMap = new LinkedHashMap<>();

    public Outfit(StorageProfile profile, PluginMessage message) {
        this.profile = profile;

        List<String> strings = message.getStrings();
        List<Integer> integers = message.getIntegers();

        this.index = integers.remove(0);
        this.displayItem = CustomSerializer.deserializeDirectly(strings.remove(0));
        int locationMapSize = integers.remove(0);
        for(int i = 0; i < locationMapSize; i++) {
            PlayerItemLocation itemLocation = PlayerItemLocation.getLocation(strings.remove(0));
            itemLocationMap.put(itemLocation, strings.remove(0));
        }
    }

    public void writeData(PluginMessage message) {
        message.writeString(CustomSerializer.serialize(displayItem));
        message.writeInt(itemLocationMap.size());
        for(Map.Entry<PlayerItemLocation, String> entry : itemLocationMap.entrySet()) {
            message.writeString(entry.getKey().getIdentifier());
            message.writeString(entry.getValue());
        }
    }

    public boolean equip(boolean useOnlinePlayer) {
        Map<PlayerItemLocation, ItemStack> proposedChanges = new HashMap<>();
        Player player = profile.getOnlinePlayer();

        List<PlayerItemLocation> emptySlots = new ArrayList<>();
        List<ItemCandidate> itemCandidates = new ArrayList<>();
        for(PlayerItemLocation itemLocation : PlayerItemLocation
                .getLocations(PlayerItemLocation.Location.INVENTORY, PlayerItemLocation.Location.ARMOR)) {
            ItemStack itemStack = itemLocation.getItem(profile.getUniqueID(), useOnlinePlayer);
            PitItem pitItem = ItemFactory.getItem(itemStack);
            if(pitItem == null) continue;
            if(!pitItem.hasUUID && !(pitItem instanceof StaticPitItem)) continue;
            itemCandidates.add(new ItemCandidate(itemLocation, pitItem, itemStack));
        }
        for(int i = 0; i < StorageManager.MAX_ENDERCHEST_PAGES; i++) {
            EnderchestPage enderchestPage = profile.getEnderchestPage(i);
            if(!enderchestPage.isWardrobeEnabled()) continue;
            Inventory inventory = enderchestPage.getInventory();
            for(int j = 0; j < StorageManager.ENDERCHEST_ITEM_SLOTS; j++) {
                int inventorySlot = j + 9;
                ItemStack itemStack = useOnlinePlayer ? inventory.getItem(inventorySlot) : enderchestPage.getItems()[j];
                PitItem pitItem = ItemFactory.getItem(itemStack);
                if(pitItem == null) continue;
                if(!pitItem.hasUUID && !(pitItem instanceof StaticPitItem)) continue;
                itemCandidates.add(new ItemCandidate(PlayerItemLocation.enderchest(i, j), pitItem, itemStack));
            }
        }

        Map<PlayerItemLocation, RequestedItemInformation> requestedItemLocationMap = new LinkedHashMap<>();
        for(Map.Entry<PlayerItemLocation, String> entry : itemLocationMap.entrySet()) {
            RequestedItemInformation requestedItem = locateItem(emptySlots, itemCandidates, entry.getValue());
            if(requestedItem == null) continue;
            requestedItemLocationMap.put(entry.getKey(), requestedItem);

            if(!requestedItem.isFulfilled()) System.out.println("缺少物品: " + requestedItem.identifier);

            System.out.println("请求的物品已识别: " + requestedItem.identifier);
            for(Map.Entry<PlayerItemLocation, Integer> sourceEntry : requestedItem.itemSourceMap.entrySet())
                System.out.println(sourceEntry.getKey().getIdentifier() + ": "+ sourceEntry.getValue());
        }

        for(PlayerItemLocation emptySlot : emptySlots) proposedChanges.put(emptySlot, new ItemStack(Material.AIR));
        for(ItemCandidate candidate : itemCandidates) proposedChanges.put(candidate.getItemLocation(), candidate.getModifiedStack());

        boolean successfulStore = profile.storeInvAndArmor(proposedChanges, emptySlots, useOnlinePlayer);
        if(!successfulStore) return false;

        for(Map.Entry<PlayerItemLocation, RequestedItemInformation> entry : requestedItemLocationMap.entrySet())
            proposedChanges.put(entry.getKey(), entry.getValue().getRequestedItem());

        for(Map.Entry<PlayerItemLocation, ItemStack> entry : proposedChanges.entrySet())
            entry.getKey().setItem(profile.getUniqueID(), entry.getValue(), useOnlinePlayer);
        if(player != null) player.updateInventory();

//		for(Map.Entry<PlayerItemLocation, ItemStack> entry : proposedChanges.entrySet()) {
//			String itemString = Misc.isAirOrNull(entry.getValue()) ? "AIR" : new NBTItem(entry.getValue()).toString();
//			System.out.println(entry.getKey().getIdentifier() + ": " + itemString);
//		}

        return true;
    }

    public void save() {
        itemLocationMap.clear();
        Player player = profile.getOnlinePlayer();

        for(int i = 0; i < 36; i++) {
            PlayerItemLocation itemLocation = PlayerItemLocation.inventory(i);
            String identifier = getItemIdentifier(player.getInventory().getItem(i));
            if(identifier == null) continue;
            itemLocationMap.put(itemLocation, identifier);
        }
        for(int i = 0; i < 4; i++) {
            PlayerItemLocation itemLocation = PlayerItemLocation.armor(i);
            String identifier = getItemIdentifier(player.getInventory().getArmorContents()[i]);
            if(identifier == null) continue;
            itemLocationMap.put(itemLocation, identifier);
        }

        System.out.println("保存衣柜 " + (getIndex() + 1));
        for(Map.Entry<PlayerItemLocation, String> entry : itemLocationMap.entrySet()) {
            System.out.println(entry.getKey().getIdentifier() + ": " + entry.getValue());
        }
    }

    public void clear() {
        itemLocationMap.clear();
        displayItem = null;
    }

    public RequestedItemInformation locateItem(List<PlayerItemLocation> emptySlots, List<ItemCandidate> candidates, String identifier) {
        RequestedItemInformation requestedItem;
        try {
            requestedItem = new RequestedItemInformation(identifier);
        } catch(PitException ignored) {
            return null;
        }

        for(ItemCandidate candidate : new ArrayList<>(candidates)) {
            if(!isSameItem(identifier, candidate)) continue;
            requestedItem.foundItem(emptySlots, candidates, candidate);
            if(requestedItem.isFulfilled()) return requestedItem;
        }

        return requestedItem;
    }

    public boolean isSameItem(String identifier, ItemCandidate candidate) {
        if(candidate.hasUUID()) return candidate.getUUID().toString().equals(identifier);
        return candidate.staticPitItem.getNBTID().equals(identifier);
    }

    public String getItemIdentifier(ItemStack itemStack) {
        PitItem pitItem = ItemFactory.getItem(itemStack);
        if(pitItem == null) return null;
        if(pitItem.hasUUID) {
            NBTItem nbtItem = new NBTItem(itemStack);
            return nbtItem.getString(NBTTag.ITEM_UUID.getRef());
        }
        return pitItem.getNBTID();
    }

    public boolean isUnlocked() {
        return RankInformation.getRank(profile.getOnlinePlayer()).outfits > index;
    }

    public ItemStack getDisplayItem() {
        if(!isUnlocked()) return
                new AItemStackBuilder(Material.STAINED_GLASS_PANE, 1, 15).getItemStack();
        if(Misc.isAirOrNull(displayItem)) return
                new AItemStackBuilder(Material.ITEM_FRAME)
                        .setName("&c无显示物品")
                        .setLore(new PitLoreBuilder(
                                "&7在设置中设置一个显示物品"
                        ))
                        .getItemStack();
        return displayItem.clone();
    }

    public void setDisplayItem(ItemStack displayItem) {
        this.displayItem = displayItem;
    }

    public int getIndex() {
        return index;
    }

    public ItemStack getStateItem() {
        OutfitState outfitState = getState();

        PitLoreBuilder loreBuilder = new PitLoreBuilder("&7数据: " + outfitState.getDisplayName());
        if (outfitState.isEquippable()) {
            loreBuilder.addLongLine("&e点击装备！");
        } else if (outfitState == OutfitState.NO_DATA) {
            loreBuilder.addLongLine("&c没有数据！");
        } else if (outfitState == OutfitState.LOCKED) {
            loreBuilder.addLongLine("&7所需等级: " + RankInformation.getMinimumRankForOutfits(getIndex() + 1).rankName, false);
            loreBuilder.addLongLine("&c等级太低！");
        }

        return new AItemStackBuilder(Material.STAINED_CLAY, 1, outfitState.getMaterialData())
                .setName("&2装备 " + (getIndex() + 1))
                .setLore(loreBuilder)
                .getItemStack();
    }

    public Map<PlayerItemLocation, String> getItemLocationMap() {
        return itemLocationMap;
    }

    public OutfitState getState() {
        if(!isUnlocked()) return OutfitState.LOCKED;
        if(itemLocationMap.isEmpty()) return OutfitState.NO_DATA;
        if(profile.getDefaultOverworldSet() == index) return OutfitState.OVERWORLD_DEFAULT;
        if(profile.getDefaultDarkzoneSet() == index) return OutfitState.DARKZONE_DEFAULT;
        return OutfitState.EQUIPPABLE;
    }

    public enum OutfitState {
        LOCKED("已锁定", ChatColor.RED, 14),
        NO_DATA("无数据", ChatColor.AQUA, 3),
        EQUIPPABLE("可装备", ChatColor.YELLOW, 4),
        OVERWORLD_DEFAULT("世界默认", ChatColor.GREEN, 13),
        DARKZONE_DEFAULT("黑暗区域默认", ChatColor.DARK_PURPLE, 10);
        private final String displayName;
        private final ChatColor chatColor;
        private final int materialData;

        OutfitState(String displayName, ChatColor chatColor, int materialData) {
            this.displayName = displayName;
            this.chatColor = chatColor;
            this.materialData = materialData;
        }

        public boolean isEquippable() {
            switch(this) {
                case EQUIPPABLE:
                case OVERWORLD_DEFAULT:
                case DARKZONE_DEFAULT:
                    return true;
            }
            return false;
        }

        public String getDisplayName() {
            return chatColor + displayName;
        }

        public ChatColor getChatColor() {
            return chatColor;
        }

        public int getMaterialData() {
            return materialData;
        }
    }

    public static class ItemCandidate {
        private final PlayerItemLocation itemLocation;
        private final ItemStack itemStack;
        private UUID uuid;
        private StaticPitItem staticPitItem;
        private int amountLeft;

        public ItemCandidate(PlayerItemLocation itemLocation, PitItem pitItem, ItemStack itemStack) {
            this.itemLocation = itemLocation;
            this.itemStack = itemStack.clone();
            this.amountLeft = itemStack.getAmount();
            if(pitItem.hasUUID) {
                NBTItem nbtItem = new NBTItem(itemStack);
                uuid = UUID.fromString(nbtItem.getString(NBTTag.ITEM_UUID.getRef()));
            } else if(pitItem instanceof StaticPitItem) {
                staticPitItem = (StaticPitItem) pitItem;
            } else {
                throw new RuntimeException();
            }
        }

        public PlayerItemLocation getItemLocation() {
            return itemLocation;
        }

        public UUID getUUID() {
            return uuid;
        }

        public boolean hasUUID() {
            return uuid != null;
        }

        public StaticPitItem getStaticPitItem() {
            return staticPitItem;
        }

        public int getAmountLeft() {
            return amountLeft;
        }

        public void removeItems(int amount) {
            amountLeft -= amount;
        }

        public ItemStack getModifiedStack() {
            ItemStack itemstack = this.itemStack.clone();
            itemstack.setAmount(amountLeft);
            return itemstack;
        }
    }

    public static class RequestedItemInformation {
        private final Map<PlayerItemLocation, Integer> itemSourceMap = new HashMap<>();
        private ItemStack itemStack = new ItemStack(Material.AIR);
        private final String identifier;
        private UUID uuid;
        private int requiredItems;
        private int foundItems;

        public RequestedItemInformation(String identifier) throws PitException {
            this.identifier = identifier;
            try {
                uuid = UUID.fromString(identifier);
                this.requiredItems = 1;
            } catch(IllegalArgumentException ignored) {
                PitItem pitItem = ItemFactory.getItem(identifier);
                if(!(pitItem instanceof StaticPitItem)) throw new PitException();
                StaticPitItem staticPitItem = (StaticPitItem) pitItem;
                this.requiredItems = staticPitItem.getMaxStackSize();
            }
        }

        public void foundItem(List<PlayerItemLocation> emptySlots, List<ItemCandidate> candidates, ItemCandidate candidate) {
            if(isFulfilled()) throw new RuntimeException();
            if(Misc.isAirOrNull(itemStack)) itemStack = candidate.itemStack;
            int itemsNeeded = requiredItems - foundItems;
            if(candidate.getAmountLeft() > itemsNeeded) {
                itemSourceMap.put(candidate.getItemLocation(), itemsNeeded);
                candidate.removeItems(itemsNeeded);
                foundItems = requiredItems;
            } else {
                itemSourceMap.put(candidate.getItemLocation(), candidate.getAmountLeft());
                candidates.remove(candidate);
                emptySlots.add(candidate.getItemLocation());
                foundItems += candidate.getAmountLeft();
            }
        }

        public boolean isFulfilled() {
            return foundItems == requiredItems;
        }

        public boolean hasUUID() {
            return uuid != null;
        }

        public ItemStack getRequestedItem() {
            ItemStack itemStack = this.itemStack.clone();
            itemStack.setAmount(foundItems);
            return itemStack;
        }
    }
}
