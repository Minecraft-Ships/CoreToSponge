package org.core.implementation.sponge.world.position.block.entity.sign;

import net.kyori.adventure.text.Component;
import org.core.world.position.block.entity.sign.SignSide;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.block.entity.Sign;
import org.spongepowered.api.data.Keys;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

//TOD -> sign sides when API 11 releases
public class SLiveSignSide implements SignSide {

    private final @NotNull Sign sign;

    public SLiveSignSide(@NotNull Sign sign, boolean front) {
        this.sign = sign;
    }

    @Override
    public SLiveSignEntity getSign() {
        return new SLiveSignEntity(this.sign);
    }

    @Override
    public List<Component> getLines() {
        return this.sign.get(Keys.SIGN_LINES).orElse(Collections.emptyList());
    }

    @Override
    public SignSide setLines(Collection<Component> componentCollection) {
        this.sign.offer(Keys.SIGN_LINES, new ArrayList<>(componentCollection));
        return this;
    }

    @Override
    public boolean isFront() {
        return true;
    }

    @Override
    public boolean isGlowing() {
        return this.sign.get(Keys.IS_GLOWING).orElse(false);
    }

    @Override
    public void setGlowing(boolean glowing) {
        this.sign.offer(Keys.IS_GLOWING, glowing);
    }
}
