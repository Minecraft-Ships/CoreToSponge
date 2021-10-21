package org.core.implementation.sponge.boss;

import org.core.adventureText.format.TextColour;
import org.core.world.boss.colour.BossColour;

public class SBossBarColour implements BossColour {

    private final TextColour colour;
    private final String name;
    private final String id;

    public SBossBarColour(String id, String name, TextColour colour) {
        this.colour = colour;
        this.name = name;
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
