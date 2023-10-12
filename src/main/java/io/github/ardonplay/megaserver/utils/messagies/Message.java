package io.github.ardonplay.megaserver.utils.messagies;

import io.github.ardonplay.megaserver.utils.messagies.utils.MessageHeaders;
import io.github.ardonplay.megaserver.utils.enums.HttpVersion;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.nio.ByteBuffer;

@Data
@SuperBuilder
public abstract class Message {
    protected final HttpVersion httpVersion;
    protected final MessageHeaders headers;
    protected final ByteBuffer body;

    public byte[] getBytes() {
        return toString().getBytes();
    }
}
