package dev.flavored.apollo.listener;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.ApolloManager;
import com.lunarclient.apollo.event.ApolloListener;
import com.lunarclient.apollo.event.ApolloReceivePacketEvent;
import com.lunarclient.apollo.event.EventBus;
import com.lunarclient.apollo.event.Listen;
import com.lunarclient.apollo.player.ApolloPlayerManagerImpl;
import com.lunarclient.apollo.player.v1.PlayerHandshakeMessage;
import dev.flavored.apollo.wrapper.MinestomApolloPlayer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.*;

import java.util.Arrays;
import java.util.List;

public class ApolloPlayerListener implements ApolloListener {

    public ApolloPlayerListener(EventNode<Event> eventNode) {
        EventBus.getBus().register(this);
        eventNode.addListener(PlayerChatEvent.class, event -> {
            System.out.println("Is player running Lunar Client? " + Apollo.getPlayerManager().hasSupport(event.getPlayer().getUuid()));
        });
        eventNode.addListener(PlayerSpawnEvent.class, this::onPlayerSpawn);
        eventNode.addListener(PlayerDisconnectEvent.class, this::onPlayerDisconnect);
        eventNode.addListener(PlayerPluginMessageEvent.class, this::onPluginMessageReceived);
    }

    private void onPlayerSpawn(PlayerSpawnEvent event) {
        if (event.isFirstSpawn()) {
            // The player must be in the `PLAY` state before sending the register plugin message.
            event.getPlayer().sendPluginMessage("minecraft:register", ApolloManager.PLUGIN_MESSAGE_CHANNEL);
        }
    }

    private void onPlayerDisconnect(PlayerDisconnectEvent event) {
        ((ApolloPlayerManagerImpl) Apollo.getPlayerManager()).removePlayer(event.getPlayer().getUuid());
    }

    private void onPluginMessageReceived(PlayerPluginMessageEvent event) {
        switch (event.getIdentifier()) {
            case "minecraft:register" -> {
                List<String> channelsToRegister = splitChannelList(event.getMessageString());
                if (channelsToRegister.contains(ApolloManager.PLUGIN_MESSAGE_CHANNEL)) {
                    ((ApolloPlayerManagerImpl) Apollo.getPlayerManager()).addPlayer(new MinestomApolloPlayer(event.getPlayer()));
                }
            }
            case ApolloManager.PLUGIN_MESSAGE_CHANNEL -> {
                ApolloManager.getNetworkManager().receivePacket(event.getPlayer().getUuid(), event.getMessage());
            }
        }
    }

    @Listen
    private void onApolloReceivePacket(ApolloReceivePacketEvent event) {
        event.unpack(PlayerHandshakeMessage.class).ifPresent(message -> {
            ((ApolloPlayerManagerImpl) Apollo.getPlayerManager()).handlePlayerHandshake(event.getPlayer(), message);
        });
    }

    private List<String> splitChannelList(String messageString) {
        return Arrays.asList(messageString.split("\0"));
    }
}
