import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class TestProcessBlock {
    public static void main(String[] args) {
        try {
            // Creation of a small test image
            BufferedImage testImage = new BufferedImage(4, 4, BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 4; x++) {
                    testImage.setRGB(x, y, new Color(x * 50, y * 50, (x + y) * 25).getRGB());
                }
            }

            // Instantiate ImageProcessor with the test image
            int squareSize = 2; // Block size
            ImageProcessor processor = new ImageProcessor(testImage, squareSize);

            // Process a block
            processor.processBlock(0, 0); // Top-left corner block

            // Save or print the result
            File outputFile = new File("pixel.jpg");
            ImageIO.write(testImage, "jpg", outputFile);
            System.out.println("Test image saved to: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error processing image: " + e.getMessage());
        }
    }
}
