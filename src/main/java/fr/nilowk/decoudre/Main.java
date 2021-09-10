package fr.nilowk.decoudre;

import fr.nilowk.decoudre.listeners.GameManager;
import fr.nilowk.decoudre.listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import javax.xml.transform.Source;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends JavaPlugin {

    private List<Player> players = new ArrayList<>();
    private List<Material> blocks = new ArrayList<>();
    private HashMap<Player, Integer> extraLife = new HashMap<>();
    private HashMap<String, Material> color = new HashMap<>();
    private Gstate state;
    public String winner = "";
    public int index = 0;
    public int time = 15;
    public int finishTime = 10;

    private void initializeBlocks() {

        blocks.add(Material.PURPLE_CONCRETE);
        blocks.add(Material.BLUE_CONCRETE);
        blocks.add(Material.LIGHT_BLUE_CONCRETE);
        blocks.add(Material.LIME_CONCRETE);
        blocks.add(Material.YELLOW_CONCRETE);
        blocks.add(Material.ORANGE_CONCRETE);
        blocks.add(Material.RED_CONCRETE);

    }

    @Override
    public void onEnable() {

        setState(Gstate.WAITING);

        initializeBlocks();

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(this), this);
        pm.registerEvents(new GameManager(this), this);

    }

    public void setState(Gstate state) {

        this.state = state;

    }

    public boolean isState(Gstate state) {

        return this.state == state;

    }

    public List<Player> getPlayers() {

        return players;

    }

    public List<Material> getBlocks() {

        return blocks;

    }

    public HashMap<Player, Integer> getExtraLife() {

        return this.extraLife;

    }

    public HashMap<String, Material> getColor() {

        return color;

    }

}
