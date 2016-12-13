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
import java.util.*;

/**
 * Encrypts a symmetric key with a number of public keys.
 *
 * @author Graham Kirby (graham.kirby@st-andrews.ac.uk)
 */
public class EncryptAESKey {

    /**
     * Encrypts a MIME-encoded key separately with each of a number of public keys.
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

            final String mime_encoded_AES_key = args[0];
            final String authorized_keys_path = args[1];
            final String destination_path = args[2];

            encryptAESKey(mime_encoded_AES_key, authorized_keys_path, destination_path);
        }
    }

    private static void encryptAESKey(final String mime_encoded_AES_key, final String authorized_keys_path, final String destination_path) throws IOException, CryptoException {

        try (OutputStreamWriter writer = FileManipulation.getOutputStreamWriter(Paths.get(destination_path))) {

            final List<PublicKey> public_keys = AsymmetricEncryption.loadPublicKeys(Paths.get(authorized_keys_path));

            for (PublicKey public_key : public_keys) {
                writeEncryptedAESKey(public_key, mime_encoded_AES_key, writer);
            }

            writer.flush();
        }
    }

    private static void writeEncryptedAESKey(final PublicKey public_key, final String mime_encoded_AES_key, final OutputStreamWriter writer) throws IOException, CryptoException {

        writer.append(AsymmetricEncryption.encrypt(public_key, mime_encoded_AES_key));
        writer.append("\n");
    }

    private static void usage() {

        System.out.println("usage: EncryptAESKey <key> <authorized keys path> <destination path>");
    }
}