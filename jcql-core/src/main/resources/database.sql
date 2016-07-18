-- entities
CREATE TABLE COMPILATION_UNIT ( ID INT, NAME VARCHAR, PACKAGE VARCHAR );
CREATE TABLE CLASS ( ID INT, NAME VARCHAR, IS_ABSTRACT INT, IS_FINAL INT, IS_PUBLIC INT, CU_ID INT , FOREIGN KEY(CU_ID) REFERENCES COMPILATION_UNIT(ID));
CREATE TABLE INTERFACE ( ID INT, NAME VARCHAR, IS_FINAL INT, IS_PUBLIC INT, CU_ID INT , FOREIGN KEY(CU_ID) REFERENCES COMPILATION_UNIT(ID));
CREATE TABLE ANNOTATION ( ID INT, NAME VARCHAR, IS_PUBLIC INT, CU_ID INT , FOREIGN KEY(CU_ID) REFERENCES COMPILATION_UNIT(ID));
CREATE TABLE ENUMERATION ( ID INT, NAME VARCHAR, IS_PUBLIC INT, CU_ID INT , FOREIGN KEY(CU_ID) REFERENCES COMPILATION_UNIT(ID));
CREATE TABLE FIELD ( ID INT, NAME VARCHAR, TYPE VARCHAR, IS_PUBLIC INT, IS_STATIC INT, IS_FINAL INT, IS_TRANSIENT INT, TYPE_ID INT );
CREATE TABLE CONSTRUCTOR ( ID INT, NAME VARCHAR, IS_PUBLIC INT, TYPE_ID INT );
CREATE TABLE METHOD ( ID INT, NAME VARCHAR, IS_ABSTRACT INT, IS_FINAL INT, IS_PUBLIC INT, TYPE_ID INT );
CREATE TABLE PARAMETER ( ID INT, NAME VARCHAR, TYPE VARCHAR, METHOD_ID INT);

-- relations
CREATE TABLE EXTENDS ( TYPE_ID INT, EXTENDED_TYPE_ID INT ); -- KEY = (TYPE_ID, EXTENDED_TYPE_ID)
CREATE TABLE IMPLEMENTS ( CLASS_ID INT, INTERFACE_ID INT ); -- KEY = (CLASS_ID, INTERFACE_ID)

-- TODO

-- 1. FIELD.TYPE_ID, CONSTRUCTOR.TYPE_ID and METHOD.TYPE_ID are FK to CLASS(ID) or INTERFACE(ID) ..
--    Should CLASS and INTERFACE be merged into TYPE with a discriminator column IS_INTERFACE ?
--    It is easier and more natural to write "Select * from Interface" than "Select * from Type where Is_Interface = true"..
--    What about annotations and enumerations which are also types?

-- 2. PARAMETER.METHOD_ID can also refer a constructor. Should CONSTRUCTOR and METHOD be merged with a discriminator column IS_CONSTRUCTOR?

-- 3. In regards to points 1 & 2: Probably merge tables and create views?

-- 4. Should full qualified package name be added in types tables (class, interface, annotation and enumeration) ?
--    This will avoid a join with Compilation_Unit when aggregating on package (the average number of classes by package for instance).
--    After all, this is a reporting database (http://martinfowler.com/bliki/ReportingDatabase.html), no problem if it is not in 3rd normal form :=)

-- 5. Does a "PACKAGE" table makes sense?

-- 6. What about method return types and thrown exceptions?

-- 7. How to design other relations? Think of:
--    7.1 "OVERRIDES": allows to determine if a method is overridden in a subtype
--    7.2 "OVERLOADS": allows to determine if a method is overloaded
--    7.3 "ANNOTATED_WITH": allows queries like "how many classes are annotated with more than 10 annotations?" This is for http://annotatiomania.com/ :=)
--    7.4 Any other meaningful relations?