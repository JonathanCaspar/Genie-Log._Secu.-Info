/* Jonathan Caspar, 20059041
 * Johnny Pho, 20046014
 */

import java.util.*;

import javafx.scene.shape.Box;

public class Differential{
	//Écrire votre numéro d'équipe içi !!! #12
	public static int teamNumber = 0;

	public static SPNServer server = new SPNServer();

	//Différentielle d'entrée \Delta_P
	//ex : ""0000 1011 0000 0000""
	//final = 0000111000000000
	public static String plain_diff = "0000101100000000";

	//Différentielle intermédiaire \Delta_I
	//ex : "0000 0110 0000 0110"
	//final = 0100000000000100
	public static String int_diff = "0100000000000100";

	//Boîte à substitutions de l'exemple de la démonstration #3
	public static String[] sub_box_exemple = new String[]{"1110", "0100", "1101", "0001", "0010", "1111", "1011", "1000",
			   												"0011", "1010", "0110", "1100", "0101", "1001", "0000", "0111"};

	public static String[] sub_box_inv_exemple = new String[]{"1110", "0011", "0100", "1000", "0001", "1100", "1010", "1111", 
				   									  			"0111", "1101", "1001", "0110", "1011", "0010", "0000", "0101"};

	//Sorties des boîtes à substitutions du SPN     0    	1		2		3		4		5		6		7
	public static String[] sub_box = new String[]{"0010", "1011", "1001", "0011", "0111", "1110", "1101", "0101", 
												  "1100", "0110", "0000", "1111", "1000", "0001", "0100", "1010"};
 	//												8		9		A 		B 		 C 		D 		E 		 F
	//Entrées des boîtes à substitutions du SPN        0    	1		2		3		4		5		6		7
	public static String[] sub_box_inv = new String[]{"1010", "1101", "0000", "0011", "1110", "0111", "1001", "0100", 
												      "1100", "0010", "1111", "0001", "1000", "0110", "0101", "1011"};
    //													8		9		A 		B 		 C 		D 		E 		 F

	//Permutations : --> Notez que la permutation "perm" inverse est la même puisqu'elle est symmétrique
	public static int[] perm = new int[]{0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15};

	public static int[] pc1 = new int[]{15, 10, 5, 0, 16, 9, 7, 1, 17, 3, 19, 8, 6, 4, 18, 12, 14, 11, 13, 2};

	public static int[] pc1_inv = new int[]{3, 7, 19, 9, 13, 2, 12, 6, 11, 5, 1, 17, 15, 18, 16, 0, 4, 8, 14, 10};

	public static int[] pc2 = new int[]{9, 7, 0, 8, 5, 1, 4, 2, 16, 12, 19, 10, 17, 15, 13, 14};

	public static int[] pc2_inv = new int[]{2, 5, 7, 6, 4, 1, 3, 0, 11, 9, 14, 15, 13, 8, 12,10};

	public static int[][] produceDiffTable(){
		int[][] result = new int[16][16]; 

		for(int i = 0; i < 16; i++){
			int[] line = new int[16];
			result[i] = line;
		}
		
		// Calculer deltaY = S(I) xor S(I xor J)
		for(int i = 0; i < 16; i++){		
			String I = toBinary(i, 4);
			String sub_I = sub(I, sub_box); // S(I)

			for(int j = 0; j < 16; j++){
				String J = toBinary(j, 4);
				String IxorJ  = xor(I, J); // I xor J
				String sub_IxorJ = sub(IxorJ, sub_box); // S(I xor J)
				
				int deltaX = Integer.parseInt(J, 2);
				int deltaY = Integer.parseInt(xor(sub_I, sub_IxorJ), 2);
				result[deltaX][deltaY]++;
			}
		}
		return result;
	}
	
	public static String toBinary(int value, int nbOfDigits) {
		String text = Integer.toBinaryString(value);

		while(text.length() != nbOfDigits){
			text = "0" + text;
		}

		return text;
	}

	//Retroune 16 bits aléatoires en String
	public static String getRandomPlaintext(){
		String text = Integer.toBinaryString((int) Math.floor(Math.random()* 65536));

		while(text.length() != 16){
			text = "0" + text;
		}

		return text;
	}

	//Permute l'input en utilisant la permutation perm
	//À utiliser aussi avec pc1, pc1_inv, etc.
	public static String permute(String input, int[] perm){
		String output = "";

		for(int i = 0; i < perm.length; i++){
			output += input.charAt(perm[i]);
		}

		return output;
	}

	//Prend une entrée de 4 bits et retourne la valeur
	//associée dans l'argument sub_box
	public static String sub(String input, String[] sub_box){
		int value = 0;

		for(int i = 0; i < input.length(); i++){
			value <<= 1;

			if(input.charAt(i) == '1'){
				value += 1;
			}
		}

		return sub_box[value];
	}

	//Retourne l'input ayant fait "amount" rotation(s) vers la gauche
	public static String left_shift(String input, int amount){
		return input.substring(amount) + input.substring(0, amount);
	}

	//Retourne l'input ayant fait "amount" rotation(s) vers la droite
	public static String right_shift(String input, int amount){
		return input.substring(input.length() - amount) + input.substring(0, input.length() - amount);
	}

	//Retourne [k_1, k_2, k_3, k_4, k_5] calculées à partir  
	//de la clef maître "master" selon la génération de  
	//sous-clefs de la troisième démonstration
	public static String[] gen_keys(String master, int n){
		String[] result = new String[n];

		String pc1_res = permute(master, pc1);

		String left = pc1_res.substring(0,10);
		String right = pc1_res.substring(10);

		for(int i = 0; i < n; i++){
			int shift = i % 2 + 1;

			left = left_shift(left, shift);
			right = left_shift(right, shift);

			String temp = left + right;

			result[i] = permute(temp, pc2);
		}

		return result;
	}

	//Retourne un ou-exclusif entre les chaînes de caractères a et b
	public static String xor(String a, String b){
		if(a.length() != b.length()){
			return null;
		}
		String result = "";

		for(int i = 0; i < a.length(); i++){
			result += a.charAt(i) ^ b.charAt(i);
		}
		return result;
	}


	public static String encrypt(String plaintext, String[] subkeys){
		String cipher = plaintext;

		for(int i = 0; i < 4; i++){
			//sub-key mixing
			//TO DO

			//substitution
			//TO DO

			//permutation
			//TO DO
		}

		//Final sub-key mixing (5th sub-key)
		//TO DO

		return cipher;
	}

	public static String getPartialSubkey(){
		int[] counts = new int[256];

		ArrayList<String> plaintexts = new ArrayList<>();

		for(int i = 0; i < 1000; i++){
			//Cr�ation de paires de messages clairs qui satisfont
			//la diff�rentielle d'entr�e \Delta_P

			plaintexts.add( xor(plain_diff, getRandomPlaintext()) );
			
		}

		//Encryption de ces messages clairs
		ArrayList<String> ciphers = server.encrypt(plaintexts,teamNumber);

		boolean[] boites_impliquees = new boolean[] {false, false, false, false};
		int[][] k5_bits_impliquees = new int[4][4];

		//D�terminer les boites de substitution impliqu�es
		for(int i = 0; i < 4; i++) {
			for(int index = (4*i); index < (4*i+4); index++) {
				
				if(int_diff.charAt(index) == '1') {
					boites_impliquees[i] = true;
					break;
				}
			}
		}
		
		//D�terminer les bits impliqu�es apr�s permutation de la sortie des boites impliqu�es
		for(int i = 0; i < 4; i++) {
			
			if(boites_impliquees[i]) {	
				for(int index = (4*i); index < (4*i+4); index++) {
					k5_bits_impliquees[i][index-(4*i)] = perm[index];
				}
			}
		}
		
		System.out.println("boites_impliquees : " + Arrays.toString(boites_impliquees));
		System.out.println("bits_impliquees : " + Arrays.deepToString(k5_bits_impliquees));

		for(int j = 0; j < 256; j++){
			//Affectation du nombre de fois que chaque sous-clef partielle
			//j possible nous donne la diff�rentielle interm�diaire 
			//"int_diff" � counts[j]
			String jBinary = toBinary(j, 8);			
						
			for(String cipher : ciphers) {
				
				// Restriction du cipher aux sections concern�es 
				String cipher8bits = "";
				for (int i = 0; i < boites_impliquees.length; i++) {
					if(boites_impliquees[i]) {
						cipher8bits += cipher.substring(4*i, 4*i+4);
					}
				}

				String cipher_xor = xor(cipher8bits, jBinary);
				
				String sub_key =  permute( cipher_xor , perm);
				
				String final_sub_key = "0000" + sub(sub_key.substring(0, 4), sub_box_inv_exemple) + "0000" + sub(sub_key.substring(4,8), sub_box_inv_exemple);
				
				if(final_sub_key.equals(int_diff)) {
					counts[j]++;
				}
			}
			
		}

		//D�terminer la fr�quence de clef la plus haute
		int max = 0;
		int maxIndex = -1;
		System.out.println("Computing max..");
		for(int j = 0; j < 256; j++){
			if(counts[j] > max) {
				max = counts[j];
				maxIndex = j;
			}
			
		}
		
		if(maxIndex == -1) return "NO VALUE FOUND";
		System.out.println("Value of maxIndex = "+maxIndex+" and max = "+max);
		
		String highest_freq_key = toBinary(maxIndex, 8);
		System.out.println("Found highest prob for key = "+ highest_freq_key);
		//D�terminer la sous-clef k_5^* avec des 'X' au bits inconnues
		String sub_key_k5 = "XXXX" + highest_freq_key.substring(0, 4) + "XXXX" + highest_freq_key.substring(4);
		return sub_key_k5;
	}

	public static String getPartialMasterkey(String partialSubkey, int n){
		String result = "";

		//Retrouver la clef maître partielle grâce au résultat
		//de getPartialSubkey() en insérant des 'X' aux bits inconnues

		//TO DO

		return result;
	}

	public static String bruteForce(String partialMasterkey){
		String result = "";
		boolean found = false;

		//Generating random plaintext
		String text = getRandomPlaintext();

		//String res_server = server.encrypt(text,teamNumber);

		for(int i = 0; i < 4096 && !found; i++){
			//Déterminer lesquelles des 2^12 (4096) possibilités de bits
			//manquantes donnent la bonne clef maître

			//TO DO
		}

		//Retourner la clef maître
		return result;
	}

	public static void main(String args[]){
		//Génération de la table des fréquences des différentielles de sortie
		//pour chaque différentielle d'entrée
		//System.out.println(Arrays.deepToString(produceDiffTable()));

		//Calcul de la sous-clef partielle k_5^* 0   
		String partialSubkey = getPartialSubkey();
		//System.out.println("Sous-clef partielle k_5^* : " + partialSubkey);

		//Calcul de la clef maître partielle k^* 
		//String partialMasterkey = getPartialMasterkey(partialSubkey, 5);
		//System.out.println("Clef maître partielle k^* : " + partialMasterkey);

		//Calcul de la clef maître par fouille exhaustive 
		//String masterkey = bruteForce(partialMasterkey);
		//System.out.println("Clef maître k :             " + masterkey);

		//Information utile --> clef de l'exemple de la démo 3 : 00100100001111010101
	}

}