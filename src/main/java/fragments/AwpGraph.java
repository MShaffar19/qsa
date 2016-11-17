package fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import fragments.clustering.Cluster;

/**
 * Graph of aligned word pairs, connected by edges iff the RMSD of aligned
 * biwords (biword = any two non-overlapping words from single protein) is low.
 * 
 * Aligned pair = pair of similar elements from different proteins.
 * 
 * @author antonin
 *
 */
public class AwpGraph {

	private List<Edge> edges = new ArrayList<Edge>();
	private Map<AwpNode, AwpNode> nodes = new HashMap<>();

	public void connect(AwpNode[] ps, double rmsd) {
		for (int i = 0; i < ps.length; i++) {
			AwpNode p = ps[i];
			if (nodes.containsKey(p)) {
				ps[i] = nodes.get(p);
			} else {
				nodes.put(p, p);
			}
		}
		edges.add(new Edge(ps[0], ps[1], rmsd));
	}

	public void printGraph() {
		for (Edge e : edges) {
			System.out.println(e.getX() + " " + e.getY() + " " + e.getRmsd());
		}
	}

	public void print() {
		Collections.sort(edges);
		for (int i = 0; i < Math.min(100, edges.size()); i++) {
			Edge e = edges.get(i);
			System.out.println("rmsd = " + e.getRmsd());
		}
	}

	public void getClusters() {
		/*
		 * Map<Integer, AwpCluster> cs = new TreeMap<>(); for (AwpNode n :
		 * nodes.keySet()) { cs.put(n.getCluster().getId(), n.getCluster()); }
		 * 
		 * for (int id : cs.keySet()) { AwpCluster c = cs.get(id); if (c.size()
		 * > 1) { System.out.println(cs.get(id)); } }
		 */
	}

	public AwpClustering cluster() {
		AwpClustering clustering = new AwpClustering();
		System.out.println("nodes: " + nodes.size());
		int id = 0;
		for (AwpNode p : nodes.keySet()) {
			p.setClusterId(id);
			AwpCluster cluster = new AwpCluster(id++, p, clustering);
			clustering.add(cluster);
			id++;
		}
		System.out.println("sorging...");
		Collections.sort(edges);
		System.out.println("sorted");
		for (Edge e : edges) {
			if (e.getX().getClusterId() != e.getY().getClusterId()) {
				clustering.merge(e.getX().getClusterId(), e.getY().getClusterId());
				e.getX().updateRmsd(e.getRmsd());
				e.getY().updateRmsd(e.getRmsd());
			}
		}
		return clustering;
	}

	public static void test_main(String[] args) {
		AwpGraph wg = new AwpGraph();
		/*
		 * int an = 10; int bn = 10; WordInterface[] wa = new WordInterface[an];
		 * for (int i = 0; i < an; i++) { wa[i] = new DummyWord(i); }
		 * WordInterface[] wb = new WordInterface[bn]; for (int i = 0; i < bn;
		 * i++) { wb[i] = new DummyWord(i + 2); }
		 */
		for (int i = 0; i < 4; i++) {
			AwpNode[] ps = { new AwpNode(new DummyWord(0 + i), new DummyWord(2 + i)),
					new AwpNode(new DummyWord(1 + i), new DummyWord(3 + i)) };
			wg.connect(ps, 10 + i);

			AwpNode[] pss = { new AwpNode(new DummyWord(0 + i), new DummyWord(2 + i)),
					new AwpNode(new DummyWord(1 + i + 1), new DummyWord(3 + i + 1)) };
			wg.connect(pss, 10 - i + 3);

		}

		// wg.printGraph();
		wg.cluster();

	}

}
