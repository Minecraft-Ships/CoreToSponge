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
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import java.util.*;
import java.util.stream.Collectors;

public class SpongePlatformServer implements PlatformServer {

    private final Server platform;
    private final Set<CommandLauncher> commands = new HashSet<>();
    private final TPSExecutor tpsExecutor = new TPSExecutor();

    public SpongePlatformServer(Server platform) {
        this.platform = platform;
    }

    public Server getServer() {
        return this.platform;
    }

    @Override
    public Set<WorldExtent> getWorlds() {
        return this
                .platform
                .worldManager()
                .worlds()
                .stream()
                .map(SWorldExtent::new)
                .collect(Collectors.toSet());
    }

    @Override
    @Deprecated
    public Optional<WorldExtent> getWorldByPlatformSpecific(String name) {
        Optional<ResourceKey> opKey = this.platform.worldManager().worldKey(UUID.fromString(name));
        if (!opKey.isPresent()) {
            return Optional.empty();
        }
        return this
                .platform
                .worldManager()
                .world(opKey.get())
                .map(SWorldExtent::new);
    }

    @Override
    public Collection<LivePlayer> getOnlinePlayers() {
        List<LivePlayer> list = new ArrayList<>();
        return this
                .platform
                .onlinePlayers()
                .stream()
                .map(SLivePlayer::new)
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<User> getOfflineUser(UUID uuid) {
        Optional<ServerPlayer> opPlayer = this.platform.player(uuid);
        if (opPlayer.isPresent()) {
            return opPlayer.map(SLivePlayer::new);
        }
        Optional<org.spongepowered.api.entity.living.player.User> opUser = this.platform.userManager().find(uuid);
        return opUser.map(SUser::new);
    }

    @Override
    public TPSExecutor getTPSExecutor() {
        return this.tpsExecutor;
    }

    /*@Override
    public Collection<CommandLauncher> getCommands() {
        return Collections.unmodifiableCollection(this.commands);
    }

    @Override
    public void registerCommands(CommandLauncher... commandLaunchers) {
        for(CommandLauncher commandLauncher : commandLaunchers) {
            Object plugin = commandLauncher.getPlugin().getLauncher();*/
            /*SCommand command = new SCommand(commandLauncher);
            Sponge.getCommandManager().register(plugin, command, commandLauncher.getName());
            this.commands.add(commandLauncher);*/
        /*}
    }*/
}
