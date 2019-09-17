package org.ships.implementation.sponge.world.position.block.details.blocks;

import org.core.CorePlugin;
import org.core.text.Text;
import org.core.world.position.BlockPosition;
import org.core.world.position.block.details.BlockSnapshot;
import org.core.world.position.block.entity.LiveTileEntity;
import org.core.world.position.block.entity.TileEntitySnapshot;
import org.core.world.position.block.entity.sign.SignTileEntitySnapshot;
import org.ships.implementation.sponge.platform.SpongePlatform;
import org.ships.implementation.sponge.text.SText;
import org.ships.implementation.sponge.world.position.SBlockPosition;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SBlockSnapshot extends SBlockDetails implements BlockSnapshot {

    protected org.spongepowered.api.block.BlockSnapshot snapshot;

    public SBlockSnapshot(org.spongepowered.api.block.BlockSnapshot snapshot){
        super(snapshot.getExtendedState());
        this.snapshot = snapshot;
        if(this.tileEntitySnapshot != null && this.tileEntitySnapshot instanceof SignTileEntitySnapshot){
            Optional<List<org.spongepowered.api.text.Text>> opLines = this.snapshot.get(Keys.SIGN_LINES);
            if(opLines.isPresent()) {
                List<org.spongepowered.api.text.Text> lines = opLines.get();
                for (int A = 0; A < lines.size(); A++) {
                    ((SignTileEntitySnapshot) this.tileEntitySnapshot).setLine(A, new SText(lines.get(A)));
                }
            }
        }
    }

    public SBlockSnapshot(org.spongepowered.api.world.Location<World> location){
        super(location.getBlock());
        this.snapshot = location.createSnapshot();
        Optional<org.spongepowered.api.block.tileentity.TileEntity> opTileEntity = location.getTileEntity();
        if(opTileEntity.isPresent()){
            ((SpongePlatform) CorePlugin.getPlatform()).createTileEntityInstance(opTileEntity.get()).ifPresent(te -> this.tileEntitySnapshot = te.getSnapshot());
        }
    }

    public SBlockSnapshot(org.spongepowered.api.block.BlockSnapshot snapshot, TileEntitySnapshot<? extends LiveTileEntity> tileEntity){
        super(snapshot.getExtendedState());
        this.snapshot = snapshot;
        this.tileEntitySnapshot = tileEntity;
    }

    @Override
    public BlockSnapshot createSnapshot(BlockPosition position) {
        SBlockPosition position1 = (SBlockPosition) position;
        org.spongepowered.api.block.BlockSnapshot snapshot = org.spongepowered.api.block.BlockSnapshot.builder().from(this.snapshot).world(position1.getSpongeLocation().getExtent().getProperties()).position(position1.getSpongeLocation().getBlockPosition()).build();
        if(this.tileEntitySnapshot != null && this.tileEntitySnapshot instanceof SignTileEntitySnapshot){
            List<org.spongepowered.api.text.Text> lines = new ArrayList<>();
            for (Text text : ((SignTileEntitySnapshot) this.tileEntitySnapshot).getLines()){
                lines.add(((SText)text).toSponge());
            }
            snapshot = snapshot.with(Keys.SIGN_LINES, lines).get();
        }
        return new SBlockSnapshot(snapshot, this.tileEntitySnapshot);
    }

    @Override
    public BlockPosition getPosition() {
        return new SBlockPosition(this.snapshot.getLocation().get());
    }

    @Override
    public BlockSnapshot createCopyOf() {
        return new SBlockSnapshot(this.snapshot.copy(), this.tileEntitySnapshot);
    }

}
