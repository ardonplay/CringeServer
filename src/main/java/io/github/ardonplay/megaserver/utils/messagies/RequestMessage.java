package io.github.ardonplay.megaserver.utils.messagies;

import io.github.ardonplay.megaserver.utils.enums.HttpMethod;
import io.github.ardonplay.megaserver.utils.messagies.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.nio.charset.StandardCharsets;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class RequestMessage extends Message {

    private final HttpMethod method;
    private final String route;

    @Override
    public String toString() {
        return method + " " + route + " " + httpVersion.toString() + "\n" + (headers != null ? headers : "") + (body != null ? "\n" +new String(body.array(), StandardCharsets.UTF_8) : "");
    }
}
