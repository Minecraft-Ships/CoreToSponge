package org.core.implementation.sponge.world.position.block.entity.sign;

import net.kyori.adventure.text.Component;
import org.core.adventureText.AText;
import org.core.adventureText.adventure.AdventureText;
import org.core.utils.ComponentUtils;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.BlockTypes;
import org.core.world.position.block.entity.sign.LiveSignTileEntity;
import org.core.world.position.block.entity.sign.SignTileEntity;
import org.core.world.position.block.entity.sign.SignTileEntitySnapshot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class SSignTileEntitySnapshot implements SignTileEntitySnapshot {

    private final SSignSideSnapshot front;
    //private SSignSideSnapshot back;

    public SSignTileEntitySnapshot(Collection<? extends Component> frontLines) {
        this(frontLines, Collections.emptyList());
    }

    public SSignTileEntitySnapshot(Collection<? extends Component> frontLines,
                                   Collection<? extends Component> backLines) {
        this.front = new SSignSideSnapshot(this, true, new ArrayList<>(frontLines));
    }

    public SSignTileEntitySnapshot(SignTileEntity ste) {
        this.front = new SSignSideSnapshot(this, true, ste.getFront().getLines());
        this.front.setGlowing(ste.isMultiSideSupported());
    }

    public SSignTileEntitySnapshot(@NotNull SSignSideSnapshot front, @Nullable SSignSideSnapshot back) {
        if (back != null && front.isFront() == back.isFront()) {
            throw new IllegalArgumentException("Both sides are marked as isFront:" + front.isFront());
        }
        SSignSideSnapshot frontSide;
        if (front.isFront()) {
            frontSide = front;
        } else {
            throw new IllegalArgumentException("Multi sign is not supported");
            //this.back = front;
        }
        if (back != null) {
            if (back.isFront()) {
                frontSide = back;
            } else {
                throw new IllegalArgumentException("Multi sign is not supported");
                //this.back = back;
            }
        }
        this.front = frontSide;
    }

    public SSignTileEntitySnapshot(Component... frontLines) {
        this(Arrays.asList(frontLines));
    }

    @Override
    public LiveSignTileEntity apply(LiveSignTileEntity lste) {
        this.front.setLines(lste.getFront().getLines());
        return lste;
    }

    @Override
    public Collection<BlockType> getSupportedBlocks() {
        return BlockTypes.OAK_SIGN.getLike();
    }

    @Override
    public SignTileEntitySnapshot getSnapshot() {
        return new SSignTileEntitySnapshot(this);
    }

    @Override
    public SSignSideSnapshot getSide(boolean frontSide) {
        if (frontSide) {
            return front;
        }
        throw new IllegalStateException("Multi sided signs are not supported ");
    }

    @Override
    public boolean isMultiSideSupported() {
        return false;
    }

    @Override
    public List<AText> getText() {
        return this.getFront().getLines().stream().map(AdventureText::new).collect(Collectors.toList());
    }

    @Override
    public SignTileEntity setText(Collection<? extends AText> text) {
        this.getFront().setLines(text.stream().map(t -> {
            if (t instanceof AdventureText) {
                return ((AdventureText)t).getComponent();
            }
            return ComponentUtils.fromLegacy(t.toLegacy());
        }).collect(Collectors.toList()));
        return this;
    }
}
