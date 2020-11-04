package sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Solution {
    private List<Node> nodes = new ArrayList<>();
    private List<Edge> edges = new ArrayList<>();

    Random rng = new Random();
    //Add Second rng for creation, creation can be the same, rng for changes can have random seed
    public Solution(String filePath){
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader(filePath));
            int numNodes = Integer.parseInt(reader.readLine());
            for(int i = 0;i < numNodes;i++){
                nodes.add(new Node(i,i));
            }
            String edge;
            String[] sNodes;
            int iNodes[] = new int[2];
            while((edge = reader.readLine()) != null){
                sNodes = edge.split(",");
                iNodes[0] = Integer.parseInt(sNodes[0]);
                iNodes[1] = Integer.parseInt(sNodes[1]);
                addingEdge(iNodes[0],iNodes[1]);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (reader != null)
                    reader.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }
    public void setSeed(String sSeed){
        long seed;
        seed = Long.valueOf(sSeed);
        rng.setSeed(seed);

    }
    public Solution(Solution solution){
        for(int i = 0; i < solution.nodes.size(); i++){
            nodes.add(new Node(i,solution.nodes.get(i).getColor()));
        }
        for (Edge edge: solution.edges) {
            int IDs[] = edge.getNodesID();
            if(addingEdge(IDs[0],IDs[1])) {
                System.err.print("Error adding Edge ");
                edge.printEdge();
            }
        }
    }
    public Solution(int numNodes, int numEdges) {
        //Generate Solution
        for(int i = 0; i < numNodes;i++){
            nodes.add(new Node(i,i));
        }
        int n0,n1;
        for(int i = 0; i < numNodes; i++){
            do {
                n1 = rng.nextInt(numNodes);
            }while(addingEdge(i, n1));
        }
        System.out.println("All nodes set");
        for(int i = 0; i < numEdges;i++){
            do{
                n0 = rng.nextInt(numNodes);
                n1 = rng.nextInt(numNodes);
            }while(addingEdge(n0, n1));
        }
    }



    public void changeNode(int numColours){
        int nodeID = rng.nextInt(nodes.size()-1);
        nodes.get(nodeID).setColor(rng.nextInt(numColours));
    }

    private boolean addingEdge(int n0, int n1){
        if(nodes.get(n0).allReadyConnected(n1)){
            return true;
        }
        Edge newEdge = new Edge(nodes.get(n0),nodes.get(n1));
        edges.add(newEdge);
        nodes.get(n0).addEdge(newEdge);
        nodes.get(n1).addEdge(newEdge);
        //newEdge.printEdge();
        return false;
    }
    public boolean validSolution(){
        for (Edge edge: edges) {
            if(edge.areTheColoursTheSame()){
                //edge.printEdge();
                return false;
            }
        }
        return true;
    }
    public int getNumColours(){
        List<Integer> colours = new ArrayList<>();
        for (Node node:nodes) {
            if(!colours.contains(node.getColor()))
                colours.add(node.getColor());
        }
        return colours.size();

    }
    public void setNodeColour(int index,int colour){
        nodes.get(index).setColor(colour);
    }
    public void checkNodes(){
        for (Node node:nodes) {
            //Do thing
        }
    }
    public void cleanUp(){
        List<Integer> missingColours = new ArrayList<>();
        List<Integer> tooLargeColours = new ArrayList<>();
        double numColours = getNumColours();
        for(int c = 0;c < getNumColours();c++)
            missingColours.add(c);
        for (Node node:nodes) {
            if(missingColours.contains(node.getColor()))
                missingColours.remove(Integer.valueOf(node.getColor()));
            if(node.getColor() > numColours-1 && !tooLargeColours.contains(node.getColor()))
                tooLargeColours.add(node.getColor());
        }
        if(!missingColours.isEmpty()) {
            for (Node node : nodes) {
                if (tooLargeColours.contains(node.getColor())) {
                    int missingColourIndex = tooLargeColours.indexOf(node.getColor());
                    node.setColor(missingColours.get(missingColourIndex));
                }
            }
        }

    }
    public int size(){
        return nodes.size();
    }
    public float checkEdges(){
        int fails = 0;
        int c = 0;
        for(int i = 0;i < edges.size(); i++){
            if(edges.get(i).areTheColoursTheSame())
                fails++;
            c++;
        }
        float passRate = (float)fails / (float)c;
        return passRate;
    }

    public Node getNode(int index)
    {
        return nodes.get(index);
    }

    public List<Node> getNodes()
    {
        return this.nodes;
    }

    //#######################################################
    //#################     PRINT    ########################
    //#######################################################

    public void printSolution(){
        for (Edge edge:edges) {
            edge.printEdge();
        }
    }
}
