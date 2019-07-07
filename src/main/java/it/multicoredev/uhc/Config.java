package it.multicoredev.uhc;

import it.multicoredev.mbcore.spigot.ConfigManager;
import it.multicoredev.mbcore.spigot.config.Configuration;
import org.bukkit.plugin.Plugin;

import java.io.File;

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
public class Config {
    private Configuration config;
    private Plugin plugin;
    private ConfigManager cm;

    Config(Plugin plugin) {
        this.plugin = plugin;
        cm = new ConfigManager(plugin);
    }

    boolean loadConfig() {
        config = cm.autoloadConfig(plugin.getResource("config.yml"), new File(plugin.getDataFolder(), "config.yml"));
        return config != null;
    }

    boolean saveConfig() {
        return cm.saveConfig(config, new File(plugin.getDataFolder(), "config.yml"));
    }

    public int getVideoLen() {
        return config.getInt("timers.video-len");
    }

    public int getUHCLen() {
        return config.getInt("timers.uhc-len");
    }

    public String getMessage(String path) {
        return config.getString("messages." + path);
    }
}
