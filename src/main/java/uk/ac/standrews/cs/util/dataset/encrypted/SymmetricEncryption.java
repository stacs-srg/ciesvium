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
 * Originally based on an article published at: http://www.codejava.net/coding/file-encryption-and-decryption-simple-example
 *
 * It's also possible to create an encrypted zip file from the Unix command line using:
 *
 * {@code
 *     zip -r archive.zip directory -e
 * }
 *
 * @author Graham Kirby (graham.kirby@st-andrews.ac.uk)
 */
public class SymmetricEncryption extends Encryption {

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

    public static String generateRandomKey() {

        byte[] key = new byte[KEY_LENGTH_IN_BYTES];

        RANDOM.nextBytes(key);

        byte[] mime_encoded_key = encodeKey(key);

        return new String(mime_encoded_key);
    }

    public static String encrypt(String key, final String plain_text) throws CryptoException {

        final InputStream input_stream = new ByteArrayInputStream(plain_text.getBytes());
        final ByteArrayOutputStream output_stream = new ByteArrayOutputStream();

        encrypt(key, input_stream, output_stream);

        return new String(output_stream.toByteArray());
    }

    public static String decrypt(String key, final String cipher_text) throws CryptoException {

        final InputStream input_stream = new ByteArrayInputStream(cipher_text.getBytes());
        final ByteArrayOutputStream output_stream = new ByteArrayOutputStream();

        decrypt(key, input_stream, output_stream);

        return new String(output_stream.toByteArray());
    }

    public static void encrypt(String key, final Path plain_text_path, final Path cipher_text_path) throws CryptoException, IOException {

        encrypt(key, Files.newInputStream(plain_text_path), Files.newOutputStream(cipher_text_path));
    }

    public static void decrypt(String key, final Path cipher_text_path, final Path plain_text_path) throws CryptoException, IOException {

        decrypt(key, Files.newInputStream(cipher_text_path), Files.newOutputStream(plain_text_path));
    }

    public static void encrypt(String key, final Path plain_text_path, final OutputStream output_stream) throws CryptoException, IOException {

        encrypt(key, Files.newInputStream(plain_text_path), output_stream);
    }

    public static void decrypt(String key, final Path cipher_text_path, final OutputStream output_stream) throws CryptoException, IOException {

        decrypt(key, Files.newInputStream(cipher_text_path), output_stream);
    }

    public static void encrypt(String mime_encoded_key, InputStream input_stream, OutputStream output_stream) throws CryptoException {

        byte[] key = decodeKey(mime_encoded_key.getBytes());

        if (key.length != KEY_LENGTH_IN_BYTES) {
            throw new CryptoException("key length must be " + KEY_LENGTH_IN_BYTES);
        }

        try {
            CIPHER.init(Cipher.ENCRYPT_MODE, createKey(key));

            final byte[] plain_text = readAllBytes(input_stream);
            final byte[] plain_text_with_header = prependHeader(plain_text);
            final byte[] encrypted = CIPHER.doFinal(plain_text_with_header);
            final byte[] mime_encoded = Base64.getMimeEncoder().encode(encrypted);

            output_stream.write(mime_encoded);
        }
        catch (BadPaddingException | InvalidKeyException | IllegalBlockSizeException | IOException e) {
            throw new CryptoException(e);
        }
    }

    public static void decrypt(String mime_encoded_key, InputStream input_stream, OutputStream output_stream) throws CryptoException {

        try {
            final byte[] key = decodeKey(mime_encoded_key.getBytes());

            CIPHER.init(Cipher.DECRYPT_MODE, createKey(key));

            final byte[] mime_encoded = readAllBytes(input_stream);
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

    private static byte[] encodeKey(final byte[] key) {

        return Base64.getMimeEncoder().encode(key);
    }

    private static byte[] decodeKey(final byte[] mime_encoded_key) {

        return Base64.getMimeDecoder().decode(mime_encoded_key);
    }

    private static SecretKey createKey(final byte[] key) {

        return new SecretKeySpec(key, 0, KEY_LENGTH_IN_BYTES, ALGORITHM);
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
