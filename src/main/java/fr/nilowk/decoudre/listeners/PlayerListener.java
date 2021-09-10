package fr.nilowk.decoudre.listeners;

import fr.nilowk.decoudre.Gstate;
import fr.nilowk.decoudre.Main;
import fr.nilowk.decoudre.tasks.AutoStart;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.*;

public class PlayerListener implements Listener {

    private Main instance;

    public PlayerListener(Main main) {

        this.instance = main;

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        Location spawn = new Location(player.getWorld(), 0.5, 110, 0.5, 0f, 0f);

        player.teleport(spawn);
        player.getInventory().clear();
        player.setFoodLevel(20);
        player.setHealth(20);

        if (!instance.isState(Gstate.WAITING) && !instance.isState(Gstate.STARTING)) {

            if (instance.isState(Gstate.FINISH)) {

                event.setJoinMessage("");
                player.kickPlayer("la partie est terminé, merci de patienter " + instance.finishTime + " secondes avant une nouvelle partie");
                return;

            } else {

                player.setGameMode(GameMode.SPECTATOR);
                event.setJoinMessage("§f" + player.getName() + " regarde la partie");
                return;

            }

        }

        player.setGameMode(GameMode.ADVENTURE);
        if (!instance.getPlayers().contains(player)) instance.getPlayers().add(player);

        event.setJoinMessage("§f" + player.getName() + " a rejoin la partie (" + instance.getPlayers().size() + "/6)");

        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("objective", "dummy");

        objective.setDisplayName(ChatColor.GOLD + "Dé à coudre");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score ExtraLife = objective.getScore(ChatColor.BLUE + "Extra Life :");
        ExtraLife.setScore(0);

        event.getPlayer().setScoreboard(scoreboard);

        if (instance.isState(Gstate.WAITING) && instance.getPlayers().size() == 2) {

            AutoStart start = new AutoStart(instance);
            instance.setState(Gstate.STARTING);
            start.runTaskTimer(instance, 0, 20);

        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        if (!instance.isState(Gstate.PLAYING) && !instance.isState(Gstate.FINISH)) {

            if (instance.getPlayers().contains(event.getPlayer())) {

                instance.getPlayers().remove(event.getPlayer());
                event.setQuitMessage("§f" + event.getPlayer().getName() + " a quitter la partie (" + instance.getPlayers().size() + "/6)");

            } else {

                event.setQuitMessage("§f" + event.getPlayer().getName() + " ne regarde plus la partie");

            }

        } else if (instance.isState(Gstate.FINISH)) {

            event.setQuitMessage("§f" + event.getPlayer().getName() + " a quitter le jeu");

        }

    }

    @EventHandler
    public void takeDamage(EntityDamageEvent event) {

        if (event.getEntityType() == EntityType.PLAYER) {

            Player player = Bukkit.getPlayer(event.getEntity().getName());
            Location spawn = new Location(player.getWorld(), 0.5, 110, 0.5, 0f, 0f);

            if (instance.isState(Gstate.WAITING) || instance.isState(Gstate.STARTING)) {

                player.teleport(spawn);
                event.setCancelled(true);

            }

        }

    }

}
