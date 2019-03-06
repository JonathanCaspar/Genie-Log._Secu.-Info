/* Jonathan Caspar, 20059041
 * Johnny Pho, 20046014
 */

import java.io.IOException; 
import java.nio.charset.*; 
import java.nio.file.Files; 
import java.nio.file.Paths;
import java.util.Arrays;
import java.io.PrintWriter;

public class Decrypt{
	public static String alphabet = "abcdefghijklmnopqrstuvwxyz";
	
	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static String decrypt(String text, String key){
		String result = "";
		int keyIndex = 0; // indice cyclique sur la longueur de la cl�
		
		//Parcours des courants C1 jusqu'� C_keysize
		for (int i = 0; i < text.length(); i++) {
			char letter = text.charAt(i);
			
			if(letter >= 'a' && letter <= 'z') { // si ce n'est pas une lettre, on l'ajoute au r�sultat sans la d�crypter
				int keyValue 	= key.charAt(keyIndex) - 'a';
				int cipherValue = letter - 'a';
				int plainValue  = (26+(cipherValue - keyValue))% 26 ;
				keyIndex = (keyIndex + 1) % key.length();
				result += alphabet.charAt(plainValue);
			}
			else result += letter;
		}

		return result;
	}

	public static int getKeySize(String text, double tolerance){
		int keySize = 0;
		
		//Parcours du courant C1 avec diff�rente taille de cl� 'key'
		for (int key = 1; key < text.length(); key++){
			double[] obsFreq = new double[26];
			double freq = 0;
			double nbLettres = 0;
			
			// Parcours du courant p
			int indexLetter = -1; // indice representant la position des lettres uniquement (exclut les chiffres, espaces ..)
			
			for (int i = 0; i < text.length(); i++) {
				char letter = text.charAt(i);
				
				if(letter >= 'a' && letter <= 'z') { // si ce n'est pas une lettre on avance au prochain caract�re
					if(indexLetter % key == 0) { // si c'est l'indice faisant partie du courant, on enregistre l'occurence de cette lettre
						int letterFound = letter - 'a';
						obsFreq[letterFound]++;
						nbLettres++;
					}
					indexLetter++;
				}
			}

			// Calcul de la somme du carr� des fr�quences observ�es
			for(int i = 0; i < 26 ; i++){
				obsFreq[i] = obsFreq[i] / nbLettres;
				freq += (obsFreq[i]*obsFreq[i]);
			}
			
			// Fr�quence trouv�e suffisement proche de celle esp�r�e ?
			if(Math.abs(freq-0.065) < tolerance/100) {
				keySize = key;
				break;
			}		
		}
		return keySize;
	}

	public static String getKey(String text, int keySize){
		String result = "";
		String parsedText = extractOnlyLetters(text);
		int[] potential_key = new int[keySize];

		//Fr�quences th�orique des lettres en anglais: f[0]=a, f[1]=b, etc.
		double[] f = new double[]{0.082,0.015,0.028,0.043,0.127,0.022,0.02,
			0.061,0.07,0.02,0.08,0.04,0.024,0.067,0.015,0.019,0.001,0.06,
			0.063,0.091,0.028,0.02,0.024,0.002,0.02,0.001};
	
		//Parcours des courants C1 jusqu'� C_keysize avec diff�rente taille de cl� p
		for (int courant = 0; courant < keySize; courant++){
			double[] obsFreq = new double[26];
			double freqTotal = 0;
			
			// Parcours des lettres du courant actuel		
			for (int i = courant; i < parsedText.length(); i++) {
				char letter = parsedText.charAt(i);
				
				if((i % keySize) == courant) {
					int letterFound = letter - 'a';
					obsFreq[letterFound]++;
					freqTotal++;
				}
			}

			// Normalisation des fr�quences observ�es pour le courant actuel
			for(int i = 0; i < 26 ; i++){
				obsFreq[i] = obsFreq[i] / freqTotal;
			}
			
			// Calculer fr�quence pour chaque k (cl� de d�calage possible) et selectionner le k qui la minimise
			double minDist = 1;
			
			for(int k = 0; k < 26 ; k++){
				double k_freq = 0;
				
				for(int i = 0; i < 26 ; i++){
					k_freq += (f[i] * obsFreq[(i+k) % 26]);
				}
				
				double distToNormalFreq = Math.abs(k_freq-0.065);
				if(distToNormalFreq < minDist) {
					minDist = distToNormalFreq;
					potential_key[courant] = k;
				}
			}
		}
		
		// Transposition de la s�quence de chiffre en s�quence de lettre
		for(int r = 0; r < keySize; r++) {
			result += alphabet.charAt(potential_key[r]); 
		}
		
		return result;
	}
	
	// Retourne la version uniquement compos�e de lettres d'une entr�e 'text'
	public static String extractOnlyLetters(String text) {
		String result = "";
				
		for (int i = 0; i < text.length(); i++) {
			char letter = text.charAt(i);	
			
			if(letter >= 'a' && letter <= 'z') result += letter;
		}
		
		return result;
	}

	public static void main(String args[]){
		String text = "";

		try{
			text += readFile("cipher.txt", StandardCharsets.UTF_8);
		}catch(IOException e) {
			System.out.println("Can't load file");
		}
		
		int tolerance = 1;

		int keySize = getKeySize(text, tolerance);
		
		String key = getKey(text, keySize);

		text = decrypt(text, key);
		try (PrintWriter out = new PrintWriter("result.txt")) {
		    out.println(text);
		}catch(IOException e) {
			System.out.println("Can't write file");
		}
	}
}