## Use Case: Encrypt File With Encrypted AES Key

This encrypts a plain-text file with a given AES key, which is itself encrypted 
separately using a number of public keys.

**Java class**:
 
    uk.ac.standrews.cs.util.dataset.encrypted.util.EncryptFileWithEncryptedAESKey
 
**Bash script**:
 
    src/main/scripts/encrypt-file-with-encrypted-aes-key.sh
 
**Parameters**:
 
1. path of file containing public-key-encrypted AES key
1. path of plain-text file
1. path of new encrypted file

**Output**: encrypted file is written to specified path
