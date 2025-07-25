/*
 * Copyright 2021 Systems Research Group, University of St Andrews:
 * <https://github.com/stacs-srg>
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
package uk.ac.standrews.cs.utilities.dataset.encrypted;

import uk.ac.standrews.cs.utilities.crypto.AsymmetricEncryption;
import uk.ac.standrews.cs.utilities.crypto.CryptoException;
import uk.ac.standrews.cs.utilities.crypto.SymmetricEncryption;
import uk.ac.standrews.cs.utilities.dataset.DataSet;

import javax.crypto.SecretKey;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Version of dataset that allows the persistent form to be encrypted. A dataset
 * can be instantiated from encrypted data, or output in encrypted form.
 *
 * @author Graham Kirby (graham.kirby@st-andrews.ac.uk)
 */
public class EncryptedDataSet extends DataSet {

    /**
     * Creates a new dataset with column labels and data read from a file with
     * the given path.
     *
     * @param path the path of the file to read column labels and data from
     * @throws IOException if the file cannot be read
     */
    public EncryptedDataSet(final Path path) throws IOException {

        super(path);
    }

    /**
     * Creates a new empty dataset with given column labels.
     *
     * @param labels            the column labels
     */
    public EncryptedDataSet(final List<String> labels) {

        super(labels);
    }

    /**
     * Creates a new dataset containing a copy of the given dataset.
     *
     * @param existing_records  the dataset to copy
     */
    public EncryptedDataSet(final DataSet existing_records) {

        super(existing_records);
    }

    /**
     * Creates a new dataset from an encrypted input stream.
     *
     * @param source_data       the encrypted data input stream
     * @param AES_key           the AES key to decrypt the input stream
     * @throws CryptoException  if data cannot be read from the input stream, or
     *                          the data cannot be decrypted with the given key
     * @throws IOException      if an IOError occurs when auto-closing streams
     */
    public EncryptedDataSet(final InputStream source_data, final SecretKey AES_key) throws CryptoException, IOException {

        try (InputStream in = source_data) {
            init(decrypt(source_data, AES_key));
        }
    }

    /**
     * Creates a new dataset from an encrypted file.
     *
     * @param path              the path of the encrypted file
     * @param AES_key           the AES key to decrypt the file
     * @throws CryptoException  if the data cannot be decrypted with the given key
     * @throws IOException      if data cannot be read from the file
     */
    public EncryptedDataSet(final Path path, final SecretKey AES_key) throws CryptoException, IOException {

        this(Files.newInputStream(path), AES_key);
    }

    /**
     * Creates a new dataset from an encrypted input stream. This constructor
     * attempts to extract the MIME-encoded AES key from the given input stream,
     * which contains versions of the AES key encrypted with various users' RSA
     * public keys.
     *
     * @param source_data           the encrypted data input stream
     * @param encrypted_key_stream  an input stream containing versions of the 
     *                              MIME-encoded AES key encrypted with various
     *                              users' public keys
     * @throws CryptoException      if the AES key cannot be extracted with this
     *                              user's private key
     * @throws IOException          if either input stream cannot be read
     */
    public EncryptedDataSet(final InputStream source_data, final InputStream encrypted_key_stream) throws IOException, CryptoException {

        this(source_data, AsymmetricEncryption.getAESKey(encrypted_key_stream));
    }

    /**
     * Prints this dataset, in encrypted form, to the given output object.
     *
     * @param out               the output object
     * @param AES_key           the AES key to encrypt the dataset
     * @throws IOException      if this dataset cannot be printed to the given
     *                          output object
     * @throws CryptoException  if the data cannot be encrypted
     */
    @SuppressWarnings("WeakerAccess")
    public void print(final Appendable out, final SecretKey AES_key) throws IOException, CryptoException {

        final StringBuilder builder = new StringBuilder();
        print(builder);

        final InputStream input_stream = new ByteArrayInputStream(builder.toString().getBytes());
        SymmetricEncryption.encrypt(AES_key, input_stream, makeOutputStream(out));
    }

    /**
     * Prints this dataset, in encrypted form, to the given file.
     *
     * @param path              the path of the output file
     * @param AES_key           the AES key to encrypt the dataset
     * @throws IOException      if this dataset cannot be printed to the given
     *                          output object
     * @throws CryptoException  if the data cannot be encrypted
     */
    public void print(final Path path, final SecretKey AES_key) throws IOException, CryptoException {

        try (final Writer writer = Files.newBufferedWriter(path)) {
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

    private static DataSet decrypt(final InputStream source_data, final SecretKey AES_key) throws CryptoException {

        final ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
        SymmetricEncryption.decrypt(AES_key, source_data, output_stream);

        return new DataSet(new ByteArrayInputStream(output_stream.toByteArray()));
    }
}
