package org.core.implementation.sponge.platform;

import org.core.entity.living.human.player.LivePlayer;
import org.core.entity.living.human.player.User;
import org.core.implementation.sponge.entity.living.human.player.live.SLivePlayer;
import org.core.implementation.sponge.entity.living.human.player.live.SUser;
import org.core.implementation.sponge.world.SWorldExtent;
import org.core.platform.PlatformServer;
import org.core.platform.plugin.Plugin;
import org.core.world.WorldExtent;
import org.core.world.position.block.details.BlockSnapshot;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.user.UserManager;
import org.spongepowered.api.world.server.ServerWorld;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class SpongePlatformServer implements PlatformServer {

    private final Server platform;

    public SpongePlatformServer(Server platform) {
        this.platform = platform;
    }

    public Server getServer() {
        return this.platform;
    }

    @Override
    public @NotNull Set<WorldExtent> getWorlds() {
        return this.platform.worldManager().worlds().stream().map(SWorldExtent::new).collect(Collectors.toSet());
    }

    @Override
    @Deprecated
    public @NotNull Optional<WorldExtent> getWorldByPlatformSpecific(String name) {
        Optional<ServerWorld> opWorld = this.platform.worldManager().world(ResourceKey.resolve(name));
        return opWorld.map(SWorldExtent::new);
    }

    @Override
    public @NotNull Collection<LivePlayer> getOnlinePlayers() {
        return this.platform.onlinePlayers().stream().map(SLivePlayer::new).collect(Collectors.toSet());
    }

    @Override
    public void applyBlockSnapshots(@NotNull Collection<? extends BlockSnapshot.AsyncBlockSnapshot> collection,
                                    @NotNull Plugin plugin,
                                    @NotNull Runnable onComplete) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public @NotNull CompletableFuture<Optional<User>> getOfflineUser(@NotNull UUID uuid) {
        Optional<ServerPlayer> opPlayer = this.platform.player(uuid);
        if (opPlayer.isPresent()) {
            return CompletableFuture.supplyAsync(() -> opPlayer.map(SLivePlayer::new));
        }
        CompletableFuture<Optional<org.spongepowered.api.entity.living.player.User>> compFutureOpUser = this.platform
                .userManager()
                .load(uuid);
        return compFutureOpUser.thenApply(opUser -> opUser.map(SUser::new));
    }

    @Override
    public @NotNull CompletableFuture<Optional<User>> getOfflineUser(@NotNull String lastName) {
        return Sponge.server().userManager().load(lastName).thenApply(opUser -> opUser.map(SUser::new));
    }

    @Override
    public @NotNull Collection<CompletableFuture<User>> getOfflineUsers() {
        UserManager userManager = Sponge.server().userManager();
        return userManager
                .streamAll()
                .map(gp -> userManager.loadOrCreate(gp.uuid()).thenApply(u -> (User) new SUser(u)))
                .collect(Collectors.toSet());
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
