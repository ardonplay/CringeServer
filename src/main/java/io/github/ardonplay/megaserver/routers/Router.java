package io.github.ardonplay.megaserver.routers;

import io.github.ardonplay.megaserver.controllers.Controller;
import io.github.ardonplay.megaserver.utils.messagies.RequestMessage;
import io.github.ardonplay.megaserver.utils.messagies.ResponseMessage;
import io.github.ardonplay.megaserver.utils.enums.HttpVersion;
import io.github.ardonplay.megaserver.utils.enums.TextStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Router {
    private static final Logger log = LoggerFactory.getLogger("ServerDetailed");
    private static final String SEP = "------------------------------------------------";

    private final List<Controller> controllers = new ArrayList<>();

    public void addController(Controller controller) {
        this.controllers.add(controller);
        this.controllers.sort(Comparator.comparingInt(o -> o.getRoute().length()));
    }

    private ResponseMessage dispatch(Controller controller, RequestMessage requestMessage) {
        switch (requestMessage.getMethod()) {
            case GET -> {
                return controller.getMapping(requestMessage);
            }
            case POST -> {
                return controller.postMapping(requestMessage);
            }
            case OPTIONS -> {
                return controller.optionsMapping(requestMessage);
            }
            default -> {
                return ResponseMessage.builder()
                        .status(418)
                        .textStatus(TextStatus.I_AM_TEAPOT)
                        .httpVersion(HttpVersion.ONE_ONE)
                        .build();
            }
        }
    }

    public ResponseMessage process(RequestMessage requestMessage) {
        log.info("\n\nRequest:\n{}\n{}{}\n", SEP, requestMessage, SEP);

        ResponseMessage responseMessage;

        for (Controller controller : this.controllers) {
            if (requestMessage.getRoute().startsWith(controller.getRoute())) {
                log.info("Transferred to the controller route: {}", controller.getRoute());
                responseMessage = dispatch(controller, requestMessage);
                log.info("\n\nResponse:\n{}\n{}\n{}\n", SEP, responseMessage, SEP);
                if (responseMessage != null)
                    return responseMessage;
                else
                    return ResponseMessage.builder().status(404).textStatus(TextStatus.NOT_FOUND).httpVersion(HttpVersion.ONE_ONE).build();
            }
        }
        log.info("Route for URL - [{}] - Not Found", requestMessage.getRoute());
        return ResponseMessage.builder().status(404).textStatus(TextStatus.NOT_FOUND).httpVersion(HttpVersion.ONE_ONE).build();

    }
}
