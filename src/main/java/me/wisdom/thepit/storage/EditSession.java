package me.wisdom.thepit.storage;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PluginMessage;
import me.wisdom.thepit.misc.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class EditSession {

    private final Player staffMember;
    private final UUID playerUUID;
    private final StorageProfile storageProfile;
    private EditType editType = null;

    private final String playerServer;
    private final boolean isPlayerOnline;

    public boolean hasResponded = false;

    public boolean playerClosed = true;

    protected InventoryHolder inventory = null;

    public EditSession(Player staffMember, UUID playerUUID, String playerServer, boolean isPlayerOnline) {
        this.staffMember = staffMember;
        this.playerUUID = playerUUID;

        this.storageProfile = StorageManager.getProfile(playerUUID);
        this.playerServer = playerServer;
        this.isPlayerOnline = isPlayerOnline;

        EditGUI gui = new EditGUI(staffMember, this);
        gui.open();
    }

    protected void respond(EditType editType) {
        this.editType = editType;

        PluginMessage response = new PluginMessage().writeString("编辑回复");
        response.writeString(staffMember.getUniqueId().toString());
        response.writeString(editType.message);
        response.writeBoolean(editType == EditType.OFFLINE_KICK);

        response.send();
        hasResponded = true;

        if(editType == EditType.CANCELED) {
            end();
            return;
        }

        Sounds.LOAD_INITIAL.play(staffMember);

        new BukkitRunnable() {
            @Override
            public void run() {
                createInventory();
            }
        }.runTaskLater(Thepit.INSTANCE, 10);

    }

    public void end() {
        StorageManager.editSessions.remove(this);
        if(editType != EditType.CANCELED) storageProfile.saveData(true);
        if(hasLeft() && editType != EditType.CANCELED) StorageManager.profiles.remove(storageProfile);

        PluginMessage message = new PluginMessage().writeString("结束编辑");
        message.writeString(playerUUID.toString()).send();

        if(inventory == null) return;
        HandlerList.unregisterAll((Listener) inventory);
        if(((EditInventoryPanel) inventory).runnable != null) ((EditInventoryPanel) inventory).runnable.cancel();
    }

    public void delete() {
        StorageManager.editSessions.remove(this);

        HandlerList.unregisterAll((Listener) inventory);
        if(((EditInventoryPanel) inventory).runnable != null) ((EditInventoryPanel) inventory).runnable.cancel();
    }

    public void createInventory() {
        inventory = new EditInventoryPanel(this);
        Thepit.INSTANCE.getServer().getPluginManager().registerEvents((Listener) inventory, Thepit.INSTANCE);

        staffMember.openInventory(inventory.getInventory());
        Sounds.LOAD_FINAL.play(staffMember);
    }

    public Player getStaffMember() {
        return staffMember;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public StorageProfile getStorageProfile() {
        return storageProfile;
    }

    public EditType getEditType() {
        return editType;
    }

    public String getPlayerServer() {
        return playerServer;
    }

    public boolean isPlayerOnline() {
        return isPlayerOnline;
    }

    public boolean hasLeft() {
        for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if(onlinePlayer.getUniqueId().equals(playerUUID)) return false;
        }
        return true;
    }
}

enum EditType {
    ONLINE("在线"),
    OFFLINE("离线"),
    OFFLINE_KICK("离线"),
    CANCELED("取消");
    public final String message;

    EditType(String message) {
        this.message = message;
    }
}
