package util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * This class is used to load resources from the resources folder.
 */
public class ResourceLoader {
    /**
     * Loads a resource from the resources' folder.
     *
     * @param resName The name of the resource.
     * @return The resource as an InputStream.
     */
    public static InputStream loadResource(String resName) {
        return ResourceLoader.class.getClassLoader().getResourceAsStream(resName);
    }

    /**
     * Loads an image from the resources' folder.
     *
     * @param resName The name of the image.
     * @return The image.
     * @throws IOException If the image could not be loaded.
     */
    public static Image loadImage(String resName) throws IOException {
        URL url = ResourceLoader.class.getClassLoader().getResource("textures/" + resName);
        if (url == null) {
            throw new IOException("Resource not found: " + resName);
        } else {
            return ImageIO.read(url);
        }
    }
}

