package cn.demomaster.huan.quickdevelop.util;

import android.annotation.SuppressLint;

import org.apache.commons_android.codec.binary.Hex;

import java.nio.charset.Charset;
import java.security.Provider;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES2 {
    private final static String HEX = "0123456789ABCDEF";
    static String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    /**
     * 字符编码(用哪个都可以，要注意new String()默认使用UTF-8编码 getBytes()默认使用ISO8859-1编码)
     */
    public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    /**
     * 加密
     *
     * @param key
     *            密钥
     * @param src
     *            加密文本
     * @return
     * @throws Exception
     */
    public static String encrypt(String key, String src) throws Exception {
        byte[] rawKey = getRawKey(key.getBytes(CHARSET_UTF8));
        byte[] result = encrypt(rawKey, src.getBytes(CHARSET_UTF8));

        System.out.println("encryptBytes="+ Arrays.toString(result));
        return Hex.encodeHexString(result);// toHex(result);
    }

    /**
     * 解密
     *
     * @param key
     *            密钥
     * @param encrypted
     *            待揭秘文本
     * @return
     * @throws Exception
     */
    public static String decrypt(String key, String encrypted) throws Exception {
        byte[] rawKey = getRawKey(key.getBytes(CHARSET_UTF8));
        byte[] enc = Hex.decodeHex(encrypted);//toByte(encrypted);
        //QDLogger.e("key="+key+",encrypted="+encrypted+",enc="+Arrays.toString(enc));
        byte[] result = decrypt(rawKey, enc);
        return new String(result);
    }

    /**
     * 获取256位的加密密钥
     *
     * @param seed
     * @return
     * @throws Exception
     */
    @SuppressLint("TrulyRandom")
    private static byte[] getRawKey(byte[] seed) throws Exception {
        return deriveKeyInsecurely(seed);
        /**
         * 请注意，如果是Android9.0以下的版本可以选择以下的生成加密Key，但是与可能上面的互不兼容，
         * 所以建议直接使用 InsecureSHA1PRNGKeyDerivator 方式生成加密key，这个类是google提供用于兼容9.0 AES生成加密Key的类
         */
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
//            return deriveKeyInsecurely(seed);
//        }
//        KeyGenerator kgen = KeyGenerator.getInstance("AES");
//        SecureRandom sr = null;
//        // 在4.2以上版本中，SecureRandom获取方式发生了改变
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            sr = SecureRandom.getInstance("SHA1PRNG", new CryptoProvider());
//        } else {
//            sr = SecureRandom.getInstance("SHA1PRNG");
//        }
//        sr.setSeed(seed);
//        // 256 bits or 128 bits,192bits
//        kgen.init(128, sr);
//        SecretKey skey = kgen.generateKey();
//        byte[] raw = skey.getEncoded();
//        return raw;
    }

    /**
     * 真正的加密过程
     *
     * @param key
     * @param src
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] key, byte[] src) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(src);
        return encrypted;
    }

    /**
     * 真正的解密过程
     *
     * @param key
     * @param encrypted
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] key, byte[] encrypted)
            throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    private static byte[] deriveKeyInsecurely(byte[] key) {
//        byte[] passwordBytes = key.getBytes(StandardCharsets.US_ASCII);
        //InsecureSHA1PRNGKeyDerivator
        return InsecureSHA1PRNGKeyDerivator.deriveInsecureKey(key, 32);
//        return new SecretKeySpec(InsecureSHA1PRNGKeyDerivator.deriveInsecureKey(key, 32), "AES");
    }
/*

    public static String toHex(String txt) {
        return toHex(txt.getBytes());
    }

    public static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                    16).byteValue();
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }
*/


    public static  class CryptoProvider extends Provider {
        /**
         * Creates a Provider and puts parameters
         */
        public CryptoProvider() {
            super("Crypto", 1.0, "HARMONY (SHA1 digest; SecureRandom; SHA1withDSA signature)");
            put("SecureRandom.SHA1PRNG",
                    "org.apache.harmony.security.provider.crypto.SHA1PRNG_SecureRandomImpl");
            put("SecureRandom.SHA1PRNG ImplementedIn", "Software");
        }
    }


    /*
     *  Licensed to the Apache Software Foundation (ASF) under one or more
     *  contributor license agreements.  See the NOTICE file distributed with
     *  this work for additional information regarding copyright ownership.
     *  The ASF licenses this file to You under the Apache License, Version 2.0
     *  (the "License"); you may not use this file except in compliance with
     *  the License.  You may obtain a copy of the License at
     *
     *     http://www.apache.org/licenses/LICENSE-2.0
     *
     *  Unless required by applicable law or agreed to in writing, software
     *  distributed under the License is distributed on an "AS IS" BASIS,
     *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     *  See the License for the specific language governing permissions and
     *  limitations under the License.
     */



    /**
     * Stripped-down version of the SHA1PRNG provided by the Crypto provider.
     *
     * The Crypto provider that offers this functionality was deprecated on Android.
     *
     * Use this class only to retrieve encrypted data that couldn't be retrieved otherwise.
     */
    private static class InsecureSHA1PRNGKeyDerivator {
        /**
         * Only public method. Derive a key from the given seed.
         *
         * Use this method only to retrieve encrypted data that couldn't be retrieved otherwise.
         *
         * @param seed seed used for the random generator, usually coming from a password
         * @param keySizeInBytes length of the array returned
         */
        public static byte[] deriveInsecureKey(byte[] seed, int keySizeInBytes) {
            InsecureSHA1PRNGKeyDerivator derivator = new InsecureSHA1PRNGKeyDerivator();
            derivator.setSeed(seed);
            byte[] key = new byte[keySizeInBytes];
            derivator.nextBytes(key);
            return key;
        }
        // constants to use in expressions operating on bytes in int and long variables:
        // END_FLAGS - final bytes in words to append to message;
        //             see "ch.5.1 Padding the Message, FIPS 180-2"
        // RIGHT1    - shifts to right for left half of long
        // RIGHT2    - shifts to right for right half of long
        // LEFT      - shifts to left for bytes
        // MASK      - mask to select counter's bytes after shift to right
        private static final int[] END_FLAGS = { 0x80000000, 0x800000, 0x8000, 0x80 };
        private static final int[] RIGHT1 = { 0, 40, 48, 56 };
        private static final int[] RIGHT2 = { 0, 8, 16, 24 };
        private static final int[] LEFT = { 0, 24, 16, 8 };
        private static final int[] MASK = { 0xFFFFFFFF, 0x00FFFFFF, 0x0000FFFF,
                0x000000FF };
        // HASHBYTES_TO_USE defines # of bytes returned by "computeHash(byte[])"
        // to use to form byte array returning by the "nextBytes(byte[])" method
        // Note, that this implementation uses more bytes than it is defined
        // in the above specification.
        private static final int HASHBYTES_TO_USE = 20;
        // value of 16 defined in the "SECURE HASH STANDARD", FIPS PUB 180-2
        private static final int FRAME_LENGTH = 16;
        // miscellaneous constants defined in this implementation:
        // COUNTER_BASE - initial value to set to "counter" before computing "nextBytes(..)";
        //                note, that the exact value is not defined in STANDARD
        // HASHCOPY_OFFSET   - offset for copy of current hash in "copies" array
        // EXTRAFRAME_OFFSET - offset for extra frame in "copies" array;
        //                     as the extra frame follows the current hash frame,
        //                     EXTRAFRAME_OFFSET is equal to length of current hash frame
        // FRAME_OFFSET      - offset for frame in "copies" array
        // MAX_BYTES - maximum # of seed bytes processing which doesn't require extra frame
        //             see (1) comments on usage of "seed" array below and
        //             (2) comments in "engineNextBytes(byte[])" method
        //
        // UNDEFINED  - three states of engine; initially its state is "UNDEFINED"
        // SET_SEED     call to "engineSetSeed"  sets up "SET_SEED" state,
        // NEXT_BYTES   call to "engineNextByte" sets up "NEXT_BYTES" state
        private static final int COUNTER_BASE = 0;
        private static final int HASHCOPY_OFFSET = 0;
        private static final int EXTRAFRAME_OFFSET = 5;
        private static final int FRAME_OFFSET = 21;
        private static final int MAX_BYTES = 48;
        private static final int UNDEFINED = 0;
        private static final int SET_SEED = 1;
        private static final int NEXT_BYTES = 2;
        // Structure of "seed" array:
        // -  0-79 - words for computing hash
        // - 80    - unused
        // - 81    - # of seed bytes in current seed frame
        // - 82-86 - 5 words, current seed hash
        private transient int[] seed;
        // total length of seed bytes, including all processed
        private transient long seedLength;
        // Structure of "copies" array
        // -  0-4  - 5 words, copy of current seed hash
        // -  5-20 - extra 16 words frame;
        //           is used if final padding exceeds 512-bit length
        // - 21-36 - 16 word frame to store a copy of remaining bytes
        private transient int[] copies;
        // ready "next" bytes; needed because words are returned
        private transient byte[] nextBytes;
        // index of used bytes in "nextBytes" array
        private transient int nextBIndex;
        // variable required according to "SECURE HASH STANDARD"
        private transient long counter;
        // contains int value corresponding to engine's current state
        private transient int state;
        /**
         *  constant defined in "SECURE HASH STANDARD"
         */
        private static final int H0 = 0x67452301;
        /**
         *  constant defined in "SECURE HASH STANDARD"
         */
        private static final int H1 = 0xEFCDAB89;
        /**
         *  constant defined in "SECURE HASH STANDARD"
         */
        private static final int H2 = 0x98BADCFE;
        /**
         *  constant defined in "SECURE HASH STANDARD"
         */
        private static final int H3 = 0x10325476;
        /**
         *  constant defined in "SECURE HASH STANDARD"
         */
        private static final int H4 = 0xC3D2E1F0;
        /**
         * offset in buffer to store number of bytes in 0-15 word frame
         */
        private static final int BYTES_OFFSET = 81;
        /**
         * offset in buffer to store current hash value
         */
        private static final int HASH_OFFSET = 82;
        /**
         * # of bytes in H0-H4 words; <BR>
         * in this implementation # is set to 20 (in general # varies from 1 to 20)
         */
        private static final int DIGEST_LENGTH = 20;
        // The "seed" array is used to compute both "current seed hash" and "next bytes".
        //
        // As the "SHA1" algorithm computes a hash of entire seed by splitting it into
        // a number of the 512-bit length frames (512 bits = 64 bytes = 16 words),
        // "current seed hash" is a hash (5 words, 20 bytes) for all previous full frames;
        // remaining bytes are stored in the 0-15 word frame of the "seed" array.
        //
        // As for calculating "next bytes",
        // both remaining bytes and "current seed hash" are used,
        // to preserve the latter for following "setSeed(..)" commands,
        // the following technique is used:
        // - upon getting "nextBytes(byte[])" invoked, single or first in row,
        //   which requires computing new hash, that is,
        //   there is no more bytes remaining from previous "next bytes" computation,
        //   remaining bytes are copied into the 21-36 word frame of the "copies" array;
        // - upon getting "setSeed(byte[])" invoked, single or first in row,
        //   remaining bytes are copied back.
        private InsecureSHA1PRNGKeyDerivator() {
            seed = new int[HASH_OFFSET + EXTRAFRAME_OFFSET];
            seed[HASH_OFFSET] = H0;
            seed[HASH_OFFSET + 1] = H1;
            seed[HASH_OFFSET + 2] = H2;
            seed[HASH_OFFSET + 3] = H3;
            seed[HASH_OFFSET + 4] = H4;
            seedLength = 0;
            copies = new int[2 * FRAME_LENGTH + EXTRAFRAME_OFFSET];
            nextBytes = new byte[DIGEST_LENGTH];
            nextBIndex = HASHBYTES_TO_USE;
            counter = COUNTER_BASE;
            state = UNDEFINED;
        }
        /*
         * The method invokes the SHA1Impl's "updateHash(..)" method
         * to update current seed frame and
         * to compute new intermediate hash value if the frame is full.
         *
         * After that it computes a length of whole seed.
         */
        private void updateSeed(byte[] bytes) {
            // on call:   "seed" contains current bytes and current hash;
            // on return: "seed" contains new current bytes and possibly new current hash
            //            if after adding, seed bytes overfill its buffer
            updateHash(seed, bytes, 0, bytes.length - 1);
            seedLength += bytes.length;
        }
        /**
         * Changes current seed by supplementing a seed argument to the current seed,
         * if this already set;
         * the argument is used as first seed otherwise. <BR>
         *
         * The method overrides "engineSetSeed(byte[])" in class SecureRandomSpi.
         *
         * @param
         *       seed - byte array
         * @throws
         *       NullPointerException - if null is passed to the "seed" argument
         */
        private void setSeed(byte[] seed) {
            if (seed == null) {
                throw new NullPointerException("seed == null");
            }
            if (state == NEXT_BYTES) { // first setSeed after NextBytes; restoring hash
                System.arraycopy(copies, HASHCOPY_OFFSET, this.seed, HASH_OFFSET,
                        EXTRAFRAME_OFFSET);
            }
            state = SET_SEED;
            if (seed.length != 0) {
                updateSeed(seed);
            }
        }
        /**
         * Writes random bytes into an array supplied.
         * Bits in a byte are from left to right. <BR>
         *
         * To generate random bytes, the "expansion of source bits" method is used,
         * that is,
         * the current seed with a 64-bit counter appended is used to compute new bits.
         * The counter is incremented by 1 for each 20-byte output. <BR>
         *
         * The method overrides engineNextBytes in class SecureRandomSpi.
         *
         * @param
         *       bytes - byte array to be filled in with bytes
         * @throws
         *       NullPointerException - if null is passed to the "bytes" argument
         */
        protected synchronized void nextBytes(byte[] bytes) {
            int i, n;
            long bits; // number of bits required by Secure Hash Standard
            int nextByteToReturn; // index of ready bytes in "bytes" array
            int lastWord; // index of last word in frame containing bytes
            // This is a bug since words are 4 bytes. Android used to keep it this way for backward
            // compatibility.
            final int extrabytes = 7;// # of bytes to add in order to computer # of 8 byte words
            if (bytes == null) {
                throw new NullPointerException("bytes == null");
            }
            // This is a bug since extraBytes == 7 instead of 3. Android used to keep it this way for
            // backward compatibility.
            lastWord = seed[BYTES_OFFSET] == 0 ? 0
                    : (seed[BYTES_OFFSET] + extrabytes) >> 3 - 1;
            if (state == UNDEFINED) {
                throw new IllegalStateException("No seed supplied!");
            } else if (state == SET_SEED) {
                System.arraycopy(seed, HASH_OFFSET, copies, HASHCOPY_OFFSET,
                        EXTRAFRAME_OFFSET);
                // possible cases for 64-byte frame:
                //
                // seed bytes < 48      - remaining bytes are enough for all, 8 counter bytes,
                //                        0x80, and 8 seedLength bytes; no extra frame required
                // 48 < seed bytes < 56 - remaining 9 bytes are for 0x80 and 8 counter bytes
                //                        extra frame contains only seedLength value at the end
                // seed bytes > 55      - extra frame contains both counter's bytes
                //                        at the beginning and seedLength value at the end;
                //                        note, that beginning extra bytes are not more than 8,
                //                        that is, only 2 extra words may be used
                // no need to set to "0" 3 words after "lastWord" and
                // more than two words behind frame
                for (i = lastWord + 3; i < FRAME_LENGTH + 2; i++) {
                    seed[i] = 0;
                }
                bits = (seedLength << 3) + 64; // transforming # of bytes into # of bits
                // putting # of bits into two last words (14,15) of 16 word frame in
                // seed or copies array depending on total length after padding
                if (seed[BYTES_OFFSET] < MAX_BYTES) {
                    seed[14] = (int) (bits >>> 32);
                    seed[15] = (int) (bits & 0xFFFFFFFF);
                } else {
                    copies[EXTRAFRAME_OFFSET + 14] = (int) (bits >>> 32);
                    copies[EXTRAFRAME_OFFSET + 15] = (int) (bits & 0xFFFFFFFF);
                }
                nextBIndex = HASHBYTES_TO_USE; // skipping remaining random bits
            }
            state = NEXT_BYTES;
            if (bytes.length == 0) {
                return;
            }
            nextByteToReturn = 0;
            // possibly not all of HASHBYTES_TO_USE bytes were used previous time
            n = (HASHBYTES_TO_USE - nextBIndex) < (bytes.length - nextByteToReturn) ? HASHBYTES_TO_USE
                    - nextBIndex
                    : bytes.length - nextByteToReturn;
            if (n > 0) {
                System.arraycopy(nextBytes, nextBIndex, bytes, nextByteToReturn, n);
                nextBIndex += n;
                nextByteToReturn += n;
            }
            if (nextByteToReturn >= bytes.length) {
                return; // return because "bytes[]" are filled in
            }
            n = seed[BYTES_OFFSET] & 0x03;
            for (;;) {
                if (n == 0) {
                    seed[lastWord] = (int) (counter >>> 32);
                    seed[lastWord + 1] = (int) (counter & 0xFFFFFFFF);
                    seed[lastWord + 2] = END_FLAGS[0];
                } else {
                    seed[lastWord] |= (int) ((counter >>> RIGHT1[n]) & MASK[n]);
                    seed[lastWord + 1] = (int) ((counter >>> RIGHT2[n]) & 0xFFFFFFFF);
                    seed[lastWord + 2] = (int) ((counter << LEFT[n]) | END_FLAGS[n]);
                }
                if (seed[BYTES_OFFSET] > MAX_BYTES) {
                    copies[EXTRAFRAME_OFFSET] = seed[FRAME_LENGTH];
                    copies[EXTRAFRAME_OFFSET + 1] = seed[FRAME_LENGTH + 1];
                }
                computeHash(seed);
                if (seed[BYTES_OFFSET] > MAX_BYTES) {
                    System.arraycopy(seed, 0, copies, FRAME_OFFSET, FRAME_LENGTH);
                    System.arraycopy(copies, EXTRAFRAME_OFFSET, seed, 0,
                            FRAME_LENGTH);
                    computeHash(seed);
                    System.arraycopy(copies, FRAME_OFFSET, seed, 0, FRAME_LENGTH);
                }
                counter++;
                int j = 0;
                for (i = 0; i < EXTRAFRAME_OFFSET; i++) {
                    int k = seed[HASH_OFFSET + i];
                    nextBytes[j] = (byte) (k >>> 24); // getting first  byte from left
                    nextBytes[j + 1] = (byte) (k >>> 16); // getting second byte from left
                    nextBytes[j + 2] = (byte) (k >>> 8); // getting third  byte from left
                    nextBytes[j + 3] = (byte) (k); // getting fourth byte from left
                    j += 4;
                }
                nextBIndex = 0;
                j = HASHBYTES_TO_USE < (bytes.length - nextByteToReturn) ? HASHBYTES_TO_USE
                        : bytes.length - nextByteToReturn;
                if (j > 0) {
                    System.arraycopy(nextBytes, 0, bytes, nextByteToReturn, j);
                    nextByteToReturn += j;
                    nextBIndex += j;
                }
                if (nextByteToReturn >= bytes.length) {
                    break;
                }
            }
        }
        /**
         * The method generates a 160 bit hash value using
         * a 512 bit message stored in first 16 words of int[] array argument and
         * current hash value stored in five words, beginning OFFSET+1, of the array argument.
         * Computation is done according to SHA-1 algorithm.
         *
         * The resulting hash value replaces the previous hash value in the array;
         * original bits of the message are not preserved.
         *
         * No checks on argument supplied, that is,
         * a calling method is responsible for such checks.
         * In case of incorrect array passed to the method
         * either NPE or IndexOutOfBoundException gets thrown by JVM.
         *
         * @params
         *        arrW - integer array; arrW.length >= (BYTES_OFFSET+6); <BR>
         *               only first (BYTES_OFFSET+6) words are used
         */
        private static void computeHash(int[] arrW) {
            int  a = arrW[HASH_OFFSET   ];
            int  b = arrW[HASH_OFFSET +1];
            int  c = arrW[HASH_OFFSET +2];
            int  d = arrW[HASH_OFFSET +3];
            int  e = arrW[HASH_OFFSET +4];
            int temp;
            // In this implementation the "d. For t = 0 to 79 do" loop
            // is split into four loops. The following constants:
            //     K = 5A827999   0 <= t <= 19
            //     K = 6ED9EBA1  20 <= t <= 39
            //     K = 8F1BBCDC  40 <= t <= 59
            //     K = CA62C1D6  60 <= t <= 79
            // are hex literals in the loops.
            for ( int t = 16; t < 80 ; t++ ) {
                temp  = arrW[t-3] ^ arrW[t-8] ^ arrW[t-14] ^ arrW[t-16];
                arrW[t] = ( temp<<1 ) | ( temp>>>31 );
            }
            for ( int t = 0 ; t < 20 ; t++ ) {
                temp = ( ( a<<5 ) | ( a>>>27 )   ) +
                        ( ( b & c) | ((~b) & d)   ) +
                        ( e + arrW[t] + 0x5A827999 ) ;
                e = d;
                d = c;
                c = ( b<<30 ) | ( b>>>2 ) ;
                b = a;
                a = temp;
            }
            for ( int t = 20 ; t < 40 ; t++ ) {
                temp = ((( a<<5 ) | ( a>>>27 ))) + (b ^ c ^ d) + (e + arrW[t] + 0x6ED9EBA1) ;
                e = d;
                d = c;
                c = ( b<<30 ) | ( b>>>2 ) ;
                b = a;
                a = temp;
            }
            for ( int t = 40 ; t < 60 ; t++ ) {
                temp = (( a<<5 ) | ( a>>>27 )) + ((b & c) | (b & d) | (c & d)) +
                        (e + arrW[t] + 0x8F1BBCDC) ;
                e = d;
                d = c;
                c = ( b<<30 ) | ( b>>>2 ) ;
                b = a;
                a = temp;
            }
            for ( int t = 60 ; t < 80 ; t++ ) {
                temp = ((( a<<5 ) | ( a>>>27 ))) + (b ^ c ^ d) + (e + arrW[t] + 0xCA62C1D6) ;
                e = d;
                d = c;
                c = ( b<<30 ) | ( b>>>2 ) ;
                b = a;
                a = temp;
            }
            arrW[HASH_OFFSET   ] += a;
            arrW[HASH_OFFSET +1] += b;
            arrW[HASH_OFFSET +2] += c;
            arrW[HASH_OFFSET +3] += d;
            arrW[HASH_OFFSET +4] += e;
        }
        /**
         * The method appends new bytes to existing ones
         * within limit of a frame of 64 bytes (16 words).
         *
         * Once a length of accumulated bytes reaches the limit
         * the "computeHash(int[])" method is invoked on the array to compute updated hash,
         * and the number of bytes in the frame is set to 0.
         * Thus, after appending all bytes, the array contain only those bytes
         * that were not used in computing final hash value yet.
         *
         * No checks on arguments passed to the method, that is,
         * a calling method is responsible for such checks.
         *
         * @params
         *        intArray  - int array containing bytes to which to append;
         *                    intArray.length >= (BYTES_OFFSET+6)
         * @params
         *        byteInput - array of bytes to use for the update
         * @params
         *        from      - the offset to start in the "byteInput" array
         * @params
         *        to        - a number of the last byte in the input array to use,
         *                that is, for first byte "to"==0, for last byte "to"==input.length-1
         */
        private static void updateHash(int[] intArray, byte[] byteInput, int fromByte, int toByte) {
            // As intArray contains a packed bytes
            // the buffer's index is in the intArray[BYTES_OFFSET] element
            int index = intArray[BYTES_OFFSET];
            int i = fromByte;
            int maxWord;
            int nBytes;
            int wordIndex = index >>2;
            int byteIndex = index & 0x03;
            intArray[BYTES_OFFSET] = ( index + toByte - fromByte + 1 ) & 077 ;
            // In general case there are 3 stages :
            // - appending bytes to non-full word,
            // - writing 4 bytes into empty words,
            // - writing less than 4 bytes in last word
            if ( byteIndex != 0 ) {       // appending bytes in non-full word (as if)
                for ( ; ( i <= toByte ) && ( byteIndex < 4 ) ; i++ ) {
                    intArray[wordIndex] |= ( byteInput[i] & 0xFF ) << ((3 - byteIndex)<<3) ;
                    byteIndex++;
                }
                if ( byteIndex == 4 ) {
                    wordIndex++;
                    if ( wordIndex == 16 ) {          // intArray is full, computing hash
                        computeHash(intArray);
                        wordIndex = 0;
                    }
                }
                if ( i > toByte ) {                 // all input bytes appended
                    return ;
                }
            }
            // writing full words
            maxWord = (toByte - i + 1) >> 2;           // # of remaining full words, may be "0"
            for ( int k = 0; k < maxWord ; k++ ) {
                intArray[wordIndex] = ( ((int) byteInput[i   ] & 0xFF) <<24 ) |
                        ( ((int) byteInput[i +1] & 0xFF) <<16 ) |
                        ( ((int) byteInput[i +2] & 0xFF) <<8  ) |
                        ( ((int) byteInput[i +3] & 0xFF)      )  ;
                i += 4;
                wordIndex++;
                if ( wordIndex < 16 ) {     // buffer is not full yet
                    continue;
                }
                computeHash(intArray);      // buffer is full, computing hash
                wordIndex = 0;
            }
            // writing last incomplete word
            // after writing free byte positions are set to "0"s
            nBytes = toByte - i +1;
            if ( nBytes != 0 ) {
                int w =  ((int) byteInput[i] & 0xFF) <<24 ;
                if ( nBytes != 1 ) {
                    w |= ((int) byteInput[i +1] & 0xFF) <<16 ;
                    if ( nBytes != 2) {
                        w |= ((int) byteInput[i +2] & 0xFF) <<8 ;
                    }
                }
                intArray[wordIndex] = w;
            }
            return ;
        }

    }
}