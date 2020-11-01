package org.ships.implementation.sponge.text.colour;

import net.kyori.adventure.text.format.TextColor;
import org.core.text.colour.TextColour;

public class SpongeGenericColour implements TextColour {

    private final TextColor colour;

    public SpongeGenericColour(TextColor colour){
        this.colour = colour;
    }

    @Override
    public int getRed() {
        return this.colour.red();
    }

    @Override
    public int getGreen() {
        return this.colour.green();
    }

    @Override
    public int getBlue() {
        return this.colour.blue();
    }
}
