package fr.nilowk.decoudre.listeners;

import fr.nilowk.decoudre.Gstate;
import fr.nilowk.decoudre.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GameManager implements Listener {

    private Main instance;

    public GameManager(Main instance) {

        this.instance = instance;

    }

    @EventHandler
    public void onTakeDamage(EntityDamageEvent event) {

        if (event.getEntityType() == EntityType.PLAYER) {

            Player player = Bukkit.getPlayer(event.getEntity().getName());

            if (instance.isState(Gstate.PLAYING)) {

                if (instance.getExtraLife().containsKey(player)) {

                    if (instance.getExtraLife().get(player) < 1) {

                        instance.getPlayers().remove(player);

                        event.setCancelled(true);
                        player.setGameMode(GameMode.SPECTATOR);
                        Location spawn = new Location(player.getWorld(), 0.5, 110, 0.5, 0f, 0f);
                        player.teleport(spawn);

                        if (instance.getPlayers().size() != 1) {

                            Bukkit.broadcastMessage(player.getName() + " est mort comme une merde plus que " + instance.getPlayers().size() + " joueurs");
                            if (instance.index >= (instance.getPlayers().size() - 1)) {

                                instance.index = 0;

                            } else {

                                instance.index++;

                            }

                            instance.time = 15;

                        } else {

                            instance.time = -1;

                        }

                    } else {

                        instance.getExtraLife().replace(player, instance.getExtraLife().get(player), instance.getExtraLife().get(player) - 1);
                        player.getScoreboard().getObjective("objective").getScore(ChatColor.BLUE + "Extra Life :").setScore(instance.getExtraLife().get(player));
                        event.setCancelled(true);
                        player.setGameMode(GameMode.SPECTATOR);
                        Location spawn = new Location(player.getWorld(), 0.5, 110, 0.5, 0f, 0f);
                        player.teleport(spawn);
                        Bukkit.broadcastMessage("§f" + player.getName() + " a utilisé une vie, il lui reste " + instance.getExtraLife().get(player) + " vie supplémentaire");

                        if (instance.index >= (instance.getPlayers().size() - 1)) {

                            instance.index = 0;

                        } else {

                            instance.index++;

                        }

                        instance.time = 15;

                    }

                } else {

                    event.setCancelled(true);
                    player.setGameMode(GameMode.SPECTATOR);
                    Location spawn = new Location(player.getWorld(), 0.5, 110, 0.5, 0f, 0f);
                    player.teleport(spawn);

                    instance.getPlayers().remove(player);

                    if (instance.getPlayers().size() != 1) {

                        Bukkit.broadcastMessage(player.getName() + " est mort comme une merde plus que " + instance.getPlayers().size() + " joueurs");
                        if (instance.index >= (instance.getPlayers().size() - 1)) {

                            instance.index = 0;

                        } else {

                            instance.index++;

                        }

                        instance.time = 15;

                    } else {

                        instance.time = -1;

                    }

                }

            }

        }

    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {

        if (event.getPlayer().getGameMode() == GameMode.ADVENTURE && event.getPlayer().getLocation().getBlockY() == 76 && instance.isState(Gstate.PLAYING)) {

            if (event.getPlayer().getLocation().getBlock().isLiquid()) {

                Location pos = event.getPlayer().getLocation();

                if (!instance.getServer().getWorld("world").getBlockAt(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ() + 1).isLiquid()) {

                    if (!instance.getServer().getWorld("world").getBlockAt(pos.getBlockX() - 1, pos.getBlockY(), pos.getBlockZ()).isLiquid()) {

                        if (!instance.getServer().getWorld("world").getBlockAt(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ() - 1).isLiquid()) {

                            if (!instance.getServer().getWorld("world").getBlockAt(pos.getBlockX() + 1, pos.getBlockY(), pos.getBlockZ()).isLiquid()) {

                                if (!instance.getExtraLife().containsKey(event.getPlayer())) {

                                    instance.getExtraLife().put(event.getPlayer(), 1);

                                } else {

                                    instance.getExtraLife().replace(event.getPlayer(), instance.getExtraLife().get(event.getPlayer()), instance.getExtraLife().get(event.getPlayer()) + 1);

                                }

                                event.getPlayer().getScoreboard().getObjective("objective").getScore(ChatColor.BLUE + "Extra Life :").setScore(instance.getExtraLife().get(event.getPlayer()));
                                Bukkit.broadcastMessage("§f" + event.getPlayer().getName() + " à gagner une vie supplémentaire, il a " + instance.getExtraLife().get(event.getPlayer()) + " vie supplémentaire");

                            }

                        }

                    }

                }

                event.getPlayer().getLocation().getBlock().setType(instance.getColor().get(event.getPlayer().getName()));

                Location spawn = new Location(event.getPlayer().getWorld(), 0.5, 110, 0.5, 0f, 0f);
                event.getPlayer().setGameMode(GameMode.SPECTATOR);
                event.getPlayer().teleport(spawn);

                if (instance.index == (instance.getPlayers().size() - 1)) {

                    instance.index = 0;

                } else {

                    instance.index++;

                }

                instance.time = 15;

            }

        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        if (instance.isState(Gstate.PLAYING) && instance.getPlayers().contains(event.getPlayer())) {


            if (event.getPlayer().getGameMode() == GameMode.ADVENTURE) {

                instance.getPlayers().remove(event.getPlayer());
                event.setQuitMessage("§f" + event.getPlayer().getName() + " a rage quit");

                if (instance.getPlayers().size() != 1) {

                    if (instance.index >= (instance.getPlayers().size() - 1)) {

                        instance.index = 0;

                    } else {

                        instance.index++;

                    }

                    instance.time = 15;

                } else {

                    instance.time = -1;

                }

            } else {

                instance.getPlayers().remove(event.getPlayer());
                event.setQuitMessage("§f" + event.getPlayer().getName() + " a rage quit");

                if (instance.getPlayers().size() == 1) {

                    Bukkit.broadcastMessage("call");
                    instance.time = -1;

                }

            }

        } else {

            if (instance.isState(Gstate.PLAYING)) {

                event.setQuitMessage("§f" + event.getPlayer().getName() + " ne regarde plus la partie");

            }

        }

    }

}
