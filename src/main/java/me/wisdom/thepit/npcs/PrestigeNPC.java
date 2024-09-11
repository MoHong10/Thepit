package me.wisdom.thepit.npcs;

import me.wisdom.thepit.controllers.MapManager;
import me.wisdom.thepit.controllers.objects.PitNPC;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.inventories.PrestigeGUI;
import me.wisdom.thepit.tutorial.Tutorial;
import me.wisdom.thepit.tutorial.TutorialObjective;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public class PrestigeNPC extends PitNPC {

    public PrestigeNPC(List<World> worlds) {
        super(worlds);
    }

    @Override
    public Location getRawLocation() {
        return null;
    }

    @Override
    public Location getFinalLocation(World world) {
        return MapManager.currentMap.getPrestigeNPCSpawn();
    }

    @Override
    public void createNPC(Location location) {
        spawnVillagerNPC(" ", location);
    }

    @Override
    public void onClick(Player player) {

        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        Tutorial tutorial = pitPlayer.overworldTutorial;
        if(tutorial.isInObjective) return;
        if(tutorial.isActive() && !tutorial.isCompleted(TutorialObjective.PRESTIGE)) {

            tutorial.sendMessage("&b&l声望: &e你是否已经达到 &7[&b&l120&7] &e等级，无法再升级了？", 0);
            tutorial.sendMessage("&b&l声望: &e那么你可能已经准备好进行声望提升了！", 20 * 4);
            tutorial.sendMessage("&b&l声望: &e声望提升会将你重置到 &9[&71&9]&e，但允许你用声望解锁新的升级！点击我了解更多！", 20 * 7);
            tutorial.completeObjective(TutorialObjective.PRESTIGE, 20 * 12);

            return;
        }

        PrestigeGUI prestigeGUI = new PrestigeGUI(player);
        prestigeGUI.open();
    }
}
