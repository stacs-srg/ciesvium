## Tutorial: Updating the Authorized Users for an Encapsulated Encrypted Data Set

This describes how to add and remove authorized users for an [encapsulated data set](creating-encrypted-dataset.html).



**Update the public key file**

To add a new authorized user, append their identifier and public key to the file <code>src/main/resources/uk/ac/standrews/cs/data/authorized_keys.txt</code>.

To remove an authorized user, delete their identifier and public key from the same file. Obviously, if you remove your own key then you will no longer be
able to access the data. Perhaps less obviously, you will not be able to make further changes to the list of authorized users.

**Regenerate the encrypted versions of the symmetric key**

Re-generate the encrypted versions of the AES key using the updated authorized user list:

<pre>src/main/scripts/re-encrypt-aes-key.sh src/main/resources/uk/ac/standrews/cs/data/authorized_keys.txt src/main/resources/uk/ac/standrews/cs/data/encrypted_key.txt</pre>
