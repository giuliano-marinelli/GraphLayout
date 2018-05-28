package graphlayout.graphic;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author Giuliano Marinelli
 */
public class Edge implements Serializable {

    private Node nodeOne;
    private Node nodeTwo;
    private HashMap<String, String> content;

    public Edge() {
    }

    public Edge(HashMap<String, String> content) {
        this.content = content;
    }

    public Edge(Node nodeOne, Node nodeTwo, HashMap<String, String> content) {
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

    public HashMap<String, String> getContent() {
        return content;
    }

    public void setContent(HashMap<String, String> content) {
        this.content = content;
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
