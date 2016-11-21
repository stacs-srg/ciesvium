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

import java.io.*;
import java.nio.file.*;

import static org.junit.Assert.*;

public class SymmetricEncryptionTest {

    private static final String SHORT_KEY = "too short";
    private static final String VALID_KEY = SymmetricEncryption.generateRandomKey();

    @Test(expected = CryptoException.class)
    public void encryptionWithShortKeyThrowsException() throws CryptoException {

        SymmetricEncryption.encrypt(SHORT_KEY, "plain text");
    }

    @Test
    public void encryptedStringCanBeDecrypted() throws CryptoException {

        String plain_text = "the quick brown fox jumps over the lazy dog";

        assertEquals(plain_text, SymmetricEncryption.decrypt(VALID_KEY, SymmetricEncryption.encrypt(VALID_KEY, plain_text)));
    }

    @Test(expected = CryptoException.class)
    public void decryptionWithWrongKeyThrowsException() throws CryptoException {

        String plain_text = "the quick brown fox jumps over the lazy dog";
        String corrupted_key = "x" + VALID_KEY;

        SymmetricEncryption.decrypt(corrupted_key, SymmetricEncryption.encrypt(VALID_KEY, plain_text));
    }

    @Test
    public void encryptedFileCanBeDecrypted() throws CryptoException, IOException {

        Path plain_text_file_path = Files.createTempFile("plain_text_test", ".txt");
        Path encrypted_text_file_path = Files.createTempFile("encrypted_text_test", ".txt");
        Path decrypted_text_file_path = Files.createTempFile("decrypted_text_test", ".txt");

        try (PrintWriter writer = new PrintWriter(Files.newOutputStream(plain_text_file_path))) {

            writer.println("the quick brown fox jumps over the lazy dog");
        }

        SymmetricEncryption.encrypt(VALID_KEY, plain_text_file_path, encrypted_text_file_path);
        SymmetricEncryption.decrypt(VALID_KEY, encrypted_text_file_path, decrypted_text_file_path);

        FileManipulation.assertThatFilesHaveSameContent(plain_text_file_path, decrypted_text_file_path);
    }
}
