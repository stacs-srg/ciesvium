## Use Case: Encrypt File With Encrypted AES Key

This encrypts a file using an AES key which is extracted from a file containing the key
encrypted with a number of authorized public keys. Each of the encrypted keys is decrypted in turn 
with this user's private key, until a valid AES key is extracted, which is then used to decrypt the 
file.

**Java class**:
 
    uk.ac.standrews.cs.util.dataset.encrypted.util.EncryptFileWithEncryptedAESKey
 
**Bash script**:
 
    src/main/scripts/encrypt-file-with-encrypted-aes-key.sh <path of encrypted AES key> <path of plain-text file> <path of new encrypted file>

**Result**: encrypted file is written to specified path
