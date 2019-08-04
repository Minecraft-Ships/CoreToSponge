package org.ships.implementation.sponge.platform;

import org.core.command.CommandLauncher;
import org.core.entity.living.human.player.LivePlayer;
import org.core.entity.living.human.player.User;
import org.core.platform.PlatformServer;
import org.core.world.WorldExtent;
import org.ships.implementation.sponge.command.SCommand;
import org.ships.implementation.sponge.entity.living.human.player.live.SLivePlayer;
import org.ships.implementation.sponge.entity.living.human.player.live.SUser;
import org.ships.implementation.sponge.world.SWorldExtent;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.ProviderRegistration;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.world.World;

import java.util.*;

public class SpongePlatformServer implements PlatformServer {

    private Server platform = Sponge.getServer();
    private Set<CommandLauncher> commands = new HashSet<>();

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
    public Collection<LivePlayer> getOnlinePlayers() {
        List<LivePlayer> list = new ArrayList<>();
        Sponge.getServer().getOnlinePlayers().forEach(p -> list.add(new SLivePlayer(p)));
        return list;
    }

    @Override
    public Optional<User> getOfflineUser(UUID uuid) {
        Optional<Player> opPlayer = Sponge.getServer().getPlayer(uuid);
        if(opPlayer.isPresent()){
           return Optional.of(new SLivePlayer(opPlayer.get()));
        }
        Optional<ProviderRegistration<UserStorageService>> opReg = Sponge.getServiceManager().getRegistration(UserStorageService.class);
        if(!opReg.isPresent()){
            return Optional.empty();
        }
        Optional<org.spongepowered.api.entity.living.player.User> opUser = opReg.get().getProvider().get(uuid);
        if(!opUser.isPresent()){
            return Optional.empty();
        }
        return Optional.of(new SUser(opUser.get()));
    }

    @Override
    public Collection<CommandLauncher> getCommands() {
        return Collections.unmodifiableCollection(this.commands);
    }

    @Override
    public void registerCommands(CommandLauncher... commandLaunchers) {
        for(CommandLauncher commandLauncher : commandLaunchers) {
            Object plugin = commandLauncher.getPlugin().getSpongeLauncher();
            SCommand command = new SCommand(commandLauncher);
            Sponge.getCommandManager().register(plugin, command, commandLauncher.getName());
            this.commands.add(commandLauncher);
        }
    }
}
