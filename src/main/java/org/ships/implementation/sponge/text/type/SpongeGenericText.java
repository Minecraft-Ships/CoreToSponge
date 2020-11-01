package org.ships.implementation.sponge.text.type;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.array.utils.ArrayUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.core.text.colour.NamedTextColours;
import org.core.text.colour.TextColour;
import org.core.text.style.NamedStyles;
import org.core.text.style.TextStyle;
import org.core.text.type.ColouredText;
import org.core.text.type.JoinedText;
import org.core.text.type.StyledText;
import org.core.text.type.Text;
import org.ships.implementation.sponge.text.colour.SpongeGenericColour;

import java.util.*;

public class SpongeGenericText<C extends Component> extends SpongeText<C> implements ColouredText, StyledText, JoinedText {

    public SpongeGenericText(C component) {
        super(component);
    }

    @Override
    public Optional<TextColour> getColour() {
        @Nullable TextColor textColour = this.getComponent().color();
        if(textColour == null){
            return Optional.empty();
        }
        TextColour colour = NamedTextColours.valueOf(textColour.red(), textColour.green(), textColour.blue());
        if(colour == null){
            colour = new SpongeGenericColour(textColour);
        }
        return Optional.of(colour);
    }

    @Override
    public String getText() {
        return PlainComponentSerializer.plain().serialize(this.getComponent());
    }

    @Override
    public Set<TextStyle> getTextStyle() {
        Set<TextStyle> set = new HashSet<>();
        this.getComponent().decorations().entrySet().stream().filter(e -> e.getValue().equals(TextDecoration.State.TRUE)).forEach(e -> {
            switch (e.getKey()){
                case OBFUSCATED: set.add(NamedStyles.OBFUSCATED); break;
                case BOLD: set.add(NamedStyles.BOLD); break;
                case STRIKETHROUGH: set.add(NamedStyles.STRIKE_THOUGH); break;
                case UNDERLINED: set.add(NamedStyles.UNDERLINE); break;
                case ITALIC: set.add(NamedStyles.ITALIC); break;
                default: throw new IllegalStateException("Unknown TextStyle of " + e.getKey().toString());
            }
        });
        return set;
    }

    @Override
    public List<Text> getChildren() {
        return ArrayUtils.convert(SpongeGenericText::new, this.getComponent().children());
    }
}
