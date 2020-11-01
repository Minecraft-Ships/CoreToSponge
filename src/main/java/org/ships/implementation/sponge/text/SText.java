package org.ships.implementation.sponge.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import org.array.utils.ArrayUtils;
import org.core.text.Text;

@Deprecated
public interface SText <S extends Component> extends Text {

    class SpongeTextUtils {

        public static final char MINECRAFT_SPECIAL_VALUE = '§';

        public static char toHex(int value){
            if(value > 16){
                throw new IllegalStateException("Hex doesn't have a value above 16. Decimal doesn't have a value above 9. Value inserted was " + value);
            }
            if(value < 10){
                return ((Integer)value).toString().charAt(0);
            }
            return (char)(97 + (value - 10));
        }

        public static String toCodes(Component component){
            if (!component.children().isEmpty()){
                return ArrayUtils.toString("", SpongeTextUtils::toCodes, component.children());
            }
            if(component instanceof TextComponent){
                TextComponent text = (TextComponent) component;
                return MINECRAFT_SPECIAL_VALUE + "" + text.color().value() + "" + MINECRAFT_SPECIAL_VALUE + text.content();
            }
            throw new IllegalStateException("Child state was " + component.getClass().getSimpleName() + ". Not supported currently");

        }

    }

    class GenericText implements SText<TextComponent> {

        private TextComponent component;

        public GenericText(TextComponent text) {
            this.component = text;
        }

        @Override
        public String toPlain() {
            return this.toSponge().content();
        }

        @Override
        @Deprecated
        public Text append(Text... text) {
            TextComponent text2 = this.toSponge();
            for(Text text1 : text){
                text2 = text2.append(((SText)text1).toSponge());
            }
            return new GenericText(text2);
        }

        @Override
        public TextComponent toSponge() {
            return this.component;
        }
    }

    S toSponge();

    @Override
    default boolean equalsExact(String chars) {
        return this.toExact().equals(chars);
    }

    default String toExact(){
        return SpongeTextUtils.toCodes(this.toSponge());
    }

    static GenericText message(String format){
        return sign(format);
    }

    static GenericText sign(String format){
        return new GenericText(TextComponent.of(format));
    }

    static <C extends Component> SText<C> of(C comp){
        if(comp instanceof TextComponent){
            return (SText<C>)new GenericText((TextComponent)comp);
        }
        throw new IllegalStateException("Unknown component type");
    }
}
