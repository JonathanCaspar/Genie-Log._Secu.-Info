/* Jonathan Caspar, 20059041
 * Johnny Pho, 20046014
 */

import java.util.*;

public class Bruijn{
	//Génère un cycle de de Bruijn pour des mots de longueur n
	//composés de k symboles différents
	public static String B(int k, int n){
		String result = "";

		//Taille de result
		int size = (int) Math.pow(k,n);

		//Emmagasine en mémoire quel index du "mot du haut"
		//a été visité
		BitSet visited = new BitSet(size);

		//TO DO : Quelques variables à initialiser içi
		while(result.length() != size){
			//TO DO
		}

		return result;
	}

	public static void main(String args[]){

		System.out.println(B(10,4));

	}
}