package org.ships.implementation.sponge.world.position.block.details.blocks;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import org.core.exceptions.DirectionNotSupported;
import org.core.vector.types.Vector3Int;
import org.core.world.direction.Direction;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.details.blocks.sign.WallSign;
import org.core.world.position.block.entity.sign.SignTileEntitySnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.key.Keys;

public class SWallSignDetails extends AbstractBlockDetails implements WallSign {

    SignTileEntitySnapshot stes;

    public SWallSignDetails(BlockState state) {
        super(state);
    }

    @Override
    public Direction getAttachedDirection() {
        Vector3i vector = this.blockstate.get(Keys.DIRECTION).get().getOpposite().asBlockOffset();
        return Direction.getDirection(vector.getX(), vector.getY(), vector.getZ()).get();
    }

    @Override
    public WallSign setAttachedDirection(Direction direction) throws DirectionNotSupported {
        Vector3Int vector = direction.getAsVector();
        Vector3d vectorD = new Vector3d(vector.getX(), vector.getY(), vector.getZ());
        org.spongepowered.api.util.Direction sDir = org.spongepowered.api.util.Direction.getClosest(vectorD);
        this.blockstate = this.blockstate.with(Keys.DIRECTION, sDir).get();
        return this;
    }

    @Override
    public SignTileEntitySnapshot getTileEntity() {
        return this.stes;
    }

    @Override
    public void setTileEntity(SignTileEntitySnapshot tile) {
        this.stes = tile;
    }

    @Override
    public BlockDetails createCopyOf() {
        return new SWallSignDetails(this.blockstate.copy());
    }
}
