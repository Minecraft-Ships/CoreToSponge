package org.ships.implementation.sponge;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.core.CorePlugin;
import org.core.config.ConfigurationFormat;
import org.core.config.ConfigurationStream;
import org.core.event.EventManager;
import org.core.platform.Platform;
import org.core.platform.PlatformServer;
import org.core.schedule.SchedulerBuilder;
import org.core.source.command.ConsoleSource;
import org.core.text.Text;
import org.core.text.colour.TextColour;
import org.core.text.style.NamedStyles;
import org.core.text.style.TextStyle;
import org.core.text.type.ColouredText;
import org.core.text.type.JoinedText;
import org.core.text.type.StyledText;
import org.core.world.boss.ServerBossBar;
import org.ships.implementation.sponge.configuration.YAMLConfigurationFile;
import org.ships.implementation.sponge.events.SEventManager;
import org.ships.implementation.sponge.platform.PlatformConsole;
import org.ships.implementation.sponge.platform.SpongePlatform;
import org.ships.implementation.sponge.platform.SpongePlatformServer;
import org.ships.implementation.sponge.scheduler.SSchedulerBuilder;
import org.ships.implementation.sponge.text.SText;
import org.ships.implementation.sponge.text.type.SpongeGenericText;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.plugin.PluginContainer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CoreToSponge extends CorePlugin.CoreImplementation {

    protected SpongePlatform platform = new SpongePlatform();
    protected SEventManager eventManager = new SEventManager();
    protected PlatformConsole console = new PlatformConsole();
    protected SpongePlatformServer server = new SpongePlatformServer(org.spongepowered.api.Sponge.server());

    public CoreToSponge(PluginContainer plugin){
        CoreImplementation.IMPLEMENTATION = this;
        org.spongepowered.api.Sponge.eventManager().registerListeners(plugin, eventManager.getRawListener());
        //TODO CHECK IF CORRECT
        //Task.builder().delayTicks(1).intervalTicks(1).name("tps").execute(getRawServer().getTPSExecutor()).build();
    }

    @Override
    public Platform getRawPlatform() {
        return this.platform;
    }

    @Override
    public EventManager getRawEventManager() {
        return this.eventManager;
    }

    @Override
    public ConsoleSource getRawConsole() {
        return this.console;
    }

    @Override
    public SchedulerBuilder createRawSchedulerBuilder() {
        return new SSchedulerBuilder();
    }

    @Override
    public ConfigurationStream.ConfigurationFile createRawConfigurationFile(File file, ConfigurationFormat type) {
        if(file == null){
            new IOException("Unknown file").printStackTrace();
            return null;
        }
        if(type == null){
            new IOException("Unknown Configuration Loader Format").printStackTrace();
            return null;
        }
        if(type.equals(ConfigurationFormat.FORMAT_YAML)){
            return new YAMLConfigurationFile(file);
        }
        throw new IllegalStateException("Unknown format type of " + type.getName());
    }

    @Override
    public PlatformServer getRawServer() {
        return this.server;
    }

    @Override
    @Deprecated
    public Text textBuilder(String chars) {
        return SText.sign(chars);
    }

    private Map<TextDecoration, TextDecoration.State> buildStyle(TextStyle... styles){
        Map<TextDecoration, TextDecoration.State> map = new HashMap<>();
        for(TextStyle style : styles){
            if(style instanceof NamedStyles){
                NamedStyles nStyle = (NamedStyles)style;
                TextDecoration decoration = null;
                switch (nStyle){
                    case OBFUSCATED:
                        decoration = TextDecoration.OBFUSCATED;
                        break;
                    case BOLD:
                        decoration = TextDecoration.BOLD;
                        break;
                    case STRIKE_THOUGH:
                        decoration = TextDecoration.STRIKETHROUGH;
                        break;
                    case UNDERLINE:
                        decoration = TextDecoration.UNDERLINED;
                        break;
                    case ITALIC:
                        decoration = TextDecoration.ITALIC;
                        break;
                    case RESET: break;
                    default:
                        throw new IllegalStateException("Unknown TextStyle of '" + nStyle.name() + "'");
                }
                if(decoration == null){
                    continue;
                }
                map.put(decoration, TextDecoration.State.TRUE);
            }
        }
        return map;
    }

    @Override
    public ColouredText colouredTextBuilder(TextColour colour, String text, TextStyle... styles) {
        return new SpongeGenericText<>(Component.text(text).color(TextColor.color(colour.getRed(), colour.getGreen(), colour.getBlue())).decorations(this.buildStyle(styles)));
    }

    @Override
    public StyledText styleTextBuilder(String text, TextStyle... styles) {
        return new SpongeGenericText<>(Component.text(text).decorations(this.buildStyle(styles)));
    }

    @Override
    public JoinedText joinTextBuilder(org.core.text.type.Text... texts) {
        return new SpongeGenericText<>(Component.join(Component.empty(), Arrays.stream(texts).map(t -> ((SText<?>)t).toSponge()).collect(Collectors.toList())));
    }

    @Override
    public ServerBossBar bossBuilder() {
        return null;
    }
}
