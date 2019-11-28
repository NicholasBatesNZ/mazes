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

import com.badlogic.gdx.math.Vector2;
import org.destinationsol.Const;
import org.destinationsol.common.SolRandom;
import org.destinationsol.game.BuildableSystem;
import org.destinationsol.game.ConfigurationSystem;
import org.destinationsol.game.ShipConfig;
import org.destinationsol.game.SolCam;
import org.destinationsol.game.SolGame;
import org.destinationsol.game.chunk.SpaceEnvConfig;

import java.util.List;

public class Maze implements BuildableSystem {
    private MazeConfigs configs;
    private MazeConfig config;
    private Vector2 position;
    private float radius;
    private float damagePerSecond;
    private boolean areObjectsCreated;

    @Override
    public void build(List<ConfigurationSystem> configurationSystems, Vector2 position, float radius) {
        for (ConfigurationSystem system : configurationSystems) {
            //try {
                configs = (MazeConfigs) system;
            //} catch (Exception ignore) { }
        }
        this.config = SolRandom.seededRandomElement(configs.getConfigurations());
        this.position = position;
        this.radius = radius;
        damagePerSecond = getMazeDps(this.config);
    }

    @Override
    public void update(SolGame game, float timeStep) {
        SolCam cam = game.getCam();
        Vector2 camPos = cam.getPosition();
        if (!areObjectsCreated && camPos.dst(position) < radius + Const.CAM_VIEW_DIST_JOURNEY * 2) {
            new MazeBuilder().build(game, this);
            areObjectsCreated = true;
        }
    }

    private static float getMazeDps(MazeConfig c) {
        float outer = getShipConfListDps(c.outerEnemies);
        float inner = getShipConfListDps(c.innerEnemies);
        float res = inner < outer ? outer : inner;
        return res * 1.25f;
    }

    private static float getShipConfListDps(List<ShipConfig> ships) {
        float maxDps = 0;
        for (ShipConfig e : ships) {
            if (maxDps < e.dps) {
                maxDps = e.dps;
            }
        }
        return maxDps;
    }

    MazeConfig getConfig() {
        return config;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    /**
     * @return the full radius including the exterior border.
     */
    @Override
    public float getRadius() {
        return radius;
    }

    @Override
    public float getMaximumRadius() {
        return 40f;
    }

    @Override
    public float getDamagePerSecond() {
        return damagePerSecond;
    }

    @Override
    public float getMapBorder() {
        return MazeBuilder.BORDER;
    }

    @Override
    public String getMapTextureLocation() {
        return "mazes:mapObjects/maze";
    }

    @Override
    public SpaceEnvConfig getSpaceEnvironmentConfiguration() {
        return config.envConfig;
    }
}
