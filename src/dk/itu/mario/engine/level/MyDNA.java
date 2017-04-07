package dk.itu.mario.engine.level;

import java.util.Random;
import java.util.*;
import java.lang.StringBuilder;

//Make any new member variables and functions you deem necessary.
//Make new constructors if necessary
//You must implement mutate() and crossover()

public class MyDNA extends DNA
{
	// New VARIABLES

	private Random random = new Random();

	// public ArrayList<Integer> intMap = new ArrayList<Integer>();

	public int numGenes = 0; //number of genes


	// Return a new DNA that differs from this one in a small way.
	// Do not change this DNA by side effect; copy it, change the copy, and return the copy.
	public MyDNA mutate ()
	{
		MyDNA copy = new MyDNA();
		//YOUR CODE GOES BELOW HERE

		// Mutation will swap characters at 2 different indexes
		String chromosome = this.getChromosome();
		// choose index from 0-19 to use
		int index1 = random.nextInt(20);
		int index2 = random.nextInt(20);

		// if indexes are the same, reroll the second index
		while (mutateIndex1 == mutateIndex2) {
			mutateIndex2 = random.nextInt(20);
		}

		StringBuilder mutationStr = new StringBuilder(chromosome);

		char char1 = mutationStr.charAt(index1);
		char char2 = mutationStr.charAt(index2);

		// Swap characters
		mutationStr.setCharAt(index1, char2);
		mutationStr.setCharAt(index2, char1);

		// Set chromosome as the result of the new string
		copy.setChromosome(mutationStr.toString());

		//YOUR CODE GOES ABOVE HERE
		return copy;
	}

	// Do not change this DNA by side effect
	public ArrayList<MyDNA> crossover (MyDNA mate)
	{
		ArrayList<MyDNA> offspring = new ArrayList<MyDNA>();
		//YOUR CODE GOES BELOW HERE
		MyDNA thisCopy = new MyDNA();
		MyDNA mateCopy = new MyDNA();

		String mateDNA = mate.getChromosome();
		String myDNA = this.getChromosome();

		StringBuilder mateChrom = new StringBuilder(mateDNA);
		StringBuilder myChrom = new StringBuilder(myDNA);

		// Grab second halves of each chromosome
		String secondHalfMate = mateChrom.substring(10, 19);
		String mySecondHalf = myChrom.substring(10, 19);

		// 1st half of myChrom + 2nd half of mateChrom
		myChrom.replace(10, 19, secondHalfMate);
		// 1st half of mateChrom + 2nd half of myChrom
		mateChrom.replace(10, 19, mySecondHalf);

		mateCopy.setChromosome(mateChrom);
		thisCopy.setChromosome(myChrom);

		// Add new DNA to list of offspring
		offspring.add(mateCopy);
		offspring.add(thisCopy);

		//YOUR CODE GOES ABOVE HERE
		return offspring;
	}

	// Optional, modify this function if you use a means of calculating fitness other than using the fitness member variable.
	// Return 0 if this object has the same fitness as other.
	// Return -1 if this object has lower fitness than other.
	// Return +1 if this objet has greater fitness than other.
	public int compareTo(MyDNA other)
	{
		int result = super.compareTo(other);
		//YOUR CODE GOES BELOW HERE

		//YOUR CODE GOES ABOVE HERE
		return result;
	}


	// For debugging purposes (optional)
	public String toString ()
	{
		String s = super.toString();
		//YOUR CODE GOES BELOW HERE

		//YOUR CODE GOES ABOVE HERE
		return s;
	}

	public void setNumGenes (int n)
	{
		this.numGenes = n;
	}

}
