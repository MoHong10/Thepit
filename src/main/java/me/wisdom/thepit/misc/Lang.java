package me.wisdom.thepit.misc;

import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.entity.Player;

public class Lang {
    public static Message COULD_NOT_FIND_PLAYER_WITH_NAME = new Message("&c&l错误！&7 找不到该名称的玩家");
    public static Message NO_PERMISSION = new Message("&c&l错误！&7 您没有权限执行此操作");
    public static Message NOT_ENOUGH_SOULS = new Message("&c&l错误！&7 您的灵魂数量不足");

    public static class Message {
        public String message;

        public Message(String message) {
            this.message = message;
        }

        public void send(Player player) {
            AOutput.send(player, message);
        }
    }
}
