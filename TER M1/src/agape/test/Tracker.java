package agape.test;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author V. Levorato This class allows to follow an algorithm and memorizes
 *         how many times the algorithm passes through a branch or an heuristic.
 */
public class Tracker {

	HashMap<String, Integer> branchmap;

	public Tracker() {
		this.branchmap = new HashMap<String, Integer>();
	}

	/**
	 * Increase by one the "index" trace.
	 *
	 * @param index point to survey.
	 */
	public void increase(String index) {
		if (this.branchmap.get(index) != null) {
			int v = this.branchmap.get(index);
			this.branchmap.put(index, v + 1);
		} else
			this.branchmap.put(index, 1);
	}

	/**
	 * Give a trace a specific value.
	 *
	 * @param index point to survey.
	 * @param value value to give to point.
	 */
	public void set(String index, int value) {
		this.branchmap.put(index, value);
	}

	/**
	 * Return the amount of times the aimed method passed through a point.
	 *
	 * @param index point to get.
	 * @return number of times the algorithm passed through the given point.
	 */
	public int get(String index) {
		return this.branchmap.get(index);
	}

	@Override
	public String toString() {
		String res = "";
		for (Entry<String, Integer> entry : this.branchmap.entrySet()) {
			res += entry.getKey() + ":" + entry.getValue() + "\n";
		}

		return res;

	}

}
