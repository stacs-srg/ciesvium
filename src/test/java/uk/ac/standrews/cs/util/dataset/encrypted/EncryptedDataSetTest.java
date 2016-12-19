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

import org.junit.*;
import uk.ac.standrews.cs.util.dataset.*;
import uk.ac.standrews.cs.util.tools.*;

import javax.crypto.*;
import java.io.*;
import java.nio.file.*;

import static org.junit.Assert.*;

public class EncryptedDataSetTest {

    private static final String NON_EMPTY_DATA_SET_FILE_NAME = "csv_test_data.csv";
    private static final char DELIMITER = ',';

    private DataSet data_set;

    @Before
    public void setup() throws IOException {

        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(NON_EMPTY_DATA_SET_FILE_NAME))) {

            data_set = new DataSet(reader, DELIMITER);
        }
    }

    @Test
    public void encryptedDataSetCreatedFromExistingDataSetCanBeDecrypted() throws IOException, CryptoException {

        final SecretKey key = SymmetricEncryption.generateRandomKey();

        final EncryptedDataSet new_data_set = new EncryptedDataSet(data_set);

        final StringBuilder encrypted_form = new StringBuilder();
        new_data_set.print(key, encrypted_form);

        final EncryptedDataSet existing_data_set = new EncryptedDataSet(key, new ByteArrayInputStream(encrypted_form.toString().getBytes()));
        assertEquals(new_data_set, existing_data_set);
    }

    @Test
    public void encryptedDataSetUsingFilesCanBeDecrypted() throws CryptoException, IOException {

        final SecretKey key = SymmetricEncryption.generateRandomKey();

        EncryptedDataSet[] data_sets = createAndReadDataSet(key, key);
        assertEquals(data_sets[0], data_sets[1]);
    }

    @Test(expected = CryptoException.class)
    public void encryptedDataSetDecryptedWithWrongKeyThrowsException() throws CryptoException, IOException {

        final SecretKey key1 = SymmetricEncryption.generateRandomKey();
        final SecretKey key2 = SymmetricEncryption.generateRandomKey();

        createAndReadDataSet(key1, key2);
    }

    private EncryptedDataSet[] createAndReadDataSet(SecretKey key1, SecretKey key2) throws IOException, CryptoException {

        final Path plain_text_path = Files.createTempFile("test", ".csv");
        final Path cipher_text_path = Files.createTempFile("test", ".txt");

        try (final OutputStreamWriter writer = FileManipulation.getOutputStreamWriter(plain_text_path)) {
            writer.append("the quick brown fox\njumps over the lazy dog");
        }

        // Creation.

        final EncryptedDataSet new_data_set = new EncryptedDataSet(plain_text_path);
        new_data_set.print(key1, cipher_text_path);

        // Use.

        EncryptedDataSet existing_data_set = new EncryptedDataSet(key2, cipher_text_path);

        return new EncryptedDataSet[]{new_data_set, existing_data_set};
    }
}
