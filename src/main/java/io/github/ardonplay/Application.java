package io.github.ardonplay;


import io.github.ardonplay.megaserver.Server;
import io.github.ardonplay.megaserver.controllers.ResponceController;
import io.github.ardonplay.megaserver.routers.Router;
import io.github.ardonplay.megaserver.utils.Configuration;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException {
        Configuration configuration = new Configuration("server.ini");
        Router router = new Router();
        router.addController(new ResponceController("/test"));
        Server server = new Server(configuration, router);
        server.run();
    }
}