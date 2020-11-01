package org.ships.implementation.sponge.world.position.block.entity.sign;

import net.kyori.adventure.text.Component;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.BlockTypes;
import org.core.world.position.block.entity.sign.LiveSignTileEntity;
import org.core.world.position.block.entity.sign.SignTileEntity;
import org.core.world.position.block.entity.sign.SignTileEntitySnapshot;
import org.ships.implementation.sponge.text.SText;
import org.spongepowered.api.data.value.ListValue;

import java.util.Collection;

public class SSignTileEntitySnapshot implements SignTileEntitySnapshot {

    protected org.core.text.Text[] lines;

    public SSignTileEntitySnapshot(ListValue<Component> sign) {
        this.lines = new org.core.text.Text[4];
        for(int A = 0; A < sign.size(); A++){
            this.lines[A] = SText.of(sign.get(A));
        }
    }

    public SSignTileEntitySnapshot(SignTileEntity ste){
        this(ste.getLines());
    }

    public SSignTileEntitySnapshot(org.core.text.Text... lines){
        this.lines = lines;
    }

    @Override
    public LiveSignTileEntity apply(LiveSignTileEntity lste) {
        lste.setLines(lines);
        return lste;
    }

    @Override
    public Collection<BlockType> getSupportedBlocks() {
        return BlockTypes.OAK_SIGN.get().getLike();
    }

    @Override
    public SignTileEntitySnapshot getSnapshot() {
        return new SSignTileEntitySnapshot(this);
    }

    @Override
    public org.core.text.Text[] getLines() {
        return lines;
    }

    @Override
    public SignTileEntity setLines(org.core.text.Text... lines) throws IndexOutOfBoundsException {
        this.lines = lines;
        return this;
    }
}
