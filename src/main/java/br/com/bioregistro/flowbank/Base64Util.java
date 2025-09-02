package br.com.bioregistro.flowbank;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

public class Base64Util {

    public static String generate() {
        UUID uuid = UUID.randomUUID();
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
        return Base64.getUrlEncoder().withoutPadding().encodeToString(buffer.array());
    }

}
