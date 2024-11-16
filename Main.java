public class Main {
    public static void main(String[] args) {
        // Validate command-line arguments
        if (args.length != 3) {
            System.err.println("Usage: java Main <filename> <square size> <mode>");
            System.err.println("Mode: 'S' for single-threaded, 'M' for multi-threaded");
            System.exit(1);
        }

        int tempSquareSize;
        // Parse and validate the square size argument
        try {
            tempSquareSize = Integer.parseInt(args[1]);
            if (tempSquareSize <= 0) {
                throw new NumberFormatException("Square size must be a positive integer.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: Square size must be a positive integer.");
            System.exit(1);
            return; // Unreachable, but added to satisfy the compiler
        }
    }
}
