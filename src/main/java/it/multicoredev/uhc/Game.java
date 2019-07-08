package it.multicoredev.uhc;

import it.multicoredev.mbcore.spigot.Chat;
import it.multicoredev.uhc.util.Misc;
import it.multicoredev.uhc.util.Time;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

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
    private boolean running = false;
    private TimerTask timer;
    private BossBar bossBar;

    public Game(Plugin plugin) {
        this.plugin = plugin;
        timer = new TimerTask();
    }

    public void startGame() {
        Misc.broadcast(config.getMessage("uhc-start"));

        //TODO Reset period to 20 ticks
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, timer, 0, 1);
        createBossbar();
        bossBar.setProgress(1);
    }

    private void createBossbar() {
        bossBar = Bukkit.getServer().createBossBar(config.getTitle() + " - " + Time.getReadableTimer(getTime()), BarColor.YELLOW, BarStyle.SOLID);
        bossBar.setVisible(true);

        for(Player player : plugin.getServer().getOnlinePlayers()) {
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

    public BossBar getBossBar() {
        return bossBar;
    }
}
