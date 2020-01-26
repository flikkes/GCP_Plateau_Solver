import parse.ProblemParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        final ProblemParser parser = new ProblemParser(args[0]);
        int[] colors = new int[parser.getNodeCount()];

        System.out.println("\n\n===================================SOLUTION===================================\n\n");

        for (int i = 1; i < 1001; i++) {
            for (int j = 0; j < colors.length; j++) {
                colors[j] = j;
            }
            System.out.println("Run " + i);
            final long before = System.currentTimeMillis();
            colors = plateauSearch(parser.getNodeConnections(), colors, getChromaticNumber(colors));
            final long after = System.currentTimeMillis();
            final long duration = after - before;
            int chromN = getChromaticNumber(colors);
            System.out.println("Duration (ms): " + duration);
            System.out.println("X(G): " + chromN);
        }

        final File solutionFile = new File(args[0] + ".sol");
        try (final FileWriter writer = new FileWriter(solutionFile, false)) {
            for (final int c : colors) {
                writer.write(c + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The algorithm as described in the pseudo code. Will try to adopt the color of another node in a specific distance.
     *
     * @param nodeConnections
     * @param colors
     * @param chrN
     * @return the new colors
     */
    private static int[] plateauSearch(final int[][] nodeConnections, final int[] colors, final int chrN) {
        final int[] newColors = Arrays.copyOf(colors, colors.length);
        int chromaticNumber = chrN;
        int newChromaticNumber = chrN;
        int node = 0;
        int limiter = 0;

        while (newChromaticNumber <= chromaticNumber && limiter < newColors.length * 20) {
            for (int dist = 1; dist < newColors.length + 1; dist++) {
                final int swapNode = node + dist < newColors.length ? node + dist : node + dist - newColors.length;
                if (newColors[node] != newColors[swapNode] && isPossible(node, newColors[swapNode], nodeConnections,
                        newColors)) {
                    newColors[node] = newColors[swapNode];
                }
            }
            chromaticNumber = newChromaticNumber;
            newChromaticNumber = getChromaticNumber(newColors);
            node = node + 1 < newColors.length ? node + 1 : 0;
            limiter = newChromaticNumber == chromaticNumber ? limiter + 1 : limiter;
        }
        return newColors;
    }

    /**
     * Count the different colors in the given array.
     *
     * @param colors
     * @return
     */
    private static int getChromaticNumber(int[] colors) {
        int res = 1;
        for (int i = 1; i < colors.length; i++) {
            int j = 0;
            for (j = 0; j < i; j++)
                if (colors[i] == colors[j])
                    break;
            if (i == j)
                res++;
        }
        return res;
    }

    /**
     * Check if a color is legal for a node in a graph.
     *
     * @param node
     * @param color
     * @param nodeConnections
     * @param colors
     * @return true if color is allowed, false if not
     */
    private static boolean isPossible(int node, int color, int[][] nodeConnections, final int[] colors) {
        if (color >= 0 && color < colors.length) {
            for (final int n : nodeConnections[node]) {
                if (colors[n] == color) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
