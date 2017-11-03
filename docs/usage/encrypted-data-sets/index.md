## Usage: Encrypted Data Sets

General encryption operations:

1. [generate a new random AES key](generate-AES-key.html)
1. [encrypt a file with a given AES key](encrypt-file-with-AES-key.html)
1. [decrypt a file with a given AES key](decrypt-file-with-AES-key.html)
1. [encrypt an AES key with a set of public keys](encrypt-AES-key.html)
1. [decrypt an AES key](decrypt-AES-key.html)
1. [encrypt a file with a given encrypted AES key](encrypt-file-with-encrypted-AES-key.html)
1. [decrypt a file with a given encrypted AES key](decrypt-file-with-encrypted-AES-key.html)

Data set operations:

1. create persistent data
    1. [get source data](setup-source.html)
    1. [encrypt data](setup-storage.html)
1. instantiate data set
    1. source: encrypted file / encrypted resource
    1. encryption: AES / public-key
1. use data set

Data set use cases:

1. [create a data set from an existing plain-text file using AES, storing data in an encrypted file](create-encrypted-dataset-with-AES-key.html)
1. [create a data set from an existing plain-text file using public-key crypto, storing data in an encrypted file](create-encrypted-dataset-with-AES-key.html)
1. [create a data set from an existing plain-text file using public-key crypto, storing data in an encrypted resource](create-encrypted-dataset-with-AES-key.html)

{% include navigation.html %}
