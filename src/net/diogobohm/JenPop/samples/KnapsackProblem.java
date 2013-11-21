package net.diogobohm.JenPop.samples;

import java.util.ArrayList;
import net.diogobohm.JenPop.Individual;

/**
 *
 * @author diogo
 */
public class KnapsackProblem extends Individual{
	
	public static final int KNAPSACK_MAX_WEIGHT = 15;
	public static final int[][] KNAPSACK_ITEM_VALUE_WEIGHT = {
		{ 4, 12 }, { 2, 2 }, { 2, 1 }, { 1, 1 }, { 10, 4 }
	};
	
	private int[] items;
	
	public KnapsackProblem() {
		generateItems();
	}
	
	public KnapsackProblem(int[] items) {
		if (items.length != KNAPSACK_ITEM_VALUE_WEIGHT.length) {
			generateItems();
		} else {
			this.items = new int[KNAPSACK_ITEM_VALUE_WEIGHT.length];
			for (int i = 0; i < items.length; i++) {
				this.items[i] = items[i];
			}
		}
	}
	
	private int generateItemAmount(int index) {
		 return (int) (Math.random() * 100000 % 
				 ((KNAPSACK_MAX_WEIGHT+1)/KNAPSACK_ITEM_VALUE_WEIGHT[index][1]));
	}
	
	private void generateItems() {
		items = new int[KNAPSACK_ITEM_VALUE_WEIGHT.length];
		for (int i = 0; i < items.length; i++) {
			items[i] = generateItemAmount(i);
		}
	}
	
	public int[] getItems() {
		return items;
	}
	
	public int calculateWeight() {
		int weight = 0;
		for (int i = 0; i < items.length; i++) {
			weight += items[i] * KNAPSACK_ITEM_VALUE_WEIGHT[i][1];
		}
		return weight;
	}

	@Override
	public long calculateValue() {
		long points = 0;
		for (int i = 0; i < items.length; i++) {
			points += items[i] * KNAPSACK_ITEM_VALUE_WEIGHT[i][0];
		}
		return points;
	}

	@Override
	public Individual applyFitness() {
		return calculateWeight() > KNAPSACK_MAX_WEIGHT? null : this;
	}

	@Override
	public Individual crossOver(Individual companion) {
		int[] compItems = ((KnapsackProblem) companion).getItems();
		int[] newItems = new int[items.length];
		int cut = (int) (Math.random() % items.length);
		
		for (int i = 0; i < cut; i++) {
			newItems[i] = items[i];
		}
		for (int i = cut; i < items.length; i++) {
			newItems[i] = compItems[i];
		}
		
		return new KnapsackProblem(newItems);
	}

	@Override
	public Individual mutate() {
		int[] newItems = new int[items.length];
		int gene = (int) (Math.random() % items.length);
		
		for (int i = 0; i < items.length; i++) {
			newItems[i] = items[i];
		}
		newItems[gene] = generateItemAmount(gene);
		return new KnapsackProblem(newItems);
	}

	@Override
	public Individual invert() {
		int[] newItems = new int[items.length];
		for (int i = 0; i < items.length; i++) {
			newItems[i] = items[items.length-1-i];
		}
		return new KnapsackProblem(newItems);
	}
	
	@Override
	public String toString() {
		String ret  = "{ "+items[0];
		
		for (int i = 1; i < items.length; i++) {
			ret += ", "+items[i];
		}
		
		return ret+" } ("+calculateValue()+", "+calculateWeight()+")";
	}
	
	public static void main(String[] args) {
		int initialPopSize = 20;
		int minPopSize = 2;
		int maxPopSize = 1024;
		int maxIterations = 1000;
		
		ArrayList<KnapsackProblem> population = new ArrayList<KnapsackProblem>();
		ArrayList<KnapsackProblem> out = new ArrayList<KnapsackProblem>();
		KnapsackProblem best = null;
		long bestValue = 0;
		
		for (int i = 0; i < maxIterations; i++) {
			if (population.size() < minPopSize) {
				for (int j = 0; j < initialPopSize; j++) {
					population.add(new KnapsackProblem());
				}
			} else if (population.size() > maxPopSize) {
				for (int j = population.size()-maxPopSize; j > 0; j--) {
					population.remove((int) Math.random() * 10000 % population.size());
				}
			}
			
			System.out.println("Iteration "+(i+1)+" - Population "+population.size());
			
			// Generate new members
			for (KnapsackProblem ind : population) {
				switch ((int) Math.random() % 3) {
					case 0: out.add((KnapsackProblem) ind.mutate()); break;
					case 1: out.add((KnapsackProblem) ind.invert()); break;
					default: out.add((KnapsackProblem) ind.crossOver(
							population.get((int) (Math.random() * 10000 % population.size()))));
				}
			}
			population.addAll(out);
			out.clear();
			
			// apply fitness and get new best
			for (KnapsackProblem ind : population) {
				if (ind.applyFitness() == null) {
					out.add(ind);
				} else {
					if (ind.calculateValue() > bestValue) {
						best = ind;
						bestValue = best.calculateValue();
					}
				}
			}
			population.removeAll(out);
			out.clear();
		}
		
		System.out.println("Best result was "+best.toString());
	}
}
