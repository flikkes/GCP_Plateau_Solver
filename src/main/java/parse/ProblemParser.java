package parse;

import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

@Getter
public class ProblemParser {
    private int nodeCount;
    private int verticeCount;
    private int[][] nodeConnections;

    public ProblemParser(final String fileName) {
        try (Stream<String> stream = Files
                .lines(new File(fileName).toPath())) {

            stream.forEach(l -> {
                if (!l.trim().isEmpty()) {
                    if (l.startsWith("p")) {
                        final String[] metaDataSplit = l.split(" ");
                        this.nodeCount = Integer.parseInt(metaDataSplit[2]);
                        this.verticeCount = Integer.parseInt(metaDataSplit[3]);
                        this.nodeConnections = new int[this.nodeCount][0];
                    } else if (l.startsWith("e")) {
                        final String[] connectionSplit = l.split(" ");
                        final int node = Integer.parseInt(connectionSplit[1]) - 1;
                        final int connectedNode = Integer.parseInt(connectionSplit[2]) - 1;
                        /*
                        Add all connections to each node, also the duplicates so iterating and checking if a color is
                        valid gets much easier
                         */
                        final int[] newConnectedNodes1 = new int[this.nodeConnections[node].length + 1];
                        final int[] newConnectedNodes2 = new int[this.nodeConnections[connectedNode].length + 1];
                        for (int i = 0; i < this.nodeConnections[node].length; i++) {
                            newConnectedNodes1[i] = this.nodeConnections[node][i];
                        }
                        for (int i = 0; i < this.nodeConnections[connectedNode].length; i++) {
                            newConnectedNodes2[i] = this.nodeConnections[connectedNode][i];
                        }
                        newConnectedNodes1[newConnectedNodes1.length - 1] = connectedNode;
                        newConnectedNodes2[newConnectedNodes2.length - 1] = node;
                        this.nodeConnections[node] = newConnectedNodes1;
                        this.nodeConnections[connectedNode] = newConnectedNodes2;
                    }
                }
            });

        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

}
