package dev.flavored.apollo.listener;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.event.ApolloListener;
import com.lunarclient.apollo.event.EventBus;
import com.lunarclient.apollo.event.Listen;
import com.lunarclient.apollo.event.player.ApolloRegisterPlayerEvent;
import com.lunarclient.apollo.player.AbstractApolloPlayer;
import com.lunarclient.apollo.player.ApolloPlayer;
import com.lunarclient.apollo.player.v1.UpdatePlayerWorldMessage;
import com.lunarclient.apollo.world.ApolloWorldManagerImpl;
import dev.flavored.apollo.wrapper.MinestomApolloWorld;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.instance.InstanceRegisterEvent;
import net.minestom.server.event.instance.InstanceUnregisterEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.Instance;

public class ApolloWorldListener implements ApolloListener {

    public ApolloWorldListener(EventNode<Event> eventNode) {
        EventBus.getBus().register(this);
        eventNode.addListener(InstanceRegisterEvent.class, this::onInstanceRegister);
        eventNode.addListener(InstanceUnregisterEvent.class, this::onInstanceUnregister);
        eventNode.addListener(PlayerSpawnEvent.class, this::onInstanceChange);

        ApolloWorldManagerImpl worldManager = (ApolloWorldManagerImpl) Apollo.getWorldManager();
        for (Instance instance : MinecraftServer.getInstanceManager().getInstances()) {
            worldManager.addWorld(new MinestomApolloWorld(instance));
        }
    }

    private void onInstanceRegister(InstanceRegisterEvent event) {
        ((ApolloWorldManagerImpl) Apollo.getWorldManager()).addWorld(new MinestomApolloWorld(event.getInstance()));
    }

    private void onInstanceUnregister(InstanceUnregisterEvent event) {
        ((ApolloWorldManagerImpl) Apollo.getWorldManager()).removeWorld(event.getInstance().getUniqueId().toString());
    }

    private void onInstanceChange(PlayerSpawnEvent event) {
        Player player = event.getPlayer();
        Apollo.getPlayerManager().getPlayer(player.getUuid()).ifPresent(apolloPlayer -> {
            UpdatePlayerWorldMessage message = UpdatePlayerWorldMessage.newBuilder()
                    .setWorld(player.getInstance().getUniqueId().toString())
                    .build();
            ((AbstractApolloPlayer) apolloPlayer).sendPacket(message);
        });
    }

    @Listen
    private void onApolloRegisterPlayer(ApolloRegisterPlayerEvent event) {
        ApolloPlayer apolloPlayer = event.getPlayer();
        apolloPlayer.getWorld().ifPresent(world -> {
            UpdatePlayerWorldMessage message = UpdatePlayerWorldMessage.newBuilder()
                    .setWorld(world.getName())
                    .build();
            ((AbstractApolloPlayer) apolloPlayer).sendPacket(message);
        });
    }
}
