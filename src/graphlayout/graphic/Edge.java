package graphlayout.graphic;

import java.util.ArrayList;

/**
 *
 * @author Giuliano
 */
public class Edge {

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

}
