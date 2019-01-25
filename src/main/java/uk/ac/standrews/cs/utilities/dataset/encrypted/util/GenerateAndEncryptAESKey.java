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

import uk.ac.standrews.cs.utilities.crypto.AsymmetricEncryption;
import uk.ac.standrews.cs.utilities.crypto.CryptoException;
import uk.ac.standrews.cs.utilities.crypto.SymmetricEncryption;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Generates and encrypts a new random symmetric key.
 *
 * @author Graham Kirby (graham.kirby@st-andrews.ac.uk)
 */
@SuppressWarnings("WeakerAccess")
public class GenerateAndEncryptAESKey {

    /**
     * Generates a MIME-encoded key and encrypts it separately with each of a number of public keys.
     *
     * @param args MIME-encoded AES key, path of file containing public keys, path of new file containing encrypted keys
     * @throws CryptoException if the encryption cannot be completed
     * @throws IOException     if a file cannot be accessed
     */
    public static void main(final String[] args) throws CryptoException, IOException {

        if (args.length < 2) {
            usage();
        } else {

            final String mime_encoded_AES_key = SymmetricEncryption.keyToString(SymmetricEncryption.generateRandomKey());
            final String authorized_keys_path = args[0];
            final String destination_path = args[1];

            AsymmetricEncryption.encryptAESKey(SymmetricEncryption.getKey(mime_encoded_AES_key), Paths.get(authorized_keys_path), Paths.get(destination_path));
        }
    }

    private static void usage() {

        System.out.println("usage: GenerateAndEncryptAESKey <authorized keys path> <destination path>");
    }
}
