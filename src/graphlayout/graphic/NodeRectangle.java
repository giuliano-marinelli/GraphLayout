package graphlayout.graphic;

import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Giuliano Marinelli
 * @param <Edge>
 */
public class NodeRectangle<Edge> extends Node implements Serializable {

    private Rectangle rectangle;

    public NodeRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public NodeRectangle(Rectangle rectangle, ArrayList content) {
        super(content);
        this.rectangle = rectangle;
    }

    public NodeRectangle(Rectangle rectangle, LinkedList edges, ArrayList content) {
        super(edges, content);
        this.rectangle = rectangle;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    /*@Override
    protected Object clone() throws CloneNotSupportedException {
        NodeRectangle<Edge> cloned = (NodeRectangle<Edge>) super.clone();
        cloned.setRectangle((Rectangle) rectangle.clone());
        return cloned;
    }*/
}
