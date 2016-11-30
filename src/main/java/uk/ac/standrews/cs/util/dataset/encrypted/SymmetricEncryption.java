/*
 * Copyright 2016 Digitising Scotland project:
 * <http://digitisingscotland.cs.st-andrews.ac.uk/>
 *
 * This file is part of the module ciesvium.
 *
 * ciesvium is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * ciesvium is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with ciesvium. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package uk.ac.standrews.cs.util.dataset.encrypted;

import uk.ac.standrews.cs.util.tools.*;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.util.*;

/**
 * A utility class that encrypts or decrypts a finite-length stream using AES.
 * The encrypted data is also Base64 MIME-encoded.
 *
 * <p>Code derived from articles linked below.</p>
 *
 * <p>Note: it's also possible to create an encrypted zip file from the Unix command line using:</p>
 *
 * <pre>
 * {@code
 * zip -r archive.zip directory -e
 * }
 * </pre>
 *
 * @author Graham Kirby (graham.kirby@st-andrews.ac.uk)
 * @see <a href="http://www.codejava.net/coding/file-encryption-and-decryption-simple-example">http://www.codejava.net/coding/file-encryption-and-decryption-simple-example</a>
 */
public class SymmetricEncryption {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    private static final int KEY_LENGTH_IN_BYTES = 16;
    private static final String CIPHER_TEXT_HEADER = "uk.ac.standrews.cs.util.dataset.encrypted\n";

    private static final Cipher CIPHER;

    private static final Random RANDOM = new SecureRandom();

    static {
        try {
            CIPHER = Cipher.getInstance(TRANSFORMATION);
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("error loading cipher " + TRANSFORMATION);
        }
    }

    private SymmetricEncryption() {

    }

    /**
     * Encrypts the given plain text string using the given AES key, and MIME-encodes the result.
     *
     * @param key the AES key
     * @param plain_text the plain text
     * @return the encrypted and MIME-encoded text
     * @throws CryptoException if the text cannot be encrypted
     */
    public static String encrypt(SecretKey key, final String plain_text) throws CryptoException {

        final InputStream input_stream = new ByteArrayInputStream(plain_text.getBytes());
        final ByteArrayOutputStream output_stream = new ByteArrayOutputStream();

        encrypt(key, input_stream, output_stream);

        return new String(output_stream.toByteArray());
    }

    /**
     * Decrypts the given encrypted and MIME-encoded text string using the given AES key.
     *
     * @param key the AES key
     * @param cipher_text the encrypted and MIME-encoded text
     * @return the plain text
     * @throws CryptoException if the decryption cannot be completed
     */
    public static String decrypt(SecretKey key, final String cipher_text) throws CryptoException {

        final InputStream input_stream = new ByteArrayInputStream(cipher_text.getBytes());
        final ByteArrayOutputStream output_stream = new ByteArrayOutputStream();

        decrypt(key, input_stream, output_stream);

        return new String(output_stream.toByteArray());
    }

    /**
     * Encrypts the given plain text file to another file, using the given AES key, and MIME-encodes the result.
     *
     * @param key the AES key
     * @param plain_text_path the path of the plain text file
     * @param cipher_text_path the path of the resulting encrypted file
     * @throws CryptoException if the encryption cannot be completed
     * @throws IOException if a file cannot be accessed
     */
    public static void encrypt(SecretKey key, final Path plain_text_path, final Path cipher_text_path) throws CryptoException, IOException {

        encrypt(key, Files.newInputStream(plain_text_path), Files.newOutputStream(cipher_text_path));
    }

    /**
     * Decrypts the given encrypted and MIME-encoded text file to another file, using the given AES key.
     *
     * @param key the AES key
     * @param cipher_text_path the path of the encrypted file
     * @param plain_text_path the path of the resulting plain text file
     * @throws CryptoException if the encryption cannot be completed
     * @throws IOException if a file cannot be accessed
     */
    public static void decrypt(SecretKey key, final Path cipher_text_path, final Path plain_text_path) throws CryptoException, IOException {

        decrypt(key, Files.newInputStream(cipher_text_path), Files.newOutputStream(plain_text_path));
    }

    /**
     * Encrypts the given plain text file to another file, using the given AES key, and MIME-encodes the result.
     *
     * @param key the AES key
     * @param plain_text_path the path of the plain text file
     * @param output_stream the output stream for the resulting encrypted data
     * @throws CryptoException if the encryption cannot be completed
     * @throws IOException if a file cannot be accessed
     */
    public static void encrypt(SecretKey key, final Path plain_text_path, final OutputStream output_stream) throws CryptoException, IOException {

        encrypt(key, Files.newInputStream(plain_text_path), output_stream);
    }

    /**
     * Decrypts the given encrypted and MIME-encoded text file to another file, using the given AES key.
     *
     * @param key the AES key
     * @param cipher_text_path the path of the encrypted file
     * @param output_stream the output stream for the resulting data
     * @throws CryptoException if the encryption cannot be completed
     * @throws IOException if a file cannot be accessed
     */
    public static void decrypt(SecretKey key, final Path cipher_text_path, final OutputStream output_stream) throws CryptoException, IOException {

        decrypt(key, Files.newInputStream(cipher_text_path), output_stream);
    }

    /**
     * Encrypts the given plain text file to another file, using the given AES key, and MIME-encodes the result.
     *
     * @param key the AES key
     * @param input_stream the input stream for the plain text
     * @param output_stream the output stream for the resulting encrypted data
     * @throws CryptoException if the encryption cannot be completed
     */
    public static void encrypt(SecretKey key, InputStream input_stream, OutputStream output_stream) throws CryptoException {

        try {
            CIPHER.init(Cipher.ENCRYPT_MODE, key);

            final byte[] plain_text = FileManipulation.readAllBytes(input_stream);
            final byte[] plain_text_with_header = prependHeader(plain_text);
            final byte[] encrypted = CIPHER.doFinal(plain_text_with_header);
            final byte[] mime_encoded = Base64.getMimeEncoder().encode(encrypted);

            output_stream.write(mime_encoded);
            output_stream.flush();
        }
        catch (BadPaddingException | InvalidKeyException | IllegalBlockSizeException | IOException e) {
            throw new CryptoException(e);
        }
    }

    /**
     * Decrypts the given encrypted and MIME-encoded text file to another file, using the given AES key.
     *
     * @param key the AES key
     * @param input_stream the input stream for the encrypted file
     * @param output_stream the output stream for the resulting data
     * @throws CryptoException if the encryption cannot be completed
     */
    public static void decrypt(SecretKey key, InputStream input_stream, OutputStream output_stream) throws CryptoException {

        try {
            CIPHER.init(Cipher.DECRYPT_MODE, key);

            final byte[] mime_encoded = FileManipulation.readAllBytes(input_stream);
            final byte[] encrypted = Base64.getMimeDecoder().decode(mime_encoded);
            final byte[] plain_text_with_header = CIPHER.doFinal(encrypted);

            checkForValidHeader(plain_text_with_header);

            final byte[] plain_text = stripHeader(plain_text_with_header);

            output_stream.write(plain_text);
        }
        catch (BadPaddingException | InvalidKeyException | IllegalBlockSizeException | IOException e) {
            throw new CryptoException(e);
        }
    }

    /**
     * Extracts an AES key from the given MIME-encoded string.
     *
     * @param mime_encoded_AES_key the MIME-encoded key
     * @return the extracted key
     * @throws CryptoException if a valid key cannot be extracted
     */
    public static SecretKey getKey(final String mime_encoded_AES_key) throws CryptoException {

        final byte[] key_bytes = MIMEDecodeKey(mime_encoded_AES_key.getBytes());

        if (key_bytes.length != KEY_LENGTH_IN_BYTES) {
            throw new CryptoException("key length must be " + KEY_LENGTH_IN_BYTES);
        }

        return getKey(key_bytes);
    }

    /**
     * Generates a random AES key.
     *
     * @return a random key
     * @throws CryptoException if the key cannot be generated
     */
    public static SecretKey generateRandomKey() throws CryptoException {

        byte[] key_bytes = new byte[KEY_LENGTH_IN_BYTES];

        RANDOM.nextBytes(key_bytes);

        return getKey(key_bytes);
    }

    protected static SecretKey getKey(final byte[] key_bytes) throws CryptoException {

        try {
            return new SecretKeySpec(key_bytes, 0, KEY_LENGTH_IN_BYTES, ALGORITHM);
        }
        catch (IllegalArgumentException e) {
            throw new CryptoException(e);
        }
    }

    private static byte[] MIMEDecodeKey(final byte[] mime_encoded_key) {

        return Base64.getMimeDecoder().decode(mime_encoded_key);
    }

    private static byte[] stripHeader(final byte[] input_bytes_with_header) {

        return Arrays.copyOfRange(input_bytes_with_header, CIPHER_TEXT_HEADER.length(), input_bytes_with_header.length);
    }

    private static void checkForValidHeader(final byte[] input_bytes_with_header) throws InvalidKeyException {

        if (!new String(input_bytes_with_header, 0, CIPHER_TEXT_HEADER.length()).equals(CIPHER_TEXT_HEADER)) {
            throw new InvalidKeyException();
        }
    }

    private static byte[] prependHeader(final byte[] output_bytes) {

        byte[] temp = new byte[output_bytes.length + CIPHER_TEXT_HEADER.length()];

        System.arraycopy(CIPHER_TEXT_HEADER.getBytes(), 0, temp, 0, CIPHER_TEXT_HEADER.length());
        System.arraycopy(output_bytes, 0, temp, CIPHER_TEXT_HEADER.length(), output_bytes.length);

        return temp;
    }
}
