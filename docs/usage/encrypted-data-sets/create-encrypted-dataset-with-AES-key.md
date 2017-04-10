## Use Case: Create Encrypted Data Set Using AES Key

### Distinct Files

Use case: store the 

#### Creation

Create an encrypted dataset from an existing plain-text file:

<pre>Path plain_text_path = Paths.get("/path/to/plain_text.csv");
<a href="/apidocs/index.html?uk/ac/standrews/cs/util/dataset/DataSet.html">EncryptedDataSet</a> new_data_set = new <a href="/apidocs/index.html?uk/ac/standrews/cs/util/dataset/DataSet.html">EncryptedDataSet</a>(plain_text_path);

// Data hasn't been encrypted yet.

// Key string previously created using <a href="/usage/encrypted-data-sets/generate-AES-key.html">generate-AES-key</a>.
SecretKey key = <a href="/apidocs/index.html?uk/ac/standrews/cs/util/dataset/encrypted/SymmetricEncryption.html">SymmetricEncryption</a>.getKey("L8rWNo0uZ+rBsTP08DR4Mw==");
    
Path cipher_text_path = Paths.get("/path/to/cipher_text.txt");

// Output encrypted data.
new_data_set.print(cipher_text_path, key);</pre>

#### Use

Access the encrypted dataset:

<pre>Path cipher_text_path = Paths.get("/path/to/cipher/text.txt");

// Key string previously created using <a href="/usage/encrypted-data-sets/generate-AES-key.html">generate-AES-key</a>.
SecretKey key = <a href="/apidocs/index.html?uk/ac/standrews/cs/util/dataset/encrypted/SymmetricEncryption.html">SymmetricEncryption</a>.getKey("L8rWNo0uZ+rBsTP08DR4Mw==");
    
<a href="/apidocs/index.html?uk/ac/standrews/cs/util/dataset/DataSet.html">EncryptedDataSet</a> existing_data_set = new <a href="/apidocs/index.html?uk/ac/standrews/cs/util/dataset/DataSet.html">EncryptedDataSet</a>(cipher_text_path, key);
existing_data_set.print(System.out);</pre>

### Project Resources

#### Creation

Create an encrypted dataset from an existing plain-text file:

<pre>Path plain_text_path = Paths.get("/path/to/plain_text.csv");
<a href="/apidocs/index.html?uk/ac/standrews/cs/util/dataset/DataSet.html">EncryptedDataSet</a> new_data_set = new <a href="/apidocs/index.html?uk/ac/standrews/cs/util/dataset/DataSet.html">EncryptedDataSet</a>(plain_text_path);

// Data hasn't been encrypted yet.

// Key string previously created using <a href="/usage/encrypted-data-sets/generate-AES-key.html">generate-AES-key</a>.
SecretKey key = <a href="/apidocs/index.html?uk/ac/standrews/cs/util/dataset/encrypted/SymmetricEncryption.html">SymmetricEncryption</a>.getKey("L8rWNo0uZ+rBsTP08DR4Mw==");
    
Path cipher_text_path = Paths.get("/path/to/cipher_text.txt");

// Output encrypted data.
new_data_set.print(cipher_text_path, key);</pre>

Copy the encrypted file into the resources tree within a directory hierarchy mirroring the Java package structure containing the class that will access the dataset.

#### Use

Access the encrypted dataset:

<pre>// Key string previously created using generate-AES-key.
SecretKey key = <a href="/apidocs/index.html?uk/ac/standrews/cs/util/dataset/encrypted/SymmetricEncryption.html">SymmetricEncryption</a>.getKey("L8rWNo0uZ+rBsTP08DR4Mw==");

InputStream input_stream = <a href="/apidocs/index.html?uk/ac/standrews/cs/util/tools/FileManipulation.html">FileManipulation</a>.getInputStreamForResource(getClass(), "cipher_text.txt");
     
<a href="/apidocs/index.html?uk/ac/standrews/cs/util/dataset/DataSet.html">EncryptedDataSet</a> existing_data_set = new <a href="/apidocs/index.html?uk/ac/standrews/cs/util/dataset/DataSet.html">EncryptedDataSet</a>(input_stream, key);
existing_data_set.print(System.out);</pre>
