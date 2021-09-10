package fr.nilowk.decoudre.tasks;

import fr.nilowk.decoudre.Gstate;
import fr.nilowk.decoudre.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class AutoStart extends BukkitRunnable {

    private int timer = 30;
    private Main instance;

    public AutoStart(Main instance) {

        this.instance = instance;

    }

    @Override
    public void run() {

        if (instance.getPlayers().size() == 6 && timer > 1) {

            timer = 1;

        }

        for(Player player : instance.getPlayers()) {

            player.setLevel(timer);

            if (timer == 10) {

                player.sendTitle("§6Lancement du jeu dans", "§610 secondes", 1, 40, 1);

            }

            if (timer <= 5 && timer != 0) {

                player.sendTitle("§6Lancement du jeu dans", "§6" + timer + " secondes", 1, 20, 1);

            }

            if (timer == 0 && instance.getPlayers().size() < 2) {

                player.sendTitle("§4Lancement du jeu annulé", "§4Raison : manque de joueurs", 1, 40, 1);

            }

        }

        if (timer == 10) {

            Bukkit.broadcastMessage("§6Lancement du jeu dans 10 secondes");

        }

        if (timer <= 5 && timer != 0) {

            Bukkit.broadcastMessage("§6Lancement du jeu dans " + timer + " secondes");

        }

        if (timer == 1) {

            for (Player player : instance.getPlayers()) {

                Location spawn = new Location(player.getWorld(), 0.5, 110, 0.5, 0f, 0f);
                player.teleport(spawn);

            }

        }

        if (timer == 0) {

            Bukkit.broadcastMessage("§6Lancement du jeu");
            if (instance.getPlayers().size() < 2) {

                instance.setState(Gstate.WAITING);
                Bukkit.broadcastMessage("§4Lancement du jeu annulé, Raison : manque de joueurs");
                cancel();

            } else {

                instance.setState(Gstate.PLAYING);

                for (Player player : instance.getPlayers()) {

                    boolean boucle = true;

                    while (boucle) {

                        int col = new Random().nextInt(7);

                        if (!instance.getColor().containsValue(instance.getBlocks().get(col))) {

                            instance.getColor().put(player.getName(), instance.getBlocks().get(col));
                            boucle = false;

                        }

                    }

                }

                GameCycle cycle = new GameCycle(instance);
                cycle.runTaskTimer(instance, 0, 20);

                cancel();

            }

        }

        timer--;

    }

}
