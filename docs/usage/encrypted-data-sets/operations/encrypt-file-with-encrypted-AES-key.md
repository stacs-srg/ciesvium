## Operation: Encrypt File With Encrypted AES Key

This encrypts a file using an AES key which is extracted from a file containing the key encrypted with a number of authorized public keys. Each of the encrypted keys is decrypted in turn with this user's private key, until a valid AES key is extracted, which is then used to encrypt the file.

**Java class**:

```java
uk.ac.standrews.cs.util.dataset.encrypted.util.EncryptFileWithEncryptedAESKey
```
 
**Bash script**:

```sh
src/main/scripts/encrypt-file-with-encrypted-aes-key.sh <path of AES key encrypted for authorized users> <path of plain-text file> <path of new encrypted file>
```

**Result**:

Encrypted file is written to specified path

{% include navigation.html %}
