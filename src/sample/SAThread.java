package sample;

import javafx.application.Application;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SAThread extends Thread {
    // Global vars:
    int rateOfDecay,numOfCycles = 100, numColours,maxChanges,numNodesToChange;

//    Record record;
//    SolutionAndRecord solutionAndRecord;
    String id;
    Solution currentSolution;
    Random rng = new Random();
    //private List<Future<SolutionAndRecord>> listOfExececutions= new ArrayList<>();
    private ExecutorService executor = Executors.newCachedThreadPool();

    // Contructor
    public SAThread(Solution solution, int numColours, List<Integer> nodesToRecolour, int newNodeRecolour)
    {
        this.id = id;
        currentSolution = new Solution(solution);
        //record = new Record(id);
        //solutionAndRecord = new SolutionAndRecord(currentSolution);
        this.numColours = numColours;
        for (Integer node: nodesToRecolour) {
            currentSolution.setNodeColour(node,newNodeRecolour);
        }
        numNodesToChange = (int)Math.ceil(currentSolution.size()/10);
        setDecay();
        // Get copy of graph.

    }

    private void setDecay(){
        maxChanges = (int)currentSolution.size(); //The more changes it makes, the more difficulty it has finding the optimum
        double max = Math.log(currentSolution.size());
        double cur = Math.log(numColours);
        double coolingFactor = max/cur;

        rateOfDecay = maxChanges / (int)Math.round(coolingFactor);
    }

    @Override
    public void run()
    {

    }
    private int getNumChildren(double improvemnet){
        int children = 1;
        children = (int)(improvemnet/currentSolution.size()*100)%4+1;
        return children;
    }
    //@Override
    public Future<Solution> findSmaller() throws Exception {
        return executor.submit(() -> {
            try {
                System.out.println("Started New Thread");
                for(int i = 0; i <numOfCycles;i++) {
                    for (int c = 0; c < rateOfDecay; c++) {
                        currentSolution.changeNode(numColours);
                    }
                }
            }catch(Exception ex) {
                ex.printStackTrace();
            }
            return currentSolution;
                    //if (currentSolution.validSolution()) {
//                        if (currentSolution.getNumColours() < numColours) {
//                            record.foundNewSolution();
//                            System.out.print(id + " found better solution of " + currentSolution.getNumColours() + " from " + numColours + " at cycle " + i);
//                            if (currentSolution.getNumColours() == 3) {
//                                System.out.print("\n");
//                                break;
//                            }
//                            int numChildren = getNumChildren(numColours - currentSolution.getNumColours());
//                            System.out.println(" spawned " + numChildren);
//
//                            currentSolution.cleanUp();
//                            numColours = currentSolution.getNumColours();
//                            //numColours = 1;
//                            SAThread child;
//                            for (int j = 0; j < numChildren; j++) {
//                                List<Integer> nodesToChange = chooseNodes(numNodesToChange);
//                                int newColour = rng.nextInt((int) currentSolution.size());
//
//
//                                child = new SAThread(currentSolution, (id + j + "."), numColours, nodesToChange, newColour);
//
//                                Future<SolutionAndRecord> futureCall = child.findSmaller();
//                                listOfExececutions.add(futureCall);
//                            }
//
//                            int resultNumColours;
//                            while(!futuresComplete()){
//                                for (int f = 0; f < listOfExececutions.size();f++) {
//                                    if(listOfExececutions.get(f).isDone() && !listOfExececutions.get(f).isCancelled()){
//                                        SolutionAndRecord result = listOfExececutions.get(f).get(); // Here the thread will be blocked
//                                        solutionAndRecord.appendRecords(result.getRecords());
//                                        Solution childSolution = result.getSolution();
//                                        resultNumColours = childSolution.getNumColours();
//                                        if (resultNumColours < numColours && childSolution.validSolution()) {
//                                            System.out.println("Replacement");
//                                            currentSolution = childSolution;
//                                            numColours = resultNumColours;
//                                            solutionAndRecord.setSolution(currentSolution);
//                                            if(numColours == 3){
//                                                System.out.println("3 has been found!!!!!!!!");
//                                                cancelAllThreads();
//                                                break;
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//
//                            break;
//                        }

                //}
                // Save solution, create children threads with reduced colours.

            //record.closingThread();
            //solutionAndRecord.appendRecords(record);

        }
        );

      }
//      private void cancelAllThreads(){
//          for (Future<SolutionAndRecord> thread:listOfExececutions) {
//              if(!thread.isDone())
//                  thread.cancel(true);
//          }
//      }
//      private boolean futuresComplete(){
//        boolean complete = true;
//          for (Future<SolutionAndRecord> thread:listOfExececutions) {
//              if(!thread.isDone()){
//                  complete = false;
//
//              }
//          }
//          return complete;
//      }
      private List<Integer> chooseNodes(int numNodes){
        List<Integer> nodes = new ArrayList<>();
        int node;
        for(int i =0;i<numNodesToChange;i++){
            do {
                node = rng.nextInt((int) currentSolution.size());
            }while(nodes.contains(node));
            nodes.add(node);
        }
        return nodes;
      }
}
