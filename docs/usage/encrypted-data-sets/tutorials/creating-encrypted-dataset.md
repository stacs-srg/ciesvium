## Tutorial: Creating an Encapsulated Encrypted Data Set

This describes how to create a self-contained encrypted data set within a Java Maven project, encrypted using public-key
encryption. The result is that a user who is authorized to access the data can do so simply by adding the project as a
Maven dependency in their own project, without needing to do any explicit key management.

**Initial assumptions**

* The data to be encrypted is initially stored in the plain-text non-encrypted CSV file <code>plain_text.csv</code>.
* The encrypted data is to be stored in an existing Maven project <code>data-test</code> within the Java package <code>uk.ac.standrews.cs.data</code>.
* The  Maven project <code><a href="https://github.com/stacs-srg/ciesvium">ciesvium</a></code> has been cloned locally.
* The current working directory is the parent directory of both <code>data-test</code> and <code>ciesvium</code>.

**Generate public and private keys**

If you don't already have a [PEM key pair](http://serverfault.com/questions/9708/what-is-a-pem-file-and-how-does-it-differ-from-other-openssl-generated-key-file), create one. For example, using [OpenSSL](https://www.openssl.org/docs/manmaster/man1/openssl-genrsa.html) on Unix:

<pre>pushd ~/.ssh
openssl genrsa -out private_key.pem 2048
chmod 600 private_key.pem
openssl rsa -in private_key.pem -pubout > public_key.pem
popd</pre>

The key pair will be used to encrypt and decrypt a symmetric (AES) key, which will itself be used to encrypt and decrypt the data.

**Add public keys to the project**

Public keys for the users authorized to access the encrypted data can be stored in a resource file within the project.
It's not essential to keep this file here, but it makes things simpler to keep track of. Solely for documentation
purposes, first add a user identifier (e.g. email address), which is ignored by the code:

<pre>echo graham.kirby@st-andrews.ac.uk >> data-test/src/main/resources/uk/ac/standrews/cs/data/authorized_keys.txt</pre>

Copy your public key file:

<pre>cat ~/.ssh/public_key.pem >> data-test/src/main/resources/uk/ac/standrews/cs/data/authorized_keys.txt</pre>

Example:

<pre>graham.kirby@st-andrews.ac.uk
-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzTDV8GGUcZByuw2zRu8+
SEbJTg+lT9Vx8H+5N/BNUViHVZb+zToQdzwnRE2vqQAdRfLwoNBHoiD+buUivy+l
2QOizY9Qs9X4952yWeGeSU8zo/hImtyM5vAi9nG+llKuFRHv3S7GKJW1shIuauG3
9dRWvSzDDhJaGTuH/gG0WPw0k+7sR3t473R5DD5bfx2SVprGPWP9r4ETo2u5Qqw+
7/pkLOdKw46qlMGVV/NlrEq89gpRenbQ8fSKHhakIhIcAMMmImqpTzbhidA7cMe/
HIE9ckCBYundUJOZD7L7AZCbxkKmscxtlljaWyqIGg79pOF++dD9NOSuSL35IIgr
twIDAQAB
-----END PUBLIC KEY-----</pre>

Repeat with the public keys for any other users who should be able to access the data. 

**Generate new symmetric key**

Generate a new AES key and encrypt it separately using each of the authorized public keys, storing the resulting
encrypted versions in a resource file:

<pre>ciesvium/src/main/scripts/generate-and-encrypt-aes-key.sh data-test/src/main/resources/uk/ac/standrews/cs/data/authorized_keys.txt data-test/src/main/resources/uk/ac/standrews/cs/data/encrypted_key.txt</pre>

Example:

<pre>UX3+4gkpe51+9tOhDBnaQ/7JIjPylqdhruQL3kzAHYBPJkrQwVqwcDQYDAHqcaE5+00XHXkb1HiT
/vO7W2HmAT8mkJMBVje054KXJ7SM1RRAwcKaUI6oXVjs/qJx0ZZszn19SMPTaBxjrS9suwnUZD9+
NXkEAHiBlsO3Jg5+ef/OQcAaVco6qgyfmMUuWP0PmnhkE7u2dIlp4nK7CV6fzTDs9cHL81qAba4H
igOn3LBekVK9O1ka8OJPxJVM1NvQahoV2Cf1zgO79htVIlrDJULU2e1DNhYhaIe+YR6Zs1udVipN
WKU0p+JREtn0y8WHHhg8NVg5FtvwwHuv7sMx4A==</pre>

If you're curious, you can print out the AES key:

<pre>ciesvium/src/main/scripts/decrypt-aes-key.sh data-test/src/main/resources/uk/ac/standrews/cs/data/encrypted_key.txt</pre>

Don't add this to the project!

**Encrypt the data**

Encrypt the data file using the encrypted AES key, storing the resulting encrypted version in a resource file:

<pre>ciesvium/src/main/scripts/encrypt-file-with-encrypted-aes-key.sh data-test/src/main/resources/uk/ac/standrews/cs/data/encrypted_key.txt plain_text.csv data-test/src/main/resources/uk/ac/standrews/cs/data/plain_text.csv.enc</pre>

**Define a data access class**

Create a class in <code>data-test</code> to access the encrypted data, containing references to the encrypted data file and the encrypted versions
of the key for the data:

<pre>package uk.ac.standrews.cs.data;
import uk.ac.standrews.cs.utilities.dataset.encrypted.EncryptedDataSet;

public class ExampleDataSet extends <a href="https://quicksilver.host.cs.st-andrews.ac.uk/apidocs/ciesvium/?uk/ac/standrews/cs/utilities/dataset/encrypted/EncryptedDataSet.html">EncryptedDataSet</a> {

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

        <a href="https://quicksilver.host.cs.st-andrews.ac.uk/apidocs/ciesvium/?uk/ac/standrews/cs/utilities/dataset/DataSet.html">DataSet</a> my_data = new ExampleDataSet();
        my_data.print(System.out);
    }
}</pre>

{% include navigation.html %}
