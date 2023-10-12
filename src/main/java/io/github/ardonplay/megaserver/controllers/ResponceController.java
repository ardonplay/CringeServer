package io.github.ardonplay.megaserver.controllers;

import io.github.ardonplay.megaserver.utils.messagies.utils.MessageHeaders;
import io.github.ardonplay.megaserver.utils.messagies.RequestMessage;
import io.github.ardonplay.megaserver.utils.messagies.ResponseMessage;
import io.github.ardonplay.megaserver.utils.enums.HttpVersion;
import io.github.ardonplay.megaserver.utils.enums.TextStatus;

import java.nio.ByteBuffer;

public class ResponceController extends Controller {
    public ResponceController(String route) {
        super(route);
    }

    @Override
    public ResponseMessage getMapping(RequestMessage requestMessage) {
        String body;
        if (!requestMessage.getRoute().contains("favicon.ico")) {
            body = "<html>\n" +
                    "<head>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <h1>It Works!</h1>\n" +
                    "</body>\n" +
                    "</html>";
        }
        else {
            body = "";
        }
        MessageHeaders responseHeaders = new MessageHeaders();
        responseHeaders.put("Content-Type", "text/html");
        responseHeaders.put("Content-Length", Integer.toString(body.length()));
        responseHeaders.put("charset", "utf-8");
        return ResponseMessage.builder()
                .status(200)
                .textStatus(TextStatus.OK)
                .headers(responseHeaders)
                .httpVersion(HttpVersion.ONE_ONE)
                .body(ByteBuffer.wrap(body.getBytes())).build();
    }


}
