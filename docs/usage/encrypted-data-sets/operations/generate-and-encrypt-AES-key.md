## Operation: Generate and Encrypt Random AES Key

This generates a new random MIME-encoded AES key and encrypts it using public-key encryption. The AES key is encrypted separately with each of a
number of authorized public keys read from a specified file. The resulting encrypted versions of the AES key are written
to a given file.

**Java class**:

    uk.ac.standrews.cs.util.dataset.encrypted.util.GenerateAndEncryptAESKey

**Bash script**:

    src/main/scripts/generate-and-encrypt-aes-key.sh <path of authorized public keys file> <path of AES key encrypted for authorized users>

**Result**: file containing new encrypted versions of the key is written to specified path
