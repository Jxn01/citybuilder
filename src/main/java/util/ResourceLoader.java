package util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

//utility class that loads images
public class ResourceLoader {
    public static InputStream loadResource(String resName) {
        return ResourceLoader.class.getClassLoader().getResourceAsStream(resName);
    }

    public static Image loadImage(String resName) throws IOException {
        URL url = ResourceLoader.class.getClassLoader().getResource(resName);
        if (url == null) {
            throw new IOException("Resource not found: " + resName);
        }
        return ImageIO.read(url);
    }
}

