package me.wisdom.thepit.battlepass.rewards;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.battlepass.PassReward;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.inventory.ItemStack;

public class PassKeyReward extends PassReward {
    public KeyType keyType;
    public int count;

    public PassKeyReward(KeyType keyType, int count) {
        this.keyType = keyType;
        this.count = count;
    }

    @Override
    public boolean giveReward(PitPlayer pitPlayer) {
        if(Misc.getEmptyInventorySlots(pitPlayer.player) < count / 64) {
            AOutput.error(pitPlayer.player, "&7请在你的背包留出空余格子");
            return false;
        }

        ConsoleCommandSender console = Thepit.INSTANCE.getServer().getConsoleSender();
        Bukkit.dispatchCommand(console, "cc give p " + keyType.refName + " " + count + " " + pitPlayer.player.getName());

        return true;
    }

    @Override
    public ItemStack getDisplayStack(PitPlayer pitPlayer, boolean hasClaimed) {
        ItemStack itemStack = new AItemStackBuilder(Material.TRIPWIRE_HOOK, count)
                .setName("&d&l钥匙 奖励")
                .setLore(new ALoreBuilder(
                        "&7奖励: &7" + count + "x " + keyType.displayName
                )).getItemStack();
        return itemStack;
    }

    public enum KeyType {
        PITSIM("basic", "&6&l天坑&e&l乱斗 &7钥匙"),
        TAINTED("tainted", "&5&l污秽 &7污秽");

        public final String refName;
        public final String displayName;

        KeyType(String refName, String displayName) {
            this.refName = refName;
            this.displayName = displayName;
        }
    }
}
