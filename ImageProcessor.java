import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.SwingUtilities;

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
    private void processBlock(int xStart, int yStart) {
        int width = image.getWidth();
        int height = image.getHeight();
        int xEnd = Math.min(xStart + squareSize, width);
        int yEnd = Math.min(yStart + squareSize, height);
        long sumRed = 0, sumGreen = 0, sumBlue = 0;
        int countOrigPixels = 0;
        // Accumulate RGB values within the block
        for (int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++) {
                int rgb = image.getRGB(x, y);
                Color color = new Color(rgb);
                sumRed += color.getRed();
                sumGreen += color.getGreen();
                sumBlue += color.getBlue();
                countOrigPixels++; // Count the number of pixels in the block
            }
        }
        // Calculate average RGB values
        int avgRed = (int) (sumRed / countOrigPixels);
        int avgGreen = (int) (sumGreen / countOrigPixels);
        int avgBlue = (int) (sumBlue / countOrigPixels);
        Color avgColor = new Color(avgRed, avgGreen, avgBlue);

        // Set the entire block to the average color
        for (int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++) {
                image.setRGB(x, y, avgColor.getRGB()); // Set the RGB value of the pixel
            }
        }
    }


    /**
     * Processes the image in a single-thread, averaging each block sequentially
     * @param repaintCallback callback to repaint the image in the GUI
     */
    public void processImageSingleThreaded(Runnable repaintCallback) {
        int width = image.getWidth();
        int height = image.getHeight();

        // Iterate the image while squareSize increments
        for (int y = 0; y < height; y += squareSize) {
            for (int x = 0; x < width; x += squareSize) {
                processBlock(x, y);
                // Schedule a repaint on the Event Dispatch Thread (EDT)
                SwingUtilities.invokeLater(repaintCallback);
                try {
                    Thread.sleep(10); // Delay to visualize
                } catch (InterruptedException e) {
                    System.err.println("Error: Thread interrupted.");
                    Thread.currentThread().interrupt(); // Restore interrupt
                }
            }
        }
        // Final repaint to ensure all changes are displayed
        SwingUtilities.invokeLater(repaintCallback);
    }


    /**
     * Processes the image in multiple-threads, dividing the image into segments
     * based on the number of available CPU cores
     * @param repaintCallback callback to repaint the image in the GUI
     */
    public void processImageMultiThreaded(Runnable repaintCallback) {
        int numThreads = Runtime.getRuntime().availableProcessors();    // Number of available CPU cores
        Thread[] threads = new Thread[numThreads];  // Array to store threads
        int height = image.getHeight();
        int segmentHeight = height / numThreads;
        // Create and start threads for each segment
        for (int i = 0; i < numThreads; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                int startY = threadIndex * segmentHeight;   // Start of the segment
                int endY = (threadIndex == numThreads - 1) ? height : startY + segmentHeight;   // Last thread handles the remainder
                
                // Iterate the image while squareSize increments within the segment assigned to the thread
                for (int y = startY; y < endY; y += squareSize) {
                    for (int x = 0; x < image.getWidth(); x += squareSize) {
                        processBlock(x, y);
                        SwingUtilities.invokeLater(repaintCallback);    // Schedule a repaint on the Event Dispatch Thread (EDT)
                        try {
                            Thread.sleep(10);   // Delay to visualize
                        } catch (InterruptedException e) {
                            System.err.println("Error: Thread interrupted.");
                            Thread.currentThread().interrupt(); // Restore interrupt
                        }
                    }
                }
            });
            threads[i].start();
        }

        // Waiting for all threads to complete
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.err.println("Error: Thread interrupted.");
                Thread.currentThread().interrupt(); // Restore interrupt
            }
        }
        // Final repaint after all threads have finished
        SwingUtilities.invokeLater(repaintCallback);
    }
}

// Source for Event Dispatch Thread (EDT): https://github.com/mgarin/weblaf/wiki/Event-Dispatch-Thread