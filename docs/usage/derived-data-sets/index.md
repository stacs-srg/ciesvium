## Usage: Derived Data Sets

A derived data set allows a new data set to be defined in terms of various transformations applied to an existing data set, by extending class **DerivedDataSet**.

The following methods need to be defined:

```java
public DataSet getSourceDataSet() throws IOException;
public abstract DataSet getDerivedDataSet(DataSet source_data_set) throws IOException;
```

For example, to add a new auto-incremented ID column to a data set:

```java
public class DerivedDataWithIDColumn extends DerivedDataSet {

    protected DerivedDataWithIDColumn() throws IOException {}

    public DataSet getSourceDataSet() {

        final DataSet data = new DataSet(Arrays.asList("col1", "col2", "col3"));

        data.addRow(Arrays.asList("a", "b", "c"));
        data.addRow(Arrays.asList("d", "e", "f"));

        return data;
    }

    public DataSet getDerivedDataSet(final DataSet source_data_set) {

        return source_data_set.extend(addIdColumn());
    }
}
```
If we print the source and derived data sets:

```java
DerivedDataSet derived = new DerivedDataWithIDColumn();
derived.getSourceDataSet().print(System.out);
derived.print(System.out);
```

We get:

```txt
col1,col2,col3
a,b,c
d,e,f

col1,col2,col3,ID
a,b,c,1
d,e,f,2
```

In this example, the source data set is defined within the derived data set class, which perhaps isn't very useful. To pass it as a parameter we can define an anonymous derived class within a method:

```java
public static DerivedDataSet makeDerivedDataSet(final DataSet original) throws IOException {

    return new DerivedDataSet() {

        public DataSet getSourceDataSet() {
            return original;
        }

        public DataSet getDerivedDataSet(final DataSet source_data_set) {
            return source_data_set.extend(addIdColumn());
        }
    };
}
```
    
To move the ID column to the first position:

```java
public static class DerivedDataWithIDColumnFirst extends DerivedDataSet {

    protected DerivedDataWithIDColumnFirst() throws IOException {
    }

    public DataSet getSourceDataSet() throws IOException {
        return new DerivedDataWithIDColumn();
    }

    public DataSet getDerivedDataSet(final DataSet source_data_set) {
        return source_data_set.project(moveIdColumnToFirst(source_data_set.getColumnLabels()));
    }
}
```

Which when instantiated and printed gives:

```txt
ID,col1,col2,col3
1,a,b,c
2,d,e,f
```
    
To filter rows, for example to omit any containing "b" in the third column:

```java
public static class DerivedDataWithFilteredRows extends DerivedDataSet {

    protected DerivedDataWithFilteredRows() throws IOException {
    }

    public DataSet getSourceDataSet() throws IOException {
        return new DerivedDataWithIDColumnFirst();
    }

    public DataSet getDerivedDataSet(final DataSet source_data_set) {
        return source_data_set.select((record, data_set) -> !record.get(2).equals("b"));
    }
}
```

Which gives:

```txt
ID,col1,col2,col3
2,d,e,f
```

To renumber IDs:

```java
public static class DerivedDataWithFilteredRowsAndRenumbered extends DerivedDataSet {

    protected DerivedDataWithFilteredRowsAndRenumbered() throws IOException {
    }

    public DataSet getSourceDataSet() throws IOException {
        return new DerivedDataWithFilteredRows();
    }

    public DataSet getDerivedDataSet(final DataSet source_data_set) {
        return renumber(source_data_set);
    }
}
```

Which gives:

```txt
ID,col1,col2,col3
1,d,e,f
```

And finally, to transform individual data elements, in this case to make them all upper case:

```java
public static class DerivedDataWithCapitalLetters extends DerivedDataSet {

    protected DerivedDataWithCapitalLetters() throws IOException {
    }

    public DataSet getSourceDataSet() throws IOException {
        return new DerivedDataWithIDColumnFirst();
    }

    public DataSet getDerivedDataSet(final DataSet source_data_set) {

        return source_data_set.map(new Mapper() {

            public List<String> mapRecord(final List<String> record, final List<String> labels) {
                final List<String> new_row = new ArrayList<>();

                for (final String element : record) {
                    new_row.add(element.toUpperCase());
                }
                return new_row;
            }

            public List<String> mapColumnLabels(final List<String> labels) {
                return labels;
            }
        });
    }
}
```

Which gives:

```txt
ID,col1,col2,col3
1,A,B,C
2,D,E,F
```

{% include navigation.html %}
