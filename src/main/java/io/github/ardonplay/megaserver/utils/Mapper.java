package io.github.ardonplay.megaserver.utils;

import io.github.ardonplay.megaserver.utils.enums.HttpMethod;
import io.github.ardonplay.megaserver.utils.enums.HttpVersion;
import io.github.ardonplay.megaserver.utils.messagies.RequestMessage;
import io.github.ardonplay.megaserver.utils.messagies.utils.MessageHeaders;
import lombok.Getter;

import java.io.*;
import java.nio.ByteBuffer;

@Getter
public class Mapper {

    public static RequestMessage parseRequest(ByteBuffer bufferedRequest) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bufferedRequest.array())));
        String[] requestLine = reader.readLine().split(" ");
        HttpMethod method = HttpMethod.valueOf(requestLine[0].trim());
        String route = requestLine[1].trim();
        HttpVersion httpVersion =  HttpVersion.findByString(requestLine[2].trim());
        MessageHeaders headers = new MessageHeaders();
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.isEmpty()) {
                String[] keyValue = line.split(":", 2);
                headers.put(keyValue[0].trim(), keyValue[1].trim());
            } else {
                break;
            }
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int data;
        while ((data = reader.read()) != -1) {
            stream.write(data);
        }
        ByteBuffer body = ByteBuffer.wrap(stream.toByteArray());

        return RequestMessage.builder()
                .method(method)
                .httpVersion(httpVersion)
                .route(route)
                .headers(headers)
                .body(body).build();
    }
}
