package org.core.implementation.sponge.world.position.block.entity.sign;

import org.core.adventureText.AText;
import org.core.adventureText.adventure.AdventureText;
import org.core.implementation.sponge.world.position.block.entity.AbstractLiveTileEntity;
import org.core.utils.ComponentUtils;
import org.core.world.position.block.entity.sign.LiveSignTileEntity;
import org.core.world.position.block.entity.sign.SignSide;
import org.core.world.position.block.entity.sign.SignTileEntity;
import org.core.world.position.block.entity.sign.SignTileEntitySnapshot;
import org.spongepowered.api.block.entity.Sign;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SLiveSignEntity extends AbstractLiveTileEntity<Sign> implements LiveSignTileEntity {

    @Deprecated
    public SLiveSignEntity(org.spongepowered.api.block.entity.BlockEntity tileEntity) {
        super((org.spongepowered.api.block.entity.Sign) tileEntity);
    }

    public SLiveSignEntity(org.spongepowered.api.block.entity.Sign tileEntity) {
        super(tileEntity);
    }

    @Override
    public SignTileEntitySnapshot getSnapshot() {
        return new SSignTileEntitySnapshot(this);
    }

    @Override
    public SignSide getSide(boolean frontSide) {
        if (frontSide) {
            return new SLiveSignSide(this.tileEntity, true);
        }
        throw new RuntimeException("Multi sided signs are not supported");
    }

    @Override
    public boolean isMultiSideSupported() {
        return false;
    }

    @Override
    public List<AText> getText() {
        return getFront().getLines().stream().map(AdventureText::new).collect(Collectors.toList());
    }

    @Override
    public SignTileEntity setText(Collection<? extends AText> text) {
        this.getFront().setLines(text.stream().map(text2 -> {
            if (text2 instanceof AdventureText) {
                return ((AdventureText) text2).getComponent();
            }
            return ComponentUtils.fromLegacy(text2.toLegacy());
        }).collect(Collectors.toList()));
        return this;
    }
}
