![MIT license](http://img.shields.io/badge/license-MIT-brightgreen.svg?style=flat)
![Build Status](https://travis-ci.org/benas/jql.svg?branch=master)
![Development Stage](https://img.shields.io/badge/development%20stage-PoC-orange.svg)

**Important note: This project is in early stage. It is published for evaluation purpose with your help!
Please [give it a try](https://github.com/benas/jql#getting-started) and send us your feedback, we would love to hear your thoughts!**

# About JQL

JQL (Java Query Language) makes it possible to query Java source code with SQL. This allows you to answer questions like:

* Is there any class that has no unit tests?
* What is the longest class name?
* Is there an interface with more than 20 methods?

This repository provides a static analysis tool that gives you precious insights on your code base in order to improve its quality.

# How does it work?

JQL engine creates a relational database from a Java code base. Once you have created the relational database, you can
query it with standard SQL. The relational model has been designed to be intuitive, natural and easy to understand and query:

### Entities:

![EDiagram](https://raw.githubusercontent.com/benas/jql/master/jql-ed.png)

### Relations:

![RDiagram](https://raw.githubusercontent.com/benas/jql/master/jql-rd.png)

### Examples of queries:

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

JQL tools are distributed in executable jars:

* jql-core: [download jar](https://oss.sonatype.org/content/groups/public/io/github/benas/jql-core/0.1/jql-core-0.1.jar)
* jql-shell: [download jar](https://oss.sonatype.org/content/groups/public/io/github/benas/jql-shell/0.1/jql-shell-0.1.jar)

**Note: JQL tools require Java 1.8+**

# Index your code base

There are two ways to create a relational database from a Java code base:

#### Using maven

Run the following command in the root folder of the project you want to analyse:

```
mvn io.github.benas:jql-maven-plugin:index
```

This will create a file named `jql.db` in the `target` directory that you can
query with your favorite SQL client or the provided [JQL shell](https://github.com/benas/jql#using-jql-shell).

#### Using java

Run the following command:

```
java -jar jql-core-{latest-version}.jar /path/to/project/to/analyse /path/to/database/directory
```

# Query your Java code with SQL

Once you have indexed your Java code in a relational format, you can query it with plain SQL:

#### Using a SQL client

Open the `jql.db` file with your favorite SQL client and start writing queries. For example, you can use [Sqlite tools](https://www.sqlite.org/download.html) or [Sqlite browser](http://sqlitebrowser.org/).

#### Using JQL Shell

JQL shell is a command line tool that allows you to write queries against the generated database.

You can start it as follows:

```
java -jar /path/to/jql-shell-{latest-version}.jar /path/to/jql.db
```

# Credits

* JQL tools use [JavaParser](https://github.com/javaparser/javaparser) to parse Java code and transform it a relational model.

* JLQ tools use [Sqlite](https://www.sqlite.org) to store Java code in a relational database.
