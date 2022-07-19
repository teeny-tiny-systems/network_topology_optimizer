package edu.utdallas.atn.p2.domain;

import java.util.*;

public class Graph {

  private final Map<Point, Integer> index;
  private final Map<Integer, Point> reverseIndex;

  private final int n;
  private final Boolean[][] connectivityAdjMatrix;

  public Graph(Graph graph) {
    this.n = graph.n;

    // Deep Copy, Map
    this.index = new HashMap<>(graph.index);
    this.reverseIndex = new HashMap<>(graph.reverseIndex);

    // Deep Copy, 2D Array
    this.connectivityAdjMatrix = new Boolean[n][n];
    for (int r = 0; r < n; r++)
      this.connectivityAdjMatrix[r] = Arrays.copyOf(graph.connectivityAdjMatrix[r], n);
  }

  public Graph(Map<Point, Integer> index) {
    this.index = index;
    this.reverseIndex = new HashMap<>();

    this.index.forEach((k, v) -> reverseIndex.put(v, k));
    this.n = index.size();
    this.connectivityAdjMatrix = new Boolean[n][n];
  }

  public void makeItCompleteGraph() {
    for (int r = 0; r < n; r++) {
      for (int c = 0; c < n; c++) {
        connectivityAdjMatrix[r][c] = true;
      }
    }
  }

  public void removeEdge(Edge edge) {
    Point start = edge.getStart();
    Point end = edge.getEnd();

    int startIndex = index.get(start);
    int endIndex = index.get(end);

    connectivityAdjMatrix[startIndex][endIndex] = false;
    connectivityAdjMatrix[endIndex][startIndex] = false;
  }

  public int getSmallestDegree() {
    int minDegree = Integer.MAX_VALUE;
    for (int r = 0; r < n; r++) {
      int degree = 0;
      for (int c = 0; c < n; c++) if (connectivityAdjMatrix[r][c]) degree++;
      minDegree = Math.min(degree, minDegree);
    }
    return minDegree;
  }

  public int getDiameter() {

    // 1. create adjMatrix
    int[][] adjMatrix = new int[n][n];
    for (int r = 0; r < n; r++)
      for (int c = 0; c < n; c++) {
        if (connectivityAdjMatrix[r][c]) adjMatrix[r][c] = 1;
        else adjMatrix[r][c] = Integer.MAX_VALUE;
      }

    // 2. run floyd-warshall to get the max hop-count from a vertex to every other vertex.
    for (int k = 0; k < n; k++) {
      for (int r = 0; r < n; r++) {
        for (int c = 0; c < n; c++) {
          adjMatrix[r][c] = Math.min(adjMatrix[r][k] + adjMatrix[k][c], adjMatrix[r][c]);
        }
      }
    }

    // 3. find the largest hop distance.
    int largestHopDistance = 0;
    for (int r = 0; r < n; r++) {
      for (int c = 0; c < n; c++) {
        largestHopDistance = Math.max(largestHopDistance, adjMatrix[r][c]);
      }
    }

    return largestHopDistance;
  }

  public List<Point> getCoordinates() {
    return new ArrayList<>(index.keySet());
  }

  public List<Edge> getEdges() {
    List<Edge> edges = new ArrayList<>();
    for (int r = 0; r < n; r++) {
      for (int c = r + 1; c < n; c++) {
        if (!connectivityAdjMatrix[r][c]) continue;

        Point start = reverseIndex.get(r);
        Point end = reverseIndex.get(c);

        edges.add(new Edge(start, end));
      }
    }

    return edges;
  }
}
