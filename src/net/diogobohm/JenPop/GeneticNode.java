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
	
	public abstract ArrayList<GeneticNode> createGeneration();
	
	public abstract GeneticNode createChild();
	
	public static GeneticNode getBestNode(ArrayList<GeneticNode> nodeList) {
		long bestScore;
		long currentScore;
		GeneticNode bestNode;
				
		if (nodeList.isEmpty()) {
			return null;
		}
		
		bestNode = nodeList.get(0);
		bestScore = bestNode.getScore();
		
		for (GeneticNode currentNode : nodeList){
			currentScore = currentNode.getScore();
			if (currentScore > bestScore) {
				bestScore = currentScore;
				bestNode = currentNode;
			}
		}
		
		return bestNode;
	}
	
	public GeneticNode getBestChild() {
		return GeneticNode.getBestNode(createGeneration());
	}
}
