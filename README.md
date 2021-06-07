# ciesvium
This project provides some Java utility classes for handling text and numerical tables, encryption and file manipulation.

In particular, the *dataset* package and its sub-packages provide a simple abstraction 
over rectangular plain-text tables, with ability to import and export CSV files. It's also possible to define derived tables using relational-like select and project operations, and tables
where the source data is held in encrypted form, with both symmetric and asymmetric encryption options.

![Java CI with Maven](https://github.com/stacs-srg/ciesvium/workflows/Java%20CI%20with%20Maven/badge.svg)

## Usage via maven
        
```
<dependency>
    <groupId>uk.ac.standrews.cs</groupId>
    <artifactId>ciesvium</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
...
<repository>
    <id>uk.ac.standrews.cs.maven.repository</id>
    <name>School of Computer Science Maven Repository</name>
    <url>https://maven.cs.st-andrews.ac.uk/</url>
</repository>
```

## See also

* [project website](https://stacs-srg.github.io/ciesvium/)
* [relevant Maven goals](https://github.com/stacs-srg/hub/tree/master/maven) (private)
