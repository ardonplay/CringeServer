package io.github.ardonplay;

import com.google.common.io.Files;
import lombok.Getter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Mapper {
    private final Map<String, String> headers;

    private final HttpMethod type;
    private final String route;
    private final String httpVersion;
    private final List<Byte> body;

    public Mapper(InputStream stream) throws IOException, InterruptedException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String[] requestLine = reader.readLine().split(" ");
        this.type = HttpMethod.valueOf(requestLine[0].trim());
        this.route = requestLine[1].trim();
        this.httpVersion = requestLine[2].trim();
        this.headers = new HashMap<>();
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.isEmpty()) {
                String[] keyValue = line.split(":", 2);
                headers.put(keyValue[0].trim(), keyValue[1].trim());
            } else {
                break;
            }
        }
        this.body = new ArrayList<>();
        if (headers.containsKey("Content-Length")) {
            int size = Integer.parseInt(headers.get("Content-Length"));
            System.out.println(size);
            System.out.println(headers);
            byte[] buffer = new byte[size];
            int readed = 0;
            while (readed < size) {
                int bytesRead = stream.read(buffer, readed, size - readed);
                if (bytesRead == -1) {
                    break;
                }
                readed += bytesRead;
                System.out.println("Readed: " + readed);
            }


            File file = new File("test.mp3");
            Files.write(buffer, file);
        }
    }

}
