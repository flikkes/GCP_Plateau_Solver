import parse.ProblemParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    private static final String _1_FULL_INS_3 = "1-FullIns_3.col";
    private static final String _1_FULL_INS_4 = "1-FullIns_4.col";
    private static final String _2_FULL_INS_4 = "2-FullIns_4.col";
    private static final String HOMER = "homer.col";
    private static final String DSJC250_9 = "DSJC250.9.col";
    private static final String FLAT300_28_0 = "flat300_28_0.col";
    private static final String SIMPLE = "simple.col";
    private static final String LE450_15A = "le450_15a.col";
    private static final String DSJC250_1 = "DSJC250.1.col";
    private static final String WAP_01A = "wap01a.col";

    public static void main(String[] args) {
        final String testFileName = WAP_01A;
        final ProblemParser parser = new ProblemParser(testFileName);
        int[] colors = new int[parser.getNodeCount()];

        System.out.println("\n\n===================================SOLUTION===================================\n\n");

        String durationString = "";
        String chromNString = "";

        for (int i = 1; i < 1001; i++) {
            for (int j = 0; j < colors.length; j++) {
                colors[j] = j;
            }
            System.out.println("Run " + i);
            final long before = System.currentTimeMillis();
            colors = plateauSearch(parser.getNodeConnections(), colors, getChromaticNumber(colors));
            int chromN = getChromaticNumber(colors);
            final long after = System.currentTimeMillis();
            final long duration = after - before;
            durationString += duration + "\n";
            chromNString += chromN + "\n";
            System.out.println("Duration (ms): " + duration);
            System.out.println("Different colors: " + chromN);
        }
        System.out.println("Durations");
        System.out.println(durationString);
        System.out.println("Chromatic numbers");
        System.out.println(chromNString);


        final File solutionFile = new File(testFileName + ".sol");
        try (final FileWriter writer = new FileWriter(solutionFile, false)) {
            for (final int c : colors) {
                writer.write(c + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
            newChromaticNumber = getChromaticNumber(newColors);
            chromaticNumber = newChromaticNumber;
            node = node + 1 < newColors.length ? node + 1 : 0;
            limiter = newChromaticNumber == chromaticNumber ? limiter + 1 : limiter;
        }
        return newColors;
    }

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
