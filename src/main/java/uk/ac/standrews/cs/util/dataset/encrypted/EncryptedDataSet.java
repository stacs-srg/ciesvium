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

import java.io.*;
import java.nio.file.*;
import java.security.*;

public class EncryptedDataSet extends DataSet {

    private static final String ENCRYPTED_KEY_END_DELIMITER = "==";

    protected EncryptedDataSet(String key, InputStream source_data) throws IOException, CryptoException {

        init(decrypt(key, source_data));
    }

    protected EncryptedDataSet(InputStream encrypted_keys, InputStream source_data) throws IOException, CryptoException {

        PrivateKey private_key = AsymmetricEncryption.getPrivateKey();

        String AES_key = getAESKey(encrypted_keys, private_key);

        init(decrypt(AES_key, source_data));
    }

    protected EncryptedDataSet(InputStream source_data) throws IOException {

        init(new DataSet(new InputStreamReader(source_data)));
    }

    protected EncryptedDataSet(DataSet existing_records) throws IOException {

        init(existing_records);
    }

    protected DataSet decrypt(String key, final InputStream source_data) throws IOException, CryptoException {

        ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
        SymmetricEncryption.decrypt(key, source_data, output_stream);

        return new DataSet(new StringReader(new String(output_stream.toByteArray())));
    }

    public void printEncrypted(String AES_key, OutputStream out) throws IOException, CryptoException {

        StringBuilder builder = new StringBuilder();

        print(builder);

        SymmetricEncryption.encrypt(AES_key, new ByteArrayInputStream(builder.toString().getBytes()), out);
    }

    private String getAESKey(InputStream encrypted_keys, PrivateKey private_key) throws IOException, CryptoException {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(encrypted_keys))) {

            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {

                builder.append(line);

                if (line.endsWith(ENCRYPTED_KEY_END_DELIMITER)) {

                    try {
                        return AsymmetricEncryption.decrypt(private_key, builder.toString());
                    }
                    catch (CryptoException e) {

                        builder = new StringBuilder();
                    }
                }
            }

            throw new CryptoException("no valid encrypted key");
        }
    }
}
