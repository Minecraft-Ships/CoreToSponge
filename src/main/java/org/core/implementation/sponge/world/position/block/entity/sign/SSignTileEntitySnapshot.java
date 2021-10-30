package org.core.implementation.sponge.world.position.block.entity.sign;

import net.kyori.adventure.text.Component;
import org.core.adventureText.AText;
import org.core.adventureText.adventure.AdventureText;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.BlockTypes;
import org.core.world.position.block.entity.sign.LiveSignTileEntity;
import org.core.world.position.block.entity.sign.SignTileEntity;
import org.core.world.position.block.entity.sign.SignTileEntitySnapshot;
import org.spongepowered.api.data.value.ListValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SSignTileEntitySnapshot implements SignTileEntitySnapshot {

    protected List<AText> lines;

    public SSignTileEntitySnapshot(ListValue<? extends Component> sign) {
        this(sign.get().stream().map(AdventureText::new).collect(Collectors.toList()));
    }

    public SSignTileEntitySnapshot(SignTileEntity ste) {
        this(ste.getText());
    }

    public SSignTileEntitySnapshot(Collection<AText> lines) {
        this.lines = lines.stream().map(l -> (AdventureText) l).collect(Collectors.toList());
    }

    public SSignTileEntitySnapshot() {
        this.lines = new ArrayList<>();
    }

    @Override
    public LiveSignTileEntity apply(LiveSignTileEntity lste) {
        lste.setText(this.lines);
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
    public List<AText> getText() {
        return this.lines;
    }

    @Override
    public SignTileEntity setText(Collection<? extends AText> text) {
        this.lines.clear();
        this.lines.addAll(text);
        return this;
    }
}
