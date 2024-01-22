package com.spaccafx.Files;

import java.net.URL;

public class ResourceLoader {
    public static URL getResource(String path) {
        return ResourceLoader.class.getResource("/" + path);
    }
}
