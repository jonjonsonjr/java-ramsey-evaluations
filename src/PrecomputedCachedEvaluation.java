/**
 * PrecomputedCachedEvaluation.java
 * 
 * Returns the mth k-sized subset of a given number of vertices.
 * 
 * @author Jon Johnson & Gabriel Triggs, with heavy influence from
 * 
 *         http://msdn.microsoft.com/en-us/library/aa289166%28VS.71%29.aspx
 */
public class PrecomputedCachedEvaluation {
  public static final int n = 43;
	public static final int k = 5;
	public static String exampleChromosome = "011001110000011100100001110110000000010100110010010111110111010100111111110100011001000010011001111100101011111000001010011101101110100101011001100110001101101000000011000010110100011111001110100010101011001110010001110000111101000101010111100100000101110111101101011010000011000110001101001110110110111001011001101011110100011111011010001100010100010100101011110101100010001100101111011011000010110000101001001010101000101110110111110011101100001000100011111011101001010101111010101110011010001111000110111010011011001001011001100000000100110010111111100010010001011010110011010101001110010100111001001011100100100100100010110011110000100101010111110101101000001111001111101100011111001010101000010011001110100100011100101011000011100010101110011101111000101000110001010100100111100101111011010100001100010010101011000100010101110101010111101001110000110110101001000010011110111001100101111011100001010";

	private static final int[][] chooseCache = {
		{0, 0, 0, 0, 0, 0},
		{0, 1, 0, 0, 0, 0},
		{0, 2, 1, 0, 0, 0},
		{0, 3, 3, 1, 0, 0},
		{0, 4, 6, 4, 1, 0},
		{0, 5, 10, 10, 5, 1},
		{0, 6, 15, 20, 15, 6},
		{0, 7, 21, 35, 35, 21},
		{0, 8, 28, 56, 70, 56},
		{0, 9, 36, 84, 126, 126},
		{0, 10, 45, 120, 210, 252},
		{0, 11, 55, 165, 330, 462},
		{0, 12, 66, 220, 495, 792},
		{0, 13, 78, 286, 715, 1287},
		{0, 14, 91, 364, 1001, 2002},
		{0, 15, 105, 455, 1365, 3003},
		{0, 16, 120, 560, 1820, 4368},
		{0, 17, 136, 680, 2380, 6188},
		{0, 18, 153, 816, 3060, 8568},
		{0, 19, 171, 969, 3876, 11628},
		{0, 20, 190, 1140, 4845, 15504},
		{0, 21, 210, 1330, 5985, 20349},
		{0, 22, 231, 1540, 7315, 26334},
		{0, 23, 253, 1771, 8855, 33649},
		{0, 24, 276, 2024, 10626, 42504},
		{0, 25, 300, 2300, 12650, 53130},
		{0, 26, 325, 2600, 14950, 65780},
		{0, 27, 351, 2925, 17550, 80730},
		{0, 28, 378, 3276, 20475, 98280},
		{0, 29, 406, 3654, 23751, 118755},
		{0, 30, 435, 4060, 27405, 142506},
		{0, 31, 465, 4495, 31465, 169911},
		{0, 32, 496, 4960, 35960, 201376},
		{0, 33, 528, 5456, 40920, 237336},
		{0, 34, 561, 5984, 46376, 278256},
		{0, 35, 595, 6545, 52360, 324632},
		{0, 36, 630, 7140, 58905, 376992},
		{0, 37, 666, 7770, 66045, 435897},
		{0, 38, 703, 8436, 73815, 501942},
		{0, 0, 741, 9139, 82251, 575757},
		{0, 0, 0, 9880, 91390, 658008},
		{0, 0, 0, 0, 101270, 749398},
		{0, 0, 0, 0, 0, 850668},
		{0, 0, 0, 0, 0, 962598}};
	private static final int upperBound = choose(n, k);
	private static int[][] edges = getEdgePossibilities();	
	private static char[] charBits;
	private static boolean[] bits;
	private static boolean[][] adj;

	public static void main(String[] args) {
		System.out.println(evaluate(exampleChromosome));
	}
	
	public static int evaluate(String bitString) {
		charBits = exampleChromosome.toCharArray();
		bits = charToBoolean(charBits);
		adj = getAdjMatrixFromBoolArray(bits);
		
		int[] arr = { 0, 0, 0, 0, 0 };
		int numCliques = 0;
		int result = 0;
			
		/* Evaluate every possible clique */
		for (int i = 0; i < upperBound; i++) {
			PrecomputedCachedEvaluation.getElement(i, arr);
			
			result = evalEdges(arr);
			
			if (result == 0 || result == 10) {
				numCliques++;
			}
		}
		
		return numCliques;
	}

	/**
	 * Returns the number of edges in arr that are red (1).
	 * 
	 * If this returns kC2 (10 for R(5, 5)), it is a red clique,
	 * 				   0,   				 it is a blue clique
	 */
	static int evalEdges(int[] arr) {
		int result = 0;
		
		for (int i = 0; i < edges.length; i++) {
			result += adj[arr[edges[i][0]]][arr[edges[i][1]]] ? 1 : 0;
		}
		
		return result;
	}
	
	/**
	 * Returns the number nCk
	 * 
	 * @param n
	 * @param k
	 * @return nCk
	 * 
	 * @precondition k < n
	 */
	public static int choose(int n, int k) {	
		return chooseCache[n][k];
	}

	/**
	 * Populates ans with the mth lexicographic subset of size k from n vertices (defined at top)
	 */
	public static void getElement(int m, int[] ans) {
		int a = n;
		int b = k;
		int x = (chooseCache[n][k] - 1) - m; // x is the "dual" of m

		for (int i = 0; i < k; i++) {
			ans[i] = getLargestV(a, b, x); // largest value v, where v < a and vCb < x
			x = x - chooseCache[ans[i]][b];
			a = ans[i];
			b--;
		}

		for (int i = 0; i < k; i++) {
			ans[i] = (n - 1) - ans[i];
		}
	}
	
	/**
	 * Same as above, but you can specify n and k.
	 * I use this for getEdgePossibilities()
	 */
	public static int[] getElement(int m, int n, int k) {
		int a = n;
		int b = k;
		int x = (choose(n, k) - 1) - m; // x is the "dual" of m
		int[] ans = new int[k];

		for (int i = 0; i < k; i++) {
			ans[i] = getLargestV(a, b, x); // largest value v, where v < a and vCb < x
			x = x - choose(ans[i], b);
			a = ans[i];
			b--;
		}

		for (int i = 0; i < k; i++) {
			ans[i] = (n - 1) - ans[i];
		}
		
		return ans;
	}

	/**
	 * Returns largest value v where v < a and Choose(v,b) <= x
	 */
	static int getLargestV(int a, int b, int x) {
		int v = a - 1;
		
		while (chooseCache[v][b] > x) {
			v--;
		}
		
		return v;
	}
	
	/**
	 * Grab an array of edges to check for cliques. These are represented 
	 * as a tuple of indices into the array returned by getElement(m), e.g.:
	 * 
	 * {
	 * 	{0, 1},
	 * 	{0, 2},
	 *  {1, 2}
	 * }
	 * 
	 * For k = 3
	 * 
	 */
	public static int[][] getEdgePossibilities() {
		int numEdges = choose(k, 2);
		int[][] ans = new int[numEdges][2];
		
		for (int i = 0; i < numEdges; i++) {
			ans[i] = getElement(i, k, 2);
		}
		
		return ans;
	}
	
	private static boolean[][] getAdjMatrixFromBoolArray(boolean[] arr) {
		int idx = 0;
		boolean[][] adj = new boolean[n][n];
		
		for (int i = 0; i < n; i++) {
			for (int j = i + 1; j < n; j++) {
				adj[i][j] = arr[idx];
				adj[j][i] = arr[idx];
				idx++;
			}
		}
		
		return adj;
	}
	
	/**
	 * Maps 1 to true, 0 to false
	 */
	private static boolean[] charToBoolean(char[] bits) {
		boolean[] result = new boolean[bits.length];
		
		for (int i = 0; i < bits.length; i++) {
			result[i] = bits[i] == '1';
		}
		
		return result;
	}
}
