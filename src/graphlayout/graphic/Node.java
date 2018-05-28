package graphlayout.graphic;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Giuliano Marinelli
 * @param <Edge>
 */
public class Node<Edge> implements Serializable {

    private LinkedList<Edge> edges;
    private HashMap<String, String> content;

    public Node() {
        edges = new LinkedList<>();
    }

    public Node(HashMap<String, String> content) {
        this();
        this.content = content;
    }

    public Node(LinkedList<Edge> edges, HashMap<String, String> content) {
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

    public HashMap<String, String> getContent() {
        return content;
    }

    public void setContent(HashMap<String, String> content) {
        this.content = content;
    }

    public int getDegree() {
        return edges.size();
    }

    /*@Override
     protected Object clone() throws CloneNotSupportedException {
     Node<Edge> cloned = (Node<Edge>) super.clone();
     cloned.setEdges((LinkedList<Edge>) edges.clone());
     cloned.setContent((ArrayList<String>) content.clone());
     return cloned;
     }*/
    @Override
    public String toString() {
        //PARA PROBAR
        return content.get("name");//+"|"+content.get(2)+"|"+content.get(3);
    }
}
