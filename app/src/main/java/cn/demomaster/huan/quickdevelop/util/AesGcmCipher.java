package cn.demomaster.huan.quickdevelop.util;

/*
 * Copyright 2018 nandsito
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.GCMParameterSpec;

/**
 * 已验证
 * Class that encapsulates KeyStore, AES key and AES-GCM cipher features.
 */
public class AesGcmCipher {

    /**
     * Default nonce size in bytes.
     */
    public static final int NONCE_SIZE = 12;

    /**
     * Chosen AES key size in bits.
     */
    private static final int AES_KEY_SIZE = 128;

    /**
     * Default authentication tag size in bits.
     */
    private static final int AUTHENTICATION_TAG_SIZE = 128;

    /**
     * Android KeyStore type.
     */
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";

    /**
     * Alias for the application AES key.
     */
    private static final String ALIAS_KEY = "lSzWQIvbJHutWFHD";

    /**
     * AES-GCM cipher.
     */
    private static final String CIPHER_AES_GCM = "AES/GCM/NoPadding";

    /**
     * Keystore from the application-specific Android provider.
     */
    private KeyStore mKeyStore;

    @RequiresApi(api = Build.VERSION_CODES.P)
    public AesGcmCipher() {
        setupKeystore();
        insertKeyIntoKeystore(createAesKey());
    }

    /**
     * Load Android keystore.
     */
    private void setupKeystore() {
        try {
            mKeyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
            mKeyStore.load(null);
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException |
                IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Insert a key into the keystore if there's none yet.
     *
     * @param key key to be inserted
     */
    private void insertKeyIntoKeystore(Key key) {
        try {
            if (!mKeyStore.containsAlias(ALIAS_KEY)) {
                mKeyStore.setKeyEntry(ALIAS_KEY, key, null, null);
            }
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create an AES key for both encryption and decryption with AES-GCM cipher. The key requires
     * the device to be unlocked for decryption.
     *
     * @return a new AES key
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    private Key createAesKey() {
        try {
            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(ALIAS_KEY,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT);
            builder.setKeySize(AES_KEY_SIZE);
            builder.setBlockModes(KeyProperties.BLOCK_MODE_GCM);
            builder.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE);
            builder.setUnlockedDeviceRequired(true);

            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES);
            keyGenerator.init(builder.build());
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Encrypt a piece of text with AES-GCM cipher.
     * <p>
     * Note that the cipher can encrypt any byte sequence.
     *
     * @param plaintext text to be encrypted
     * @return concatenated nonce and cipher output
     */
    public byte[] doEncrypt(byte[] plaintext) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_AES_GCM);
            cipher.init(Cipher.ENCRYPT_MODE, mKeyStore.getKey(ALIAS_KEY, null));
            byte[] nonce = cipher.getIV();
            byte[] ciphertext = new byte[NONCE_SIZE + cipher.getOutputSize(plaintext.length)];

            System.arraycopy(nonce, 0, ciphertext, 0, NONCE_SIZE);

            cipher.doFinal(plaintext, 0, plaintext.length, ciphertext, NONCE_SIZE);
            return ciphertext;
        } catch (ShortBufferException | NoSuchAlgorithmException | NoSuchPaddingException |
                KeyStoreException | InvalidKeyException | UnrecoverableKeyException |
                BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Decrypt a ciphertext with AES-GCM.
     *
     * @param ciphertext concatenated nonce and encryption cipher output
     * @return plaintext
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public byte[] doDecrypt(byte[] ciphertext) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_AES_GCM);
            GCMParameterSpec gcmParameterSpec =
                    new GCMParameterSpec(AUTHENTICATION_TAG_SIZE, ciphertext, 0, NONCE_SIZE);
            cipher.init(Cipher.DECRYPT_MODE, mKeyStore.getKey(ALIAS_KEY, null), gcmParameterSpec);

            byte[] plaintext = new byte[cipher.getOutputSize(ciphertext.length - NONCE_SIZE)];
            cipher.doFinal(ciphertext, NONCE_SIZE, ciphertext.length - NONCE_SIZE, plaintext, 0);
            return plaintext;
        } catch (ShortBufferException | NoSuchAlgorithmException | NoSuchPaddingException |
                KeyStoreException | InvalidKeyException | InvalidAlgorithmParameterException |
                UnrecoverableKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            AesGcmCipher mAesGcmCipher = new AesGcmCipher();
            byte[] plaintext = "國際公司讓他仨個地方·".getBytes(CHARSET_UTF8);
            byte[] ciphertext = mAesGcmCipher.doEncrypt(plaintext);

            String S3 = Base64.encodeToString(ciphertext, Base64.NO_WRAP);
            System.out.println("S3=" + S3);
            try {
                byte[] encrypted = (new BASE64Decoder()).decodeBuffer(S3);
                byte[] plaintext3 = mAesGcmCipher.doDecrypt(encrypted);
                System.out.println("plaintext3=" + new String(plaintext3));
                //System.out.println("解密111111：" + AESUtils200.decrypt(S3, AES_SECRET_SERVER));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }
}
