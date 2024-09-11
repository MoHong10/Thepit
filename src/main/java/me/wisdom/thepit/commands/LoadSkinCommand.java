package me.wisdom.thepit.commands;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.SkinManager;
import me.wisdom.thepitapi.misc.AOutput;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LoadSkinCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if (!player.isOp()) return false;

        if (args.length < 1) {
            AOutput.error(player, "&c&l错误!&7 用法: /" + label + " <skinName>");
            return false;
        }

        loadTexture(player, args[0]);

        return false;
    }

    public static void loadTexture(Player player, String skinName) {
        AOutput.send(player, "尝试加载皮肤信息: " + skinName);

        SkinManager.loadAndSkinNPC(skinName, new BukkitRunnable() {
            @Override
            public void run() {
                NPC tempNPC = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, skinName);
                tempNPC.spawn(player.getLocation());
                SkinTrait skinTrait = CitizensAPI.getTraitFactory().getTrait(SkinTrait.class);
                tempNPC.addTrait(skinTrait);
                skinTrait.setSkinName(skinName);

                System.out.println("新的 皮肤(\"" + skinName + "\",\n\t\t\"" + skinTrait.getTexture() +
                        "\",\n\t\t\"" + skinTrait.getSignature() + "\"\n);");
                AOutput.send(player, "&9&l皮肤!&7 皮肤信息已打印到控制台");

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        tempNPC.destroy();
                    }
                }.runTaskLater(Thepit.INSTANCE, 20);
            }
        });
    }
}
