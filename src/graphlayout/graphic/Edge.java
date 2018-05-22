package graphlayout.graphic;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Giuliano Marinelli
 */
public class Edge implements Serializable {

    private Node nodeOne;
    private Node nodeTwo;
    private ArrayList<String> content;

    public Edge() {
    }

    public Edge(ArrayList<String> content) {
        this.content = content;
    }

    public Edge(Node nodeOne, Node nodeTwo, ArrayList<String> content) {
        this.nodeOne = nodeOne;
        this.nodeTwo = nodeTwo;
        this.content = content;
    }

    public Node getNodeOne() {
        return nodeOne;
    }

    public void setNodeOne(Node nodeOne) {
        this.nodeOne = nodeOne;
    }

    public Node getNodeTwo() {
        return nodeTwo;
    }

    public void setNodeTwo(Node nodeTwo) {
        this.nodeTwo = nodeTwo;
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
        return Math.max(nodeOne.getDegree(), nodeTwo.getDegree());
    }

    /*@Override
    protected Object clone() throws CloneNotSupportedException {
        Edge cloned = (Edge) super.clone();
        cloned.setNodeOne((Node) nodeOne.clone());
        cloned.setNodeTwo((Node) nodeTwo.clone());
        cloned.setContent((ArrayList<String>) content.clone());
        return cloned;
    }*/
}
