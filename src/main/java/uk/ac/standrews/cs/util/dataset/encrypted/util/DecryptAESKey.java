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
 * Extracts an AES key from a file containing the key encrypted separately with the public key
 * of each authorized user.
 *
 * @author Graham Kirby (graham.kirby@st-andrews.ac.uk)
 */
public class DecryptAESKey {

    /**
     * Extracts a MIME-encoded AES key from a file containing the key encrypted separately with the public key
     * of each authorized user.
     *
     * @param args path of file containing encrypted key, path of plain-text file, path of new encrypted file
     * @throws CryptoException if the encryption cannot be completed
     * @throws IOException if a file cannot be accessed
     */
    public static void main(String[] args) throws CryptoException, IOException {

        if (args.length < 1) {
            usage();
        }
        else {

            final String encrypted_keys_path = args[0];

            decryptAESKey(encrypted_keys_path);
        }
    }

    private static void decryptAESKey(final String encrypted_keys_path) throws IOException, CryptoException {

        SecretKey AES_key = AsymmetricEncryption.getAESKey(Paths.get(encrypted_keys_path));
        System.out.println(SymmetricEncryption.keyToString(AES_key));
    }

    private static void usage() {

        System.out.println("usage: DecryptAESKey <encrypted keys path>");
    }
}
