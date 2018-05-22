package graphlayout.graphic;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Giuliano Marinelli
 * @param <Edge>
 */
public class Node<Edge> {

    private LinkedList<Edge> edges;
    private ArrayList<String> content;

    public Node() {
        edges = new LinkedList<>();
    }

    public Node(ArrayList<String> content) {
        this();
        this.content = content;
    }

    public Node(LinkedList<Edge> edges, ArrayList<String> content) {
        this();
        this.edges = edges;
        this.content = content;
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

    public ArrayList<String> getContent() {
        return content;
    }

    public void setContent(ArrayList<String> content) {
        this.content = content;
    }

    public void addContent(String info) {
        content.add(info);
    }

    public int getDegree() {
        return edges.size();
    }
}
