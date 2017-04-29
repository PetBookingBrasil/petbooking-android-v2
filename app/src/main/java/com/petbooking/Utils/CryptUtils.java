package com.petbooking.Utils;

import android.provider.Settings;
import android.util.Base64;

import com.petbooking.BuildConfig;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * Created by Luciano José on 28/04/2017.
 */

public abstract class CryptUtils {
    private static final String UTF8 = "utf-8";
    private static final String salt = Settings.Secure.ANDROID_ID;
    private static final char[] SEKRIT = CryptUtils.class.getName().toCharArray();
    private static final String ALGORITHM = "PBEWithMD5AndDES";

    public static String encrypt(String decryptedText) {
        try {
            final byte[] bytes = decryptedText != null ? decryptedText.getBytes(UTF8) : new byte[0];
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(SEKRIT));
            Cipher pbeCipher = Cipher.getInstance(ALGORITHM);
            pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(salt.getBytes(UTF8), 20));
            return new String(Base64.encode(pbeCipher.doFinal(bytes), Base64.DEFAULT), UTF8);
        } catch (Exception e) {
            return "";
        }
    }

    public static String decrypt(String encryptedText) {
        try {
            final byte[] bytes = encryptedText != null ? Base64.decode(encryptedText, Base64.DEFAULT) : new byte[0];
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(SEKRIT));
            Cipher pbeCipher = Cipher.getInstance(ALGORITHM);
            pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(salt.getBytes(UTF8), 20));
            return new String(pbeCipher.doFinal(bytes), UTF8);
        } catch (Exception e) {
            return "";
        }
    }
}
