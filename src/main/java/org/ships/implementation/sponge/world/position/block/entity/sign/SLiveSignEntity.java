package org.ships.implementation.sponge.world.position.block.entity.sign;

import net.kyori.adventure.text.Component;
import org.core.world.position.block.entity.sign.LiveSignTileEntity;
import org.core.world.position.block.entity.sign.SignTileEntity;
import org.core.world.position.block.entity.sign.SignTileEntitySnapshot;
import org.ships.implementation.sponge.text.SText;
import org.ships.implementation.sponge.world.position.block.entity.AbstractLiveTileEntity;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.value.ListValue;

import java.util.ArrayList;
import java.util.List;

public class SLiveSignEntity extends AbstractLiveTileEntity<org.spongepowered.api.block.entity.Sign> implements LiveSignTileEntity {

    @Deprecated
    public SLiveSignEntity(org.spongepowered.api.block.entity.BlockEntity tileEntity){
        super((org.spongepowered.api.block.entity.Sign)tileEntity);
    }

    public SLiveSignEntity(org.spongepowered.api.block.entity.Sign tileEntity) {
        super(tileEntity);
    }

    @Override
    public SignTileEntitySnapshot getSnapshot() {
        return new SSignTileEntitySnapshot(this);
    }

    @Override
    public org.core.text.Text[] getLines() {
        org.core.text.Text[] lines = new org.core.text.Text[4];
        ListValue.Mutable<Component> text = getSpongeTileEntity().lines();
        for(int A = 0; A < text.size(); A++){
            lines[A] = SText.of(text.get(A));
        }
        return lines;
    }

    @Override
    public SignTileEntity setLines(org.core.text.Text... lines) throws IndexOutOfBoundsException {
        List<Component> toLines = new ArrayList<>();
        for(org.core.text.Text line : lines) {
            toLines.add(((SText<?>)line).toSponge());
        }
        getSpongeTileEntity().offer(Keys.SIGN_LINES, toLines);
        return this;
    }
}
