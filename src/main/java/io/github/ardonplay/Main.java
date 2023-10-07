package io.github.ardonplay;


import java.io.IOException;
public class Main {

    public static void main(String[] args) throws IOException {
        Configuration configuration = new Configuration("server.ini");
        Server server = new Server(configuration);
        server.run();
    }
}