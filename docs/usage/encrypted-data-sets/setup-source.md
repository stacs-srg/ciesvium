## Operation: Set Up Encrypted Data Set Source

### Use New Data Values

<pre>List&lt;String&gt; headings = Arrays.asList("heading 1", "heading 2", "heading 3");
<a href="/apidocs/index.html?uk/ac/standrews/cs/util/dataset/encrypted/EncryptedDataSet.html">EncryptedDataSet</a> source_data_set = new <a href="/apidocs/index.html?uk/ac/standrews/cs/util/dataset/encrypted/EncryptedDataSet.html">EncryptedDataSet</a>(headings);
source_data_set.addRow("the", "quick", "brown", "fox");</pre>

### Use Existing Plain Text File

<pre>Path plain_text_path = Paths.get("/path/to/plain_text.csv");
<a href="/apidocs/index.html?uk/ac/standrews/cs/util/dataset/encrypted/EncryptedDataSet.html">EncryptedDataSet</a> source_data_set = new <a href="/apidocs/index.html?uk/ac/standrews/cs/util/dataset/encrypted/EncryptedDataSet.html">EncryptedDataSet</a>(plain_text_path);</pre>

### Use Existing Data Set

<pre><a href="/apidocs/index.html?uk/ac/standrews/cs/util/dataset/DataSet.html">DataSet</a> existing_data_set = ...
<a href="/apidocs/index.html?uk/ac/standrews/cs/util/dataset/encrypted/EncryptedDataSet.html">EncryptedDataSet</a> source_data_set = new <a href="/apidocs/index.html?uk/ac/standrews/cs/util/dataset/encrypted/EncryptedDataSet.html">EncryptedDataSet</a>(existing_data_set);</pre>
