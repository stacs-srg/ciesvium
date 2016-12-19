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
package uk.ac.standrews.cs.util.dataset.encrypted.examples;

import uk.ac.standrews.cs.util.dataset.encrypted.*;
import uk.ac.standrews.cs.util.tools.*;

import javax.crypto.*;
import java.io.*;
import java.nio.file.*;

/**
 * @author Graham Kirby (graham.kirby@st-andrews.ac.uk)
 */
public class SymmetricallyEncryptedDataset {

    public static void main(String[] args) throws CryptoException, IOException {

        // Set up.

        final String plain_text_path_string = Files.createTempFile("test", ".csv").toString();
        final String cipher_text_path_string = Files.createTempFile("test", ".txt").toString();

        try (final OutputStreamWriter writer = FileManipulation.getOutputStreamWriter(Paths.get(plain_text_path_string))) {
            writer.append("the quick brown fox\njumps over the lazy dog");
        }

        // Creation.

        Path plain_text_path = Paths.get(plain_text_path_string);
        Path cipher_text_path = Paths.get(cipher_text_path_string);

        final EncryptedDataSet new_data_set = new EncryptedDataSet(plain_text_path);
        final SecretKey key = SymmetricEncryption.getKey("L8rWNo0uZ+rBsTP08DR4Mw==");

        new_data_set.print(key, cipher_text_path);

        // Use.

        EncryptedDataSet existing_data_set = new EncryptedDataSet(SymmetricEncryption.getKey("L8rWNo0uZ+rBsTP08DR4Mw=="), cipher_text_path);
        existing_data_set.print(System.out);
    }
}
