package structure;

/**
 *
 * @author Antonin Pavelka
 */
public interface ResidueFilter {

	public boolean reject(String pdbCode, ResidueId residueId);
}
