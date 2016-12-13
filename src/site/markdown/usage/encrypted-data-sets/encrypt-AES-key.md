## Use Case: Encrypt AES Key

This encrypts an AES key separately with each of a given set of public keys.

**Java class**:
 
    uk.ac.standrews.cs.util.dataset.encrypted.util.EncryptAESKey
 
**Bash script**:
 
    src/main/scripts/encrypt-aes-key.sh
 
**Parameters**:
 
1. MIME-encoded AES key
1. path of file containing public keys
1. path of new file containing encrypted keys

**Output**: file containing encrypted keys is written to specified path
