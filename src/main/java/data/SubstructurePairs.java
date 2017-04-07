package data;

import io.Directories;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import pdb.ChainId;

public class SubstructurePairs implements Iterable<SubstructurePair> {

	private List<SubstructurePair> pairs;

	private SubstructurePairs(List<SubstructurePair> pairs) {
		this.pairs = pairs;
	}

	/**
	 * CLICK file format. http://mspc.bii.a-star.edu.sg/minhn/reference.html
	 */
	public static SubstructurePairs parseClick(Directories dirs) {
		List<SubstructurePair> pairs = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(
			dirs.getTopologyIndependentPairs()))) {
			String line;
			while ((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, "\t");
				Substructure a = parse(st.nextToken().trim());
				Substructure b = parse(st.nextToken().trim());
				pairs.add(new SubstructurePair(a, b));
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return new SubstructurePairs(pairs);
	}

	public static SubstructurePairs parseHomstrad(Directories dirs) {
		List<SubstructurePair> pairs = new ArrayList<>();
		String line = null;
		try (BufferedReader br = new BufferedReader(new FileReader(dirs.getHomstradPairs()))) {
			br.readLine();
			while ((line = br.readLine()) != null) {
				try {
					StringTokenizer st = new StringTokenizer(line, "\t");
					st.nextToken();
					Substructure a = parse(st.nextToken().trim());
					Substructure b = parse(st.nextToken().trim());
					pairs.add(new SubstructurePair(a, b));
				} catch (Exception ex) {
					System.err.println("fail: " + line);
					//ex.printStackTrace();
				}
			}
		} catch (Exception ex) {
			System.err.println(line);
			ex.printStackTrace();

		}
		return new SubstructurePairs(pairs);
	}

	private static Substructure parse(String s) {
		if (s.length() == 5) {
			return new Substructure(s.substring(0, 4), new ChainId(s.charAt(4)));
		} else if (s.length() == 4) {
			return new Substructure(s.substring(0, 4));
		} else {
			throw new RuntimeException();
		}
	}

	@Override
	public Iterator<SubstructurePair> iterator() {
		return pairs.iterator();
	}

	public int size() {
		return pairs.size();
	}
}
