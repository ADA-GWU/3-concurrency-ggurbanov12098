import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ImageAverager {
    @SuppressWarnings("unused")
    private BufferedImage originalImage;
    @SuppressWarnings("unused")
    private final int squareSize;
    @SuppressWarnings("unused")
    private final String mode;


    public ImageAverager(String filename, int squareSize, String mode) {
        this.squareSize = squareSize;
        this.mode = mode.toUpperCase();

        try {
            originalImage = ImageIO.read(new File(filename));
        } catch (IOException e) {
            System.err.println("Error: Unable to load image file.");
            System.exit(1);
        }
    }




    //////////////// Experimental Area ¯\_(ツ)_/¯ ////////////////

    public boolean isLoaded() {
        return originalImage != null;
    }

    public static void main(String[] args) {
        // Example usage
        ImageAverager imageAverager = new ImageAverager("test.jpg", 1, "test");

        if (imageAverager.isLoaded()) {
            System.out.println("Image loaded successfully.");
        } else {
            System.out.println("Failed to load the image.");
        }
    }
}
