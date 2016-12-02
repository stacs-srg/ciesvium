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
import uk.ac.standrews.cs.util.tools.*;

import java.io.*;
import java.nio.file.*;
import java.security.*;

/**
 * Encrypts a symmetric key with a number of public keys.
 *
 * @author Graham Kirby (graham.kirby@st-andrews.ac.uk)
 */
public class EncryptKey {

    /**
     * Encrypts a MIME-encoded key with each of a number of public keys read from a given file.
     *
     * @param args MIME-encoded AES key, path of file containing public keys, path of new file containing encrypted keys
     * @throws CryptoException if the encryption cannot be completed
     * @throws IOException if a file cannot be accessed
     */
    public static void main(String[] args) throws CryptoException, IOException {

        if (args.length < 3) {
            usage();
        }
        else {

            String key = args[0];
            String authorized_keys_path = args[1];
            String destination_path = args[2];

            try (OutputStreamWriter writer = FileManipulation.getOutputStreamWriter(Paths.get(destination_path))) {

                for (String authorized_key : AsymmetricEncryption.loadPublicKeys(Paths.get(authorized_keys_path))) {

                    final PublicKey public_key = AsymmetricEncryption.getPublicKeyFromString(authorized_key);
                    final String encrypted_symmetric_key = AsymmetricEncryption.encrypt(public_key, key);
                    writer.append(encrypted_symmetric_key);
                    writer.append("\n");
                }

                writer.flush();
            }
        }
    }

    private static void usage() {

        System.out.println("usage: EncryptKey <key> <authorized keys path> <destination path>");
    }
}
