package io.github.ardonplay.megaserver.utils;

import org.ini4j.Ini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;


import static java.util.stream.Collectors.toMap;

public class Configuration {
    private final Logger logger = LoggerFactory.getLogger("Configuration");

    private final Map<String, String> keys;

    private Map<String, Map<String, String>> parseIniFile(File fileToParse)
            throws IOException {

        return new Ini(fileToParse).entrySet().stream()
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Configuration(String path) throws IOException {
        File config = new File(Objects.requireNonNull(getClass().getClassLoader().getResource(path)).getFile());
        this.keys = parseIniFile(config).get("server");
        logger.warn("Config: {}", keys);
    }

    public String getHost() {
        return keys.get("host");
    }

    public int getPort() {
        return Integer.parseInt(keys.get("port"));
    }

    public String getPath() {
        return keys.get("path");
    }

}
