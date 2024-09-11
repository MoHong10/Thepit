package me.wisdom.thepit.commands.essentials;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.misc.Lang;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GamemodeCommand implements CommandExecutor {
    public static GamemodeCommand INSTANCE;

    public GamemodeCommand() {
        INSTANCE = this;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if (!player.hasPermission("pitsim.gamemode") && !Thepit.isDev()) {
            Lang.NO_PERMISSION.send(player);
            return false;
        }

        GamemodeInfo gamemodeInfo = null;
        String targetName = null;
        if (label.equals("gm") || label.equals("gamemode")) {
            if (args.length < 1) {
                AOutput.error(player, "&c&l错误!&7 请选择一个游戏模式");
                return false;
            }

            gamemodeInfo = GamemodeInfo.getGamemode(args[0]);
            if (gamemodeInfo == null) {
                AOutput.error(player, "&c&l错误!&7 找不到该游戏模式");
                return false;
            }
            if (args.length >= 2 && player.hasPermission("pitsim.gamemode")) targetName = args[1];
        } else {
            if (args.length >= 1 && player.hasPermission("pitsim.gamemode")) targetName = args[0];
        }

        if (label.equals("gms")) {
            gamemodeInfo = GamemodeInfo.SURVIVAL;
        } else if (label.equals("gmc")) {
            gamemodeInfo = GamemodeInfo.CREATIVE;
        } else if (label.equals("gma")) {
            gamemodeInfo = GamemodeInfo.ADVENTURE;
        } else if (label.equals("gmsp")) {
            gamemodeInfo = GamemodeInfo.SPECTATOR;
        }
        assert gamemodeInfo != null;

        Player target;
        if (targetName != null) {
            target = Bukkit.getPlayer(targetName);
            if (target == null) {
                AOutput.error(player, "&c&l错误!&7 找不到该玩家");
                return false;
            }
        } else {
            target = player;
        }

        target.setGameMode(gamemodeInfo.gameMode);
        AOutput.send(target, "&a&l游戏模式!&7 已切换到 " + gamemodeInfo.displayName);
        if (target != player) AOutput.send(player, "&a&l游戏模式!&7 已为 " +
                Misc.getDisplayName(target) + " &7切换游戏模式到 " + gamemodeInfo.displayName);

        return false;
    }

    public enum GamemodeInfo {
        SURVIVAL(GameMode.SURVIVAL, "&c生存", "survival", "0", "s"),
        CREATIVE(GameMode.CREATIVE, "&9创造", "creative", "1", "c"),
        ADVENTURE(GameMode.ADVENTURE, "&e冒险", "adventure", "2", "a"),
        SPECTATOR(GameMode.SPECTATOR, "&f旁观", "spectator", "3", "sp");

        public GameMode gameMode;
        public String displayName;
        public List<String> refNames;

        GamemodeInfo(GameMode gameMode, String displayName, String... refNames) {
            this.gameMode = gameMode;
            this.displayName = displayName;
            this.refNames = new ArrayList<>(Arrays.asList(refNames));
        }

        public static GamemodeInfo getGamemode(String refName) {
            for (GamemodeInfo value : values()) if (value.refNames.contains(refName.toLowerCase())) return value;
            return null;
        }
    }
}
