package crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    
    private final SecretKeySpec key;
    private IvParameterSpec iv;
    private Cipher cipher;

    public AES(int length) {
        byte[] byteKey = generateRandomBytes(length);
        key = new SecretKeySpec(byteKey, "AES");
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public AES(int length, byte[] seed) {
        byte[] byteKey = generateRandomBytes(length, seed);
        key = new SecretKeySpec(byteKey, "AES");
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public byte[] encrypt(byte[] message) {
        byte[] ivBytes = generateRandomBytes(16);
        iv = new IvParameterSpec(ivBytes);
        
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            return cipher.doFinal(message);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public byte[] decrypt(byte[] ivBytes, byte[] encrypted) {
        iv = new IvParameterSpec(ivBytes);
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            return cipher.doFinal(encrypted);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public SecretKeySpec getKey() {
        return key;
    }

    public IvParameterSpec getIvSpec() {
        return iv;
    }
    
    private byte[] generateRandomBytes(int numOfBytes) {
        Random random = new SecureRandom();
        
        byte[] byteKey = new byte[numOfBytes];
        random.nextBytes(byteKey);
        
        return byteKey;
    }
    
    private byte[] generateRandomBytes(int numOfBytes, byte[] seed) { 
        Random random = new SecureRandom(seed);
        
        byte[] byteKey = new byte[numOfBytes];
        random.nextBytes(byteKey);
        
        return byteKey;
    }
    
}
