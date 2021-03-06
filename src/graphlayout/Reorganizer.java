package graphlayout;

import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import graphlayout.graphic.*;
import graphlayout.gui.GraphPainter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import tecladoIn.TecladoIn;

/**
 *
 * @author Giuliano Marinelli
 */
public abstract class Reorganizer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //TESTEO DE CLONACION
        /*try {
         Graph<NodeRectangle, EdgeMultiline> g1 = generateCompleteGraph(3);
         linearOptimization(g1);

         Graph<NodeRectangle, EdgeMultiline> g2 = generateCompleteGraph(4);
         linearOptimization(g2);

         Graph<NodeRectangle, EdgeMultiline> c1 = (Graph<NodeRectangle, EdgeMultiline>) copy(g1);

         System.out.println("g1=" + g1.getNodes().toString() + "\nc1=" + c1.getNodes().toString() + "\ng2=" + g2.getNodes().toString());

         System.out.println("SWAP 0,2 g2");
         swapNodesOrder(g2, 0, 2);
         System.out.println("g1=" + g1.getNodes().toString() + "\nc1=" + c1.getNodes().toString() + "\ng2=" + g2.getNodes().toString());

         System.out.println("SWAP 1,2 c1");
         swapNodesOrder(c1, 1, 2);
         System.out.println("g1=" + g1.getNodes().toString() + "\nc1=" + c1.getNodes().toString() + "\ng2=" + g2.getNodes().toString());
         } catch (IOException ex) {
         Logger.getLogger(Reorganizer.class.getName()).log(Level.SEVERE, null, ex);
         } catch (ClassNotFoundException ex) {
         Logger.getLogger(Reorganizer.class.getName()).log(Level.SEVERE, null, ex);
         }*/
        Document xml = Reorganizer.reorganize("src/files/example.uxf");
        /*try {
         XMLOutputter xmlOutput = new XMLOutputter();
         xmlOutput.setFormat(Format.getPrettyFormat());
         xmlOutput.output(xml, new FileWriter("src/files/nuevo.uxf"));
         } catch (IOException ex) {
         System.err.println("A problem in the creation of XML (.uxf) file.");
         Logger.getLogger(ERExporter.class.getName()).log(Level.SEVERE, null, ex);
         }*/
    }

    public static Document reorganize(String file) {
        Document xml = null;
        /*for (int i = 5; i <= 25; i++) {
         System.out.print("K_" + i + " -> ");

         Graph graph = generateCompleteGraph(i);
         linearOptimization(graph);
         int obtCr = graphCrossingNumber(graph);

         int cr = completeGraphCrossingNumber(i);

         if (cr == obtCr) {
         System.out.println("CROSSING NUMBER = " + obtCr + " = " + cr);
         } else {
         System.out.println((char) 27 + "[31m" + "CROSSING NUMBER = " + obtCr + " != " + cr);
         }
         for (int j = 0; j < 10; j++) {
         Graph randGraph = generateRandomGraph(i);
         linearOptimization(randGraph);
         int randCr = graphCrossingNumber(randGraph);
         if (randCr <= cr) {
         System.out.println("    CROSSING NUMBER RAND = " + randCr + " <= " + cr);
         } else {
         System.out.println((char) 27 + "[31m" + "    CROSSING NUMBER RAND = " + randCr + " > " + cr);
         }
         }
         }*/

        int numNodes = 5;
        //Graph graphFile = importGraph(file);
        //Graph graphComp = generateCompleteGraph(numNodes);
        Graph graphRandom = generateRandomGraph(numNodes);
        //linearOptimization(graphFile);
        //linearOptimization(graphComp);
        linearOptimization(graphRandom);
        //System.out.println("CROSSING NUMBER FILE = " + graphCrossingNumber(graphFile));
        //System.out.println("CROSSING NUMBER COMPLETE = " + graphCrossingNumber(graphComp));
        //System.out.println("CROSSING NUMBER RANDOM = " + graphCrossingNumber(graphRandom));
        //GraphPainter graphPainterFile = new GraphPainter(graphFile);
        GraphPainter graphPainterRandom = new GraphPainter(graphRandom);
        //GraphPainter graphPainterComp = new GraphPainter(graphComp);
        //GraphPainter graphPainterComp = new GraphPainter(graphComp, graphPainterFile);
        //GraphPainter graphPainterComp = new GraphPainter(graphComp, graphPainterRandom);
        /*if (reorganize(graphFile)) {
         System.out.println("Graph reorganized.");
         xml = exportGraph(graphFile);
         } else {
         System.err.println("There was a mistake.");
         }*/
        return xml;
    }

    public static boolean reorganize(Graph<NodeRectangle, EdgeMultiline> graph) {
        boolean result;
        if (result = !nodesCollide(graph)) {
            alignEdgePoints(graph);
        } else {
            System.err.println("Nodes of the graph must not collide.");
        }
        return result;
    }

    public static void linearOptimization(Graph<NodeRectangle, EdgeMultiline> graph) {
        int cross = 0;
        LinkedList<NodeRectangle> nodes = graph.getNodes();
        //ordena la lista de nodos por su grado
        Collections.sort(nodes, new Comparator<NodeRectangle>() {
            @Override
            public int compare(NodeRectangle o1, NodeRectangle o2) {
                int result = 0;
                /*if (o1.getEdges().size() < o2.getEdges().size()) {
                 result = -1;
                 } else if (o1.getEdges().size() > o2.getEdges().size()) {
                 result = 1;
                 }*/
                if (getNodeLevel(o1) < getNodeLevel(o2)) {
                    result = -1;
                } else if (getNodeLevel(o1) > getNodeLevel(o2)) {
                    result = 1;
                }
                return result;
            }
        });
        int size = nodes.size();
        LinkedList<NodeRectangle> nodesAux = new LinkedList<>();
        //si es par arranca por abajo
        boolean isTop = (nodes.size() % 2 != 0);
        LinkedList<EdgeMultiline> edges;
        //ordena la lista con los nodos de mayor grado a los extremos
        //e indica a cada arco si va por arriba o por abajo
        for (NodeRectangle<EdgeMultiline> node : nodes) {
            //System.out.println(node.getEdges().size());
            edges = node.getEdges();
            for (EdgeMultiline edge : edges) {
                //System.out.println(edge.getContent().toString());
                //if (edge.getContent().size() == 1) {
                edge.getContent().put("isTop", (isTop) ? "1" : "0");
                //} else {
                //edge.getContent().put("isTop", ((isTop) ? "1" : "0"));
                //}
            }
            if (isTop) {
                nodesAux.addFirst(node);
                node.getContent().put("isTop", "1");
                isTop = false;
            } else {
                nodesAux.addLast(node);
                node.getContent().put("isTop", "1");
                isTop = true;
            }
        }
        nodes = nodesAux;
        int i = 0;
        //asigna a los nodos un orden posicional
        for (NodeRectangle node : nodes) {
            node.getContent().put("order", i + "");
            i++;
        }

        graph.setNodes(nodes);

        /*for (NodeRectangle node : nodes) {
         System.out.print(node.getContent().get("name"));
         System.out.print(" -> I(" + nodes.indexOf(node) + ")");
         System.out.print(" -> O(" + node.getContent().get("order") + ")");
         System.out.print(" -> EDG(" + node.getEdges().size() + ")");
         System.out.println(" -> " + ((node.getContent().get("isTop").equals("1")) ? "arriba" : "abajo"));

         edges = node.getEdges();
         for (EdgeMultiline edge : edges) {
         System.out.print("    [" + edge.getContent().get("name"));
         System.out.println(", " + ((edge.getContent().get("isTop").equals("1")) ? "arriba" : "abajo") + "]");
         }
         }*/
        //optimizaciones
        firstLinearOptimization(graph);
        //secondOptimization(graph);
    }

    public static void firstLinearOptimization(Graph<NodeRectangle, EdgeMultiline> graph) {
        LinkedList<NodeRectangle> nodes = graph.getNodes();
        int numNodes = nodes.size();
        int optimizeNumber = (numNodes > 5) ? ((numNodes / 2) - 2) : 0;
        int indexLeft = 0;
        int indexRight = nodes.size() - 1;
        int numChanges = optimizeNumber;
        boolean isTop = true;
        LinkedList<EdgeMultiline> edges;
        int crossNum = graphCrossingNumber(graph);
        int newCrossNum;
        //System.out.println("CAMBIO:");
        for (int i = 0; i < optimizeNumber; i++) {
            if (isTop) {
                edges = nodes.get(indexLeft).getEdges();
                indexLeft++;
                isTop = false;
            } else {
                edges = nodes.get(indexRight).getEdges();
                indexRight--;
                isTop = true;
            }
            for (EdgeMultiline edge : edges) {
                int ordEdgeNodeOne = Integer.parseInt(edge.getNodeOne().getContent().get("order") + "");
                int ordEdgeNodeTwo = Integer.parseInt(edge.getNodeTwo().getContent().get("order") + "");
                if (Math.abs(ordEdgeNodeOne - ordEdgeNodeTwo) <= numChanges + 1
                        && Math.abs(ordEdgeNodeOne - ordEdgeNodeTwo) > 1) {
                    edge.getContent().put("isTop", ((edge.getContent().get("isTop").equals("1")) ? "0" : "1"));
                    newCrossNum = graphCrossingNumber(graph);
                    if (crossNum < newCrossNum) {
                        edge.getContent().put("isTop", ((edge.getContent().get("isTop").equals("1")) ? "0" : "1"));
                    } else {
                        crossNum = newCrossNum;
                    }
                    //System.out.println("    " + ordEdgeNodeOne + "-" + ordEdgeNodeTwo + "<" + numChanges);
                }
            }
            numChanges--;
        }
    }

    public static void secondLinearOptimization(Graph<NodeRectangle, EdgeMultiline> graph) {
        boolean isTop;
        int crossNum = graphCrossingNumber(graph);
        int newCrossNum;
        LinkedList<EdgeMultiline> edges = graph.getEdges();
        for (EdgeMultiline edgeOne : edges) {
            isTop = edgeOne.getContent().get("isTop").equals("1");
            int ordEdgeOneNodeOne = Integer.parseInt(edgeOne.getNodeOne().getContent().get("order") + "");
            int ordEdgeOneNodeTwo = Integer.parseInt(edgeOne.getNodeTwo().getContent().get("order") + "");
            for (EdgeMultiline edgeTwo : edges) {
                if (edgeTwo.getContent().get("isTop").equals("1") == isTop) {
                    int ordEdgeTwoNodeOne = Integer.parseInt(edgeTwo.getNodeOne().getContent().get("order") + "");
                    int ordEdgeTwoNodeTwo = Integer.parseInt(edgeTwo.getNodeTwo().getContent().get("order") + "");
                    if (Math.min(ordEdgeOneNodeOne, ordEdgeOneNodeTwo) < Math.min(ordEdgeTwoNodeOne, ordEdgeTwoNodeTwo)
                            && Math.max(ordEdgeOneNodeOne, ordEdgeOneNodeTwo) < Math.max(ordEdgeTwoNodeOne, ordEdgeTwoNodeTwo)
                            && Math.max(ordEdgeOneNodeOne, ordEdgeOneNodeTwo) > Math.min(ordEdgeTwoNodeOne, ordEdgeTwoNodeTwo)) {
                        edgeOne.getContent().put("isTop", ((edgeOne.getContent().get("isTop").equals("1")) ? "0" : "1"));
                        newCrossNum = graphCrossingNumber(graph);
                        if (crossNum <= newCrossNum) {
                            edgeOne.getContent().put("isTop", ((edgeOne.getContent().get("isTop").equals("1")) ? "0" : "1"));
                        } else {
                            crossNum = newCrossNum;
                            secondLinearOptimization(graph);
                        }
                    }
                }
            }
        }
    }

    public static Graph geneticOptimization(Graph<NodeRectangle, EdgeMultiline> graph) throws CloneNotSupportedException {
        //return firstGeneticOptimization(graph);
        return secondGeneticOptimization(graph);
    }

    private static Graph firstGeneticOptimization(Graph<NodeRectangle, EdgeMultiline> graph) throws CloneNotSupportedException {
        //Genera poblacion inicial
        Graph<NodeRectangle, EdgeMultiline> optGraph = graph;
        LinkedList<Graph<NodeRectangle, EdgeMultiline>> population = generatePopulation(graph);
        int generation = 0;
        boolean goal = false;
        do {
            //Los ordena por su fitness (evaluados por crossing number)
            /*Collections.sort(population, new Comparator<Graph>() {
             @Override
             public int compare(Graph o1, Graph o2) {
             int result = 0;
             if (graphCrossingNumber(o1) < graphCrossingNumber(o2)) {
             result = -1;
             } else if (graphCrossingNumber(o1) > graphCrossingNumber(o2)) {
             result = 1;
             }
             return result;
             }
             });*/
            //Calcula el fitness total de la poblacion
            /*int fitnessSum = 0;
             for (Graph<NodeRectangle, EdgeMultiline> individual : population) {
             fitnessSum += graphCrossingNumber(individual);
             }*/
            //Selecciona a la mitad de la poblacion con mayor fitness para ser padres
            /*LinkedList<Graph<NodeRectangle, EdgeMultiline>> parents = new LinkedList<>();
             for (int i = 0; i < population.size() / 2; i++) {
             parents.add(population.get(i));
             }*/
            Random random = new Random();
            int i = 0;
            Graph<NodeRectangle, EdgeMultiline> individual;
            while (i < population.size() && !goal) {
                individual = population.get(i);
                if (random.nextDouble() < 0.3) {
                    nodeOrderMutation(individual);
                }
                if (random.nextDouble() < 0.1) {
                    edgeCurveMutation(individual);
                }
                //System.out.println("    Individual CN: " + graphCrossingNumber(individual));
                if (graphCrossingNumber(individual) == 0) {
                    goal = true;
                    System.out.println(graph.getNodes().toString());
                    optGraph = individual;

                    System.out.println("    Goal: " + individual.getNodes().toString());
                }
                i++;
            }
            generation++;
        } while (generation < 1000 && !goal);
        if (!goal) {
            //Los ordena por su fitness (evaluados por crossing number)
            Collections.sort(population, new Comparator<Graph>() {
                @Override
                public int compare(Graph o1, Graph o2) {
                    int result = 0;
                    if (graphCrossingNumber(o1) < graphCrossingNumber(o2)) {
                        result = -1;
                    } else if (graphCrossingNumber(o1) > graphCrossingNumber(o2)) {
                        result = 1;
                    }
                    return result;
                }
            });
            //System.out.println("    first " + graphCrossingNumber(population.getFirst()) + " last " + graphCrossingNumber(population.getLast()));
            //System.out.println("    graph " + graphCrossingNumber(graph) + " individual " + graphCrossingNumber(population.getFirst()));
            if (graphCrossingNumber(graph) > graphCrossingNumber(population.getFirst())) {
                optGraph = population.getFirst();
            }
        }
        System.out.println("    Population size: " + population.size());
        System.out.println("    Best: " + optGraph.getNodes().toString());
        System.out.println("    Generations: " + generation);
        return optGraph;
    }

    private static Graph secondGeneticOptimization(Graph<NodeRectangle, EdgeMultiline> graph) throws CloneNotSupportedException {
        //Genera poblacion inicial
        Graph<NodeRectangle, EdgeMultiline> optGraph = graph;
        int generation = 0;
        boolean goal = false;
        LinkedList<Graph<NodeRectangle, EdgeMultiline>> population;
        do {
            population = generatePopulation(optGraph);
            Random random = new Random();
            int i = 0;
            Graph<NodeRectangle, EdgeMultiline> individual;
            while (i < population.size() && !goal) {
                individual = population.get(i);
                if (random.nextDouble() < 0.3) {
                    nodeOrderMutation(individual);
                }
                if (random.nextDouble() < 0.1) {
                    edgeCurveMutation(individual);
                }
                //System.out.println("    Individual CN: " + graphCrossingNumber(individual));
                if (graphCrossingNumber(individual) == 0) {
                    goal = true;
                    optGraph = individual;
                    System.out.println("    Goal: " + individual.getNodes().toString());
                }
                i++;
            }
            if (!goal) {
                //Los ordena por su fitness (evaluados por crossing number)
                Collections.sort(population, new Comparator<Graph>() {
                    @Override
                    public int compare(Graph o1, Graph o2) {
                        int result = 0;
                        if (graphCrossingNumber(o1) < graphCrossingNumber(o2)) {
                            result = -1;
                        } else if (graphCrossingNumber(o1) > graphCrossingNumber(o2)) {
                            result = 1;
                        }
                        return result;
                    }
                });
                if (graphCrossingNumber(graph) > graphCrossingNumber(population.getFirst())) {
                    optGraph = population.getFirst();
                }
            }
            generation++;
        } while (generation < 100 && !goal);
        System.out.println("    Population size: " + population.size());
        System.out.println("    Best: " + optGraph.getNodes().toString());
        System.out.println("    Generations: " + generation);
        return optGraph;
    }

    private static LinkedList<Graph<NodeRectangle, EdgeMultiline>> generatePopulation(Graph<NodeRectangle, EdgeMultiline> graph) throws CloneNotSupportedException {
        LinkedList<Graph<NodeRectangle, EdgeMultiline>> population = new LinkedList<>();
        int numNodes = graph.getNodes().size();
        /*CLONACION SE ESPERA QUE NO SE COMPORTE CORRECTAMENTE DEBIDO A LAS REFERENCIAS CRUZADAS*/
        for (int i = 0; i < numNodes; i++) {
            for (int j = i + 1; j < numNodes; j++) {
                try {
                    Graph<NodeRectangle, EdgeMultiline> neighbor = (Graph<NodeRectangle, EdgeMultiline>) copy(graph);
                    swapNodesOrder(graph, i, j);
                    population.add(neighbor);
                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(Reorganizer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return population;
    }

    private static void nodeOrderMutation(Graph<NodeRectangle, EdgeMultiline> graph) {
        Random random = new Random();
        int indexOne = random.nextInt(graph.getNodes().size());
        int indexTwo;
        do {
            indexTwo = random.nextInt(graph.getNodes().size());
        } while (indexOne == indexTwo);
        swapNodesOrder(graph, indexOne, indexTwo);
    }

    private static void edgeCurveMutation(Graph<NodeRectangle, EdgeMultiline> graph) {
        Random random = new Random();
        EdgeMultiline randEdge = graph.getEdges().get(random.nextInt(graph.getEdges().size()));
        invertCurve(graph, Integer.parseInt(randEdge.getNodeOne().getContent().get("order") + ""), Integer.parseInt(randEdge.getNodeTwo().getContent().get("order") + ""));
    }

    public static int graphCrossingNumber(Graph<NodeRectangle, EdgeMultiline> graph) {
        int cross = 0;
        boolean isTop;
        LinkedList<EdgeMultiline> edges = graph.getEdges();
        for (EdgeMultiline edgeOne : edges) {
            isTop = edgeOne.getContent().get("isTop").equals("1");
            int ordEdgeOneNodeOne = Integer.parseInt(edgeOne.getNodeOne().getContent().get("order") + "");
            int ordEdgeOneNodeTwo = Integer.parseInt(edgeOne.getNodeTwo().getContent().get("order") + "");
            for (EdgeMultiline edgeTwo : edges) {
                if (edgeTwo.getContent().get("isTop").equals("1") == isTop) {
                    int ordEdgeTwoNodeOne = Integer.parseInt(edgeTwo.getNodeOne().getContent().get("order") + "");
                    int ordEdgeTwoNodeTwo = Integer.parseInt(edgeTwo.getNodeTwo().getContent().get("order") + "");
                    if (Math.min(ordEdgeOneNodeOne, ordEdgeOneNodeTwo) < Math.min(ordEdgeTwoNodeOne, ordEdgeTwoNodeTwo)
                            && Math.max(ordEdgeOneNodeOne, ordEdgeOneNodeTwo) < Math.max(ordEdgeTwoNodeOne, ordEdgeTwoNodeTwo)
                            && Math.max(ordEdgeOneNodeOne, ordEdgeOneNodeTwo) > Math.min(ordEdgeTwoNodeOne, ordEdgeTwoNodeTwo)) {
                        /*System.out.println(
                         Math.min(ordEdgeOneNodeOne, ordEdgeOneNodeTwo) + "<" + Math.min(ordEdgeTwoNodeOne, ordEdgeTwoNodeTwo)
                         + " && " + Math.max(ordEdgeOneNodeOne, ordEdgeOneNodeTwo) + "<" + Math.max(ordEdgeTwoNodeOne, ordEdgeTwoNodeTwo)
                         + " && " + Math.max(ordEdgeOneNodeOne, ordEdgeOneNodeTwo) + ">" + Math.min(ordEdgeTwoNodeOne, ordEdgeTwoNodeTwo)
                         );
                         System.out.println("    +1 cruce");*/
                        cross++;
                    }
                }
            }
        }
        return cross;
    }

    public static int completeGraphCrossingNumber(int numNodes) {
        return (int) ((1.0 / 4.0) * Math.floor(numNodes / 2) * Math.floor((numNodes - 1) / 2) * Math.floor((numNodes - 2) / 2) * Math.floor((numNodes - 3) / 2));
    }

    public static int edgeCrossingNumber(Graph<NodeRectangle, EdgeMultiline> graph, EdgeMultiline edgeOne) {
        int cross = 0;
        boolean isTop;
        LinkedList<EdgeMultiline> edges = graph.getEdges();
        isTop = edgeOne.getContent().get("isTop").equals("1");
        int ordEdgeOneNodeOne = Integer.parseInt(edgeOne.getNodeOne().getContent().get("order") + "");
        int ordEdgeOneNodeTwo = Integer.parseInt(edgeOne.getNodeTwo().getContent().get("order") + "");
        for (EdgeMultiline edgeTwo : edges) {
            if (edgeTwo.getContent().get("isTop").equals("1") == isTop) {
                int ordEdgeTwoNodeOne = Integer.parseInt(edgeTwo.getNodeOne().getContent().get("order") + "");
                int ordEdgeTwoNodeTwo = Integer.parseInt(edgeTwo.getNodeTwo().getContent().get("order") + "");
                if ((Math.min(ordEdgeOneNodeOne, ordEdgeOneNodeTwo) < Math.min(ordEdgeTwoNodeOne, ordEdgeTwoNodeTwo)
                        && Math.max(ordEdgeOneNodeOne, ordEdgeOneNodeTwo) < Math.max(ordEdgeTwoNodeOne, ordEdgeTwoNodeTwo)
                        && Math.max(ordEdgeOneNodeOne, ordEdgeOneNodeTwo) > Math.min(ordEdgeTwoNodeOne, ordEdgeTwoNodeTwo))
                        || (Math.min(ordEdgeOneNodeOne, ordEdgeOneNodeTwo) > Math.min(ordEdgeTwoNodeOne, ordEdgeTwoNodeTwo)
                        && Math.max(ordEdgeOneNodeOne, ordEdgeOneNodeTwo) > Math.max(ordEdgeTwoNodeOne, ordEdgeTwoNodeTwo)
                        && Math.max(ordEdgeTwoNodeOne, ordEdgeTwoNodeTwo) > Math.min(ordEdgeOneNodeOne, ordEdgeOneNodeTwo))) {
                    cross++;
                }
            }
        }
        return cross;
    }

    public static LinkedList<EdgeMultiline> crossingEdges(Graph<NodeRectangle, EdgeMultiline> graph, EdgeMultiline edgeOne) {
        LinkedList<EdgeMultiline> crossEdges = new LinkedList<>();
        boolean isTop;
        LinkedList<EdgeMultiline> edges = graph.getEdges();
        isTop = edgeOne.getContent().get("isTop").equals("1");
        int ordEdgeOneNodeOne = Integer.parseInt(edgeOne.getNodeOne().getContent().get("order") + "");
        int ordEdgeOneNodeTwo = Integer.parseInt(edgeOne.getNodeTwo().getContent().get("order") + "");
        for (EdgeMultiline edgeTwo : edges) {
            if (edgeTwo.getContent().get("isTop").equals("1") == isTop) {
                int ordEdgeTwoNodeOne = Integer.parseInt(edgeTwo.getNodeOne().getContent().get("order") + "");
                int ordEdgeTwoNodeTwo = Integer.parseInt(edgeTwo.getNodeTwo().getContent().get("order") + "");
                if ((Math.min(ordEdgeOneNodeOne, ordEdgeOneNodeTwo) < Math.min(ordEdgeTwoNodeOne, ordEdgeTwoNodeTwo)
                        && Math.max(ordEdgeOneNodeOne, ordEdgeOneNodeTwo) < Math.max(ordEdgeTwoNodeOne, ordEdgeTwoNodeTwo)
                        && Math.max(ordEdgeOneNodeOne, ordEdgeOneNodeTwo) > Math.min(ordEdgeTwoNodeOne, ordEdgeTwoNodeTwo)) /*|| (Math.min(ordEdgeOneNodeOne, ordEdgeOneNodeTwo) > Math.min(ordEdgeTwoNodeOne, ordEdgeTwoNodeTwo)
                         && Math.max(ordEdgeOneNodeOne, ordEdgeOneNodeTwo) > Math.max(ordEdgeTwoNodeOne, ordEdgeTwoNodeTwo)
                         && Math.max(ordEdgeTwoNodeOne, ordEdgeTwoNodeTwo) > Math.min(ordEdgeOneNodeOne, ordEdgeOneNodeTwo))*/) {
                    crossEdges.add(edgeTwo);
                }
            }
        }
        return crossEdges;
    }

    public static void alignEdgePoints(Graph<NodeRectangle, EdgeMultiline> graph) {
        LinkedList<NodeRectangle> nodes = graph.getNodes();
        LinkedList<EdgeMultiline> edges;
        LinkedList<Line2D> lines;
        Rectangle rectangle;
        Point2D p1;
        Point2D p2;
        Point2D fromPoint;
        Point2D toPoint;
        for (NodeRectangle node : nodes) {
            edges = node.getEdges();
            rectangle = node.getRectangle();
            for (EdgeMultiline edge : edges) {
                lines = edge.getLines();
                p1 = lines.getFirst().getP1();
                p2 = lines.getLast().getP2();
                boolean isP1 = false;
                if (node.getRectangle().contains(p1)) {
                    isP1 = true;
                    fromPoint = lines.getFirst().getP1();
                    toPoint = lines.getLast().getP2();
                } else {
                    fromPoint = lines.getLast().getP2();
                    toPoint = lines.getFirst().getP1();
                }
                double difX = toPoint.getX() - fromPoint.getX();
                double difY = toPoint.getY() - fromPoint.getY();

                System.out.println("ARCO: " + edge.getContent());
                System.out.println("dif=" + Math.abs(Math.abs(difX) - Math.abs(difY)));
                System.out.println("div=max(" + Math.abs(difX) + "," + Math.abs(difY) + ")" + (Math.max(Math.abs(difX), Math.abs(difY)) / 2));
                //if (Math.abs(Math.abs(difX) - Math.abs(difY)) > 0) {//No es diagonal
                if ((Math.abs(difX) < Math.abs(difY))) { //Arriba o abajo
                    if (difY > 0) { //Abajo
                        fromPoint.setLocation(rectangle.getCenterX(), rectangle.getCenterY() + rectangle.getHeight() / 2);
                    } else { //Arriba
                        fromPoint.setLocation(rectangle.getCenterX(), rectangle.getCenterY() - rectangle.getHeight() / 2);
                    }
                } else { //Derecha o izquierda
                    if (difX > 0) { //Derecha
                        fromPoint.setLocation(rectangle.getCenterX() + rectangle.getWidth() / 2, rectangle.getCenterY());
                    } else { //Izquierda
                        fromPoint.setLocation(rectangle.getCenterX() - rectangle.getWidth() / 2, rectangle.getCenterY());
                    }
                }
                /*} else { //Es diagonal
                 if (difY > 0) { //Abajo
                 if (difX > 0) { //Abajo-Derecha
                 fromPoint.setLocation(rectangle.getCenterX() + rectangle.getWidth() / 2, rectangle.getCenterY() + rectangle.getHeight() / 2);
                 } else { //Abajo-Izquierda
                 fromPoint.setLocation(rectangle.getCenterX() - rectangle.getWidth() / 2, rectangle.getCenterY() + rectangle.getHeight() / 2);
                 }
                 } else { //Arriba
                 if (difX > 0) { //Arriba-Derecha
                 fromPoint.setLocation(rectangle.getCenterX() + rectangle.getWidth() / 2, rectangle.getCenterY() - rectangle.getHeight() / 2);
                 } else { //Arriba-Izquierda
                 fromPoint.setLocation(rectangle.getCenterX() - rectangle.getWidth() / 2, rectangle.getCenterY() - rectangle.getHeight() / 2);
                 }
                 }
                 }*/
                System.out.println("fromPoint: " + fromPoint.getX() + "," + fromPoint.getY());
                fromPoint.setLocation(((int) ((fromPoint.getX() + 5) / 10)) * 10, ((int) ((fromPoint.getY() + 5) / 10)) * 10);
                System.out.println("    fromPoint: " + fromPoint.getX() + "," + fromPoint.getY());
                if (isP1) {
                    System.out.println("    ORIG: " + lines.getFirst().getP1().getX() + ","
                            + lines.getFirst().getP1().getY());
                    lines.getFirst().setLine(fromPoint, lines.getFirst().getP2());
                } else {
                    System.out.println("    ORIG: " + lines.getLast().getP2().getX() + ","
                            + lines.getLast().getP2().getY());
                    lines.getLast().setLine(lines.getLast().getP1(), fromPoint);
                }
            }
        }

    }

    public static boolean nodesCollide(Graph<NodeRectangle, EdgeMultiline> graph) {
        LinkedList<NodeRectangle> nodes = graph.getNodes();
        boolean collide = false;
        int i = 0;
        while (collide && i < nodes.size()) {
            int j = 0;
            while (collide && j < nodes.size() && i != j) {
                collide = nodes.get(i).getRectangle().intersects(nodes.get(j).getRectangle());
                j++;
            }
            i++;
        }
        return collide;
    }

    public static Graph importGraph(String file) {
        LinkedList<NodeRectangle> nodes = new LinkedList();
        LinkedList<EdgeMultiline> edges = new LinkedList();
        Graph<NodeRectangle, EdgeMultiline> graph = new Graph(nodes, edges);

        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File(file);

        Document document;
        try {
            document = (Document) builder.build(xmlFile);
            Element rootNode = document.getRootElement();
            List list = rootNode.getChildren("element");

            //Parametros de cada <element> del documento
            String id;
            String type;
            int x;
            int y;
            int w;
            int h;
            String panelAttributes;
            String additAttributes;
            String customCode;

            //Recorrer cada <element>
            for (int i = 0; i < list.size(); i++) {
                Element node = (Element) list.get(i);

                Element coordenadas = node.getChild("coordinates");
                x = Integer.parseInt(coordenadas.getChildText("x"));
                y = Integer.parseInt(coordenadas.getChildText("y"));
                w = Integer.parseInt(coordenadas.getChildText("w"));
                h = Integer.parseInt(coordenadas.getChildText("h"));
                panelAttributes = node.getChildText("panel_attributes");
                additAttributes = node.getChildText("additional_attributes");

                if (node.getChild("type") != null) {
                    type = node.getChild("type").getText();
                } else {
                    type = "";
                }

                if (node.getChild("id") != null) {
                    id = node.getChild("id").getText();
                } else {
                    id = "";
                }

                if (node.getChild("custom_code") != null) {
                    customCode = node.getChild("custom_code").getText();
                } else {
                    customCode = "";
                }

                HashMap<String, String> content = new HashMap<>();
                content.put("attributes", panelAttributes);

                if (id.equals("Relation")) {
                    LinkedList<Line2D> lines = new LinkedList<>();

                    String[] values = additAttributes.split(";");
                    for (int k = 0; k < values.length - 2; k = k + 2) {
                        lines.add(new Line2D.Double(x + Double.valueOf(values[k]), y + Double.valueOf(values[k + 1]),
                                x + Double.valueOf(values[k + 2]), y + Double.valueOf(values[k + 3])));
                    }
                    edges.add(new EdgeMultiline(lines, content));
                } else {
                    if (type.equals("")) {
                        Rectangle rectangle = new Rectangle(x, y, w + 1, h + 1);
                        content.put("name", id);
                        nodes.add(new NodeRectangle(rectangle, content));
                    } else {
                        Rectangle rectangle = new Rectangle(x, y, w, h);
                        content.put("type", type);
                        content.put("customCode", customCode);
                        nodes.add(new NodeRectangle(rectangle, content));
                    }
                }
            }

            for (EdgeMultiline edge : edges) {
                LinkedList<Line2D> lines = edge.getLines();
                Point2D p1 = lines.getFirst().getP1();
                Point2D p2 = lines.getLast().getP2();
//                System.out.println("EDGE: " + edge.getContent()
//                        + " puntos: " + p1
//                        + " y " + p2);
                for (NodeRectangle node : nodes) {
//                    System.out.println("    RECTANGLE: " + node.getRectangle().getBounds());
                    if (node.getRectangle().contains(p1)) {
//                        System.out.println("        COLLIDE P1");
                        node.addEdge(edge);
                        edge.setNodeOne(node);
                    } else if (node.getRectangle().contains(p2)) {
//                        System.out.println("        COLLIDE P2");
                        node.addEdge(edge);
                        edge.setNodeTwo(node);
                    }
                }
            }

        } catch (JDOMException | IOException ex) {
            Logger.getLogger(Reorganizer.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return graph;
    }

    public static Document exportGraph(Graph<NodeRectangle, EdgeMultiline> graph) {
        Element diagram = new Element("diagram").setAttribute("program", "umlet").setAttribute("version", "14.2");
        Document document = new Document(diagram);

        diagram.addContent(new Element("zoom_level").setText("10"));
        for (NodeRectangle<EdgeMultiline> node : graph.getNodes()) {
            Element element = new Element("element");
            diagram.addContent(element);

            boolean isCustom = false;
            if (node.getContent().get("type").equals("CustomElementImpl")) {
                element.addContent(new Element("type").setText(node.getContent().get("type").toString()));
                isCustom = true;
            } else {
                element.addContent(new Element("id").setText(node.getContent().get("type").toString()));
            }
            Element coordinates = new Element("coordinates");
            element.addContent(coordinates);

            coordinates.addContent(new Element("x").setText((int) node.getRectangle().getX() + ""));
            coordinates.addContent(new Element("y").setText((int) node.getRectangle().getY() + ""));

            element.addContent(new Element("panel_attributes").setText(node.getContent().get("attributes").toString()));
            element.addContent(new Element("additional_attributes"));

            if (isCustom) {
                element.addContent(new Element("custom_code").setText(node.getContent().get("customCode").toString()));
                coordinates.addContent(new Element("w").setText((int) node.getRectangle().getWidth() + ""));
                coordinates.addContent(new Element("h").setText((int) node.getRectangle().getHeight() + ""));
            } else {
                coordinates.addContent(new Element("w").setText((int) (node.getRectangle().getWidth() - 1) + ""));
                coordinates.addContent(new Element("h").setText((int) (node.getRectangle().getHeight() - 1) + ""));
            }
        }

        for (EdgeMultiline edge : graph.getEdges()) {
            Element element = new Element("element");
            diagram.addContent(element);

            element.addContent(new Element("id").setText("Relation"));

            Element coordinates = new Element("coordinates");
            element.addContent(coordinates);

            element.addContent(new Element("panel_attributes").setText(edge.getContent().get("attributes")));

            Element additionalAttributes = new Element("additional_attributes");
            element.addContent(additionalAttributes);

            ArrayList<Double> points = new ArrayList<>();
            double maxX = 0;
            double maxY = 0;
            double minX = Integer.MAX_VALUE;
            double minY = Integer.MAX_VALUE;
            double pX;
            double pY;
            for (Line2D line : edge.getLines()) {
                pX = line.getP1().getX();
                pY = line.getP1().getY();
                points.add(pX);
                points.add(pY);
                if (pX > maxX) {
                    maxX = pX;
                }
                if (pY > maxY) {
                    maxY = pY;
                }
                if (pX < minX) {
                    minX = pX;
                }
                if (pY < minY) {
                    minY = pY;
                }
            }
            pX = edge.getLines().getLast().getP2().getX();
            pY = edge.getLines().getLast().getP2().getY();
            points.add(pX);
            points.add(pY);
            if (pX > maxX) {
                maxX = pX;
            }
            if (pY > maxY) {
                maxY = pY;
            }
            if (pX < minX) {
                minX = pX;
            }
            if (pY < minY) {
                minY = pY;
            }

            int coordX = (int) minX - 10;
            int coordY = (int) minY - 10;

            coordinates.addContent(new Element("x").setText(coordX + ""));
            coordinates.addContent(new Element("y").setText(coordY + ""));
            coordinates.addContent(new Element("w").setText((int) maxX - coordX + 20 + ""));
            coordinates.addContent(new Element("h").setText((int) maxY - coordY + 20 + ""));

            String commaPoints = "";
            for (int i = 0; i < points.size() - 1; i++) {
                if (i % 2 == 0) {
                    commaPoints += points.get(i) - coordX + ";";
                } else {
                    commaPoints += points.get(i) - coordY + ";";
                }
            }
            commaPoints += points.get(points.size() - 1) - coordY;

            additionalAttributes.setText(commaPoints);
        }
        return document;
    }

    public static Graph generateCompleteGraph(int numberNodes) {
        Graph completeGraph = new Graph();
        LinkedList<NodeRectangle> nodes = completeGraph.getNodes();
        for (int i = 0; i < numberNodes; i++) {
            HashMap<String, String> nodeContent = new HashMap<>();
            nodeContent.put("name", i + "");
            nodeContent.put("type", "node");
            NodeRectangle newNode = new NodeRectangle(null, nodeContent);
            completeGraph.addNode(newNode);
            for (NodeRectangle node : nodes) {
                if (node != newNode) {
                    HashMap<String, String> edgeContent = new HashMap<>();
                    edgeContent.put("name", node.getContent().get("name") + "->" + newNode.getContent().get("name"));
                    EdgeMultiline newEdge = new EdgeMultiline(null, node, newNode, edgeContent);
                    completeGraph.addEdge(newEdge);
                    node.addEdge(newEdge);
                    newNode.addEdge(newEdge);
                }
            }
        }
        return completeGraph;
    }

    public static Graph generateRandomGraph(int numberNodes) {
        Graph randomGraph = new Graph();
        LinkedList<NodeRectangle> nodes = randomGraph.getNodes();
        for (int i = 0; i < numberNodes; i++) {
            HashMap<String, String> nodeContent = new HashMap<>();
            nodeContent.put("name", i + "");
            nodeContent.put("type", "node");
            NodeRectangle newNode = new NodeRectangle(null, nodeContent);
            randomGraph.addNode(newNode);
        }
        LinkedList<Integer> finishedIndex = new LinkedList<>();
        for (NodeRectangle node : nodes) {
            if (finishedIndex.size() != numberNodes - 1) {
                Random random = new Random();
                //random.nextInt(max - min + 1) + min;
                int amountEdges = random.nextInt(numberNodes - finishedIndex.size());
                LinkedList<Integer> visitedIndex = new LinkedList<>();
                finishedIndex.add(nodes.indexOf(node));
                for (int i = 0; i < amountEdges; i++) {
                    NodeRectangle toNode;
                    int toNodeIndex = -1;
                    do {
                        toNodeIndex = random.nextInt(numberNodes);
                    } while (visitedIndex.contains(toNodeIndex) || finishedIndex.contains(toNodeIndex));
                    visitedIndex.add(toNodeIndex);
                    toNode = nodes.get(toNodeIndex);
                    HashMap<String, String> edgeContent = new HashMap<>();
                    edgeContent.put("name", node.getContent().get("name") + "->" + toNode.getContent().get("name"));
                    EdgeMultiline newEdge = new EdgeMultiline(null, node, toNode, edgeContent);
                    randomGraph.addEdge(newEdge);
                    node.addEdge(newEdge);
                    toNode.addEdge(newEdge);
                }
            }
        }
        return randomGraph;
    }

    public static int getNodeLevel(Node node) {
        LinkedList<Edge> edges = node.getEdges();
        int minDegree = Integer.MAX_VALUE;
        for (Edge edge : edges) {
            minDegree = Math.min(minDegree, edge.getDegree());
        }
        return minDegree;
    }

    public static void swapNodesOrder(Graph<NodeRectangle, EdgeMultiline> graph, int indexOne, int indexTwo) {
        if (indexOne >= 0 && indexOne < graph.getNodes().size()
                && indexTwo >= 0 && indexTwo < graph.getNodes().size()) {
            LinkedList<NodeRectangle> nodes = graph.getNodes();
            Object orderAux = nodes.get(indexTwo).getContent().get("order");
            nodes.get(indexTwo).getContent().put("order", nodes.get(indexOne).getContent().get("order"));
            nodes.get(indexOne).getContent().put("order", orderAux);
            Collections.swap(nodes, indexOne, indexTwo);
        }
    }

    public static void invertCurve(Graph<NodeRectangle, EdgeMultiline> graph, int indexOne, int indexTwo) {
        LinkedList<NodeRectangle> nodes = graph.getNodes();
        LinkedList<EdgeMultiline> edges = graph.getEdges();
        NodeRectangle nodeOne = nodes.get(indexOne);
        NodeRectangle nodeTwo = nodes.get(indexTwo);
        EdgeMultiline targetEdge = null;
        for (EdgeMultiline edge : edges) {
            if ((edge.getNodeOne() == nodeOne && edge.getNodeTwo() == nodeTwo)
                    || (edge.getNodeOne() == nodeTwo && edge.getNodeTwo() == nodeOne)) {
                targetEdge = edge;
            }
        }
        if (targetEdge != null) {
            if (targetEdge.getContent().get("isTop").equals("1")) {
                targetEdge.getContent().put("isTop", "0");
            } else {
                targetEdge.getContent().put("isTop", "1");
            }
        }
    }

    public static Object copy(final Serializable obj) throws IOException, ClassNotFoundException {
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        Object copy = null;

        try {
            // write the object
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            out = new ObjectOutputStream(baos);
            out.writeObject(obj);
            out.flush();

            // read in the copy
            byte data[] = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            in = new ObjectInputStream(bais);
            copy = in.readObject();
        } finally {
            out.close();
            in.close();
        }

        return copy;
    }

}
