package dk.itu.mario.engine.level.generator;

import java.util.*;
import java.lang.StringBuilder;
import java.lang.System.*;

import dk.itu.mario.MarioInterface.Constraints;
import dk.itu.mario.MarioInterface.GamePlay;
import dk.itu.mario.MarioInterface.LevelGenerator;
import dk.itu.mario.MarioInterface.LevelInterface;
import dk.itu.mario.engine.level.Level;
import dk.itu.mario.engine.level.MyLevel;
import dk.itu.mario.engine.level.MyDNA;

import dk.itu.mario.engine.PlayerProfile;

import dk.itu.mario.engine.sprites.SpriteTemplate;
import dk.itu.mario.engine.sprites.Enemy;

public class MyLevelGenerator{

	public boolean verbose = true; //print debugging info
	private Random random = new Random();
	private double bestFitness = 0.0;
	private int fitnessCount = 0;

	// MAKE ANY NEW MEMBER VARIABLES HERE

	// Called by the game engine.
	// Returns the level to be played.
	public Level generateLevel(PlayerProfile playerProfile)
	{
		// Call genetic algorithm to optimize to the player profile
		MyDNA dna = this.geneticAlgorithm(playerProfile);

		// Post process
		dna = this.postProcess(dna);

		// Convert the solution to the GA into a Level
		MyLevel level = new MyLevel(dna, LevelInterface.TYPE_OVERGROUND);

		return (Level)level;
	}

	// Genetic Algorithm implementation
	private MyDNA geneticAlgorithm (PlayerProfile playerProfile)
	{
		// Set the population size
		int populationSize = getPopulationSize();

		// Make the population array
		ArrayList<MyDNA> population = new ArrayList<MyDNA>();

		// Make the solution, which is initially null
		MyDNA solution = null;

		// Generate a random population
		for (int i=0; i < populationSize; i++) {
			MyDNA newIndividual = this.generateRandomIndividual();
			newIndividual.setFitness(this.evaluateFitness(newIndividual, playerProfile));
			population.add(newIndividual);
		}
		if (this.verbose) {
			System.out.println("Initial population:");
			printPopulation(population);
		}

		// Iteration counter
		int count = 0;

		// Iterate until termination criteria met
		while (!this.terminate(population, count)) {
			// Make a new, possibly larger population
			ArrayList<MyDNA> newPopulation = new ArrayList<MyDNA>();

			// Keep track of individual's parents (for this iteration only)
			Hashtable parents = new Hashtable();

			// Mutuate a number of individuals
			ArrayList<MyDNA> mutationPool = this.selectIndividualsForMutation(population);
			for (int i=0; i < mutationPool.size(); i++) {
				MyDNA parent = mutationPool.get(i);
				// Mutate
				MyDNA mutant = parent.mutate();
				// Evaluate fitness
				double fitness = this.evaluateFitness(mutant, playerProfile);
				mutant.setFitness(fitness);
				// Add mutant to new population
				newPopulation.add(mutant);
				// Create a list of parents and remember it in a hash
				ArrayList<MyDNA> p = new ArrayList<MyDNA>();
				p.add(parent);
				parents.put(mutant, p);
			}


			// Do Crossovers
			for (int i=0; i < this.numberOfCrossovers(); i++) {
				// Pick two parents
				MyDNA parent1 = this.pickIndividualForCrossover(newPopulation, null);
				MyDNA parent2 = this.pickIndividualForCrossover(newPopulation, parent1);

				if (parent1 != null && parent2 != null) {
					// Crossover produces one or more children
					ArrayList<MyDNA> children = parent1.crossover(parent2);

					// Add children to new population and remember their parents
					for (int j=0; j < children.size(); j++) {
						// Get a child
						MyDNA child = children.get(j);
						// Evaluate fitness
						double fitness = this.evaluateFitness(child, playerProfile);
						child.setFitness(fitness);
						// Add it to new population
						newPopulation.add(child);
						// Create a list of parents and remember it in a hash
						ArrayList<MyDNA> p = new ArrayList<MyDNA>();
						p.add(parent1);
						p.add(parent2);
						parents.put(child, p);
					}
				}

			}

			// Cull the population
			// There is more than one way to do it.
			if (this.competeWithParentsOnly()) {
				population = this.competeWithParents(population, newPopulation, parents);
			}
			else {
				population = this.globalCompetition(population, newPopulation);
			}

			//increment counter
			count = count + 1;

			if (this.verbose) {
				MyDNA best = this.getBestIndividual(population);
				System.out.println("" + count + ": Best: " + best + " fitness: " + best.getFitness());
			}
		}

		// Get the winner
		solution = this.getBestIndividual(population);

		if (this.verbose) {
			System.out.println("Solution: " + solution + " fitnes: " + solution.getFitness());
		}

		return solution;
	}

	// Create a random individual.
	private MyDNA generateRandomIndividual ()
	{
		MyDNA individual = new MyDNA();
		// YOUR CODE GOES BELOW HERE

		// New StringBuilder for chromosome
		StringBuilder newChromosome = new StringBuilder();

		// Generates new integer and appends the corresponding alphabetic value to stringbuilder
		System.out.println("Generating new individual...");
		for (int i = 0; i < 13; i++) {
			int newNum = random.nextInt(13);
			System.out.println("Number = " + newNum);
			switch(newNum) {
				case 0:
					System.out.println("Appending value 'a'");
					newChromosome.append('a');
					break;
				case 1:
					System.out.println("Appending value 'b'");
					newChromosome.append('b');
					break;
				case 2:
					System.out.println("Appending value 'c'");
					newChromosome.append('c');
					break;
				case 3:
					System.out.println("Appending value 'd'");
					newChromosome.append('d');
					break;
				case 4:
					System.out.println("Appending value 'e'");
					newChromosome.append('e');
					break;
				case 5:
					System.out.println("Appending value 'f'");
					newChromosome.append('f');
					break;
				case 6:
					System.out.println("Appending value 'g'");
					newChromosome.append('g');
					break;
				case 7:
					System.out.println("Appending value 'h'");
					newChromosome.append('h');
					break;
				case 8:
					System.out.println("Appending value 'i'");
					newChromosome.append('i');
					break;
				case 9:
					System.out.println("Appending value 'j'");
					newChromosome.append('j');
					break;
				case 10:
					System.out.println("Appending value 'k'");
					newChromosome.append('k');
					break;
				case 11:
					System.out.println("Appending value 'l'");
					newChromosome.append('l');
					break;
				case 12:
					System.out.println("Appending value 'm'");
					newChromosome.append('m');
					break;
				default:
					break;
			}
		}

		System.out.println("New chromosome value is = " + newChromosome.toString());

		// Set chromosome for new DNA
		individual.setChromosome(newChromosome.toString());

		// YOUR CODE GOES ABOVE HERE
		return individual;
	}

	// Returns true if the genetic algorithm should terminate.
	private boolean terminate (ArrayList<MyDNA> population, int count)
	{
		boolean decision = false;
		// YOUR CODE GOES BELOW HERE
		// 1. Check if number of generations is large
		if (count > 1000) {
			return true;
		}


		// 2. Check if pre-determined fitness has been reached
		MyDNA bestIndiv = getBestIndividual(population);
		double currBestFitness = bestIndiv.getFitness();
		// If the best fitness so far is .9, return true.
		if (currBestFitness > 0.90) {
			return true;
		}

		// 3. Check for convergence
		// If the fitness is the same, incrememnt the counter
		if (bestFitness == currBestFitness) {
			fitnessCount++;
		}

		// If current best fitness is better than previous best fitness, assign new value
		if (currBestFitness > bestFitness) {
			bestFitness = currBestFitness;
			fitnessCount = 0;
		}

		// If the fitness has converged to an acceptable value, return true
		if (fitnessCount >= 50 && bestFitness >= 0.8) {
			return true;
		}

		// YOUR CODE GOES ABOVE HERE
		return decision;
	}

	// Return a list of individuals that should be copied and mutated.
	private ArrayList<MyDNA> selectIndividualsForMutation (ArrayList<MyDNA> population)
	{
		ArrayList<MyDNA> selected = new ArrayList<MyDNA>();
		// YOUR CODE GOES BELOW HERE

		// Number of individuals should be equal to amount of crossovers
		int amount = numberOfCrossovers();

		// Select number of individuals for mutation
		while (selected.size() < amount) {

			int index = random.nextInt(population.size());
			MyDNA indiv = population.get(index);

			// If individual is not in the selected list, add it.
			if(!selected.contains(index)) {
				// Add individual, this will increment selected size
				selected.add(population.get(index));
				System.out.println("selected size is now " + selected.size());
			}
		}


		// YOUR CODE GOES ABOVE HERE
		return selected;
	}

	// Returns the size of the population.
	private int getPopulationSize ()
	{
		int num = 200; // Default needs to be changed
		// YOUR CODE GOES BELOW HERE
		// YOUR CODE GOES ABOVE HERE
		return num;
	}

	// Returns the number of times crossover should happen per iteration.
	private int numberOfCrossovers ()
	{
		int num = 20; // Default is no crossovers
		// YOUR CODE GOES BELOW HERE
		// YOUR CODE GOES ABOVE HERE
		return num;

	}

	// Pick one of the members of the population that is not the same as excludeMe
	private MyDNA pickIndividualForCrossover (ArrayList<MyDNA> population, MyDNA excludeMe)
	{
		MyDNA picked = null;
		// YOUR CODE GOES BELOW HERE
		int index = random.nextInt(population.size());
		picked = population.get(index);

		// If picked is same as excludeMe, re-select a member from population
		while (picked == excludeMe) {
			index = random.nextInt(population.size());
			picked = population.get(index);
		}

		return picked;

	}

	// Returns true if children compete to replace parents.
	// Retursn false if the the global population competes.
	private boolean competeWithParentsOnly ()
	{
		boolean doit = false;
		// YOUR CODE GOES BELOW HERE
		// DO IT
		// YOUR CODE GOES ABOVE HERE
		return doit;
	}

	// Determine if children are fitter than parents and keep the fitter ones.
	private ArrayList<MyDNA> competeWithParents (ArrayList<MyDNA> oldPopulation, ArrayList<MyDNA> newPopulation, Hashtable parents)
	{
		ArrayList<MyDNA> finalPopulation = new ArrayList<MyDNA>();
		// YOUR CODE GOES BELOW HERE

		// YOUR CODE GOES ABOVE HERE
		if (finalPopulation.size() != this.getPopulationSize()) {
			System.err.println("Population not the correct size.");
			System.exit(1);
		}
		return finalPopulation;
	}

	// Combine the old population and the new population and return the top fittest individuals.
	private ArrayList<MyDNA> globalCompetition (ArrayList<MyDNA> oldPopulation, ArrayList<MyDNA> newPopulation)
	{
		ArrayList<MyDNA> finalPopulation = new ArrayList<MyDNA>();
		// YOUR CODE GOES BELOW HERE

		// Add all of old+new population into list
		ArrayList<MyDNA> competitionPool = new ArrayList<MyDNA>();
		competitionPool.addAll(oldPopulation);
		competitionPool.addAll(newPopulation);

		// Begin competition
		while(finalPopulation.size() < 200) {
			// Find best individual
			MyDNA bestIndiv = getBestIndividual(competitionPool);
			finalPopulation.add(bestIndiv);
			// Remove individual from the competition pool
			competitionPool.remove(bestIndiv);
		}


		// YOUR CODE GOES ABOVE HERE
		if (finalPopulation.size() != this.getPopulationSize()) {
			System.err.println("Population not the correct size.");
			System.err.println("Final population size = " + finalPopulation.size());
			System.err.println("CORRECT population size = " + this.getPopulationSize());
			System.exit(1);
		}
		return finalPopulation;
	}

	// Return the fittest individual in the population.
	private MyDNA getBestIndividual (ArrayList<MyDNA> population)
	{
		MyDNA best = population.get(0);
		double bestFitness = Double.NEGATIVE_INFINITY;
		for (int i=0; i < population.size(); i++) {
			MyDNA current = population.get(i);
			double currentFitness = current.getFitness();
			if (currentFitness > bestFitness) {
				best = current;
				bestFitness = currentFitness;
			}
		}
		return best;
	}

	// Changing this function is optional.
	private double evaluateFitness (MyDNA dna, PlayerProfile playerProfile)
	{
		double fitness = 0.0;
		// YOUR CODE GOES BELOW HERE
		MyLevel level = new MyLevel(dna, LevelInterface.TYPE_OVERGROUND);
		fitness = playerProfile.evaluateLevel(level);
		// YOUR CODE GOES ABOVE HERE
		return fitness;
	}

	private MyDNA postProcess (MyDNA dna)
	{
		// YOUR CODE GOES BELOW HERE

		// YOUR CODE GOES ABOVE HERE
		return dna;
	}

	//for this to work, you must implement MyDNA.toString()
	private void printPopulation (ArrayList<MyDNA> population)
	{
		for (int i=0; i < population.size(); i++) {
			MyDNA dna = population.get(i);
			System.out.println("Individual " + i + ": " + dna + " fitness: " + dna.getFitness() + " string: " + dna.getChromosome());
		}
	}

	// MAKE ANY NEW MEMBER FUNCTIONS HERE

}
