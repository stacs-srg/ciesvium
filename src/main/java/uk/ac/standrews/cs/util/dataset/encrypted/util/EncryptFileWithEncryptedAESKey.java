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
package uk.ac.standrews.cs.util.dataset.encrypted.util;

import uk.ac.standrews.cs.util.dataset.encrypted.*;

import javax.crypto.*;
import java.io.*;
import java.nio.file.*;

/**
 * Encrypts a file using a public-key-encrypted AES key.
 *
 * @author Graham Kirby (graham.kirby@st-andrews.ac.uk)
 */
public class EncryptFileWithEncryptedAESKey {

    /**
     * Encrypts a file with an AES key extracted from a file containing the key encrypted separately with the public key
     * of each authorized user.
     *
     * @param args path of file containing encrypted key, path of plain-text file, path of new encrypted file
     * @throws CryptoException if the encryption cannot be completed
     * @throws IOException if a file cannot be accessed
     */
    public static void main(String[] args) throws CryptoException, IOException {

        if (args.length < 3) {
            usage();
        }
        else {
            final String encrypted_key_path = args[0];
            final String plain_text_path = args[1];
            final String cipher_text_path = args[2];

            encryptFileWithEncryptedAESKey(encrypted_key_path, plain_text_path, cipher_text_path);
        }
    }

    private static void encryptFileWithEncryptedAESKey(final String encrypted_key_path, final String plain_text_path, final String cipher_text_path) throws IOException, CryptoException {

        SecretKey AES_key = AsymmetricEncryption.getAESKey(Paths.get(encrypted_key_path));

        SymmetricEncryption.encrypt(AES_key, Paths.get(plain_text_path), Paths.get(cipher_text_path));
    }

    private static void usage() {

        System.out.println("usage: EncryptFileWithEncryptedAESKey <encrypted key path> <plain text path> <cipher text path>");
    }
}
