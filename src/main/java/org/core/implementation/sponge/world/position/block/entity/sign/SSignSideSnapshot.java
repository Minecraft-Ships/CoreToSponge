package org.core.implementation.sponge.world.position.block.entity.sign;

import net.kyori.adventure.text.Component;
import org.core.world.position.block.entity.sign.SignSide;
import org.core.world.position.block.entity.sign.SignTileEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SSignSideSnapshot implements SignSide {

    private final boolean isFront;
    private final SSignTileEntitySnapshot from;
    private List<Component> lines;
    private boolean isGlowing;

    public SSignSideSnapshot(SSignTileEntitySnapshot from, boolean isFront, Component... lines) {
        this(from, isFront, Arrays.asList(lines));
    }

    public SSignSideSnapshot(SSignTileEntitySnapshot from, boolean isFront, Collection<Component> lines) {
        this.lines = new ArrayList<>(lines);
        this.isFront = isFront;
        this.from = from;
    }

    @Override
    public SignTileEntity getSign() {
        return this.from;
    }

    @Override
    public List<Component> getLines() {
        return this.lines;
    }

    @Override
    public SignSide setLines(Collection<Component> componentCollection) {
        this.lines = new ArrayList<>(componentCollection);
        return this;
    }

    @Override
    public boolean isFront() {
        return this.isFront;
    }

    @Override
    public boolean isGlowing() {
        return this.isGlowing;
    }

    @Override
    public void setGlowing(boolean glowing) {
        this.isGlowing = glowing;
    }
}
