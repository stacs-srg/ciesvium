## Operation: Decrypt AES Key

This extracts a MIME-encoded AES key from a file containing the key encrypted with a number of authorized public keys. Each of the encrypted keys is decrypted in turn with this user's private key, until a valid AES key is extracted, which is then printed.

**Java class**:

```java
uk.ac.standrews.cs.util.dataset.encrypted.util.DecryptAESKey
```

**Bash script**:
```sh
src/main/scripts/decrypt-aes-key.sh <path of AES key encrypted for authorized users>
```

**Result**:

Extracted key is printed to standard out

{% include navigation.html %}
