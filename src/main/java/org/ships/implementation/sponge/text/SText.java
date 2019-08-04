package org.ships.implementation.sponge.text;

import org.core.text.Text;
import org.core.text.TextColours;
import org.spongepowered.api.text.serializer.TextSerializers;

public class SText implements Text {

    protected org.spongepowered.api.text.Text text;

    public SText(org.spongepowered.api.text.Text text){
        this.text = text;
    }

    public org.spongepowered.api.text.Text toSponge(){
        return this.text;
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof Text)){
            return false;
        }
        Text text = (Text) obj;
        return this.toPlain().equals(text.toPlain());
    }

    @Override
    public boolean equalsExact(String chars) {
        return equals(new SText(TextSerializers.FORMATTING_CODE.deserialize(chars)));
    }

    @Override
    public String toPlain() {
        String plain = this.text.toPlain();
        plain = TextColours.stripColours(plain);
        return plain;
    }

    @Override
    public Text append(Text... text) {
        SText text2 = this;
        for(Text toAdd : text){
            text2 = new SText(text2.text.toBuilder().append(((SText)toAdd).text).build());
        }
        return text2;
    }
}
