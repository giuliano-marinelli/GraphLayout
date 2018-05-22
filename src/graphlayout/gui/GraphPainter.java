package graphlayout.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.LinkedList;
import javax.swing.*;
import graphlayout.graphic.*;
import graphlayout.Reorganizer;
import static graphlayout.Reorganizer.graphCrossingNumber;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Giuliano Marinelli
 */
public class GraphPainter extends JFrame implements ActionListener {

    private Graph graph;
    private JPanel pnl = new JPanel();
    private JButton btnRandomGraph = new JButton("Random");
    private JButton btnInvertNodes = new JButton("InvertN");
    private JButton btnInvertCurve = new JButton("InvertC");
    private JButton btnGeneticOpt = new JButton("Genetic");
    private JTextField tfInvertOne = new JTextField("1");
    private JTextField tfInvertTwo = new JTextField("2");
    private JTextField tfAmountNodes = new JTextField("5");

    public GraphPainter(Graph graph) {
        this.graph = graph;
        setTitle("Graph Painter");
        setSize(600, 700);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        btnRandomGraph.setBounds(15, 10, 85, 20);
        btnRandomGraph.addActionListener(this);
        btnInvertNodes.setBounds(115, 10, 85, 20);
        btnInvertNodes.addActionListener(this);
        btnInvertCurve.setBounds(115, 35, 85, 20);
        btnInvertCurve.addActionListener(this);
        btnGeneticOpt.setBounds(300, 10, 85, 20);
        btnGeneticOpt.addActionListener(this);
        tfInvertOne.setBounds(205, 10, 20, 20);
        tfInvertTwo.setBounds(230, 10, 20, 20);
        tfAmountNodes.setBounds(205, 35, 45, 20);
        tfAmountNodes.setText("" + graph.getNodes().size());

        pnl.setBounds(0, 0, 600, 100);
        pnl.setLayout(null);
        pnl.add(btnRandomGraph);
        pnl.add(btnInvertNodes);
        pnl.add(btnInvertCurve);
        pnl.add(btnGeneticOpt);
        pnl.add(tfInvertOne);
        pnl.add(tfInvertTwo);
        pnl.add(tfAmountNodes);
        add(pnl);
    }

    public GraphPainter(Graph graph, Component c) {
        this(graph);
        c.setLocation(c.getX() - 300, c.getY());
        setLocationRelativeTo(c);
        setLocation(getX() + 600, getY());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRandomGraph) {
            graph = Reorganizer.generateRandomGraph(Integer.parseInt(tfAmountNodes.getText()));
            Reorganizer.spiralOptimization(graph);
            /* if (graphCrossingNumber(graph) == 1 && graph.getNodes().size() == 5) {
                Reorganizer.swapNodesOrder(graph, 1, 2);
                repaint();
                System.out.println("CROSSING NUMBER RANDOM INVERT NODES (1,2) = " + graphCrossingNumber(graph));
            } else {*/
            System.out.println("CROSSING NUMBER RANDOM = " + graphCrossingNumber(graph));
            //}
            repaint();
        } else if (e.getSource() == btnInvertNodes) {
            Reorganizer.swapNodesOrder(graph, Integer.parseInt(tfInvertOne.getText()), Integer.parseInt(tfInvertTwo.getText()));
            System.out.println("CROSSING NUMBER INVERT NODES = " + graphCrossingNumber(graph));
            repaint();
        } else if (e.getSource() == btnInvertCurve) {
            Reorganizer.invertCurve(graph, Integer.parseInt(tfInvertOne.getText()), Integer.parseInt(tfInvertTwo.getText()));
            System.out.println("CROSSING NUMBER INVERT CURVE = " + graphCrossingNumber(graph));
            repaint();
        } else if (e.getSource() == btnGeneticOpt) {
            try {
                Reorganizer.geneticOptimization(graph);
                System.out.println("CROSSING NUMBER GENETIC OPTIMIZATION = " + graphCrossingNumber(graph));
                repaint();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(GraphPainter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void paint(Graphics g) {
        g.clearRect(0, 100, (int) getWidth(), (int) getHeight());

        LinkedList<Color> degreeColors = new LinkedList<>();
        degreeColors.add(Color.GREEN);
        degreeColors.add(Color.YELLOW);
        degreeColors.add(Color.ORANGE);
        degreeColors.add(Color.RED);
        degreeColors.add(Color.BLUE);
        degreeColors.add(Color.decode("#9900FF"));

        int degree = 0;
        for (Color degreeColor : degreeColors) {
            g.setColor(degreeColor);
            g.drawLine(30, 80 + (degree * 20), 100, 80 + (degree * 20));
            g.drawString(degree + 1 + "", 15, 80 + (degree * 20));
            degree++;
        }

        LinkedList<NodeRectangle> nodes = graph.getNodes();
        LinkedList<EdgeMultiline> edges = graph.getEdges();
        for (EdgeMultiline edge : edges) {
            int degreeOne = edge.getNodeOne().getDegree();
            int degreeTwo = edge.getNodeTwo().getDegree();
            int dominantDegree = Math.max(degreeOne, degreeTwo);
            //int dominantDegree = Reorganizer.edgeCrossingNumber(graph, edge);
            switch (dominantDegree) {
                case 1:
                    g.setColor(degreeColors.get(0));
                    break;
                case 2:
                    g.setColor(degreeColors.get(1));
                    break;
                case 3:
                    g.setColor(degreeColors.get(2));
                    break;
                case 4:
                    g.setColor(degreeColors.get(3));
                    break;
                case 5:
                    g.setColor(degreeColors.get(4));
                    break;
                case 6:
                    g.setColor(degreeColors.get(5));
                    break;
                default:
                    g.setColor(Color.BLACK);
                    break;
            }

            boolean isTop = ((edge.getContent().get(1).equals("1")));
            int ordEdgeNodeOne = Integer.parseInt(edge.getNodeOne().getContent().get(3) + "");
            int ordEdgeNodeTwo = Integer.parseInt(edge.getNodeTwo().getContent().get(3) + "");
            int distance = Math.abs(ordEdgeNodeOne - ordEdgeNodeTwo) * getWidth() / (nodes.size() + 1);
            int x = (Math.min(ordEdgeNodeOne, ordEdgeNodeTwo) + 1) * getWidth() / (nodes.size() + 1);
            int y = (getHeight() / 2) - (distance / 2);
            int startAng = 0;
            int topInvert = 1;
            if (!isTop) {
                startAng = 180;
                topInvert = -1;
            }

            if (Math.abs(ordEdgeNodeOne - ordEdgeNodeTwo) < 2) {
                g.drawLine(x, (getHeight() / 2), x + getWidth() / (nodes.size() + 1), (getHeight() / 2));
            } else {
                g.drawArc(x, y, distance, distance, startAng, 180);

                LinkedList<EdgeMultiline> crossEdges = Reorganizer.crossingEdges(graph, edge);

                for (EdgeMultiline crossEdge : crossEdges) {
                    //System.out.println("-----------");
                    boolean crossIsTop = ((crossEdge.getContent().get(1).equals("1")));
                    int crossOrdEdgeNodeOne = Integer.parseInt(crossEdge.getNodeOne().getContent().get(3) + "");
                    int crossOrdEdgeNodeTwo = Integer.parseInt(crossEdge.getNodeTwo().getContent().get(3) + "");
                    int crossDistance = Math.abs(crossOrdEdgeNodeOne - crossOrdEdgeNodeTwo) * getWidth() / (nodes.size() + 1);
                    int crossX = (Math.min(crossOrdEdgeNodeOne, crossOrdEdgeNodeTwo) + 1) * getWidth() / (nodes.size() + 1);
                    int crossY = (getHeight() / 2) - (crossDistance / 2);

                    double p0 = (x + distance / 2);
                    //System.out.println("P0=" + x + "+" + distance / 2 + "=" + p0);
                    double p1 = (crossX + crossDistance / 2);
                    //System.out.println("P1=" + crossX + "+" + crossDistance / 2 + "=" + p0);
                    double d = Math.abs(p0 - p1);
                    //System.out.println("d=" + d);
                    double a = (Math.pow(distance / 2, 2) - Math.pow(crossDistance / 2, 2) + Math.pow(d, 2)) / (2 * d);
                    //System.out.println("a=" + a);
                    double p2 = p0 + a * (p1 - p0) / d;
                    //System.out.println("P2=" + p2);
                    double h = Math.sqrt(Math.pow(distance / 2, 2) - Math.pow(a, 2));

                    g.setColor(Color.BLACK);
                    g.drawOval((int) p2 - 5, (int) ((getHeight() / 2) - h * topInvert - 5), 10, 10);
                }
            }

            int i = 1;
            for (NodeRectangle node : nodes) {
                g.setColor(Color.BLACK);
                g.drawOval(i * getWidth() / (nodes.size() + 1) - 7, getHeight() / 2 - 7, 14, 14);
                g.drawString((String) node.getContent().get(3), i * getWidth() / (nodes.size() + 1) - 3, getHeight() / 2 + 4);
                i++;
            }

        }
    }

}
