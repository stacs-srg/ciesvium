/*
 * Copyright 2017 Systems Research Group, University of St Andrews:
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
package uk.ac.standrews.cs.util.dataset.encrypted.util;

import uk.ac.standrews.cs.util.dataset.encrypted.CryptoException;
import uk.ac.standrews.cs.util.dataset.encrypted.SymmetricEncryption;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Decrypts a file using AES.
 *
 * @author Graham Kirby (graham.kirby@st-andrews.ac.uk)
 */
public class DecryptFileWithAESKey {

    /**
     * Decrypts a file with a given AES key.
     *
     * @param args MIME-encoded AES key, path of encrypted file, path of new plain-text file
     * @throws CryptoException if the decryption cannot be completed
     * @throws IOException if a file cannot be accessed
     */
    public static void main(String[] args) throws CryptoException, IOException {

        if (args.length < 3) {
            usage();
        }
        else {

            final String AES_key = args[0];
            final String cipher_text_path = args[1];
            final String plain_text_path = args[2];

            SymmetricEncryption.decrypt(SymmetricEncryption.getKey(AES_key), Paths.get(cipher_text_path), Paths.get(plain_text_path));
        }
    }

    private static void usage() {

        System.out.println("usage: DecryptFileWithAES <key> <cipher text path> <plain text path>");
    }
}
