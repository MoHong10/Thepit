package me.wisdom.thepit.npcs;

import me.wisdom.thepit.controllers.LobbySwitchManager;
import me.wisdom.thepit.controllers.MapManager;
import me.wisdom.thepit.controllers.objects.PitNPC;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.inventories.KeeperGUI;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.tutorial.Tutorial;
import me.wisdom.thepit.tutorial.TutorialObjective;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public class KeeperNPC extends PitNPC {

    public KeeperNPC(List<World> worlds) {
        super(worlds);
    }

    @Override
    public Location getRawLocation() {
        return null;
    }

    @Override
    public Location getFinalLocation(World world) {
        return MapManager.currentMap.getKeeperNPCSpawn();
    }

    @Override
    public void createNPC(Location location) {
        spawnPlayerNPC("&2&l守护者", "googasesportsog", location, false);
    }

    @Override
    public void onClick(Player player) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        Tutorial tutorial = pitPlayer.overworldTutorial;
        if(tutorial.isInObjective) return;
        if(tutorial.isActive() && !tutorial.isCompleted(TutorialObjective.KEEPER)) {

            String playerName = Misc.getRankColor(player.getUniqueId()) + player.getDisplayName();
            tutorial.sendMessage("&2&l守护者: &e你好 " + playerName + "&e! 找不到玩家或无法高效连杀吗？", 0);
            tutorial.sendMessage("&2&l守护者: &e 如果线上有足够的玩家，我可能能帮你解决这个问题！", 20 * 4);
            tutorial.sendMessage("&2&l守护者: &e 再次点击我，如果可能的话，我会带你去另一个大厅！", 20 * 8);
            tutorial.completeObjective(TutorialObjective.KEEPER, 20 * 12);

            return;
        }

        if(LobbySwitchManager.recentlyJoined.contains(player)) {
            AOutput.error(player, "&c&l错误!&7 加入后5秒内不能使用此命令！");
            return;
        }

        KeeperGUI keeperGUI = new KeeperGUI(player);
        keeperGUI.open();
    }
}
