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

import uk.ac.standrews.cs.util.dataset.*;

import javax.crypto.*;
import java.io.*;

/**
 * Version of dataset that allows the persistent form to be encrypted. A dataset can be instantiated from encrypted
 * data, or output in encrypted form.
 *
 * @author Graham Kirby (graham.kirby@st-andrews.ac.uk)
 */
public class EncryptedDataSet extends DataSet {

    /**
     * Creates a new dataset from an encrypted input stream.
     *
     * @param AES_key the AES key to decrypt the input stream
     * @param source_data the encrypted data input stream
     * @throws CryptoException if data cannot be read from the input stream, or the data cannot be decrypted with the given key
     */
    public EncryptedDataSet(SecretKey AES_key, InputStream source_data) throws CryptoException {

        init(decrypt(AES_key, source_data));
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

        init(decrypt(AES_key, source_data));
    }

    /**
     * {@inheritDoc}
     */
    public EncryptedDataSet(DataSet existing_records) throws IOException {

        super(existing_records);
    }

    /**
     * Prints this dataset, in encrypted form, to the given output object.
     *
     * @param AES_key the AES key to encrypt the dataset
     * @param out the output object
     * @throws IOException if this dataset cannot be printed to the given output object
     * @throws CryptoException if the data cannot be encrypted
     */
    public void print(SecretKey AES_key, Appendable out) throws IOException, CryptoException {

        StringBuilder builder = new StringBuilder();

        print(builder);

        SymmetricEncryption.encrypt(AES_key, new ByteArrayInputStream(builder.toString().getBytes()), makeOutputStream(out));
    }

    private static OutputStream makeOutputStream(final Appendable out) {

        return new OutputStream() {

            @Override
            public void write(final int b) throws IOException {

                out.append((char) b);
            }
        };
    }

    private static DataSet decrypt(SecretKey AES_key, final InputStream source_data) throws CryptoException {

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
