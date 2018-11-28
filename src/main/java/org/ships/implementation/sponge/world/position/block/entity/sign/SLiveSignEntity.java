package org.ships.implementation.sponge.world.position.block.entity.sign;

import org.core.world.position.block.entity.sign.LiveSignTileEntity;
import org.core.world.position.block.entity.sign.SignTileEntity;
import org.core.world.position.block.entity.sign.SignTileEntitySnapshot;
import org.ships.implementation.sponge.world.position.block.entity.AbstractLiveTileEntity;
import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.value.mutable.ListValue;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.ArrayList;
import java.util.List;

public class SLiveSignEntity extends AbstractLiveTileEntity<org.spongepowered.api.block.tileentity.Sign> implements LiveSignTileEntity {

    public SLiveSignEntity(Sign tileEntity) {
        super(tileEntity);
    }

    @Override
    public SignTileEntitySnapshot getSnapshot() {
        return new SSignTileEntitySnapshot(this);
    }

    @Override
    public String[] getLines() {
        String[] lines = new String[4];
        ListValue<Text> text = getSpongeTileEntity().lines();
        for(int A = 0; A < text.size(); A++){
            lines[A] = text.get(A).toString();
        }
        return lines;
    }

    @Override
    public SignTileEntity setLines(String... lines) throws IndexOutOfBoundsException {
        List<Text> toLines = new ArrayList<>();
        for(String line : lines) {
            toLines.add(TextSerializers.FORMATTING_CODE.deserialize(line));
        }
        getSpongeTileEntity().offer(Keys.SIGN_LINES, toLines);
        return this;
    }
}
