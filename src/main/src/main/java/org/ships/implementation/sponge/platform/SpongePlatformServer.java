package org.ships.implementation.sponge.platform;

import org.core.command.BaseCommandLauncher;
import org.core.platform.PlatformServer;
import org.core.world.WorldExtent;
import org.ships.implementation.sponge.command.SCommand;
import org.ships.implementation.sponge.world.SWorldExtent;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.World;

import java.util.*;

public class SpongePlatformServer implements PlatformServer {

    private Server platform = Sponge.getServer();
    private Set<BaseCommandLauncher> commands = new HashSet<>();

    public SpongePlatformServer(Server platform){
        this.platform = platform;
    }

    @Override
    public Set<WorldExtent> getWorlds() {
        Set<WorldExtent> set = new HashSet<>();
        this.platform.getWorlds().forEach(w -> set.add(new SWorldExtent(w)));
        return set;
    }

    @Override
    public Optional<WorldExtent> getWorldByPlatformSpecific(String name) {
        Optional<World> opWorld = this.platform.getWorld(UUID.fromString(name));
        if(!opWorld.isPresent()){
            return Optional.empty();
        }
        return Optional.of(new SWorldExtent(opWorld.get()));
    }

    @Override
    public Collection<BaseCommandLauncher> getCommands() {
        return Collections.unmodifiableCollection(this.commands);
    }

    @Override
    public void registerCommands(BaseCommandLauncher... commandLaunchers) {
        for(BaseCommandLauncher commandLauncher : commandLaunchers) {
            Object plugin = commandLauncher.getPlugin().getSpongeLauncher();
            SCommand command = new SCommand(commandLauncher);
            Sponge.getCommandManager().register(plugin, command, commandLauncher.getName());
            this.commands.add(commandLauncher);
        }
    }
}
