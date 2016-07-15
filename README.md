# About JCQL

JCQL (Java Code Query Language) makes it possible to query Java source code in SQL. This allows you to ask question like:

* Is there any class that has no unit tests?
* What is the longest class name?
* Is there any class that has more than 20 methods?

This kind of static analysis allows you to have precious insights in order to improve your code base.
JCQL tools can be easily integrated in your build process to continuously check if your coding rules are respected.

# How does it work

JCQL tools create a relational model from a given Java source code base. Once you have created the relational model, you can
query it with standard SQL. The relational model has been designed to be intuitive, natural and easy to understand and query.
Here is a quick overview of the core tables:

# Indexing your code base

JCQL tools provide two ways to create a relational database from a given code base:

#### Using maven

Run the following command in the root folder of the project you want to analyse:

```
mvn io.github.benas:jcql-maven-plugin:index
```

This will create in the `target` directory a file named `jcql.db` that you can
query with your favorite SQL client or the one provided by JCQL tools

#### Using java

Run the following command in the folder of the project you want to analyse:

```
java -jar /path/to/jcql-core-{latest-version}.jar
```

Or from any directory:

```
java -jar jcql-core-{latest-version}.jar /path/to/project/to/analyse /path/to/database/directory
```

# Query your Java code with SQL

Once you have indexed your Java code in a relational format, you can query it with plain SQL:

#### Using a SQL client

Just open the `jcql.db` file with your favorite SQL client and start writing queries

#### Using JCQL Shell

The JCQL shell is a command line tool that allows you to write queries against the generated database.

You can start it as follows in the root folder of the project you indexed:

```
java -jar /path/to/jcql-shell-{latest-version}.jar
```

Or by specifying the Sqlite database file

```
java -jar /path/to/jcql-shell-{latest-version}.jar /path/to/jcql.db
```