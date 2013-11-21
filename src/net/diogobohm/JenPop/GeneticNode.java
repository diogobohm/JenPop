/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.diogobohm.JenPop;

import java.util.ArrayList;

/**
 *
 * @author diogo
 */
public abstract class GeneticNode {

    private long worstScore = 0;

    public long getWorstScore() {
        return worstScore;
    }

    public void setWorstScore(long worstScore) {
        this.worstScore = worstScore;
    }

    public abstract long getScore();
    
    public abstract long promoteScore(int grade);
    
    public abstract long punishScore(int grade);

    public abstract ArrayList<GeneticNode> createGeneration();

    public abstract GeneticNode createChild();

    public static GeneticNode getBestNode(ArrayList<GeneticNode> nodeList) {
        long bestScore = Long.MIN_VALUE;
        long currentScore = Long.MIN_VALUE;
        GeneticNode bestNode = null;

        if (nodeList.isEmpty()) {
            return null;
        }

        for (GeneticNode currentNode : nodeList) {
            currentScore = currentNode.getScore();
            if (currentScore > bestScore) {
                bestScore = currentScore;
                bestNode = currentNode;
            }
        }

        return bestNode;
    }
    
    public static GeneticNode getWorstNode(ArrayList<GeneticNode> nodeList) {
        long worstScore;
        long currentScore;
        GeneticNode worstNode;

        if (nodeList.isEmpty()) {
            return null;
        }

        worstNode = nodeList.get(0);
        worstScore = worstNode.getScore();

        for (GeneticNode currentNode : nodeList) {
            currentScore = currentNode.getScore();
            if (currentScore < worstScore) {
                worstScore = currentScore;
                worstNode = currentNode;
            }
        }

        return worstNode;
    }
    
    public GeneticNode getWorstChild() {
        return GeneticNode.getWorstNode(createGeneration());
    }

    public GeneticNode getBestChild() {
        return getBestChildMiniMax(this, true, 1);
    }
    
    public GeneticNode getBestChild(int depth) {
        return getBestChildMiniMax(this, true, 8);
    }
    
    private static GeneticNode getBestChildMiniMax(GeneticNode node, boolean start, int depth) {
        if (depth <= 0) {
            return node;
        }
        
        ArrayList<GeneticNode> generation = node.createGeneration();
        GeneticNode alphaNode = null;
        
        if (generation.isEmpty()) {
            return node;
        }
        
        if (depth % 2 == 1) { //Opponent plays
            long alpha = Long.MAX_VALUE;
            alphaNode = generation.get(0);
            for (GeneticNode child : generation) {
                GeneticNode testNode = getBestChildMiniMax(child, false, depth-1);
                if (testNode.getScore() < alpha) {
                    alphaNode = start ? child : testNode;
                    alpha = testNode.getScore();
                }
            }
            return alphaNode;
            
        } else { //CPU plays
            long alpha = Long.MIN_VALUE;
            alphaNode = generation.get(0);
            for (GeneticNode child : generation) {
                GeneticNode testNode = getBestChildMiniMax(child, false, depth-1);
                if (testNode.getScore() > alpha) {
                    alphaNode = start ? child : testNode;
                    alpha = testNode.getScore();
                }
            }
            return alphaNode;
        }
    }
        
    /*
        if (maxGenerations == 1) {
            return GeneticNode.getBestNode(createGeneration());
        }
        
        boolean cputurn = maxGenerations % 2 == 1;
        ArrayList<GeneticNode> generation = createGeneration();
        GeneticNode bestNode = this;
        
        if (cputurn) {
            bestNode = getBestNode(generation);
            long bestNodeScore = bestNode.getScore();
            for (GeneticNode node : generation) {
                GeneticNode bestChild = node.getBestChild(maxGenerations - 1);
                
                if (bestChild.getScore() > bestNodeScore) {
                    bestNodeScore = bestChild.getScore();
                    bestNode = node;
                    System.out.println(String.valueOf(maxGenerations) + " Found better score: " + String.valueOf(bestNodeScore));
                }
         
            }
        } else {
            bestNode = getWorstNode(generation);
            long bestNodeScore = bestNode.getScore();
            for (GeneticNode node : generation) {
                GeneticNode bestChild = node.getBestChild(maxGenerations - 1);
                
                if (bestChild.getScore() < bestNodeScore) {
                    bestNodeScore = bestChild.getScore();
                    bestNode = node;
                    System.out.println(String.valueOf(maxGenerations) + " Found better score: " + String.valueOf(bestNodeScore));
                }
         
            }
        }
        return bestNode;
    }
    */
}
