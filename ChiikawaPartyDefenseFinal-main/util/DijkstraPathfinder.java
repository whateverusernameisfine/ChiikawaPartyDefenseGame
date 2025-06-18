package util;

import java.awt.Point;
import java.util.*;

public class DijkstraPathfinder {

    private static final int[] DX = {-1, 1, 0, 0}; // left, right, up, down
    private static final int[] DY = {0, 0, -1, 1};

    public static List<Point> findPath(Point start, Point goal, int[][] costGrid) {
        int rows = costGrid.length;
        int cols = costGrid[0].length;

        int[][] dist = new int[rows][cols];
        Point[][] prev = new Point[rows][cols];
        boolean[][] visited = new boolean[rows][cols];

        for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
        dist[start.y][start.x] = 0;

        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.cost));
        pq.offer(new Node(start.x, start.y, 0));

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            int cx = current.x;
            int cy = current.y;

            if (visited[cy][cx]) continue;
            visited[cy][cx] = true;

            if (cx == goal.x && cy == goal.y) break;

            for (int d = 0; d < 4; d++) {
                int nx = cx + DX[d];
                int ny = cy + DY[d];

                if (nx >= 0 && ny >= 0 && nx < cols && ny < rows) {
                    int tileCost = costGrid[ny][nx];
                    if (tileCost == Integer.MAX_VALUE) continue; // Unwalkable

                    int newCost = dist[cy][cx] + tileCost;
                    if (newCost < dist[ny][nx]) {
                        dist[ny][nx] = newCost;
                        prev[ny][nx] = new Point(cx, cy);
                        pq.offer(new Node(nx, ny, newCost));
                    }
                }
            }
        }

        // Reconstruct path
        List<Point> path = new ArrayList<>();
        Point current = goal;
        while (current != null && !current.equals(start)) {
            path.add(0, current);
            current = prev[current.y][current.x];
        }
        if (current != null) path.add(0, start);
        return path;
    }

    private static class Node {
        int x, y, cost;

        public Node(int x, int y, int cost) {
            this.x = x;
            this.y = y;
            this.cost = cost;
        }
    }
}
