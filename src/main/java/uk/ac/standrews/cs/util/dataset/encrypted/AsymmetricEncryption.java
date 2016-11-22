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

import org.bouncycastle.jce.provider.*;
import uk.ac.standrews.cs.util.tools.*;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.security.spec.*;
import java.util.*;

/**
 * A utility class that encrypts or decrypts a string using RSA public key encryption.
 * The encrypted data is also Base64 MIME-encoded.
 *
 * This code works with keys in PEM format, generated as follows:
 *
 * <code>
 * openssl genrsa -out private_key.pem 2048
 * chmod 600 private_key.pem
 * openssl rsa -in private_key.pem -pubout &gt; public_key.pem
 * </code>
 *
 * Code derived from articles:
 *
 * http://www.codejava.net/coding/file-encryption-and-decryption-simple-example
 * http://stackoverflow.com/questions/11787571/how-to-read-pem-file-to-get-private-and-public-key#19166352
 *
 * @author Graham Kirby (graham.kirby@st-andrews.ac.uk)
 */
public class AsymmetricEncryption extends Encryption {

    private static final String TRANSFORMATION = "RSA";
    private static final String ALGORITHM = "RSA";

    private static final Cipher CIPHER;
    private static final KeyFactory KEY_FACTORY;

    private static final String USER_HOME = System.getProperty("user.home");
    private static final Path USER_HOME_PATH = Paths.get(USER_HOME);
    private static final Path DEFAULT_KEY_DIRECTORY = USER_HOME_PATH.resolve(Paths.get(".ssh"));
    private static final Path DEFAULT_PRIVATE_KEY_PATH = DEFAULT_KEY_DIRECTORY.resolve(Paths.get("private_key.pem"));
    private static final Path DEFAULT_PUBLIC_KEY_PATH = DEFAULT_KEY_DIRECTORY.resolve(Paths.get("public_key.pem"));

    private static final String PRIVATE_KEY_HEADER = "-----BEGIN RSA PRIVATE KEY-----";
    private static final String PRIVATE_KEY_FOOTER = "-----END RSA PRIVATE KEY-----";
    private static final String PUBLIC_KEY_HEADER = "-----BEGIN PUBLIC KEY-----";
    private static final String PUBLIC_KEY_FOOTER = "-----END PUBLIC KEY-----";

    static {
        try {
            // Code compiles without using Bouncy Castle library, but key loading doesn't work with default provider.
            Security.addProvider(new BouncyCastleProvider());

            CIPHER = Cipher.getInstance(TRANSFORMATION);
            KEY_FACTORY = KeyFactory.getInstance(ALGORITHM);
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("error loading cipher " + TRANSFORMATION + " or algorithm " + ALGORITHM);
        }
    }

    public static String encrypt(PublicKey public_key, String plain_text) throws IOException, CryptoException {

        final InputStream input_stream = new ByteArrayInputStream(plain_text.getBytes());
        final ByteArrayOutputStream output_stream = new ByteArrayOutputStream();

        encrypt(public_key, input_stream, output_stream);

        return new String(output_stream.toByteArray());
    }

    public static String decrypt(PrivateKey private_key, String cipher_text) throws IOException, CryptoException {

        final InputStream input_stream = new ByteArrayInputStream(cipher_text.getBytes());
        final ByteArrayOutputStream output_stream = new ByteArrayOutputStream();

        decrypt(private_key, input_stream, output_stream);

        return new String(output_stream.toByteArray());
    }

    public static void encrypt(PublicKey public_key, final Path plain_text_path, final Path cipher_text_path) throws CryptoException, IOException {

        encrypt(public_key, Files.newInputStream(plain_text_path), Files.newOutputStream(cipher_text_path));
    }

    public static void decrypt(PrivateKey private_key, final Path cipher_text_path, final Path plain_text_path) throws CryptoException, IOException {

        decrypt(private_key, Files.newInputStream(cipher_text_path), Files.newOutputStream(plain_text_path));
    }

    public static void encrypt(PublicKey public_key, final Path plain_text_path, final OutputStream output_stream) throws CryptoException, IOException {

        encrypt(public_key, Files.newInputStream(plain_text_path), output_stream);
    }

    public static void decrypt(PrivateKey private_key, final Path cipher_text_path, final OutputStream output_stream) throws CryptoException, IOException {

        decrypt(private_key, Files.newInputStream(cipher_text_path), output_stream);
    }

    public static void encrypt(PublicKey public_key, InputStream input_stream, OutputStream output_stream) throws CryptoException {

        try {
            CIPHER.init(Cipher.ENCRYPT_MODE, public_key);

            final byte[] plain_text = readAllBytes(input_stream);
            final byte[] encrypted = CIPHER.doFinal(plain_text);
            final byte[] mime_encoded = Base64.getMimeEncoder().encode(encrypted);

            output_stream.write(mime_encoded);
        }
        catch (BadPaddingException | InvalidKeyException | IllegalBlockSizeException | IOException e) {
            throw new CryptoException(e);
        }
    }

    public static void decrypt(PrivateKey private_key, InputStream input_stream, OutputStream output_stream) throws CryptoException {

        try {
            CIPHER.init(Cipher.DECRYPT_MODE, private_key);

            final byte[] mime_encoded = readAllBytes(input_stream);
            final byte[] encrypted = Base64.getMimeDecoder().decode(mime_encoded);
            final byte[] plain_text = CIPHER.doFinal(encrypted);

            output_stream.write(plain_text);
        }
        catch (BadPaddingException | InvalidKeyException | IllegalBlockSizeException | IOException e) {
            throw new CryptoException(e);
        }
    }

    public static PrivateKey getPrivateKey() throws IOException, CryptoException {

        return getPrivateKey(DEFAULT_PRIVATE_KEY_PATH);
    }

    public static PublicKey getPublicKey() throws IOException, CryptoException {

        return getPublicKey(DEFAULT_PUBLIC_KEY_PATH);
    }

    public static PrivateKey getPrivateKey(Path key_path) throws IOException, CryptoException {

        return getPrivateKeyFromString(getKey(key_path));
    }

    public static PublicKey getPublicKey(Path key_path) throws IOException, CryptoException {

        return getPublicKeyFromString(getKey(key_path));
    }

    public static PrivateKey getPrivateKeyFromString(final String key_in_pem_format) throws IOException, CryptoException {

        try {
            final String base64_encoded_private_key = stripPrivateKeyDelimiters(key_in_pem_format);
            final byte[] private_key = Base64.getMimeDecoder().decode(base64_encoded_private_key);

            return KEY_FACTORY.generatePrivate(new PKCS8EncodedKeySpec(private_key));
        }
        catch (InvalidKeySpecException e) {
            throw new CryptoException(e);
        }
    }

    public static PublicKey getPublicKeyFromString(final String key_in_pem_format) throws IOException, CryptoException {

        try {
            final String base64_encoded_public_key = stripPublicKeyDelimiters(key_in_pem_format);
            final byte[] public_key = Base64.getMimeDecoder().decode(base64_encoded_public_key);

            return KEY_FACTORY.generatePublic(new X509EncodedKeySpec(public_key));
        }
        catch (InvalidKeySpecException e) {
            throw new CryptoException(e);
        }
    }

    private static String stripPrivateKeyDelimiters(final String key_in_pem_format) {

        return key_in_pem_format.replace(PRIVATE_KEY_HEADER + "\n", "").replace(PRIVATE_KEY_FOOTER, "");
    }

    private static String stripPublicKeyDelimiters(final String key_in_pem_format) {

        return key_in_pem_format.replace(PUBLIC_KEY_HEADER + "\n", "").replace(PUBLIC_KEY_FOOTER, "");
    }

    private static String getKey(Path key_path) throws CryptoException {

        try {
            return new String(Files.readAllBytes(key_path));
        }
        catch (IOException e) {
            throw new CryptoException("can't access key file: " + key_path);
        }
    }

    public static List<String> loadPublicKeys(final Path path) throws IOException {

        final BufferedReader reader = new BufferedReader(FileManipulation.getInputStreamReader(path));

        List<String> key_list = new ArrayList<>();

        StringBuilder builder = null;

        String line;
        while ((line = reader.readLine()) != null) {

            if (line.equals(PUBLIC_KEY_HEADER)) {

                builder = new StringBuilder();
                builder.append(line);
                builder.append("\n");
            }
            else {
                if (line.equals(PUBLIC_KEY_FOOTER)) {

                    if (builder != null) {
                        builder.append(line);
                        key_list.add(builder.toString());
                        builder = null;
                    }
                }
                else {
                    if (builder != null) {
                        builder.append(line);
                        builder.append("\n");
                    }
                }
            }
        }

        return key_list;
    }
}
