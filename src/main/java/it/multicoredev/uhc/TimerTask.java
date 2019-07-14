package it.multicoredev.uhc;

import it.multicoredev.uhc.util.Misc;
import it.multicoredev.uhc.util.Time;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;

import static it.multicoredev.uhc.Main.config;
import static it.multicoredev.uhc.Main.game;

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
public class TimerTask implements Runnable {
    private long time;

    public void run() {
        long worldTime = Bukkit.getServer().getWorld(config.getWorld()).getTime();

        if (!isDeathmatch()) {
            int endVid = Time.endVideo(time, config.getVideoLen());
            if (endVid != -1) {
                Misc.broadcast(config.getMessage("end-video").replace("{n}", String.valueOf(endVid)));
            }

            if(!isDay(worldTime)) {
                game.getBossBar().setColor(BarColor.BLUE);
                game.getBossBar().setProgress((double) (worldTime - 12542) / (double) (23999 - 12542));
            } else {
                game.getBossBar().setColor(BarColor.YELLOW);
                game.getBossBar().setProgress((double) worldTime / (double) 12542);
            }
        } else {
            game.getBossBar().setColor(BarColor.RED);
            game.getBossBar().setProgress(1);
        }

        config.getCache().set("timer", time);
        if(time % 10 == 0) {
            new Thread(() -> config.saveCache()).start();
        }

        game.updateBossbar();
        time++;
    }

    private boolean isDay(long worldTime) {
        return worldTime < 12542;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isDeathmatch() {
        return time >= config.getUHCLen() * 60;
    }
}
