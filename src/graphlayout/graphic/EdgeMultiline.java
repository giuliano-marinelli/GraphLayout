package graphlayout.graphic;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Giuliano
 */
public class EdgeMultiline extends Edge {

    LinkedList<Line2D> lines;

    public EdgeMultiline(LinkedList<Line2D> lines) {
        this.lines = lines;
    }

    public EdgeMultiline(LinkedList<Line2D> lines, ArrayList<String> content) {
        super(content);
        this.lines = lines;
    }

    public EdgeMultiline(LinkedList<Line2D> lines, Node nodeOne, Node nodeTwo, ArrayList<String> content) {
        super(nodeOne, nodeTwo, content);
        this.lines = lines;
    }

    public LinkedList<Line2D> getLines() {
        return lines;
    }

    public void setLines(LinkedList<Line2D> lines) {
        this.lines = lines;
    }

}
