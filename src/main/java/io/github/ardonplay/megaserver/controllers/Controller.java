package io.github.ardonplay.megaserver.controllers;

import io.github.ardonplay.megaserver.utils.messagies.RequestMessage;
import io.github.ardonplay.megaserver.utils.messagies.ResponseMessage;
import lombok.Getter;
@Getter
public abstract class Controller {

    private final String route;

    protected Controller(String route) {
        this.route = route;
    }

    public ResponseMessage getMapping(RequestMessage requestMessage) {
        return null;
    }

    public ResponseMessage postMapping(RequestMessage requestMessage) {
        return null;
    }

    public ResponseMessage optionsMapping(RequestMessage requestMessage) {
        return null;
    }

}
