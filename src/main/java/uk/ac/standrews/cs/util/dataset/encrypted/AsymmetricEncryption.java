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
import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.security.spec.*;
import java.util.*;

/**
 * <p>A utility class that encrypts or decrypts data using RSA public key encryption.
 * The encrypted data is also Base64 MIME-encoded.</p>
 *
 * <p>This code works with keys in PEM format, generated as follows:</p>
 *
 * <pre>
 * {@code
 * openssl genrsa -out private_key.pem 2048
 * chmod 600 private_key.pem
 * openssl rsa -in private_key.pem -pubout > public_key.pem
 * }
 * </pre>
 *
 * <p>Code derived from articles linked below.</p>
 *
 * @author Graham Kirby (graham.kirby@st-andrews.ac.uk)
 * @see <a href="http://www.codejava.net/coding/file-encryption-and-decryption-simple-example">http://www.codejava.net/coding/file-encryption-and-decryption-simple-example</a>
 * @see <a href="http://stackoverflow.com/questions/11787571/how-to-read-pem-file-to-get-private-and-public-key#19166352">http://stackoverflow.com/questions/11787571/how-to-read-pem-file-to-get-private-and-public-key#19166352</a>
 */
public class AsymmetricEncryption extends Encryption {

    /**
     * The name of the directory in this user's home directory in which private and public keys are stored.
     */
    public static final String DEFAULT_KEY_DIR = ".ssh";

    /**
     * The name of the private key file.
     */
    public static final String DEFAULT_PRIVATE_KEY_FILE = "private_key.pem";

    /**
     * The name of the public key file.
     */
    public static final String DEFAULT_PUBLIC_KEY_FILE = "public_key.pem";

    /**
     * The delimiting header in the private key file.
     */
    public static final String PRIVATE_KEY_HEADER = "-----BEGIN RSA PRIVATE KEY-----";

    /**
     * The delimiting footer in the private key file.
     */
    public static final String PRIVATE_KEY_FOOTER = "-----END RSA PRIVATE KEY-----";

    /**
     * The delimiting header in the public key file.
     */
    public static final String PUBLIC_KEY_HEADER = "-----BEGIN PUBLIC KEY-----";

    /**
     * The delimiting footer in the public key file.
     */
    public static final String PUBLIC_KEY_FOOTER = "-----END PUBLIC KEY-----";

    private static final String TRANSFORMATION = "RSA";
    private static final String ALGORITHM = "RSA";

    private static final Cipher CIPHER;
    private static final KeyFactory KEY_FACTORY;

    private static final String USER_HOME = System.getProperty("user.home");

    private static final Path USER_HOME_PATH = Paths.get(USER_HOME);
    private static final Path DEFAULT_KEY_PATH = USER_HOME_PATH.resolve(Paths.get(DEFAULT_KEY_DIR));
    private static final Path DEFAULT_PRIVATE_KEY_PATH = DEFAULT_KEY_PATH.resolve(Paths.get(DEFAULT_PRIVATE_KEY_FILE));
    private static final Path DEFAULT_PUBLIC_KEY_PATH = DEFAULT_KEY_PATH.resolve(Paths.get(DEFAULT_PUBLIC_KEY_FILE));


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

    private AsymmetricEncryption() {

    }

    /**
     * Encrypts the given plain text string using the given public key, and MIME-encodes the result.
     *
     * @param public_key the public key
     * @param plain_text the plain text
     * @return the encrypted and MIME-encoded text
     * @throws CryptoException if the encryption cannot be completed
     */
    public static String encrypt(PublicKey public_key, String plain_text) throws CryptoException {

        final InputStream input_stream = new ByteArrayInputStream(plain_text.getBytes());
        final ByteArrayOutputStream output_stream = new ByteArrayOutputStream();

        encrypt(public_key, input_stream, output_stream);

        return new String(output_stream.toByteArray());
    }

    /**
     * Decrypts the given encrypted and MIME-encoded text string using the given private key.
     *
     * @param private_key the private key
     * @param cipher_text the encrypted and MIME-encoded text
     * @return the plain text
     * @throws CryptoException if the decryption cannot be completed
     */
    public static String decrypt(PrivateKey private_key, String cipher_text) throws CryptoException {

        final InputStream input_stream = new ByteArrayInputStream(cipher_text.getBytes());
        final ByteArrayOutputStream output_stream = new ByteArrayOutputStream();

        decrypt(private_key, input_stream, output_stream);

        return new String(output_stream.toByteArray());
    }

    /**
     * Encrypts the given plain text file to another file, using the given public key, and MIME-encodes the result.
     *
     * @param public_key the public key
     * @param plain_text_path the path of the plain text file
     * @param cipher_text_path the path of the resulting encrypted file
     * @throws CryptoException if the encryption cannot be completed
     * @throws IOException if a file cannot be accessed
     */
    public static void encrypt(PublicKey public_key, final Path plain_text_path, final Path cipher_text_path) throws CryptoException, IOException {

        encrypt(public_key, Files.newInputStream(plain_text_path), Files.newOutputStream(cipher_text_path));
    }

    /**
     * Decrypts the given encrypted and MIME-encoded text file to another file, using the given private key.
     *
     * @param private_key the private key
     * @param cipher_text_path the path of the encrypted file
     * @param plain_text_path the path of the resulting plain text file
     * @throws CryptoException if the encryption cannot be completed
     * @throws IOException if a file cannot be accessed
     */
    public static void decrypt(PrivateKey private_key, final Path cipher_text_path, final Path plain_text_path) throws CryptoException, IOException {

        decrypt(private_key, Files.newInputStream(cipher_text_path), Files.newOutputStream(plain_text_path));
    }

    /**
     * Encrypts the given plain text file, using the given public key, MIME-encodes the result, and outputs it to the given stream.
     *
     * @param public_key the public key
     * @param plain_text_path the path of the plain text file
     * @param output_stream the output stream for the resulting encrypted data
     * @throws CryptoException if the encryption cannot be completed
     * @throws IOException if the plain text file cannot be accessed
     */
    public static void encrypt(PublicKey public_key, final Path plain_text_path, final OutputStream output_stream) throws CryptoException, IOException {

        encrypt(public_key, Files.newInputStream(plain_text_path), output_stream);
    }

    /**
     * Decrypts the given encrypted and MIME-encoded text file, using the given private key, and outputs it to the given stream.
     *
     * @param private_key the private key
     * @param cipher_text_path the path of the encrypted file
     * @param output_stream the output stream for the resulting data
     * @throws CryptoException if the encryption cannot be completed
     * @throws IOException if the encrypted file cannot be accessed
     */
    public static void decrypt(PrivateKey private_key, final Path cipher_text_path, final OutputStream output_stream) throws CryptoException, IOException {

        decrypt(private_key, Files.newInputStream(cipher_text_path), output_stream);
    }

    /**
     * Encrypts the plain text read from the given stream, using the given public key, MIME-encodes the result, and outputs it to another given stream.
     *
     * @param public_key the public key
     * @param input_stream the input stream for the plain text
     * @param output_stream the output stream for the resulting encrypted data
     * @throws CryptoException if the encryption cannot be completed
     */
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

    /**
     * Decrypts the encrypted and MIME-encoded data read from the given stream, using the given private key, and outputs it to another given stream.
     *
     * @param private_key the private key
     * @param input_stream the input stream for the encrypted file
     * @param output_stream the output stream for the resulting data
     * @throws CryptoException if the encryption cannot be completed
     */
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

    /**
     * Gets this user's private key.
     * The key is assumed to be stored in the file {@value #DEFAULT_PRIVATE_KEY_FILE} in the directory {@value #DEFAULT_KEY_DIR} in
     * this user's home directory.
     *
     * @return this user's private key
     * @throws CryptoException if the private key cannot be accessed
     */
    public static PrivateKey getPrivateKey() throws CryptoException {

        return getPrivateKey(DEFAULT_PRIVATE_KEY_PATH);
    }

    /**
     * Gets this user's public key.
     * The key is assumed to be stored in the file {@value #DEFAULT_PUBLIC_KEY_FILE} in the directory {@value #DEFAULT_KEY_DIR} in
     * this user's home directory.
     *
     * @return this user's public key
     * @throws CryptoException if the public key cannot be accessed
     */
    public static PublicKey getPublicKey() throws CryptoException {

        return getPublicKey(DEFAULT_PUBLIC_KEY_PATH);
    }

    /**
     * Gets a private key from a given file.
     *
     * @param key_path the path of the private key file
     * @return the private key
     * @throws CryptoException if the private key cannot be accessed
     */
    public static PrivateKey getPrivateKey(Path key_path) throws CryptoException {

        return getPrivateKeyFromString(getKey(key_path));
    }

    /**
     * Gets a public key from a given file.
     *
     * @param key_path the path of the public key file
     * @return the public key
     * @throws CryptoException if the public key cannot be accessed
     */
    public static PublicKey getPublicKey(Path key_path) throws CryptoException {

        return getPublicKeyFromString(getKey(key_path));
    }

    /**
     * Gets a private key from a string. The string is assumed to be in PEM format, with delimiters {@value #PRIVATE_KEY_HEADER} and
     * {@value #PRIVATE_KEY_FOOTER}.
     *
     * @param key_in_pem_format the private key in PEM format
     * @return the private key
     * @throws CryptoException if the private key cannot be extracted
     */
    public static PrivateKey getPrivateKeyFromString(final String key_in_pem_format) throws CryptoException {

        try {
            final String base64_encoded_private_key = stripPrivateKeyDelimiters(key_in_pem_format);
            final byte[] private_key = Base64.getMimeDecoder().decode(base64_encoded_private_key);

            return KEY_FACTORY.generatePrivate(new PKCS8EncodedKeySpec(private_key));
        }
        catch (InvalidKeySpecException e) {
            throw new CryptoException(e);
        }
    }

    /**
     * Gets a public key from a string. The string is assumed to be in PEM format, with delimiters {@value #PUBLIC_KEY_HEADER} and
     * {@value #PUBLIC_KEY_FOOTER}.
     *
     * @param key_in_pem_format the public key in PEM format
     * @return the public key
     * @throws CryptoException if the public key cannot be extracted
     */
    public static PublicKey getPublicKeyFromString(final String key_in_pem_format) throws CryptoException {

        try {
            final String base64_encoded_public_key = stripPublicKeyDelimiters(key_in_pem_format);
            final byte[] public_key = Base64.getMimeDecoder().decode(base64_encoded_public_key);

            return KEY_FACTORY.generatePublic(new X509EncodedKeySpec(public_key));
        }
        catch (InvalidKeySpecException e) {
            throw new CryptoException(e);
        }
    }

    /**
     * Loads a list of public keys in PEM format from the given file.
     *
     * @param path the file containing public keys
     * @return a list of keys in PEM format
     * @throws IOException if the file cannot be accessed
     */
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
}
