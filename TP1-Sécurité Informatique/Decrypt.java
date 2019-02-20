/* Jonathan Caspar, 20059041
 * Johnny Pho, 20046014
 */

import java.io.IOException; 
import java.nio.charset.*; 
import java.nio.file.Files; 
import java.nio.file.Paths;
import java.io.PrintWriter;

public class Decrypt{
	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static String decrypt(String text, String key){
		String result = "";
		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		int keyIndex = 0;
		
		//Parcours du texte chiffré
		for (int i = 0; i < text.length(); i++){
			//Décryptage pour chaque période
			int cipherValue = text.charAt(i) - 'a';
			keyIndex = (keyIndex + 1)%26;
			int keyValue 	= Character.getNumericValue(key.charAt(keyIndex));
			//System.out.println("Found keyValue = "+keyValue);
			int plainValue  = (26 + Math.abs(cipherValue - keyValue)) % 26 ;
			result += alphabet.charAt(plainValue);
		}
		/*
		int cipherValue = 'a' - 'a';
		int keyValue 	= Character.getNumericValue(key.charAt(0));
		int plainValue  = Math.abs( 26 + (cipherValue - keyValue) % 26 );
		result += alphabet.charAt(plainValue);
		System.out.println("a ("+cipherValue+") - "+keyValue+" = "+  alphabet.charAt(Math.abs(26+(cipherValue - keyValue)%26) ));
*/System.out.println(result);
		return result;
	}

	public static int getKeySize(String text, double tolerance){
		int keySize = 0;
		
		//Parcours du courant C1 avec différente taille de clé p
		for (int p = 1; p < text.length(); p++){
			double[] obsFreq = new double[26];
			double freq = 0;
			double freqTotal = 0;
			
			// Parcours du courant p
			for(int i = 0; (1 + (i*p)) <= text.length() ; i++){
				char letter = text.charAt(i*p);
				if ((letter >= 'a' && letter <= 'z')) {
					int letterFound = letter - 'a';
					//System.out.println("Found " + text.charAt(i*p) + " = " + letterFound);
					obsFreq[letterFound]++;
					freqTotal++;
				}
				
			}
			
			// Calcul de la somme du carré des fréquences observées
			for(int i = 0; i < 26 ; i++){
				double freqNormalized = obsFreq[i]/freqTotal;
				freq += (freqNormalized*freqNormalized);
			}
			
			if(Math.abs(freq-0.065) < tolerance/100000) keySize = p;
			
		}
		System.out.println(keySize);
		return keySize;
	}

	public static String getKey(String text, int keySize){
		String result = "";

		//Fréquences théorique des lettres en anglais: f[0]=a, f[1]=b, etc.
		double[] f = new double[]{0.082,0.015,0.028,0.043,0.127,0.022,0.02,
			0.061,0.07,0.02,0.08,0.04,0.024,0.067,0.015,0.019,0.001,0.06,
			0.063,0.091,0.028,0.02,0.024,0.002,0.02,0.001};

		int[] potential_key = new int[keySize];
	
		for(int courant = 1; courant < keySize; courant++) {
			
			double[] h = new double[26];
			double freqTotal = 0;
			
			// Parcours du courant p
			for(int i = 0; (courant + (i*keySize)) < text.length() ; i++){
				char letter = text.charAt(courant+(i*keySize));
				
				if ((letter >= 'a' && letter <= 'z')) {
					int letterFound = letter - 'a';
					h[letterFound]++;
					freqTotal++;
				}
				
			}
			
			// Normalisation des fréquences observées
			for(int i = 0; i < 26 ; i++){
				h[i] = h[i]/freqTotal;
			}
			
			//Calculer fréquence pour chaque k (clé de décalage possible)
			double min_dist = 1;
			for(int k = 0; k < 26 ; k++){
				double k_freq = 0;
				
				for(int i = 0; i < 26 ; i++){
					k_freq += (f[i] * h[Math.abs((i-k) % 26)]);
				}
				
				double dist_to_usual_freq = Math.abs(k_freq-0.065);
				if(dist_to_usual_freq < min_dist) {
					min_dist = dist_to_usual_freq;
					potential_key[courant-1] = k;
				}
			}

		}
		
		for(int r = 0; r < keySize; r++) {
			result += potential_key[r];
		}
		System.out.println(result);
		return result;
	}

	public static void main(String args[]){
		String text = "";

		try{
			text += readFile("src/cipher.txt", StandardCharsets.UTF_8);
		}catch(IOException e) {
			System.out.println("Can't load file");
		}
		
		//TO DO: Vous devez trouver la tolérance nécessaire
		//à utiliser pour trouver la longueur de la clef
		int tolerance = 1;

		int keySize = getKeySize(text, tolerance);

		String key = getKey(text, keySize);

		text = decrypt(text, key);

		try (PrintWriter out = new PrintWriter("src/result.txt")) {
		    out.println(text);
		}catch(IOException e) {
			System.out.println("Can't write file");
		}

	}


}