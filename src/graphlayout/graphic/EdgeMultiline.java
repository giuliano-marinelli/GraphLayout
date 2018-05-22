package graphlayout.graphic;

import java.awt.geom.Line2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Giuliano Marinelli
 */
public class EdgeMultiline extends Edge implements Serializable {

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

    /*@Override
    protected Object clone() throws CloneNotSupportedException {
        EdgeMultiline cloned = (EdgeMultiline) super.clone();
        cloned.setLines((LinkedList<Line2D>) lines.clone());
        return cloned;
    }*/
}
