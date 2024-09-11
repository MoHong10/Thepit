package me.wisdom.thepit.npcs;

import me.wisdom.thepit.controllers.MapManager;
import me.wisdom.thepit.controllers.objects.PitNPC;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.darkzone.progression.ProgressionManager;
import me.wisdom.thepit.darkzone.progression.SkillBranch;
import me.wisdom.thepit.darkzone.progression.skillbranches.BrewingBranch;
import me.wisdom.thepit.inventories.PotionMasterGUI;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public class PotionMasterNPC extends PitNPC {

    public PotionMasterNPC(List<World> worlds) {
        super(worlds);
    }

    @Override
    public Location getRawLocation() {
        return new Location(MapManager.getDarkzone(), 216.5, 91, -102.5, 25, 0);
    }

    @Override
    public void createNPC(Location location) {
        spawnPlayerNPC("&d&lPOTIONS", "Wiizard", location, false);
    }

    @Override
    public void onClick(Player player) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        if(!ProgressionManager.isUnlocked(pitPlayer, BrewingBranch.INSTANCE, SkillBranch.MajorUnlockPosition.FIRST_PATH)) {
            Sounds.NO.play(player);
            AOutput.error(player, "&c在技能树的酿造部分解锁对药水大师的访问权限！");
            return;
        }

        PotionMasterGUI potionMasterGUI = new PotionMasterGUI(player);
        potionMasterGUI.open();
    }
}
