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
		int keyIndex = 0;
		
		//Parcours des courants C1 jusqu'à C_keysize avec différente taille de clé p
		for (int i = 0; i < text.length(); i++) {
			char letter = text.charAt(i);
			
			if(letter >= 'a' && letter <= 'z') { // si ce n'est pas une lettre on avance à la prochaine lettre
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
		
		//Parcours du courant C1 avec différente taille de clé p
		for (int key = 1; key < text.length(); key++){
			double[] obsFreq = new double[26];
			double freq = 0;
			double freqTotal = 0;
			
			// Parcours du courant p
			int indexCourant = -1;
			
			for (int i = 0; i < text.length(); i++) {
				char letter = text.charAt(i);
				
				if(letter >= 'a' && letter <= 'z') { // si ce n'est pas une lettre on avance à la prochaine lettre
					if(indexCourant % key == 0) {
						int letterFound = letter - 'a';
						obsFreq[letterFound]++;
						freqTotal++;
					}
					indexCourant++;
				}
			}

			// Calcul de la somme du carré des fréquences observées
			for(int i = 0; i < 26 ; i++){
				obsFreq[i] = obsFreq[i] / freqTotal;
				freq += (obsFreq[i]*obsFreq[i]);
			}
			
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

		//Fréquences théorique des lettres en anglais: f[0]=a, f[1]=b, etc.
		double[] f = new double[]{0.082,0.015,0.028,0.043,0.127,0.022,0.02,
			0.061,0.07,0.02,0.08,0.04,0.024,0.067,0.015,0.019,0.001,0.06,
			0.063,0.091,0.028,0.02,0.024,0.002,0.02,0.001};

		int[] potential_key = new int[keySize];
	
		//Parcours des courants C1 jusqu'à C_keysize avec différente taille de clé p
		for (int courant = 0; courant < keySize; courant++){
			//System.out.println("----- Courant " + (courant));
			double[] obsFreq = new double[26];
			double freqTotal = 0;
			
			// Parcours du courant			
			for (int i = courant; i < parsedText.length(); i++) {
				char letter = parsedText.charAt(i);
				
				if((i % keySize) == courant) {
					int letterFound = letter - 'a';
					//System.out.println("----Found --- "+letter);
					obsFreq[letterFound]++;
					freqTotal++;
				}
			}

			// Normalisation des fréquences observées pour le courant actuel
			for(int i = 0; i < 26 ; i++){
				obsFreq[i] = obsFreq[i] / freqTotal;
			}
			
			
			//Calculer fréquence pour chaque k (clé de décalage possible)
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
					//System.out.println("Courant : "+courant+ " k = "+ k + " = "+alphabet.charAt(k));
					
				}
				//System.out.println("k = "+ k+ " = "+alphabet.charAt(k) +" freq = "+ k_freq);

			}

		}
		
		for(int r = 0; r < keySize; r++) {
			result += alphabet.charAt(potential_key[r]); 
		}
		System.out.println(result);
		return result;
	}
	
	public static String extractOnlyLetters(String text) {
		String result = "";
				
		for (int i = 0; i < text.length(); i++) {
			char letter = text.charAt(i);
			
			if(letter >= 'a' && letter <= 'z') { // si ce n'est pas une lettre on avance à la prochaine lettre
				result += letter;

			}
		}
		
		return result;
	}

	public static void main(String args[]){
		String text = "";

		try{
			text += readFile("cipherOriginal/cipher.txt", StandardCharsets.UTF_8);
		}catch(IOException e) {
			System.out.println("Can't load file");
		}
		//TO DO: Vous devez trouver la tolérance nécessaire
		//à utiliser pour trouver la longueur de la clef
		int tolerance = 1;

		int keySize = getKeySize(text, tolerance);
		System.out.println(keySize);
		
		String key = getKey(text, keySize);

		text = decrypt(text, key);
		try (PrintWriter out = new PrintWriter("result.txt")) {
		    out.println(text);
		}catch(IOException e) {
			System.out.println("Can't write file");
		}
	}
}