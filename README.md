![MIT license](http://img.shields.io/badge/license-MIT-brightgreen.svg?style=flat)
![Build Status](https://travis-ci.org/benas/jcql.svg?branch=master)
![Development Stage](https://img.shields.io/badge/development%20stage-alpha-orange.svg)

**Important note: This project is in early stage. It's a PoC published for evaluation purpose with your help!
If the idea is successful, the project may be renamed and the code base will be completely rewritten, otherwise this repository will be deleted.
Even though the project is not released yet, you can still [give it a try](https://github.com/benas/jcql#getting-started) and send us your feedback, you are very welcome!**

# About JCQL

JCQL (Java Code Query Linter) makes it possible to query Java source code with SQL. This allows you to answer questions like:

* Is there any class that has no unit tests?
* What is the longest class name?
* Is there any class that has more than 20 methods?

It is a static analysis tool that gives you precious insights on your code base in order to improve its quality.
JCQL can be easily integrated in your build process to continuously check if your coding rules are respected.

# How it works?

JCQL creates a relational database from a given Java code base. Once you have created the relational model, you can
query it with standard SQL. The relational model has been designed to be intuitive, natural and easy to understand and query:

#### Entities:

![EDiagram](https://raw.githubusercontent.com/benas/jcql/master/jcql-ed.png)

#### Relations:

![RDiagram](https://raw.githubusercontent.com/benas/jcql/master/jcql-rd.png)

#### Examples of queries:

###### Find interfaces with more than 20 methods

```sql
SELECT i.NAME as name, COUNT(m.ID) as nbMethods
FROM INTERFACE i, METHOD m
WHERE m.TYPE_ID = i.ID GROUP BY i.ID HAVING COUNT(m.ID) > 20;
```

###### Find classes with more than 5 constructors

```sql
SELECT c.NAME as name, COUNT(c.ID) as nbConstructors
FROM CLASS c, CONSTRUCTOR co
WHERE co.TYPE_ID = c.ID GROUP BY c.NAME HAVING COUNT(c.ID) > 5;
```

###### Find methods with more than 5 parameters

```sql
SELECT t.NAME as typeName, m.NAME as methodName, COUNT(m.ID) as nbParams
FROM TYPE t, METHOD m, PARAMETER p
WHERE p.METHOD_ID = m.ID and m.TYPE_ID = t.ID
GROUP BY m.ID HAVING COUNT(m.ID) > 5;
```

###### Find the top 10 longest class names:

```sql
SELECT name, LENGTH(name) as length from CLASS ORDER BY length DESC LIMIT 10;
```

# Getting started

JCQL is in early stage and is not released yet. You can still try the first snapshot version:

* jcql-core: [download jar](https://oss.sonatype.org/content/repositories/snapshots/io/github/benas/jcql-core/0.1-SNAPSHOT/jcql-core-0.1-20160719.092316-1.jar)
* jcql-shell: [download jar](https://oss.sonatype.org/content/repositories/snapshots/io/github/benas/jcql-shell/0.1-SNAPSHOT/jcql-shell-0.1-20160719.092346-1.jar)

**Note: JCQL require Java 1.8+**

# Index your code base

JCQL provides two ways to create a relational database from a given Java code base:

#### Using maven

Add the following repository in your `pom.xml` :

```xml
<repository>
    <id>ossrh</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
</repository>
```

and run the following command in the root folder of the project you want to analyse:

```
mvn io.github.benas:jcql-maven-plugin:0.1-SNAPSHOT:index
```

This will create a file named `jcql.db` in the `target` directory that you can
query with your favorite SQL client or the one provided by JCQL.

#### Using java

Run the following command:

```
java -jar jcql-core-{latest-version}.jar /path/to/project/to/analyse /path/to/database/directory
```

# Query your Java code with SQL

Once you have indexed your Java code in a relational format, you can query it with plain SQL:

#### Using a SQL client

Open the `jcql.db` file with your favorite SQL client and start writing queries. For example, you can use [Sqlite tools](https://www.sqlite.org/download.html) or [Sqlite browser](http://sqlitebrowser.org/).

#### Using JCQL Shell

The JCQL shell is a command line tool that allows you to write queries against the generated database.

You can start it as follows:

```
java -jar /path/to/jcql-shell-{latest-version}.jar /path/to/jcql.db
```

# Credits

* JCQL tools use the great [java-parser](https://github.com/javaparser/javaparser) library to parse Java code and transform it a relational model.

* JCQL tools use [Sqlite](https://www.sqlite.org) to store and query relational data about Java code
