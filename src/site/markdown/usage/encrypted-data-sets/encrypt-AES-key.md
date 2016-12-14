## Use Case: Encrypt AES Key

This encrypts a MIME-encoded AES key using public-key encryption. A number of authorized public keys are read from
a specified file, and the AES key encrypted separately with each public key. The resulting encrypted
AES keys are written to a given file.

**Java class**:
 
    uk.ac.standrews.cs.util.dataset.encrypted.util.EncryptAESKey
 
**Bash script**:
 
    src/main/scripts/encrypt-aes-key.sh <mime-encoded AES key> <path of public keys file> <path of new encrypted file>

**Result**: file containing encrypted keys is written to specified path
