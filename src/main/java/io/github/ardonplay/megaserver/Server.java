package io.github.ardonplay.megaserver;

import io.github.ardonplay.megaserver.routers.Router;
import io.github.ardonplay.megaserver.utils.*;
import io.github.ardonplay.megaserver.utils.exceptions.ServerRuntimeException;
import io.github.ardonplay.megaserver.utils.messagies.RequestMessage;
import io.github.ardonplay.megaserver.utils.messagies.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.*;

public class Server {
    private static final int THREAD_POOL_SIZE = 28;
    private final AsynchronousServerSocketChannel server;

    private final ExecutorService executorService;

    private final Router router;
    private static final Logger log =
            LoggerFactory.getLogger("Server");

    public Server(Configuration configuration, Router router) throws IOException {
        this.router = router;
        this.server = AsynchronousServerSocketChannel.open();
        server.bind(new InetSocketAddress(configuration.getHost(), configuration.getPort()));
        this.executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    public void run() {
        log.info("Server started!");
        while (true) {
            try {
                Connection connection = new Connection(server.accept().get(Integer.MAX_VALUE, TimeUnit.DAYS));
                log.info("Client connected");
                executorService.execute(() -> handleConnection(connection));
            } catch (ExecutionException | InterruptedException | TimeoutException e) {
                log.warn("Error received: {}", e.getMessage());
                throw new ServerRuntimeException(e.getMessage());
            }
        }
    }

    public void handleConnection(Connection connection) {
        try {
            log.info("Client connected");
            long startTime = System.currentTimeMillis();
            long duration = 3000;

            while (connection.isOpen()) {
                if (System.currentTimeMillis() - startTime > duration) {
                    connection.close();
                    break;
                }

                var byteRequest = connection.read();

                if (byteRequest.available() == 0)
                    continue;

                RequestMessage requestMessage = Mapper.parseRequest(ByteBuffer.wrap(byteRequest.readAllBytes()));

                log.info("Request: {} {}", requestMessage.getMethod(), requestMessage.getRoute());

                ResponseMessage responceMessage = router.process(requestMessage);
                connection.write(new ByteArrayInputStream(responceMessage.getBytes()));

                if (shouldKeepAlive(requestMessage)) {
                    requestMessage.getHeaders().put("Connection", "keep-alive");
                    connection.setKeepAliveOption();
                    log.info("keep-alive request");
                    duration += 3000;
                } else {
                    connection.close();
                }
            }
            log.info("Client disconnected");
        } catch (ExecutionException | InterruptedException | IOException e) {
            log.warn("Error received: {}", e.getMessage());
            throw new ServerRuntimeException(e.getMessage());
        }
    }

    private boolean shouldKeepAlive(RequestMessage requestMessage) {
        return requestMessage.getHeaders().containsKey("Connection")
                && requestMessage.getHeaders().get("Connection").equals("keep-alive");
    }
}
