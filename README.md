# ciesvium
This project provides some Java utility classes for handling text and numerical tables, encryption and file manipulation.

In particular, the [dataset](https://ciesvium.cs.st-andrews.ac.uk/apidocs/index.html?uk/ac/standrews/cs/util/dataset/package-summary.html) package and its sub-packages provide a simple abstraction 
over rectangular plain-text tables, with ability to import and export CSV files. It's also possible to define derived tables using relational-like select and project operations, and tables
where the source data is held in encrypted form, with both symmetric and asymmetric encryption options.

## Usage via maven

```
<repository>
    <id>uk.ac.standrews.cs.maven.repository</id>
    <name>School of Computer Science Maven Repository</name>
    <url>https://maven.cs.st-andrews.ac.uk/</url>
</repository>
```
        
```
<dependency>
    <groupId>uk.ac.standrews.cs</groupId>
    <artifactId>ciesvium</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

## See also

* [API documentation](https://quicksilver.host.cs.st-andrews.ac.uk/apidocs/ciesvium/)
* [project website](https://stacs-srg.github.io/ciesvium/)
* [build server](https://beast.cs.st-andrews.ac.uk/teamcity/viewType.html?buildTypeId=Ciesvium_Build) (private)
* [relevant Maven goals](https://github.com/stacs-srg/hub/tree/master/maven) (private)
