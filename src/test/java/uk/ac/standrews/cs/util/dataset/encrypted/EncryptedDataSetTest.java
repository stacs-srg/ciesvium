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

import javax.crypto.*;
import java.io.*;

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
    public void encryptedDataSetCanBeDecrypted() throws IOException, CryptoException {

        final SecretKey key = SymmetricEncryption.generateRandomKey();

        final EncryptedDataSet encrypted_data_set = new EncryptedDataSet(data_set);

        final StringBuilder encrypted_form = new StringBuilder();

        encrypted_data_set.print(key, encrypted_form);

        assertEquals(encrypted_data_set, new EncryptedDataSet(key, new ByteArrayInputStream(encrypted_form.toString().getBytes())));
    }
}
