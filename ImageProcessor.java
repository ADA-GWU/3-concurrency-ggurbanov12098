import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageProcessor {
    private final BufferedImage image;
    private final int squareSize;
    /**
     * Constructs an ImageProcessor with the given image and square size
     * @param image      The BufferedImage to process
     * @param squareSize The side length of the averaging square
     */
    public ImageProcessor(BufferedImage image, int squareSize) {
        this.image = image;
        this.squareSize = squareSize;
    }

    /**
     * Processes single block of the image, calculating the average color by
     * setting the entire block to average color
     * @param xStart The starting x-coordinate of the block
     * @param yStart The starting y-coordinate of the block
     */
    public void processBlock(int xStart, int yStart) {
        int width = image.getWidth();
        int height = image.getHeight();
        int xEnd = Math.min(xStart + squareSize, width);
        int yEnd = Math.min(yStart + squareSize, height);
        long sumRed = 0, sumGreen = 0, sumBlue = 0;
        int count = 0;
        // Accumulate RGB values within the block
        for (int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++) {
                int rgb = image.getRGB(x, y);
                Color color = new Color(rgb);
                sumRed += color.getRed();
                sumGreen += color.getGreen();
                sumBlue += color.getBlue();
                count++;
            }
        }
        // Calculate average RGB values
        int avgRed = (int) (sumRed / count);
        int avgGreen = (int) (sumGreen / count);
        int avgBlue = (int) (sumBlue / count);
        Color avgColor = new Color(avgRed, avgGreen, avgBlue);
        // Set the entire block to the average color
        for (int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++) {
                image.setRGB(x, y, avgColor.getRGB());
            }
        }
    }



}
