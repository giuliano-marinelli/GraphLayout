package graphlayout.graphic;

import java.util.LinkedList;

/**
 *
 * @author Giuliano
 * @param <Node>
 * @param <Edge>
 */
public class Graph<Node, Edge> {

    private LinkedList<Node> nodes;
    private LinkedList<Edge> edges;

    public Graph() {
        nodes = new LinkedList<>();
        edges = new LinkedList<>();
    }

    public Graph(LinkedList<Node> nodes, LinkedList<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    public LinkedList<Node> getNodes() {
        return nodes;
    }

    public void setNodes(LinkedList<Node> nodes) {
        this.nodes = nodes;
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public LinkedList<Edge> getEdges() {
        return edges;
    }

    public void setEdges(LinkedList<Edge> edges) {
        this.edges = edges;
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }
    
}
