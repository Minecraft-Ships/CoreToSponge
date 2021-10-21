package org.core.implementation.sponge.text;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.core.text.TextColour;

public abstract class STextColour implements TextColour {

    public static class STextColourWhite extends STextColour {

        private STextColourWhite() {
            super(NamedTextColor.WHITE);
        }

        @Override
        public String formatChar() {
            return " §f";
        }
    }

    public static class STextColourDarkRed extends STextColour {

        private STextColourDarkRed() {
            super(NamedTextColor.DARK_RED);
        }

        @Override
        public String formatChar() {
            return " §4";
        }
    }

    public static class STextColourGold extends STextColour {

        private STextColourGold() {
            super(NamedTextColor.GOLD);
        }

        @Override
        public String formatChar() {
            return " §6";
        }
    }

    public static class STextColourBlue extends STextColour {

        private STextColourBlue() {
            super(NamedTextColor.BLUE);
        }

        @Override
        public String formatChar() {
            return " §9";
        }
    }

    public static class STextColourGreen extends STextColour {

        private STextColourGreen() {
            super(NamedTextColor.GREEN);
        }

        @Override
        public String formatChar() {
            return " §a";
        }
    }

    public static class STextColourAqua extends STextColour {

        private STextColourAqua() {
            super(NamedTextColor.AQUA);
        }

        @Override
        public String formatChar() {
            return " §b";
        }
    }

    public static class STextColourRed extends STextColour {

        private STextColourRed() {
            super(NamedTextColor.RED);
        }

        @Override
        public String formatChar() {
            return " §c";
        }
    }

    public static class STextColourYellow extends STextColour {

        private STextColourYellow() {
            super(NamedTextColor.YELLOW);
        }

        @Override
        public String formatChar() {
            return "§e";
        }
    }

    public static class STextColourDarkGreen extends STextColour {

        private STextColourDarkGreen() {
            super(NamedTextColor.DARK_GREEN);
        }

        @Override
        public String formatChar() {
            return "§2";
        }
    }

    public static class STextColourDarkAqua extends STextColour {

        private STextColourDarkAqua() {
            super(NamedTextColor.DARK_AQUA);
        }

        @Override
        public String formatChar() {
            return "§3";
        }
    }

    public static class STextColourDarkBlue extends STextColour {

        private STextColourDarkBlue() {
            super(NamedTextColor.DARK_BLUE);
        }

        @Override
        public String formatChar() {
            return "§1";
        }
    }

    public static class STextColourDarkGray extends STextColour {

        private STextColourDarkGray() {
            super(NamedTextColor.DARK_GRAY);
        }

        @Override
        public String formatChar() {
            return "§8";
        }
    }

    public static class STextColourLightPurple extends STextColour {

        private STextColourLightPurple() {
            super(NamedTextColor.LIGHT_PURPLE);
        }

        @Override
        public String formatChar() {
            return "§d";
        }
    }

    public static class STextColourDarkPurple extends STextColour {

        private STextColourDarkPurple() {
            super(NamedTextColor.DARK_PURPLE);
        }

        @Override
        public String formatChar() {
            return "§5";
        }
    }

    public static class STextColourGray extends STextColour {

        private STextColourGray() {
            super(NamedTextColor.GRAY);
        }

        @Override
        public String formatChar() {
            return "§7";
        }
    }

    public static class STextColourBlack extends STextColour {

        private STextColourBlack() {
            super(NamedTextColor.BLACK);
        }

        @Override
        public String formatChar() {
            return "§0";
        }
    }

    /*public static class STextColourNone extends STextColour {

        private STextColourNone() {
            super(NamedTextColor.NONE);
        }

        @Override
        public String formatChar() {
            return "§r";
        }
    }*/

    /*public static class STextColourReset extends STextColour {

        private STextColourReset() {
            super(NamedTextColor.RESET);
        }

        @Override
        public String formatChar() {
            return "§r";
        }
    }*/

    protected NamedTextColor colour;

    protected STextColour(NamedTextColor colour){
        this.colour = colour;
    }

    @Override
    public String getId() {
        return "minecraft:" + this.colour.toString();
    }

    @Override
    public String getName() {
        return this.colour.toString();
    }

    @Override
    public String toString(){
        return this.formatChar();
    }

    public static STextColour getInstance(TextColor colour){
        if(colour.equals(NamedTextColor.YELLOW)){
            return new STextColourYellow();
        }else if(colour.equals(NamedTextColor.RED)){
            return new STextColourRed();
        }else if(colour.equals(NamedTextColor.AQUA)) {
            return new STextColourAqua();
        }else if(colour.equals(NamedTextColor.GREEN)) {
            return new STextColourGreen();
        }else if(colour.equals(NamedTextColor.BLUE)) {
            return new STextColourBlue();
        /*}else if(colour.equals(NamedTextColor.RESET)) {
            return new STextColourReset();*/
        }else if(colour.equals(NamedTextColor.WHITE)) {
            return new STextColourWhite();
        }else if(colour.equals(NamedTextColor.GOLD)){
            return new STextColourGold();
        }else if(colour.equals(NamedTextColor.DARK_RED)){
            return new STextColourDarkRed();
        }else if(colour.equals(NamedTextColor.DARK_GREEN)){
            return new STextColourDarkGreen();
        }else if(colour.equals(NamedTextColor.DARK_AQUA)){
            return new STextColourDarkAqua();
        }else if(colour.equals(NamedTextColor.DARK_BLUE)){
            return new STextColourDarkBlue();
        }else if(colour.equals(NamedTextColor.DARK_GRAY)){
            return new STextColourDarkGray();
        }else if(colour.equals(NamedTextColor.DARK_PURPLE)){
            return new STextColourDarkPurple();
        }else if(colour.equals(NamedTextColor.LIGHT_PURPLE)){
            return new STextColourLightPurple();
        }else if(colour.equals(NamedTextColor.GRAY)){
            return new STextColourGray();
        }else if(colour.equals(NamedTextColor.BLACK)){
            return new STextColourBlack();
        /*}else if(colour.equals(NamedTextColor.NONE)){
            return new STextColourNone();*/
        }else{
            System.err.println("Text colour ("+ colour.toString() + ") is not supported with getInstance");
            return null;
        }
    }
}
