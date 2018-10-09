/*
 * Copyright 2018 Systems Research Group, University of St Andrews:
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
package uk.ac.standrews.cs.utilities.dataset.encrypted.examples;

import uk.ac.standrews.cs.utilities.FileManipulation;
import uk.ac.standrews.cs.utilities.crypto.CryptoException;
import uk.ac.standrews.cs.utilities.crypto.SymmetricEncryption;
import uk.ac.standrews.cs.utilities.dataset.encrypted.EncryptedDataSet;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Graham Kirby (graham.kirby@st-andrews.ac.uk)
 */
@SuppressWarnings("WeakerAccess")
public class SymmetricallyEncryptedDataset {

    @SuppressWarnings("WeakerAccess")
    public static void main(final String[] args) throws CryptoException, IOException {

        new SymmetricallyEncryptedDataset().testDataSetUsingFiles();
        new SymmetricallyEncryptedDataset().testDataSetUsingResources();
    }

    private void testDataSetUsingFiles() throws IOException, CryptoException {

        // Set up.

        final String plain_text_path_string = Files.createTempFile("test", ".csv").toString();
        final String cipher_text_path_string = Files.createTempFile("test", ".txt").toString();

        try (final OutputStreamWriter writer = FileManipulation.getOutputStreamWriter(Paths.get(plain_text_path_string))) {
            writer.append("the quick brown fox\njumps over the lazy dog");
        }

        // Creation.

        final Path plain_text_path = Paths.get(plain_text_path_string);
        final Path cipher_text_path = Paths.get(cipher_text_path_string);

        final EncryptedDataSet new_data_set = new EncryptedDataSet(plain_text_path);
        final SecretKey key = SymmetricEncryption.getKey("L8rWNo0uZ+rBsTP08DR4Mw==");

        new_data_set.print(cipher_text_path, key);

        // Use.

        final EncryptedDataSet existing_data_set = new EncryptedDataSet(cipher_text_path, SymmetricEncryption.getKey("L8rWNo0uZ+rBsTP08DR4Mw=="));
        existing_data_set.print(System.out);
    }

    private void testDataSetUsingResources() throws IOException, CryptoException {

        // Assume that encrypted dataset has already been created and copied into resources tree.

        // Key string previously created using generate-AES-key.
        final SecretKey key = SymmetricEncryption.getKey("L8rWNo0uZ+rBsTP08DR4Mw==");

        final InputStream input_stream = getClass().getResourceAsStream("cipher_text.txt");

        final EncryptedDataSet existing_data_set = new EncryptedDataSet(input_stream, key);
        existing_data_set.print(System.out);
    }

    // Following methods defined in order to check that website examples compile.

    @SuppressWarnings("UnusedDeclaration")
    private static void create() throws CryptoException, IOException {

        final Path plain_text_path = Paths.get("/path/to/plain_text.csv");
        final EncryptedDataSet new_data_set = new EncryptedDataSet(plain_text_path);

        // Data hasn't been encrypted yet.

        // Key string previously created using generate-AES-key.
        final SecretKey key = SymmetricEncryption.getKey("L8rWNo0uZ+rBsTP08DR4Mw==");

        final Path cipher_text_path = Paths.get("/path/to/cipher/text.txt");

        // Output encrypted data.
        new_data_set.print(cipher_text_path, key);
    }

    @SuppressWarnings("UnusedDeclaration")
    private static void access() throws CryptoException, IOException {

        final Path cipher_text_path = Paths.get("/path/to/cipher_text.txt");

        // Key string previously created using generate-AES-key.
        final SecretKey key = SymmetricEncryption.getKey("L8rWNo0uZ+rBsTP08DR4Mw==");

        final EncryptedDataSet existing_data_set = new EncryptedDataSet(cipher_text_path, key);
        existing_data_set.print(System.out);
    }
}
