## Usage: Encrypted Data Sets

Encrypted data sets are based on <a href="https://quicksilver.host.cs.st-andrews.ac.uk/apidocs/index.html?uk/ac/standrews/cs/utilities/dataset/encrypted/EncryptedDataSet.html">EncryptedDataSet</a>,
a subclass of <a href="https://quicksilver.host.cs.st-andrews.ac.uk/apidocs/index.html?uk/ac/standrews/cs/utilities/dataset/DataSet.html">DataSet</a>.
They allow the persistent data to be encrypted using either symmetric or public key encryption. The latter offers the
convenience of avoiding explicit key management for authorized users.

### Documentation

* [tutorial on creating a self-contained Maven project encapsulating an encrypted data set](tutorials/creating-encrypted-dataset.html)
* [tutorial on changing the authorized users for an encapsulated encrypted data set](tutorials/updating-authorized-users.html)
* [individual encryption and decryption operations](operations/)

{% include navigation.html %}
