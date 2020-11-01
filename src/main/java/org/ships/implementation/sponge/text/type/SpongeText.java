package org.ships.implementation.sponge.text.type;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.core.text.type.Text;

public class SpongeText <C extends Component> implements Text {

    private final C component;

    public SpongeText(C component){
        this.component = component;
    }

    public C getComponent(){
        return this.component;
    }

    @Override
    public String toLegacyString() {
        return LegacyComponentSerializer.legacy(Text.LEGACY_CHARACTER_CODE).serialize(this.component);
    }
}
