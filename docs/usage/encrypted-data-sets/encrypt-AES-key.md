## Use Case: Encrypt AES Key

This encrypts a MIME-encoded AES key using public-key encryption. The AES key is encrypted separately with each of a
number of authorized public keys read from a specified file. The resulting encrypted versions of the AES key are written
to a given file.

Each public key is assumed to be in [PEM format](http://serverfault.com/questions/9708/what-is-a-pem-file-and-how-does-it-differ-from-other-openssl-generated-key-file),
and delimited by *begin* and *end* comments as illustrated in the example
below. The email address shown preceding each key is for reference only, and is ignored by this method. See later on
this page for an example of how to generate a PEM key pair.

**Java class**:
 
    uk.ac.standrews.cs.util.dataset.encrypted.util.EncryptAESKey
 
**Bash script**:
 
    src/main/scripts/encrypt-aes-key.sh <mime-encoded AES key> <path of public keys file> <path of new encrypted file>

**Result**: file containing encrypted keys is written to specified path

**Example input key**:

<pre>lr8Rnrakxi1+SYbX+Xnieg==</pre>

**Example input file**:

<pre>graham.kirby@st-andrews.ac.uk
-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzTDV8GGUcZByuw2zRu8+
SEbJTg+lT9Vx8H+5N/BNUViHVZb+zToQdzwnRE2vqQAdRfLwoNBHoiD+buUivy+l
2QOizY9Qs9X4952yWeGeSU8zo/hImtyM5vAi9nG+llKuFRHv3S7GKJW1shIuauG3
9dRWvSzDDhJaGTuH/gG0WPw0k+7sR3t473R5DD5bfx2SVprGPWP9r4ETo2u5Qqw+
7/pkLOdKw46qlMGVV/NlrEq89gpRenbQ8fSKHhakIhIcAMMmImqpTzbhidA7cMe/
HIE9ckCBYundUJOZD7L7AZCbxkKmscxtlljaWyqIGg79pOF++dD9NOSuSL35IIgr
twIDAQAB
-----END PUBLIC KEY-----
alan.dearle@st-andrews.ac.uk
-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA5lgg/0gILDwBpESOwjb5
lXv1NhHSebRVeJoQxU0F1m/hssExWVXVTa7n/hZnE6+aMAyp5Dh9qwax6EoRpSop
GSn22pKqWmd/95xhe17C9D5FzDSZ85xV52Q8sRM3Yqa0IdnotVWKJHkM0RYWo8N3
OdK37SXfoevNq1T8D/Y/hWI4Wa5o2zwCuROIoqyju0P/+wTBtRuWixBEf7uvYwPe
ZvxU18YOVYpwFRswiuqIlzqN2icyxOwvwgVMrRvolz2r15+rprI7IbLH7kCPkZMe
titJPW0DwdUGyxug76JpBKuFWOcT2ElEcDPExmh4Fp0nFHDClQv2H/3xCaYpRdNO
zQIDAQAB
-----END PUBLIC KEY-----
tsd4@st-andrews.ac.uk
-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuoVhsR87zgcZM8c77944
DEPZJQKHYYDDfPV5NYovo0O17rfKB6wSPSPBvehSIhd5PrIsFP58Y4nErelunRQ1
wa/kd3P3UxKbT254QUUGa5fHlY8t30N84BGdE/3yI4Hi1kwdJjRStXUjY6qcjWC0
0lGIbUmwtaLahJkYiQZ67JvQjdRgW0MDneFKivdMiPQdyDnc0Nn9o2/+SZnnMpE2
U0CNTNEPxzvuSntWkezz8vI5SI9CqWP2UkTUUOcXuVMPiNFdyGnjH71ssgQh/14p
AYGxSMMYJyAPfDCAgaYGpIt4DrthONHrhWltpC0cAk3Xsqa2au61IMJYcyDfvn6u
tQIDAQAB
-----END PUBLIC KEY-----</pre>

**Example output file**:

<pre>YAgStYOQwmEMYyiRafzyuy+ixlLRzOwd2kXiWiV00758TtUoZDH4BvJOtcVt5ydxDLN6QxA0KKOt
h7nbIuFjI76sOGZ+jmxa4gjpbSxJJpfa3LI7/9ygt5oLQyHxbsfCMF+xR/Czz4vgWbf/sV8i3+F9
MfVXufks1Z2OVVxy+t8refVBVoPilguufIxnAaXR+IvS3U5BZwYDVGxjTMopgZ9GQ2/xFnNGZwlr
2OUDresZ30+jYtJuTOnr37hur3BQiPdl+R2YgU9fHQ+IjfgDjfDUUM8KpBHeEaKYakOpYwkDBbdN
uUL6f3YQuUKDFkcBBZEbD+vkmQd8WiAmu1zkSg==
ZaVA9UEr9PfkRRFfzlFGQFQX0KixkRY1jrM4GIlnpayV5pIcXUKXZYRF2ZP+UsaLiI/QkvhumM5t
M8MAjvFkG7/TTWkDc42t8LnVkjoDT89J5dsA7CJoxMzQ+5q5y1G0dg2CET07rVCInXkRDiQ8hFNh
2mMCB1xFGypEGNohB8qHAqsl+X3dqpg/s20efPXCdzaqrVKlUSbo9/DwNy0AScZOkhBPwxJ0amEB
lYOtmgolX751Dj0UqOEWCMKktb7nmVLb6wyMNzJaFN0qoKxSny65LPgvLniLuMuQAHsS9qmLxK9T
SjkKSfA89sgrJU4Cd6GgiHAxtfKIFr9yTtKwGw==
fg72ur8CIXdiPgZOEBq9P3smOJmVIanhB9Jf7aGyeQu7AjNBMzWnN3Jlqsh763ZirIndckZoMGwX
F2Tm1IHK6yESU5Jx3A12i/2V8cWpupTGrm/L+7SKDO+CqPVpYSmobBKcZUTrqWdNCbbTdXU/J2Pu
8Eq5H069VCJHVIglyqX5YhhyUsDWpRIHtc3ljTJWbTo95QfguHhL4n0+nyUHs+/Dx+KDETvi0KkU
6HyKQSPyPgjzaV+xwz92Q20sOBl+qGTCAanAFL9LjjfFV69uNzufHHIf80nvJC4BTvDb9pdU7+gY
cm+4NfhasqddIj/uLm7yWw3NpzSQoeGML4UtaQ==</pre>

**Generating a key pair**:

A key pair in PEM format can be generated on Unix as follows:

<pre>cd ~/.ssh
openssl genrsa -out private_key.pem 2048
chmod 600 private_key.pem
openssl rsa -in private_key.pem -pubout > public_key.pem</pre>