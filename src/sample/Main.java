package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import jdk.nashorn.internal.runtime.ConsString;

import java.time.LocalTime;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main extends Application {
    Random rng = new Random();

    private int RNGMIN = 10, RNGMAX = 50;
    @Override
    public void start(Stage primaryStage) throws Exception{
        //rng.setSeed(seed);
        Solution firstSolution = initialization();//Get First Worst Soulution
        System.out.println("RUN");
        List<Integer> emptyList = new ArrayList<>();
        LocalTime start = LocalTime.now();
        SAThread child =  new SAThread(firstSolution,"0.",(int)firstSolution.size(), emptyList ,0); //Create Master Thread

        Future<SolutionAndRecord> futureCall = child.findSmaller();                                                              //Start Thread
        SolutionAndRecord result = futureCall.get();
        Solution finalSolution = result.getSolution();
        if(finalSolution.validSolution()){
            finalSolution.printSolution();
            System.out.println(finalSolution.getNumColours());
            System.out.println(result.printRecords(start).toString());
            drawGraph(finalSolution, primaryStage);
        }
        else{
            System.out.println("Init Solution best solution");
            drawGraph(firstSolution, primaryStage);
        }
        //minimumSolution =

        System.out.println("Fin");
    }

    private Solution initialization(){
        Parameters parameters = getParameters();
        List<String> list = parameters.getRaw();
        int nodes = 0;
        int edges = 0;

        if(list.get(0).equals("Random")){
            nodes = rng.nextInt(RNGMAX) + RNGMIN;
            System.out.println(nodes);
            int edgeMax = ((nodes*nodes)-nodes)/4;
            System.out.println(edgeMax);
            if(edgeMax > 0)
                edges = rng.nextInt(edgeMax);
            else
                edges = 0;
            System.out.println(edges);
        }else if(list.size() == 2 || list.size() == 3){
            nodes = Integer.parseInt(list.get(0));
            edges = Integer.parseInt(list.get(1))-nodes;
        } else if(!list.get(0).contains(".csv"))
            System.err.println("Arguments incorrect");
        Solution initSolution;
        if(list.get(0).contains(".csv")){
            initSolution = new Solution(list.get(0));
        }else {
            initSolution = new Solution(nodes,edges);
        }
        if(list.size() == 3)
            initSolution.setSeed(list.get(2));
        initSolution.printSolution();
        System.out.println("\n"+initSolution.getNumColours());
        return initSolution;
    }

    private void drawGraph(Solution solution, Stage primaryStage){
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Pane pane = new Pane();
        Canvas canvas = new Canvas(1000, 1000);
        pane.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        int numColours = solution.getNumColours();
        int numNodes = (int) solution.size();

        double theta = 360/numNodes;
        double thetaTemp = theta;
        double r = 200;
        double midX = canvas.getWidth()/2;
        double midY = canvas.getHeight()/2;
        double nodeSize = r*Math.sqrt(2-2*Math.cos(theta))/6;  // Find an appropriate size of the nodes.

        // Draw and find the nodes.
        for(Node node : solution.getNodes()) {
             // Known variable.  Final length of vector.
            double x; // Unknown variable. x location.
            double y; // Unknown variable. y Location.
            try {
                // Conversion from polar to Cartesian coordinates.
                y = r*Math.cos(theta);
                x = r*Math.sin(theta);

                // Slide over, so it orbits the center node.
                x += midX;
                y += midY;

                node.setxLoc(x);
                node.setyLoc(y);

                gc.setFill(getColour(node.getColor()));
                gc.fillOval(node.getxLoc(), node.getyLoc(), nodeSize, nodeSize);

                //System.out.println(node.getxLoc() + ", " + node.getyLoc());
                System.out.println(theta + "");
                theta += thetaTemp;

            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // Draw and find the edges.
        for(int i = 0; i < solution.getNodes().size(); i++)
        {
            Node node = solution.getNodes().get(i);
            for (Edge edge : node.getEdges())
            {
                int targetNodeId = Math.max(edge.getNodesID()[0], edge.getNodesID()[1]);
                if(targetNodeId <= i) {
                    continue; // We must have already done this edge previously.
                }
                else {

                    Node targetNode = null;
                    for(Node nodeT : solution.getNodes()) {
                        if (nodeT.getId() == targetNodeId) {
                            targetNode = nodeT;
                            break;
                        }
                    }
                    gc.setStroke(Color.BLACK);
                    gc.setLineWidth(2);
                    gc.strokeLine(node.getxLoc() + nodeSize/2, node.getyLoc() + nodeSize/2, targetNode.getxLoc() + nodeSize/2, targetNode.getyLoc() + nodeSize/2);
                }
            }
        }

        primaryStage.setTitle("Graphing Colour");
        primaryStage.setScene(new Scene(pane));
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }

    private Color getColour(int nodeColour){
        switch(nodeColour){
            case(0):return Color.BLUE;
            case(1):return Color.ALICEBLUE;
            case(2):return Color.CORNSILK;
            case(3):return Color.AQUA;
            case(4):return Color.AQUAMARINE;
            case(5):return Color.AZURE;
            case(6):return Color.BISQUE;
            case(7):return Color.BLANCHEDALMOND;
            case(8):return Color.BLUEVIOLET;
            case(9):return Color.BROWN;
            case(10):return Color.BURLYWOOD;
            case(11):return Color.CADETBLUE;
            case(12):return Color.CHARTREUSE;
            case(13):return Color.CHOCOLATE;
            case(14):return Color.CORAL;
            default:return Color.BLACK;
        }
    }
}
