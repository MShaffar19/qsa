package alignment.score;

import geometry.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import pdb.PdbLine;
import pdb.Residue;
import pdb.SimpleStructure;

public class Equivalence {

	private final SimpleStructure[] s = new SimpleStructure[2];
	private final Residue[][] rr;

	public Equivalence(SimpleStructure sa, SimpleStructure sb, Residue[][] mapping) {
		this.s[0] = sa;
		this.s[1] = sb;
		this.rr = mapping;

	}

	public SimpleStructure get(int i) {
		return s[i];
	}

	public void save(Point shift, File f) {
		try {
			int serial = 1;
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
				for (int i = 0; i < rr[0].length; i++) {
					for (int k = 0; k < 2; k++) {
						Point p = rr[k][i].getPosition();
						if (shift != null) {
							p = p.plus(shift);
						}
						PdbLine pl = new PdbLine(serial + k, "CA", "C", "GLY",
							Integer.toString(serial + k), 'A', p.x, p.y, p.z);
						bw.write(pl.toString());
						bw.newLine();
					}
					bw.write(PdbLine.getConnectString(serial, serial + 1));
					bw.newLine();
					serial += 2;
				}
			}

		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public Point center() {
		Point[] all = new Point[2 * size()];
		int index = 0;
		for (int k = 0; k < 2; k++) {
			for (int i = 0; i < size(); i++) {
				all[index++] = rr[k][i].getPosition();
			}
		}
		return Point.center(all);
	}

	public int size() {
		return rr[0].length;
	}

	public int matchingResidues() {
		return rr[0].length;
	}

	private int minSize() {
		return Math.min(s[0].size(), s[1].size());
	}

	public double matchingResiduesRelative() {
		return (double) matchingResidues() / minSize();
	}

	/**
	 * https://en.wikipedia.org/wiki/Template_modeling_score
	 */
	public double tmScore() {
		int lengthTarget = minSize();
		int lengthAligned = matchingResidues();
		if (lengthAligned < 16) {
			return 0; // possible problem in equation for very small matches?
		}
		double d0 = 1.24 * Math.pow(lengthTarget - 15, 1 / 3) - 1.8;
		double score = 0;
		for (int i = 0; i < rr[0].length; i++) {
			Residue r = rr[0][i];
			Residue q = rr[1][i];
			double d = r.getPosition().distance(q.getPosition());
			double dd = (d / d0);
			score += 1 / (1 + dd * dd);
		}
		return score / lengthTarget;
	}

	public double tmScoreOld() {
		double score = 0;
		for (int i = 0; i < rr[0].length; i++) {
			Residue r = rr[0][i];
			Residue q = rr[1][i];
			double d = r.getPosition().distance(q.getPosition());
			double dd = (d / 10);
			score += 1 / (1 + dd * dd);
		}
		return score / minSize();
	}
}