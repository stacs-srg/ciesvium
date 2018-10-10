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
package uk.ac.standrews.cs.utilities.dataset.encrypted.util;

import uk.ac.standrews.cs.utilities.crypto.CryptoException;
import uk.ac.standrews.cs.utilities.crypto.SymmetricEncryption;

/**
 * Generates a new random symmetric key.
 *
 * @author Graham Kirby (graham.kirby@st-andrews.ac.uk)
 */
@SuppressWarnings("WeakerAccess")
public class GenerateAESKey {

    /**
     * Generates a new AES key and prints it in MIME-encoded form.
     *
     * @param args ignored
     * @throws CryptoException if the key cannot be generated
     */
    public static void main(final String[] args) throws CryptoException {

        System.out.println(SymmetricEncryption.keyToString(SymmetricEncryption.generateRandomKey()));
    }
}
