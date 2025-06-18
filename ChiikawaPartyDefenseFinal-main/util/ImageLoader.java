package util;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;

public class ImageLoader {
    private static final HashMap<String, Image> imageCache = new HashMap<>();
    private static final HashMap<String, ImageIcon> iconCache = new HashMap<>();

    public static Image load(String name) {
        if (!imageCache.containsKey(name)) {
            try {
                URL url = ImageLoader.class.getResource("/images/" + name);
                if (url == null) {
                    System.err.println("[ImageLoader] ❌ Image not found: /images/" + name);
                    return null;
                }
                Image image = new ImageIcon(url).getImage();
                imageCache.put(name, image);
            } catch (Exception e) {
                System.err.println("[ImageLoader] ⚠️ Error loading image: /images/" + name);
                e.printStackTrace();
                return null;
            }
        }
        return imageCache.get(name);
    }

    public static ImageIcon loadIcon(String name) {
        if (!iconCache.containsKey(name)) {
            try {
                URL url = ImageLoader.class.getResource("/images/" + name);  // FIXED
                if (url == null) {
                    System.err.println("[ImageLoader] ❌ Icon not found: /images/" + name);
                    return null;
                }
                ImageIcon icon = new ImageIcon(url);
                iconCache.put(name, icon);
            } catch (Exception e) {
                System.err.println("[ImageLoader] ⚠️ Error loading icon: /images/" + name);
                e.printStackTrace();
                return null;
            }
        }
        return iconCache.get(name);
    }

}
