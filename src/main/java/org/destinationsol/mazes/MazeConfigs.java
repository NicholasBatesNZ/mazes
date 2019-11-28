/*
 * Copyright 2019 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.destinationsol.mazes;

import org.destinationsol.game.ConfigurationSystem;
import org.json.JSONObject;
import org.destinationsol.files.HullConfigManager;
import org.destinationsol.game.item.ItemManager;

import java.util.ArrayList;
import java.util.List;

public class MazeConfigs implements ConfigurationSystem {
    private List<MazeConfig> configurations;

    public MazeConfigs() {
        configurations = new ArrayList<>();
    }

    @Override
    public void loadConfiguration(String moduleName, String name, JSONObject mazeNode, HullConfigManager hullConfigManager, ItemManager itemManager) {
        MazeConfig c = MazeConfig.load(moduleName, name, mazeNode, hullConfigManager, itemManager);
        configurations.add(c);
    }

    @Override
    public String getJSONValidatorLocation() {
        return "mazes:schemaMazesConfig";
    }

    @Override
    public String getConfigurationLocations() {
        return "[a-zA-Z0-9]*:mazesConfig";
    }

    List<MazeConfig> getConfigurations() {
        return configurations;
    }
}
