package me.wisdom.thepit.npcs;

import me.wisdom.thepit.battlepass.inventories.PassGUI;
import me.wisdom.thepit.controllers.MapManager;
import me.wisdom.thepit.controllers.objects.PitNPC;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.tutorial.Tutorial;
import me.wisdom.thepit.tutorial.TutorialObjective;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public class PassNPC extends PitNPC {

    public PassNPC(List<World> worlds) {
        super(worlds);
    }

    @Override
    public Location getRawLocation() {
        return null;
    }

    @Override
    public Location getFinalLocation(World world) {
        return MapManager.currentMap.getPassNPCSpawn();
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
        if(tutorial.isActive() && !tutorial.isCompleted(TutorialObjective.PASS)) {

            String playerName = Misc.getRankColor(player.getUniqueId()) + player.getDisplayName();
            tutorial.sendMessage("&3&l战斗通行证: &e你好 " + playerName + "&e！想要一些免费奖励吗？", 0);
            tutorial.sendMessage("&3&l战斗通行证: &e在这里你可以参与 &6每日任务 &e和 &6每周任务 &e来换取奖励", 20 * 3);
            tutorial.sendMessage("&3&l战斗通行证: &e你还可以通过购买 &3战斗通行证 &e在 &f&nstore.pitsim.net &e获取更多奖励", 20 * 8);
            tutorial.completeObjective(TutorialObjective.PASS, 20 * 12);

            return;
        }

        PassGUI passGUI = new PassGUI(player);
        passGUI.open();
    }
}
