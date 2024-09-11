package me.wisdom.thepit.npcs;

import me.wisdom.thepit.controllers.MapManager;
import me.wisdom.thepit.controllers.objects.PitNPC;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.inventories.PerkGUI;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.tutorial.Tutorial;
import me.wisdom.thepit.tutorial.TutorialObjective;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public class PerkNPC extends PitNPC {

    public PerkNPC(List<World> worlds) {
        super(worlds);
    }

    @Override
    public Location getRawLocation() {
        return null;
    }

    @Override
    public Location getFinalLocation(World world) {
        return MapManager.currentMap.getPerksNPCSpawn();
    }

    @Override
    public void createNPC(Location location) {
        spawnVillagerNPC(" ", location);
    }

    @Override
    public void onClick(Player player) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        if(pitPlayer.isOnMega() && !player.isOp()) {
            AOutput.error(player, "&c&l错误!&7 在巨星状态下无法使用此功能！");
            return;
        }

        Tutorial tutorial = pitPlayer.overworldTutorial;
        if(tutorial.isInObjective) return;
        if(tutorial.isActive() && !tutorial.isCompleted(TutorialObjective.PERKS)) {

            String playerName = Misc.getRankColor(player.getUniqueId()) + player.getDisplayName();
            tutorial.sendMessage("&a&l增益: &e哦，你好 " + playerName + "&e！你需要一些助力来帮助你吗？", 0);
            tutorial.sendMessage("&a&l增益: &e你来对地方了！在我这里，你可以设置增益、击杀连击和巨星连击，提升你的游戏表现！", 20 * 3);
            tutorial.sendMessage("&a&l增益: &e利用这些升级成为你所做事情的最佳！在我这里，可能性是无限的！", 20 * 8);
            tutorial.completeObjective(TutorialObjective.PERKS, 20 * 12);

            return;
        }

        PerkGUI perkGUI = new PerkGUI(player);
        perkGUI.open();
    }
}
