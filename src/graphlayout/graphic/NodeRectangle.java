package graphlayout.graphic;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Giuliano
 */
public class NodeRectangle<Edge> extends Node {

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

}
