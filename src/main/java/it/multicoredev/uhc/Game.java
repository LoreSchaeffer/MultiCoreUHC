package it.multicoredev.uhc;

import it.multicoredev.uhc.util.Misc;
import it.multicoredev.uhc.util.Time;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static it.multicoredev.uhc.Main.config;

/**
 * Copyright © 2019 by Lorenzo Magni
 * This file is part of MultiCoreUHC.
 * MultiCoreUHC is under "The 3-Clause BSD License", you can find a copy <a href="https://opensource.org/licenses/BSD-3-Clause">here</a>.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
public class Game {
    private Plugin plugin;
    private BukkitTask task;
    private boolean running = false;
    private TimerTask timer;
    private BossBar bossBar;
    private ArrayList<Player> alivePlayers = new ArrayList<>();
    private ArrayList<Player> deadPlayers = new ArrayList<>();
    private ArrayList<Player> respawnedPlayers = new ArrayList<>();
    private HashMap<UUID, String> missingPlayers = new HashMap<>();

    public Game(Plugin plugin) {
        this.plugin = plugin;
        timer = new TimerTask();
    }

    public void startGame() {
        Misc.broadcast(config.getMessage("uhc-start"));
        config.createCache();

        List<String> players = new ArrayList<>();
        for (Player p : alivePlayers) {
            players.add(p.getUniqueId().toString());
        }
        config.getCache().set("alive-players", players);
        new Thread(() -> config.saveCache()).start();

        //TODO Reset period to 20 ticks
        task = plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, timer, 0, 1);
        createBossbar();
        bossBar.setProgress(1);

        running = true;
    }

    public void endGame() {
        task.cancel();

        File cache = new File(plugin.getDataFolder(), "cache.yml");
        if (cache.exists()) cache.delete();

        bossBar.setTitle(config.getTitle() + " - " + Time.getReadableTimer(getTime()) + " - " + config.getMessage("ended"));
    }

    private void createBossbar() {
        bossBar = Bukkit.getServer().createBossBar(config.getTitle() + " - " + Time.getReadableTimer(getTime()), BarColor.YELLOW, BarStyle.SOLID);
        bossBar.setVisible(true);

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            bossBar.addPlayer(player);
        }
    }

    public void updateBossbar() {
        bossBar.setTitle(config.getTitle() + " - " + Time.getReadableTimer(getTime()));
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public TimerTask getTimer() {
        return timer;
    }

    public boolean isDeathmatch() {
        return timer.isDeathmatch();
    }

    public long getTime() {
        return timer.getTime();
    }

    public void setTime(long time) {
        timer.setTime(time);
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public HashMap<UUID, String> getMissingPlayers() {
        return missingPlayers;
    }

    public void addMissingPlayer(UUID uuid, String list) {
        missingPlayers.put(uuid, list);
    }

    public void removeMissingPlayer(UUID uuid) {
        missingPlayers.remove(uuid);
    }

    public ArrayList<Player> getAlivePlayers() {
        return alivePlayers;
    }

    public ArrayList<Player> getDeadPlayers() {
        return deadPlayers;
    }

    public ArrayList<Player> getRespawnedPlayers() {
        return respawnedPlayers;
    }

    public void addAlivePlayer(Player player) {
        alivePlayers.add(player);

        if (config.getCache() == null) return;

        List<String> players = new ArrayList<>();
        for (Player p : alivePlayers) {
            players.add(p.getUniqueId().toString());
        }
        config.getCache().set("alive-players", players);
        new Thread(() -> config.saveCache()).start();
    }

    public void removeAlivePlayer(Player player) {
        alivePlayers.remove(player);

        if (config.getCache() == null) return;

        List<String> players = new ArrayList<>();
        for (Player p : alivePlayers) {
            players.add(p.getUniqueId().toString());
        }
        config.getCache().set("alive-players", players);
        new Thread(() -> config.saveCache()).start();
    }

    public void addDeadPlayer(Player player) {
        deadPlayers.add(player);

        if (config.getCache() == null) return;

        List<String> players = new ArrayList<>();
        for (Player p : deadPlayers) {
            players.add(p.getUniqueId().toString());
        }
        config.getCache().set("dead-players", players);
        new Thread(() -> config.saveCache()).start();
    }

    public void removeDeadPlayer(Player player) {
        deadPlayers.remove(player);

        if (config.getCache() == null) return;

        List<String> players = new ArrayList<>();
        for (Player p : deadPlayers) {
            players.add(p.getUniqueId().toString());
        }
        config.getCache().set("dead-players", players);
        new Thread(() -> config.saveCache()).start();
    }

    public void addRespawnedPlayer(Player player) {
        respawnedPlayers.add(player);

        if (config.getCache() == null) return;

        List<String> players = new ArrayList<>();
        for (Player p : respawnedPlayers) {
            players.add(p.getUniqueId().toString());
        }
        config.getCache().set("respawned-players", players);
        new Thread(() -> config.saveCache()).start();
    }

    public void removeRespawnedPlayer(Player player) {
        deadPlayers.remove(player);

        if (config.getCache() == null) return;

        List<String> players = new ArrayList<>();
        for (Player p : respawnedPlayers) {
            players.add(p.getUniqueId().toString());
        }
        config.getCache().set("respawned-players", players);
        new Thread(() -> config.saveCache()).start();
    }

    public void launchFireworks(Player player) {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            List<String> cmds = config.getFireworksCommands();
            int index = 0;
            int max = cmds.size() - 1;
            int cycles = 100;

            while (cycles > 0) {
                Location location = Bukkit.getPlayer(player.getUniqueId()).getLocation();
                if (index > max) {
                    index = 0;
                }

                String cmd = cmds.get(index).replace("{x}", String.valueOf(location.getX())).replace("[y}", String.valueOf(location.getY())).replace("{z}", String.valueOf(location.getZ()));
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);

                index++;
                cycles--;
            }

        }, 0, 20);
    }
}
