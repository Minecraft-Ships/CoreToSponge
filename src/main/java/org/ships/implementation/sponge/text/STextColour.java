package org.ships.implementation.sponge.text;

import org.core.text.TextColour;
import org.spongepowered.api.text.format.TextColors;

public abstract class STextColour implements TextColour {

    public static class STextColourWhite extends STextColour {

        private STextColourWhite() {
            super(org.spongepowered.api.text.format.TextColors.WHITE);
        }

        @Override
        public String formatChar() {
            return " §f";
        }
    }

    public static class STextColourDarkRed extends STextColour {

        private STextColourDarkRed() {
            super(org.spongepowered.api.text.format.TextColors.DARK_RED);
        }

        @Override
        public String formatChar() {
            return " §4";
        }
    }

    public static class STextColourGold extends STextColour {

        private STextColourGold() {
            super(org.spongepowered.api.text.format.TextColors.GOLD);
        }

        @Override
        public String formatChar() {
            return " §6";
        }
    }

    public static class STextColourBlue extends STextColour {

        private STextColourBlue() {
            super(org.spongepowered.api.text.format.TextColors.BLUE);
        }

        @Override
        public String formatChar() {
            return " §9";
        }
    }

    public static class STextColourGreen extends STextColour {

        private STextColourGreen() {
            super(org.spongepowered.api.text.format.TextColors.GREEN);
        }

        @Override
        public String formatChar() {
            return " §a";
        }
    }

    public static class STextColourAqua extends STextColour {

        private STextColourAqua() {
            super(org.spongepowered.api.text.format.TextColors.AQUA);
        }

        @Override
        public String formatChar() {
            return " §b";
        }
    }

    public static class STextColourRed extends STextColour {

        private STextColourRed() {
            super(org.spongepowered.api.text.format.TextColors.RED);
        }

        @Override
        public String formatChar() {
            return " §c";
        }
    }

    public static class STextColourYellow extends STextColour {

        private STextColourYellow() {
            super(org.spongepowered.api.text.format.TextColors.YELLOW);
        }

        @Override
        public String formatChar() {
            return "§e";
        }
    }

    public static class STextColourDarkGreen extends STextColour {

        private STextColourDarkGreen() {
            super(org.spongepowered.api.text.format.TextColors.DARK_GREEN);
        }

        @Override
        public String formatChar() {
            return "§2";
        }
    }

    public static class STextColourDarkAqua extends STextColour {

        private STextColourDarkAqua() {
            super(org.spongepowered.api.text.format.TextColors.DARK_AQUA);
        }

        @Override
        public String formatChar() {
            return "§3";
        }
    }

    public static class STextColourDarkBlue extends STextColour {

        private STextColourDarkBlue() {
            super(org.spongepowered.api.text.format.TextColors.DARK_BLUE);
        }

        @Override
        public String formatChar() {
            return "§1";
        }
    }

    public static class STextColourDarkGray extends STextColour {

        private STextColourDarkGray() {
            super(org.spongepowered.api.text.format.TextColors.DARK_GRAY);
        }

        @Override
        public String formatChar() {
            return "§8";
        }
    }

    public static class STextColourLightPurple extends STextColour {

        private STextColourLightPurple() {
            super(org.spongepowered.api.text.format.TextColors.LIGHT_PURPLE);
        }

        @Override
        public String formatChar() {
            return "§d";
        }
    }

    public static class STextColourDarkPurple extends STextColour {

        private STextColourDarkPurple() {
            super(org.spongepowered.api.text.format.TextColors.DARK_PURPLE);
        }

        @Override
        public String formatChar() {
            return "§5";
        }
    }

    public static class STextColourGray extends STextColour {

        private STextColourGray() {
            super(org.spongepowered.api.text.format.TextColors.GRAY);
        }

        @Override
        public String formatChar() {
            return "§7";
        }
    }

    public static class STextColourBlack extends STextColour {

        private STextColourBlack() {
            super(org.spongepowered.api.text.format.TextColors.BLACK);
        }

        @Override
        public String formatChar() {
            return "§0";
        }
    }

    public static class STextColourNone extends STextColour {

        private STextColourNone() {
            super(org.spongepowered.api.text.format.TextColors.NONE);
        }

        @Override
        public String formatChar() {
            return "§r";
        }
    }

    public static class STextColourReset extends STextColour {

        private STextColourReset() {
            super(TextColors.RESET);
        }

        @Override
        public String formatChar() {
            return "§r";
        }
    }

    protected org.spongepowered.api.text.format.TextColor colour;

    protected STextColour(org.spongepowered.api.text.format.TextColor colour){
        this.colour = colour;
    }

    @Override
    public String getId() {
        return this.colour.getId();
    }

    @Override
    public String getName() {
        return this.colour.getName();
    }

    @Override
    public String toString(){
        return this.formatChar();
    }

    public static STextColour getInstance(org.spongepowered.api.text.format.TextColor colour){
        if(colour.equals(org.spongepowered.api.text.format.TextColors.YELLOW)){
            return new STextColourYellow();
        }else if(colour.equals(org.spongepowered.api.text.format.TextColors.RED)){
            return new STextColourRed();
        }else if(colour.equals(org.spongepowered.api.text.format.TextColors.AQUA)) {
            return new STextColourAqua();
        }else if(colour.equals(org.spongepowered.api.text.format.TextColors.GREEN)) {
            return new STextColourGreen();
        }else if(colour.equals(org.spongepowered.api.text.format.TextColors.BLUE)) {
            return new STextColourBlue();
        }else if(colour.equals(org.spongepowered.api.text.format.TextColors.RESET)) {
            return new STextColourReset();
        }else if(colour.equals(org.spongepowered.api.text.format.TextColors.WHITE)) {
            return new STextColourWhite();
        }else if(colour.equals(org.spongepowered.api.text.format.TextColors.GOLD)){
            return new STextColourGold();
        }else if(colour.equals(org.spongepowered.api.text.format.TextColors.DARK_RED)){
            return new STextColourDarkRed();
        }else if(colour.equals(org.spongepowered.api.text.format.TextColors.DARK_GREEN)){
            return new STextColourDarkGreen();
        }else if(colour.equals(org.spongepowered.api.text.format.TextColors.DARK_AQUA)){
            return new STextColourDarkAqua();
        }else if(colour.equals(org.spongepowered.api.text.format.TextColors.DARK_BLUE)){
            return new STextColourDarkBlue();
        }else if(colour.equals(org.spongepowered.api.text.format.TextColors.DARK_GRAY)){
            return new STextColourDarkGray();
        }else if(colour.equals(org.spongepowered.api.text.format.TextColors.DARK_PURPLE)){
            return new STextColourDarkPurple();
        }else if(colour.equals(org.spongepowered.api.text.format.TextColors.LIGHT_PURPLE)){
            return new STextColourLightPurple();
        }else if(colour.equals(org.spongepowered.api.text.format.TextColors.GRAY)){
            return new STextColourGray();
        }else if(colour.equals(org.spongepowered.api.text.format.TextColors.BLACK)){
            return new STextColourBlack();
        }else if(colour.equals(org.spongepowered.api.text.format.TextColors.NONE)){
            return new STextColourNone();
        }else{
            System.err.println("Text colour ("+ colour.getId() + ") is not supported with getInstance");
            return null;
        }
    }
}
