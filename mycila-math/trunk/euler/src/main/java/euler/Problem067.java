package euler;

import com.mycila.Matrix;
import org.jgrapht.alg.BellmanFordShortestPath;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * http://projecteuler.net/index.php?section=problems&id=67
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem067 {

    public static void main(String[] args) throws Exception {

        long time = System.currentTimeMillis();

        // SAME PROBLEM AS 18

        // create the matrix from the triangle
        Matrix<Integer> matrix = readTriangle(new File("data/triangle.txt"));

        // create the graph
        DirectedAcyclicGraph<String, Edge> dag = createDAG(matrix);

        // get the shortest path, wich will be the longest path since we negated the weights
        BellmanFordShortestPath<String, Edge> path = new BellmanFordShortestPath<String, Edge>(dag, "start");
        System.out.println("SUM: " + (int) -path.getCost("(0,0)") + " in " + (System.currentTimeMillis() - time) + "ms");

        // display the path
        StringBuilder sb = new StringBuilder();
        for (Edge edge : path.getPathEdgeList("(0,0)")) {
            sb.append((int) -edge.getWeight()).append(" ");
        }
        System.out.println("PATH: " + sb);
    }

    private static DirectedAcyclicGraph<String, Edge> createDAG(Matrix<Integer> matrix) throws DirectedAcyclicGraph.CycleFoundException {
        DirectedAcyclicGraph<String, Edge> dag = new DirectedAcyclicGraph<String, Edge>(Edge.class);
        dag.addVertex("start");
        for (int row = matrix.rowCount() - 1; row >= 0; row--)
            for (int col = 0; col < matrix.columnCount() && matrix.isSet(row, col); col++) {
                String vertex = "(" + row + "," + col + ")";
                dag.addVertex(vertex);
                if (row == matrix.rowCount() - 1)
                    dag.setEdgeWeight(dag.addDagEdge("start", vertex), -matrix.get(row, col).doubleValue());
                else {
                    dag.setEdgeWeight(dag.addDagEdge("(" + (row + 1) + "," + col + ")", vertex), -matrix.get(row, col).doubleValue());
                    dag.setEdgeWeight(dag.addDagEdge("(" + (row + 1) + "," + (col + 1) + ")", vertex), -matrix.get(row, col).doubleValue());
                }
            }
        return dag;
    }

    private static Matrix<Integer> readTriangle(File triangle) throws FileNotFoundException {
        Matrix<Integer> matrix = new Matrix<Integer>(100, 100);
        Scanner lines = new Scanner(triangle);
        lines.useDelimiter("\\n");
        for (int row = 0; lines.hasNext(); row++) {
            Scanner numbers = new Scanner(lines.next());
            numbers.useDelimiter("\\s");
            for (int col = 0; numbers.hasNext(); col++) {
                matrix.set(row, col, numbers.nextInt());
            }
        }
        return matrix;
    }

    public static class Edge extends DefaultWeightedEdge {
        @Override
        public double getWeight() {
            return super.getWeight();
        }

        @Override
        public String getSource() {
            return (String) super.getSource();
        }

        @Override
        public String getTarget() {
            return (String) super.getTarget();
        }
    }
}