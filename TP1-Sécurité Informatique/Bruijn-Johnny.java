/* Jonathan Caspar, 20059041
 * Johnny Pho, 20046014
 */

import java.util.*;

public class Bruijn{
	//Gﾃｩnﾃｨre un cycle de de Bruijn pour des mots de longueur n
	//composﾃｩs de k symboles diffﾃｩrents
	public static String B(int k, int n){
		String result = "";

		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		//Taille de result
		int size = (int) Math.pow(k,n);

		//Emmagasine en mﾃｩmoire quel index du "mot du haut"
		//a ﾃｩtﾃｩ visitﾃｩ
		BitSet visited = new BitSet(size);
		visited.set(0, size, true);
		
		int[] link = new int[size];
		int alphaCut = size / k;
		int jump = 0;
		boolean skip = false;
		
		for(int i = 0; i < size; i++) {
			if(((i % alphaCut) == 0) && skip) {
				jump++;
			}
			else skip = true;
			
			link[i] = ((i % alphaCut) * k) + jump;
			
		}
		/*
		for(int i = 0; i < 16; i++) {
	         if((i % 2) == 0) visited.set(i);
	      }
		System.out.println(visited);
		visited.clear(0);
		System.out.println(visited.get(0));
		System.out.println(visited);
		*/
		//TO DO : Quelques variables ﾃ� initialiser iﾃｧi
		//while(result.length() != size){
			//TO DO
			for(int i = 0; i < size; i++) {
				if(visited.get(i)) {
					visited.clear(i);
					result += Character.toString(alphabet.charAt(i / alphaCut));
					int next = i;
					boolean endLink = true;
					while(endLink) {
						System.out.println(next);
						if(link[next] != i) {
							next = link[next];
							result += Character.toString(alphabet.charAt(next / alphaCut));
							visited.clear(next);
						}
						else {
							endLink = false;
						}
					}
					System.out.println("-----");
				}
				//System.out.println(visited.get(i) + " for " + i);
			}
		//}
		
		return result;
	}

	public static void main(String args[]){

		System.out.println(B(10,4));

	}
}