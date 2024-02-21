package dev.flavored.apollo.wrapper;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.player.ApolloPlayer;
import com.lunarclient.apollo.recipients.ForwardingRecipients;
import com.lunarclient.apollo.recipients.Recipients;
import com.lunarclient.apollo.world.ApolloWorld;
import net.minestom.server.instance.Instance;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public final class MinestomApolloWorld implements ApolloWorld, ForwardingRecipients {

    private final Instance instance;

    public MinestomApolloWorld(Instance instance) {
        this.instance = instance;
    }

    @Override
    public String getName() {
        // NOTE: Minestom instances do not have unique names (as with Bukkit worlds), so we use the unique ID instead
        //       since Apollo requires a unique world name.
        return instance.getUniqueId().toString();
    }

    @Override
    public Collection<ApolloPlayer> getPlayers() {
        return instance.getPlayers().stream()
                .map(player -> Apollo.getPlayerManager().getPlayer(player.getUuid()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<? extends Recipients> recipients() {
        return this.getPlayers();
    }
}
