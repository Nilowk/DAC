package fr.nilowk.decoudre.tasks;

import fr.nilowk.decoudre.Gstate;
import fr.nilowk.decoudre.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameCycle extends BukkitRunnable {

    private Main instance;

    public GameCycle(Main instance) {

        this.instance = instance;

    }

    @Override
    public void run() {

        if (instance.time < 0) {

            instance.setState(Gstate.FINISH);
            FinishTask finish = new FinishTask(instance);
            finish.runTaskTimer(instance, 0, 20);
            instance.winner = instance.getPlayers().get(0).getName();

            cancel();

        }

        for (Player player : instance.getPlayers()) {

            if (instance.time == 15) {

                Location spawn = new Location(player.getWorld(), 0.5, 110, 0.5, 0f, 0f);
                instance.getPlayers().get(instance.index).teleport(spawn);
                player.setGameMode(GameMode.SPECTATOR);

            }
            if (instance.time >= 0) {

                player.setLevel(instance.time);

            }

        }

        if (instance.time == 15) {

            Player player = instance.getPlayers().get(instance.index);
            player.setGameMode(GameMode.ADVENTURE);

        }

        if (instance.time == 0) {

            Player player = instance.getPlayers().get(instance.index);
            player.setGameMode(GameMode.SPECTATOR);
            instance.getPlayers().remove(player);

            if (instance.getPlayers().size() != 1) {

                if (instance.index >= (instance.getPlayers().size() - 1)) {

                    instance.index = 0;

                } else {

                    instance.index++;

                }

                instance.time = 16;

            } else {

                instance.time = -1;

            }

        }

        instance.time--;

    }

}
