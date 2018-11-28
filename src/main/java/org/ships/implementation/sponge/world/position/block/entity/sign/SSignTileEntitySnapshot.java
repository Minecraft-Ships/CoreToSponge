package org.ships.implementation.sponge.world.position.block.entity.sign;

import org.core.exceptions.BlockNotSupported;
import org.core.world.position.BlockPosition;
import org.core.world.position.block.entity.LiveTileEntity;
import org.core.world.position.block.entity.sign.LiveSignTileEntity;
import org.core.world.position.block.entity.sign.SignTileEntity;
import org.core.world.position.block.entity.sign.SignTileEntitySnapshot;
import org.spongepowered.api.data.value.immutable.ImmutableListValue;
import org.spongepowered.api.data.value.mutable.ListValue;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class SSignTileEntitySnapshot implements SignTileEntitySnapshot {

    protected String[] lines;

    public SSignTileEntitySnapshot(org.spongepowered.api.data.manipulator.immutable.tileentity.ImmutableSignData sign) {
        this.lines = new String[4];
        ImmutableListValue<Text> lines = sign.lines();
        for(int A = 0; A < lines.size(); A++){
            this.lines[A] = lines.get(A).toString();
        }
    }

    public SSignTileEntitySnapshot(org.spongepowered.api.data.manipulator.mutable.tileentity.SignData sign) {
        this.lines = new String[4];
        ListValue<Text> lines = sign.lines();
        for(int A = 0; A < lines.size(); A++){
            this.lines[A] = lines.get(A).toString();
        }
    }

    public SSignTileEntitySnapshot(SignTileEntity ste){
        this(ste.getLines());
    }

    public SSignTileEntitySnapshot(String... lines){
        this.lines = lines;
    }

    @Override
    public SignTileEntity apply(BlockPosition position) throws BlockNotSupported {
        Optional<LiveTileEntity> opTE =position.getTileEntity();
        if(!opTE.isPresent()){
            throw new BlockNotSupported(position.getBlockType(), "SignEntitySnapshot");
        }
        LiveTileEntity lte = opTE.get();
        if(!(lte instanceof LiveSignTileEntity)){
            throw new BlockNotSupported(position.getBlockType(), "SignEntitySnapshot");
        }
        LiveSignTileEntity lste = (LiveSignTileEntity)lte;
        lste.setLines(lines);
        return this;
    }

    @Override
    public SignTileEntitySnapshot getSnapshot() {
        return new SSignTileEntitySnapshot(this);
    }

    @Override
    public String[] getLines() {
        return lines;
    }

    @Override
    public SignTileEntity setLines(String... lines) throws IndexOutOfBoundsException {
        this.lines = lines;
        return this;
    }
}
