package me.wisdom.thepit.inventories.view;

import me.wisdom.pitguilds.Guild;
import me.wisdom.pitguilds.GuildManager;
import me.wisdom.pitguilds.GuildMember;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.storage.StorageProfile;
import me.wisdom.thepitapi.gui.AGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.*;

public class ViewGUI extends AGUI {
    public StorageProfile target;
    public UUID uuid;
    public String name;

    public MainViewPanel mainViewPanel;
    public InventoryViewPanel inventoryViewPanel;

    public boolean playerClosed = true;

    public static Map<UUID, ViewGUI> viewGUIs = new HashMap<>();

    public ViewGUI(Player player, StorageProfile target, UUID uuid, String name) {
        super(player);
        this.target = target;
        this.uuid = uuid;
        this.name = name;

        if(!target.isLoaded()) return;
        if(PitPlayer.getViewPlayer(target.getUniqueID()) == null && PitPlayer.getPitPlayer(player) == null) return;

        this.mainViewPanel = new MainViewPanel(this);
        this.inventoryViewPanel = new InventoryViewPanel(this);
        setHomePanel(mainViewPanel);

        viewGUIs.put(player.getUniqueId(), this);
    }

    public PitPlayer getPitPlayer() {
        Player onlinePlayer = Bukkit.getPlayer(uuid);
        return(onlinePlayer == null ? PitPlayer.getViewPlayer(uuid) : PitPlayer.getPitPlayer(player));
    }

    public static Guild getGuild(UUID playerUUID) {
        try {
            Field field = GuildManager.class.getDeclaredField("memberList");
            field.setAccessible(true);

            List<GuildMember> memberList = new ArrayList<>(((Map<UUID, GuildMember>) field.get(null)).values());
            for(GuildMember guildMember : memberList) {
                if(guildMember.playerUUID.equals(playerUUID)) return guildMember.getGuild();
            }
        } catch(NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
