## Use Case: Decrypt File With AES Key

This decrypts an encrypted file with a given AES key, which is itself encrypted 
separately using a number of public keys.

**Java class**:
 
    uk.ac.standrews.cs.util.dataset.encrypted.util.DecryptFileWithEncryptedAESKey
 
**Bash script**:
 
    src/main/scripts/decrypt-file-with-encrypted-aes-key.sh
 
**Parameters**:
 
1. path of file containing public-key-encrypted AES key
1. path of encrypted file
1. path of new plain-text file

**Output**: decrypted file is written to specified path
