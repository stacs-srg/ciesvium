## Home

This project provides some Java utility classes for handling text and numerical tables, encryption and file manipulation.

* [Java API documentation](https://quicksilver.host.cs.st-andrews.ac.uk/apidocs/ciesvium/)
* [download jar files](https://quicksilver.host.cs.st-andrews.ac.uk/artifacts/ciesvium/)
* [Maven details](https://github.com/stacs-srg/ciesvium/blob/master/README.md)

## Build and coverage status

[![CircleCI](https://circleci.com/gh/stacs-srg/ciesvium.svg?style=svg)](https://circleci.com/gh/stacs-srg/ciesvium) [![codecov](https://codecov.io/gh/stacs-srg/ciesvium/branch/master/graph/badge.svg)](https://codecov.io/gh/stacs-srg/ciesvium)

## Overview

In particular, the [dataset](https://quicksilver.host.cs.st-andrews.ac.uk/apidocs/ciesvium/index.html?uk/ac/standrews/cs/util/dataset/package-summary.html) package and its sub-packages provide a simple abstraction over rectangular plain-text tables, with ability to import and export CSV files. It's also possible to define derived tables using relational-like select and project operations, and tables where the source data is held in encrypted form, with both symmetric and asymmetric encryption options.

See also:

* [usage](usage/)

{% include navigation.html %}
