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

		//TO DO

		return result;
	}

	public static int getKeySize(String text, double tolerance){
		int keySize = 0;

		//TO DO

		return keySize;
	}

	public static String getKey(String text, int keySize){
		String result = "";

		//Fréquences théorique des lettres en anglais: f[0]=a, f[1]=b, etc.
		double[] f = new double[]{0.082,0.015,0.028,0.043,0.127,0.022,0.02,
			0.061,0.07,0.02,0.08,0.04,0.024,0.067,0.015,0.019,0.001,0.06,
			0.063,0.091,0.028,0.02,0.024,0.002,0.02,0.001};

		//TO DO

		return result;
	}

	public static void main(String args[]){
		String text = "";

		try{
			text += readFile("cipher.txt", StandardCharsets.UTF_8);
		}catch(IOException e) {
			System.out.println("Can't load file");
		}

		//TO DO: Vous devez trouver la tolérance nécessaire
		//à utiliser pour trouver la longueur de la clef
		int tolerance = 0;

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