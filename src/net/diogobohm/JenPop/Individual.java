package net.diogobohm.JenPop;

/**
 * Class that describes an individial, a possible member of a population.
 * Individuals must obey the rules of genetic algorithms:
 *     - Must be able to be scored as a value;
 *     - Must be evaluated by a fitness function;
 * 
 * This class also provide abstract methods to generate new individuals,
 * such as crossover, inversion and mutation.
 */
public abstract class Individual {
	
	public abstract long calculateValue();
	
	public abstract Individual applyFitness();
	
	public abstract Individual crossOver(Individual companion);
	
	public abstract Individual mutate();
	
	public abstract Individual invert();
}
