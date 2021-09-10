package fr.nilowk.decoudre.tasks;

import fr.nilowk.decoudre.Gstate;
import fr.nilowk.decoudre.Main;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class FinishTask extends BukkitRunnable {

    private Main instance;
    private int timer = 10;

    public FinishTask(Main instance) {

        this.instance = instance;

    }

    @Override
    public void run() {

        instance.finishTime = timer;

        for (Player player : instance.getServer().getOnlinePlayers()) {

            player.sendTitle("§6The Winner is", "§6" + instance.winner, 1, 20, 1);
            player.setGameMode(GameMode.SPECTATOR);

        }

        if (timer <= 5 && timer != 0) {

            Bukkit.broadcastMessage("§6Fin de la partie dans " + timer + " secondes, une nouvelle partie va recommencer");

        }

        if (timer <= 0) {

            instance.getPlayers().clear();
            instance.getColor().clear();
            instance.getExtraLife().clear();
            instance.winner = "";
            instance.index = 0;
            instance.time = 15;
            instance.finishTime = 10;

            int x = -6;

            while (x != 6) {

                int z = 2;

                while (z != 14) {

                    for (Material block : instance.getBlocks()) {

                        if (instance.getServer().getWorld("world").getBlockAt(x,76, z).getType() == block) {

                            instance.getServer().getWorld("world").getBlockAt(x,76, z).setType(Material.WATER);

                        }

                    }

                    z++;

                }

                x++;

            }

            for (Player player : instance.getServer().getOnlinePlayers()) {

                player.sendTitle("§6La partie est", "§6Relancer", 1, 40, 1);
                instance.getPlayers().add(player);
                player.setGameMode(GameMode.ADVENTURE);
                Location spawn = new Location(player.getWorld(), 0.5, 110, 0.5, 0f, 0f);
                player.teleport(spawn);
                player.getScoreboard().getObjective("objective").getScore(ChatColor.BLUE + "Extra Life :").setScore(0);

            }

            if (instance.getPlayers().size() < 2) {

                instance.setState(Gstate.WAITING);

            } else {

                AutoStart start = new AutoStart(instance);
                instance.setState(Gstate.STARTING);
                start.runTaskTimer(instance, 0, 20);

            }

            cancel();

        }

        timer--;

    }

}
