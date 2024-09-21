package me.wisdom.thepit.controllers;

import me.wisdom.thepit.commands.ClaimCommand;
import me.wisdom.thepit.controllers.objects.PluginMessage;
import me.wisdom.thepit.events.MessageEvent;
import me.wisdom.thepit.sql.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class DiscordManager implements Listener {
    public static final String DISCORD_TABLE = "DiscordAuthentication";

    public static long getLastBoostRewardClaim(UUID uuid) {
        SQLTable table = TableManager.getTable(DISCORD_TABLE);
        if (table == null) throw new RuntimeException("Discord 表未注册！");

        ResultSet rs = table.selectRow(new Constraint("uuid", uuid.toString()), new Field("last_boosting_claim"));


        try {
            long returnVal = -1;
            if(rs.next()) {
                returnVal = rs.getLong("last_boosting_claim");
            }
            rs.close();
            return returnVal;
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return Long.MAX_VALUE;
    }

    public static void setLastBoostRewardClaim(UUID uuid, long millis) {
        SQLTable table = TableManager.getTable(DISCORD_TABLE);
        if (table == null) throw new RuntimeException("Discord 表格未注册！");

        table.updateRow(new Constraint("uuid", uuid.toString()), new Value("last_boosting_claim", millis));
    }

    @EventHandler
    public void onMessage(MessageEvent event) {
        PluginMessage message = event.getMessage();
        List<String> strings = message.getStrings();
        List<Boolean> booleans = message.getBooleans();
        if(strings.isEmpty()) return;

        if(strings.get(0).equals("BOOSTER_CLAIM")) {
            UUID playerUUID = UUID.fromString(strings.get(1));
            Player player = Bukkit.getPlayer(playerUUID);
            boolean isBooster = booleans.get(0);
            if(player != null) ClaimCommand.callback(player, isBooster);
        }
    }
}
