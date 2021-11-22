import java.io.*;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESAlgo {
	
	private static final String algo = "AES";
	private byte[] keyValue;
	private final Base64.Encoder encoder;
	private final Base64.Decoder decoder;
	public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	public AESAlgo(String key) {
		keyValue = key.getBytes();
		this.encoder = Base64.getUrlEncoder();
		this.decoder = Base64.getUrlDecoder();
	}

	public String encrypt(String Data) throws Exception {
		
		Key key = generateKey();
		Cipher c = Cipher.getInstance(algo);
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encVal = c.doFinal(Data.getBytes());
		String encryptedValue = encoder.encodeToString(encVal);
		return encryptedValue;
	}
	public String decrypt(String encryptedData) throws Exception {

		Key key = generateKey();
		Cipher c = Cipher.getInstance(algo);
		c.init(Cipher.DECRYPT_MODE, key);
		byte[] decodedValue = decoder.decode(encryptedData);
		byte[] decValue = c.doFinal(decodedValue);
		String decryptedValue = new String(decValue);

		return decryptedValue;
	}
	private Key generateKey() throws Exception {
		
		Key key = new SecretKeySpec(keyValue, algo);
		return key;
	}

}