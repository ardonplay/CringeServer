package io.github.ardonplay.megaserver.utils.enums;

public enum HttpVersion {
    ONE_ONE("HTTP/1.1");
    private final String stringValue;

    HttpVersion(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    public static HttpVersion findByString(String value) {
        for (HttpVersion version : HttpVersion.values()) {
            if (version.stringValue.equals(value)) {
                return version;
            }
        }
        throw new IllegalArgumentException("Invalid HttpVersion: " + value);
    }
}
