import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.KeyFactory;	
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.BadPaddingException;
import java.security.spec.InvalidKeySpecException;	
import java.util.Base64;	
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import java.security.spec.PKCS8EncodedKeySpec;
public class RSA
{
	private PrivateKey privateKey;
	private PublicKey publicKey;
	private String Encodedpublickey;
	private String Encodedprivatekey;
	public void Keygenerate()
	{
		try
		{
			KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
			keygen.initialize(2048);
			KeyPair pair = keygen.generateKeyPair();
		    this.privateKey= pair.getPrivate();
		    this.publicKey=pair.getPublic();
		    Encodedpublickey=Base64.getEncoder().encodeToString(publicKey.getEncoded());
	        Encodedprivatekey=Base64.getEncoder().encodeToString(privateKey.getEncoded());
		}
		catch(NoSuchAlgorithmException e){}
	}

	public byte[] encrypt(String text,String b64publickey) throws IllegalBlockSizeException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException
	{
		PublicKey pk = null;
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(b64publickey.getBytes()));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        pk = keyFactory.generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, pk);
		return cipher.doFinal(text.getBytes());
	}

	public static String decrypt(byte[] data, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException 
	{
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(data));
     }

	public String decrypt(String cmessage, String b64privatekey) throws IllegalBlockSizeException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException
	{
		
		PrivateKey pk=null;
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(b64privatekey.getBytes()));
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        pk = keyFactory.generatePrivate(keySpec);
        return decrypt(Base64.getDecoder().decode(cmessage.getBytes()),pk);
	}

	public String getpubkey()
    {
        return Encodedpublickey;
    }

    public String getprivkey()
    {
        return Encodedprivatekey;
    }

	public static void main(String args[]) throws IllegalBlockSizeException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException
	{
		RSA obj=new RSA();
		obj.Keygenerate();
		String pubkey=obj.Encodedpublickey;
		String encryptedString = Base64.getEncoder().encodeToString(obj.encrypt("HElloo how are you", pubkey));
		System.out.println(encryptedString);
		String decryptedString = obj.decrypt(encryptedString, obj.Encodedprivatekey);
		System.out.println(decryptedString);
	}
}