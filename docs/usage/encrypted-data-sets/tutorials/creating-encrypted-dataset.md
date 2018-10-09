## Tutorial: Creating an Encrypted Data Set

This describes how to 

**Initial assumptions**

* The data to be encrypted is initially stored in the plain-text non-encrypted file <code>plain_text.csv</code>.
* The encrypted data is to be stored in a Java Maven project, with the root package <code>uk.ac.standrews.cs.data</code>.

**Generate public and private keys**

If you don't already have a PEM key pair, create one. For example, using [OpenSSL](https://www.openssl.org/docs/manmaster/man1/openssl-genrsa.html) on Unix:

<pre>cd ~/.ssh
openssl genrsa -out private_key.pem 2048
chmod 600 private_key.pem
openssl rsa -in private_key.pem -pubout > public_key.pem</pre>

The key pair will be used to encrypt and decrypt a symmetric (AES) key, which will itself be used to encrypt and decrypt the data.

**Add public keys to the project**

Public keys for the users authorized to access the encrypted data can be stored in a resource file within the project.
It's not essential to keep this file here, but it makes things simpler to keep track of. Solely for documentation
purposes, first add a user identifier (e.g. email address), which is ignored by the code:

<pre>cat user@email.com >> src/main/resources/uk/ac/standrews/cs/data/authorized_keys.txt</pre>

Copy your public key file:

<pre>cat ~/.ssh/public_key.pem >> src/main/resources/uk/ac/standrews/cs/data/authorized_keys.txt</pre>

Repeat with the PEM public keys for any other users who should be able to access the data. 

**Generate new symmetric key**

Generate a new AES key and encrypt it separately using each of the authorized public keys, storing the resulting
encrypted versions in a resource file:

<pre>src/main/scripts/generate-and-encrypt-aes-key.sh src/main/resources/uk/ac/standrews/cs/data/authorized_keys.txt src/main/resources/uk/ac/standrews/cs/data/encrypted_key.txt</pre>

If you're curious, you can print out the AES key:

<pre>src/main/scripts/decrypt-aes-key.sh src/main/resources/uk/ac/standrews/cs/data/encrypted_key.txt</pre>

Don't add this to the project!

**Encrypt the data**

Encrypt the data file using the encrypted AES key, storing the resulting encrypted version in a resource file:

<pre>src/main/scripts/encrypt-file-with-encrypted-aes-key.sh src/main/resources/uk/ac/standrews/cs/data/encrypted_key.txt plain_text.csv src/main/resources/uk/ac/standrews/cs/data/plain_text.csv.enc</pre>

**Define a data access class**

Create a class to access the encrypted data, containing references to the encrypted data file and the encrypted versions
of the key for the data:

<pre>package uk.ac.standrews.cs.data;
import uk.ac.standrews.cs.utilities.dataset.encrypted.EncryptedDataSet;

public class ExampleDataSet extends <a href="https://quicksilver.host.cs.st-andrews.ac.uk/apidocs/index.html?uk/ac/standrews/cs/utilities/dataset/encrypted/EncryptedDataSet">EncryptedDataSet</a> {

    public ExampleDataSet() throws Exception {
        super(
            ExampleDataSet.class.getResourceAsStream("plain_text.csv.enc"),
            ExampleDataSet.class.getResourceAsStream("encrypted_key.txt"));
    }
}</pre>

Install and/deploy the project as appropriate. Authorized users will now be able to be access the data by instantiating this class.

**Use the encrypted data**

In some arbitrary class:

<pre>package uk.ac.standrews.cs.test;
import uk.ac.standrews.cs.data.ExampleDataSet;
import uk.ac.standrews.cs.utilities.dataset.DataSet;

public class ExampleDataSetUse {

    public static void main(String[] args) throws Exception {

        <a href="https://quicksilver.host.cs.st-andrews.ac.uk/apidocs/index.html?uk/ac/standrews/cs/utilities/dataset/encrypted/EncryptedDataSet">DataSet</a> my_data = new ExampleDataSet();
        my_data.print(System.out);
    }
}</pre>
