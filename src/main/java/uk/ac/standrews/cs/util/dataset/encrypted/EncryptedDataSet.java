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

import uk.ac.standrews.cs.util.dataset.DataSet;

import javax.crypto.SecretKey;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Version of dataset that allows the persistent form to be encrypted. A dataset can be instantiated from encrypted
 * data, or output in encrypted form.
 *
 * @author Graham Kirby (graham.kirby@st-andrews.ac.uk)
 */
public class EncryptedDataSet extends DataSet {

    public EncryptedDataSet(List<String> labels) {

        super(labels);
    }

    /**
     * Creates a new dataset from an encrypted input stream.
     *
     * @param source_data the encrypted data input stream
     * @param AES_key the AES key to decrypt the input stream
     * @throws CryptoException if data cannot be read from the input stream, or the data cannot be decrypted with the given key
     */
    public EncryptedDataSet(InputStream source_data, SecretKey AES_key) throws CryptoException {

        init(decrypt(source_data, AES_key));
    }

    public EncryptedDataSet(Path source_data, SecretKey AES_key) throws CryptoException, IOException {

        try (InputStream input_stream = Files.newInputStream(source_data)) {
            init(decrypt(input_stream, AES_key));
        }
    }

    /**
     * Creates a new dataset from an encrypted input stream. This constructor attempts to extract the MIME-encoded AES key
     * from the given input stream, which contains versions of the AES key encrypted with various users' RSA public
     * keys.
     *
     * @param encrypted_key_stream an input stream containing versions of the MIME-encoded AES key encrypted with various users' public keys
     * @param source_data the encrypted data input stream
     * @throws IOException if the key input stream cannot be read
     * @throws CryptoException if data cannot be read from the input stream, or the AES key cannot be extracted with this user's private key
     */
    public EncryptedDataSet(InputStream encrypted_key_stream, InputStream source_data) throws IOException, CryptoException {

        SecretKey AES_key = AsymmetricEncryption.getAESKey(encrypted_key_stream);

        init(decrypt(source_data, AES_key));
    }

    /**
     * Creates a new dataset containing a copy of the given dataset.
     *
     * @param existing_records the dataset to copy
     */
    public EncryptedDataSet(DataSet existing_records) {

        super(existing_records);
    }

    public EncryptedDataSet(Path source_data) throws IOException {

        super(source_data);
    }

    /**
     * Prints this dataset, in encrypted form, to the given output object.
     *
     * @param out the output object
     * @param AES_key the AES key to encrypt the dataset
     * @throws IOException if this dataset cannot be printed to the given output object
     * @throws CryptoException if the data cannot be encrypted
     */
    public void print(Appendable out, SecretKey AES_key) throws IOException, CryptoException {

        final StringBuilder builder = new StringBuilder();
        print(builder);

        final InputStream input_stream = new ByteArrayInputStream(builder.toString().getBytes());
        SymmetricEncryption.encrypt(AES_key, input_stream, makeOutputStream(out));
    }

    public void print(Path path, SecretKey AES_key) throws IOException, CryptoException {

        try (Writer writer = Files.newBufferedWriter(path)) {
            print(writer, AES_key);
        }
    }

    private static OutputStream makeOutputStream(final Appendable out) {

        return new OutputStream() {

            @Override
            public void write(final int b) throws IOException {

                out.append((char) b);
            }
        };
    }

    private static DataSet decrypt(final InputStream source_data, SecretKey AES_key) throws CryptoException {

        ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
        SymmetricEncryption.decrypt(AES_key, source_data, output_stream);

        try {
            return new DataSet(new StringReader(new String(output_stream.toByteArray())));
        }
        catch (IOException e) {
            throw new CryptoException("unexpected IO exception reading from a string", e);
        }
    }
}
