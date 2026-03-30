package kr.pyke.network.payload.c2s;

import java.util.function.Supplier;

import kr.pyke.CheeseBridge;
import kr.pyke.integration.ChzzkBridge;
import kr.pyke.integration.ChzzkDonationEvent;
import kr.pyke.util.DonationLogger;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class C2S_DonationPayload {
    private final String donor;
    private final String donationAmount;
    private final String donationMessage;

    public C2S_DonationPayload(String donor, String donationAmount, String donationMessage) {
        this.donor = donor;
        this.donationAmount = donationAmount;
        this.donationMessage = donationMessage;
    }

    public String donor() { return donor; }
    public String donationAmount() { return donationAmount; }
    public String donationMessage() { return donationMessage; }

    public static void encode(C2S_DonationPayload packet, FriendlyByteBuf buf) {
        buf.writeUtf(packet.donor);
        buf.writeUtf(packet.donationAmount);
        buf.writeUtf(packet.donationMessage);
    }

    public static C2S_DonationPayload decode(FriendlyByteBuf buf) {
        return new C2S_DonationPayload(buf.readUtf(), buf.readUtf(), buf.readUtf());
    }

    public static void handle(C2S_DonationPayload packet, Supplier<NetworkEvent.Context> ctx) {
        ServerPlayer player = ctx.get().getSender();
        if (player == null) return;

        String receiverName = player.getName().getString();

        ctx.get().enqueueWork(() -> {
            try {
                DonationLogger.logDonation(packet.donor(), receiverName, packet.donationAmount());
                ChzzkBridge.triggerDonation(player, new ChzzkDonationEvent(packet.donor(), packet.donationAmount(), packet.donationMessage()));
            }
            catch (Exception e) { CheeseBridge.LOGGER.error("플레이어 {}의 후원 보상 처리 중 시스템 예외 발생:", receiverName, e); }
        });
        ctx.get().setPacketHandled(true);
    }
}