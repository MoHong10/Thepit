package me.wisdom.thepit.npcs;

import me.wisdom.thepit.controllers.MapManager;
import me.wisdom.thepit.controllers.objects.PitNPC;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.inventories.help.KitGUI;
import me.wisdom.thepit.tutorial.Tutorial;
import me.wisdom.thepit.tutorial.TutorialObjective;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;

import java.util.List;

public class KitNPC extends PitNPC {

    public KitNPC(List<World> worlds) {
        super(worlds);
    }

    @Override
    public Location getRawLocation() {
        return null;
    }

    @Override
    public Location getFinalLocation(World world) {
        return MapManager.currentMap.getKitsNPCSpawn();
    }

    @Override
    public void createNPC(Location location) {
        NPCRegistry registry = CitizensAPI.getNPCRegistry();
        NPC npc = registry.createNPC(EntityType.RABBIT, " ");
        npc.spawn(location);
        Rabbit rabbit = (Rabbit) npc.getEntity();
        rabbit.setRabbitType(Rabbit.Type.WHITE);
        npc.getEntity().setCustomNameVisible(false);
        npcs.add(npc);
    }

    @Override
    public void onClick(Player player) {

        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        Tutorial tutorial = pitPlayer.overworldTutorial;

        if(tutorial.isInObjective) return;
        if(tutorial.isActive() && !tutorial.isCompleted(TutorialObjective.KITS)) {

            tutorial.sendMessage("&c&l礼包: &e如果你不知道用什么，你就只是一个待宰的猎物……", 0);
            tutorial.sendMessage("&c&l礼包: &e幸运的是，我有你需要的基本装备，让你像专业人士一样游戏！", 20 * 4);
            tutorial.sendMessage("&c&l礼包: &e再次点击我以访问礼包。最好每种礼包都拿一个！", 20 * 8);
            tutorial.completeObjective(TutorialObjective.KITS, 20 * 12);

            return;
        }

        KitGUI kitGUI = new KitGUI(player);
        kitGUI.kitPanel.openPanel(kitGUI.kitPanel);
    }
}
