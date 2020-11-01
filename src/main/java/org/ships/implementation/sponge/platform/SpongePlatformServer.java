package org.ships.implementation.sponge.platform;

import org.core.command.CommandLauncher;
import org.core.entity.living.human.player.LivePlayer;
import org.core.entity.living.human.player.User;
import org.core.platform.PlatformServer;
import org.core.platform.tps.TPSExecutor;
import org.core.world.WorldExtent;
import org.ships.implementation.sponge.entity.living.human.player.live.SLivePlayer;
import org.ships.implementation.sponge.entity.living.human.player.live.SUser;
import org.ships.implementation.sponge.world.SWorldExtent;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.server.ServerWorld;

import java.util.*;

public class SpongePlatformServer implements PlatformServer {

    private Server platform = Sponge.getServer();
    private Set<CommandLauncher> commands = new HashSet<>();
    private TPSExecutor tpsExecutor = new TPSExecutor();

    public SpongePlatformServer(Server platform){
        this.platform = platform;
    }

    @Override
    public Set<WorldExtent> getWorlds() {
        Set<WorldExtent> set = new HashSet<>();
        this.platform.getWorldManager().getWorlds().forEach(w -> set.add(new SWorldExtent(w)));
        return set;
    }

    @Override
    public Optional<WorldExtent> getWorldByPlatformSpecific(String name) {
        Optional<ServerWorld> opWorld = this.platform.getWorldManager().getWorld(ResourceKey.resolve(name));
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
        Optional<ServerPlayer> opPlayer = Sponge.getServer().getPlayer(uuid);
        if(opPlayer.isPresent()){
           return Optional.of(new SLivePlayer(opPlayer.get()));
        }
        Optional<org.spongepowered.api.entity.living.player.User> opUser = Sponge.getServer().getUserManager().get(uuid);
        if(!opUser.isPresent()){
            return Optional.empty();
        }
        return Optional.of(new SUser(opUser.get()));
    }

    @Override
    public TPSExecutor getTPSExecutor() {
        return this.tpsExecutor;
    }

    @Override
    public Collection<CommandLauncher> getCommands() {
        return Collections.unmodifiableCollection(this.commands);
    }

    @Override
    public void registerCommands(CommandLauncher... commandLaunchers) {
        for(CommandLauncher commandLauncher : commandLaunchers) {
            Object plugin = commandLauncher.getPlugin().getLauncher();
            /*SCommand command = new SCommand(commandLauncher);
            Sponge.getCommandManager().register(plugin, command, commandLauncher.getName());
            this.commands.add(commandLauncher);*/
        }
    }
}
