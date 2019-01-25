/*
 * Copyright 2019 Systems Research Group, University of St Andrews:
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
package uk.ac.standrews.cs.utilities.dataset.encrypted.util;

import uk.ac.standrews.cs.utilities.crypto.CryptoException;
import uk.ac.standrews.cs.utilities.crypto.SymmetricEncryption;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Encrypts a file using AES.
 *
 * @author Graham Kirby (graham.kirby@st-andrews.ac.uk)
 */
@SuppressWarnings("WeakerAccess")
public class EncryptFileWithAESKey {

    /**
     * Encrypts a file with a given AES key.
     *
     * @param args MIME-encoded AES key, path of plain-text file, path of new encrypted file
     * @throws CryptoException if the encryption cannot be completed
     * @throws IOException     if a file cannot be accessed
     */
    public static void main(String[] args) throws CryptoException, IOException {

        if (args.length < 3) {
            usage();
        } else {

            final String key = args[0];
            final String plain_text_path = args[1];
            final String cipher_text_path = args[2];

            SymmetricEncryption.encrypt(SymmetricEncryption.getKey(key), Paths.get(plain_text_path), Paths.get(cipher_text_path));
        }
    }

    private static void usage() {

        System.out.println("usage: EncryptFileWithAESKey <key> <plain text path> <cipher text path>");
    }
}
