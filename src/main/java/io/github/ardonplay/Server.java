package io.github.ardonplay;

import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public class Server {
    private final ServerSocket server;

    private static final Logger logger =
            LoggerFactory.getLogger("Server");

    public Server(Configuration configuration) throws IOException {
        this.server = new ServerSocket();
        server.bind(new InetSocketAddress(configuration.getHost(), configuration.getPort()));
    }

    public void run() {
        logger.warn("Server started!");
        try {
            while (true) {
                Socket socket = server.accept();
                logger.info("Client connected!");

                try {
                    OutputStream stream = socket.getOutputStream();
                    socket.setReceiveBufferSize(Integer.MAX_VALUE);
                    BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());
                    byte[] buffer = new byte[socket.getReceiveBufferSize()];
                    inputStream.read(buffer, 0, socket.getReceiveBufferSize());
                    Mapper mapper = new Mapper(new ByteArrayInputStream(buffer));
                    logger.info(mapper.getHeaders().toString());
                    stream.write("HTTP/1.1 200 OK\r\n".getBytes());
                    stream.write("Content-Type: text/html; charset=utf-8\r\n".getBytes());
                    stream.write("\r\n".getBytes());
                    File file = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("test.html")).getFile());
                    stream.write(Files.toByteArray(file));
                    stream.close();
                    logger.info("Client disconnected!");
                } catch (IOException e) {
                    logger.error(e.getMessage());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
