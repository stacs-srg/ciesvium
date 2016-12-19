## Use Case: Create Encrypted Data Set Using AES Key

### Distinct Files

Create an encrypted dataset from an existing plain-text file:

<pre>Path plain_text_path = Paths.get("/path/to/plain/text.csv");
<a href="/apidocs/index.html?uk/ac/standrews/cs/util/dataset/DataSet.html">EncryptedDataSet</a> new_data_set = new <a href="/apidocs/index.html?uk/ac/standrews/cs/util/dataset/DataSet.html">EncryptedDataSet</a>(plain_text_path);
    
// Key string previously created using <a href="/usage/encrypted-data-sets/generate-AES-key.html">generate-AES-key</a>.
SecretKey key = <a href="/apidocs/index.html?uk/ac/standrews/cs/util/dataset/encrypted/SymmetricEncryption.html">SymmetricEncryption</a>.getKey("L8rWNo0uZ+rBsTP08DR4Mw==");
    
Path cipher_text_path = Paths.get("/path/to/cipher/text.txt");

// Output encrypted data.
new_data_set.print(key, cipher_text_path);</pre>

Access an encrypted dataset:

<pre>Path cipher_text_path = Paths.get("/path/to/cipher/text.txt");

// Key string previously created using <a href="/usage/encrypted-data-sets/generate-AES-key.html">generate-AES-key</a>.
SecretKey key = <a href="/apidocs/index.html?uk/ac/standrews/cs/util/dataset/encrypted/SymmetricEncryption.html">SymmetricEncryption</a>.getKey("L8rWNo0uZ+rBsTP08DR4Mw==");
    
<a href="/apidocs/index.html?uk/ac/standrews/cs/util/dataset/DataSet.html">EncryptedDataSet</a> existing_data_set = new <a href="/apidocs/index.html?uk/ac/standrews/cs/util/dataset/DataSet.html">EncryptedDataSet</a>(key, cipher_text_path);
existing_data_set.print(System.out);</pre>
