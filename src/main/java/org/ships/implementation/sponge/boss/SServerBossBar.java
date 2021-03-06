package org.ships.implementation.sponge.boss;

import net.kyori.adventure.bossbar.BossBar;
import org.core.adventureText.AText;
import org.core.adventureText.adventure.AdventureText;
import org.core.entity.living.human.player.LivePlayer;
import org.core.text.Text;
import org.core.world.boss.ServerBossBar;
import org.core.world.boss.colour.BossColour;
import org.ships.implementation.sponge.entity.living.human.player.live.SLivePlayer;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class SServerBossBar implements ServerBossBar {

    private final BossBar bossBar;
    private final Set<LivePlayer> players = new HashSet<>();

    public SServerBossBar(BossBar bar) {
        this.bossBar = bar;
    }

    @Deprecated
    @Override
    public Text getMessage() {
        throw new RuntimeException("Use getTitle()");
    }

    @Deprecated
    @Override
    public ServerBossBar setMessage(Text text) {
        throw new RuntimeException("use setTitle(AText)");
    }

    @Override
    public AText getTitle() {
        return new AdventureText(this.bossBar.name());
    }

    @Override
    public ServerBossBar setTitle(AText text) {
        this.bossBar.name(((AdventureText) text).getComponent());
        return this;
    }

    @Override
    public BossColour getColour() {
        return null;
    }

    @Override
    public ServerBossBar setColour(BossColour colour) {

        return null;
    }

    @Override
    public int getValue() {
        return (int) (this.bossBar.progress() * 10);
    }

    @Override
    public ServerBossBar setValue(int value) {
        this.bossBar.progress(value / 10);
        return this;
    }

    @Override
    public Set<LivePlayer> getPlayers() {
        return this.players;
    }

    @Override
    public ServerBossBar register(LivePlayer... players) {
        Stream.of(players).map(p -> (SLivePlayer) p).forEach(p -> {
            p.getSpongeEntity().showBossBar(this.bossBar);
            this.players.add(p);
        });
        return this;
    }

    @Override
    public ServerBossBar deregister(LivePlayer... players) {
        Stream.of(players).map(p -> (SLivePlayer) p).forEach(p -> {
            p.getSpongeEntity().hideBossBar(this.bossBar);
            this.players.remove(p);
        });
        return this;
    }
}
