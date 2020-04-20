# ciesvium
This project provides some Java utility classes for handling text and numerical tables, encryption and file manipulation.

In particular, the [dataset](https://ciesvium.cs.st-andrews.ac.uk/apidocs/index.html?uk/ac/standrews/cs/util/dataset/package-summary.html) package and its sub-packages provide a simple abstraction 
over rectangular plain-text tables, with ability to import and export CSV files. It's also possible to define derived tables using relational-like select and project operations, and tables
where the source data is held in encrypted form, with both symmetric and asymmetric encryption options.

![Java CI with Maven](https://github.com/stacs-srg/ciesvium/workflows/Java%20CI%20with%20Maven/badge.svg) [![javadoc](https://javadoc.io/badge2/com.github.stacs-srg/ciesvium/javadoc.svg)](https://javadoc.io/doc/com.github.stacs-srg/ciesvium) [![codecov](https://codecov.io/gh/stacs-srg/ciesvium/branch/master/graph/badge.svg)](https://codecov.io/gh/stacs-srg/ciesvium)

## Usage via maven
        
```
<dependency>
    <groupId>com.github.stacs-srg</groupId>
    <artifactId>ciesvium</artifactId>
    <version>1.0.0</version>
</dependency>
```

## See also

* [API documentation](https://javadoc.io/doc/com.github.stacs-srg/ciesvium)
* [project website](https://stacs-srg.github.io/ciesvium/)
* [relevant Maven goals](https://github.com/stacs-srg/hub/tree/master/maven) (private)
