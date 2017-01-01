![MIT license](http://img.shields.io/badge/license-MIT-brightgreen.svg?style=flat)
![Build Status](https://travis-ci.org/benas/jql.svg?branch=master)
![Development Stage](https://img.shields.io/badge/development%20stage-PoC-orange.svg)

**Important note: This project is in early stage. It is published for evaluation purpose with your help!
Please [give it a try](https://github.com/benas/jql#how-to-get-started-) and send us your feedback, we would love to hear your thoughts!**

# About JQL

JQL (Java Query Language) makes it possible to query Java source code with SQL. This allows you to answer questions like:

* Is there any class that has no unit tests?
* What is the longest class name?
* Is there an interface with more than 20 methods?

This repository provides a static analysis tool that gives you precious insights on your code base in order to improve its quality.

# How does it work?

JQL engine creates a relational database from a Java code base. Once you have created the relational database, you can
query it with standard SQL. The relational model has been designed to be intuitive, natural and easy to understand and query.
Here are the core entities and relations:

### Entities

![EDiagram](https://raw.githubusercontent.com/benas/jql/master/jql-ed.png)

### Relations

![RDiagram](https://raw.githubusercontent.com/benas/jql/master/jql-rd.png)

You can find a complete description of the database schema [here](https://github.com/benas/jql/wiki/database-schema).

### Examples of queries

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

You can find some real world examples on popular open source projects [here](https://github.com/benas/jql/wiki/Samples#examples).

# How to get started ?

JQL engine is distributed as a single executable jar that creates a self-contained serverless Sqlite relational database from your Java code base:

```
java -jar jql-core-0.1.jar /path/to/project/to/analyse /path/to/database/directory
```

This command will index your code and create a file named `jql.db` in the specified directory.

For maven based projects, just run the following command in the root directory of the project to analyse:

```
mvn io.github.benas:jql-maven-plugin:index
```

The `jql.db` file will be created in the `target` directory.

Once you are done, you can open the `jql.db` file with your favorite SQL client and start writing queries.

You can find all details about how to get started [here](https://github.com/benas/jql/wiki/getting-started).

# How to contribute?

Since the project is still a PoC, we are not yet focused on the quality of the code base.
The main goal for the moment is to see if the relational model is adequate for Java code analysis and linting.

If you want to contribute, please consider the tool as a black box for now.

Any suggestion or improvement of the relational model is very welcome!

You can try to index a code base (or [download a pre-indexed database sample](https://github.com/benas/jql/wiki/Samples)) and write some queries.
 If there is a question you cannot answer, you have certainly found a way to improve the current model!
 So please open an issue with your question and this would be a great contribution!

# Credits

* JQL tools use [JavaParser](https://github.com/javaparser/javaparser) to parse Java code and transform it into a relational model.

* JQL tools use [Sqlite](https://www.sqlite.org) to store Java code model in a relational database.

# License

JQL tools are released under the MIT license:

```
The MIT License

Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
```
