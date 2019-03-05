/* Jonathan Caspar, 20059041
 * Johnny Pho, 20046014
 */

import java.util.*;

public class Bruijn{
	//G??e un cycle de de Bruijn pour des mots de longueur n
	//compos? de k symboles diff?ents
	public static String B(int k, int n){
		String result = "";

		//Taille de result
		int size = (int) Math.pow(k,n);

		//Emmagasine en memoire quel index du "mot du haut" a ??visit?		
		BitSet visited = new BitSet(size);

		int[] lowerWord = new int[size];
		int[] upperWord = new int[size];
		int[] correspondance = new int[size];

		//Formation de lowerWord (suite cyclique de l'alphabet) et upperWord (lowerWord trié)
		//et correspondance entre upperWord/lowerWord
		int lowerK = 0;
		int upperK = 0;
		
		for (int i = 0; i < size; i++) {
			lowerWord[i] = lowerK;
			lowerK = (lowerK+1) % k;
			
			correspondance[i] = upperK + (i % (size/k))*k;
			
			upperWord[i] = upperK;
			if( ((i+1) % (size/k)) == 0 ) upperK++;
			
		}

		//Calcul des cycles
		int[] cycles = new int[size];
		int cyclePos = 0;
	
		while(visited.cardinality() <= size){ // tant qu'on a pas tout visité			
			int first = visited.nextClearBit(0);
			int last = first;

			visited.set(first);

			while (visited.cardinality() <= size) {
				cycles[cyclePos++] = last;
				last = correspondance[last];
				visited.set(last);
				
				if(last == first) break;
				
			}
		}
		
		//Substitution alphabétique des cycles
		for (int x : cycles) {
			result += upperWord[x];
		}

		return result;
	}

	public static void main(String args[]){
		System.out.println(B(10,4));
	}
}