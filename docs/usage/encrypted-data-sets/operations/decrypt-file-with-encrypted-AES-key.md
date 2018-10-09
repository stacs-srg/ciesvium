## Operation: Decrypt File With Encrypted AES Key

This decrypts an encrypted file using an AES key which is extracted from a file containing the key
encrypted with a number of authorized public keys. Each of the encrypted keys is decrypted in turn 
with this user's private key, until a valid AES key is extracted, which is then used to decrypt the 
file.

**Java class**:
 
    uk.ac.standrews.cs.util.dataset.encrypted.util.DecryptFileWithEncryptedAESKey
 
**Bash script**:
 
    src/main/scripts/decrypt-file-with-encrypted-aes-key.sh <path of AES key encrypted for authorized users> <path of encrypted file> <path of new plain-text file>

**Result**: decrypted file is written to specified path
