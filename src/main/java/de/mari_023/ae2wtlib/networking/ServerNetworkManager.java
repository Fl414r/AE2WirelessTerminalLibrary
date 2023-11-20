package de.mari_023.ae2wtlib.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import de.mari_023.ae2wtlib.AE2wtlib;
import dev.architectury.networking.NetworkManager;

public class ServerNetworkManager {

    public static void registerServerBoundPacket(String name, PacketDeserializer deserializer) {
        NetworkManager.registerReceiver(NetworkManager.c2s(), AE2wtlib.makeID(name),
                (buf, context) -> {
                    buf.retain();
                    context.queue(() -> {
                        deserializer.create(buf).processPacketData(context.getPlayer());
                        buf.release();
                    });
                });
    }

    public static void sendToClient(ServerPlayer player, AE2wtlibPacket packet) {
        NetworkManager.sendToPlayer(player, AE2wtlib.makeID(packet.getPacketName()),
                packet.getPacketBuffer());
    }

    @FunctionalInterface
    public interface PacketDeserializer {
        AE2wtlibPacket create(FriendlyByteBuf buf);
    }
}
