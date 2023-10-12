package io.github.ardonplay.megaserver.utils.messagies;
import io.github.ardonplay.megaserver.utils.enums.TextStatus;
import io.github.ardonplay.megaserver.utils.messagies.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.nio.charset.StandardCharsets;


@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class ResponseMessage extends Message {
    private final TextStatus textStatus;
    private final int status;

    @Override
    public String toString() {
        return httpVersion.toString() + " "  + status + " " + textStatus +  "\n" + (headers != null ? headers : "") + (body != null ? "\n" +new String(body.array(), StandardCharsets.UTF_8) : "");
    }

}
