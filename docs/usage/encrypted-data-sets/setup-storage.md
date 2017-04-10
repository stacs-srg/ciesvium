## Operation: Set Up Encrypted Data Set Storage

In these examples it is assumed that *source_data_set* has already been [created](setup-source.html).

### Encrypt File with Symmetric Key

<pre>// Key string previously created using <a href="/usage/encrypted-data-sets/generate-AES-key.html">generate-AES-key</a>.
SecretKey key = <a href="/apidocs/index.html?uk/ac/standrews/cs/util/dataset/encrypted/SymmetricEncryption.html">SymmetricEncryption</a>.getKey("L8rWNo0uZ+rBsTP08DR4Mw==");
    
Path storage_path = Paths.get("/path/to/cipher_text.txt");

// Output encrypted data.
source_data_set.print(storage_path, key);</pre>

### Encrypt Resource with Symmetric Key

* Create encrypted file as above.
* Copy file into the resources tree within a directory hierarchy mirroring the Java package structure containing the class that 
will access the dataset.

### Encrypt File with Public Keys

<pre>// Generate new random key.
SecretKey key = <a href="/apidocs/index.html?uk/ac/standrews/cs/util/dataset/encrypted/SymmetricEncryption.html">SymmetricEncryption</a>.generateRandomKey();

// Output encrypted data.
Path storage_path = Paths.get("/path/to/cipher_text.txt");
source_data_set.print(storage_path, key);

// Encrypt key with public keys for those authorized to access data.
Path authorized_keys_path = Paths.get("/path/to/authorized_keys.txt");
Path encrypted_key_path = Paths.get("/path/to/encrypted_key.txt");
<a href="/apidocs/index.html?uk/ac/standrews/cs/util/dataset/encrypted/AsymmetricEncryption.html">AsymmetricEncryption</a>.encryptAESKey(key, authorized_keys_path, encrypted_key_path);</pre>

### Encrypt Resource with Public Keys

* Create encrypted file and encrypted key file as above.
* Copy files into the resources tree within a directory hierarchy mirroring the Java package structure containing the class that 
will access the dataset.
