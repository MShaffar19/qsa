package fragments;

import java.io.Serializable;

/**
 *
 * @author Antonin Pavelka
 */
public class Parameters implements Serializable {

	private static final long serialVersionUID = 1L;

	private Parameters() {
	}

	public static Parameters create() {
		return new Parameters();
	}

	public double sequenceNeighborLimit() {
		return 5;
	}

	public int getWordLength() {
		return 5;
	}

	public double getResidueContactDistance() {
		return 8;
	}

	public int skipX() {
		return 1;
	}

	public int skipY() {
		return 1;
	}

	public double getMaxFragmentRmsd() {
		return 3.5; // 1:3, 4:3.5
	}

	public double getMaxWordDistDiff() {
		return 3;
	}

	public double getMaxWordRmsd() { // or is it fragment?
		return 3.5; // 1:2, 2:3.5, 3:4, 4:3.5
	}

	public double newMaxDeviation() {
		return 7;
	}

	public double newAvgDeviation() {
		return 5;
	}

	// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public double tmFilter() {
		return 0.3;
	}

	public double rigid() {
		return 3;
	}

	// ----
	public boolean visualize() {
		return true;
	}

	public boolean debug() {
		return true;
	}

	public boolean displayFirstOnly() {
		return true;
	}

	public double[] getRanges() {
		double a = 2;
		double b = 0.5;
		double[] ranges = {a, a, a, a, b, b};
		return ranges;
	}

	public int[] getBins() {
		int a = 40;
		int b = 60;
		int[] bins = {a, a, a, a, b, b};
		return bins;
	}

	/**
	 * RMSD cutoff for word clusters.
	 */
	public double getWordClusteringThreshold() {
		return 2;
	}

	/**
	 * RMSD cutoff for word, how similar words can form alignment.
	 */
	public double getWordQueryRange() {
		return 1;
	}

	/**
	 * How far can cluster representant be from a query if that cluster should be included in evaluation ( or rather
	 * that representant considered as one of the query word labels)
	 */
	public double getWordLabelThreshold() {
		return getWordQueryRange() + getWordClusteringThreshold();
	}
}
