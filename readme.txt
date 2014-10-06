# What is this?
It adds, subtracts, and multiplies numbers. But they can be big numbers.

## Anything cool?
Not really.

I support signed digits.

LinkedList implementation follows JDK interface and can work with the standard
Java Collections framework just fine.

That's it.

# Who are you?
I am Seyed Raoofi.

# How do I run?
This project was built with JDK8 with source/binary compatibility set to Java
7. It may have some remnants of JDK8 and require JDK8 to build. However, it
should run using JRE7. To be safe, I'm going to say that you need JRE8 and
JDK8 to run and build this project.

To run it, you can just go to the jar found in:

    target/BigIntCalculator-1.0-SNAPSHOT.jar

You can execute it by invoking:

	java -jar BigIntCalculator-1.0-SNAPSHOT.jar
	
Note that this does not have GUI as the runner was provided. 

# How do I build?
You could use Maven directly to build and get dependencies. Frankly, I don't
know much about manually invoking Maven but it should work in that regard. If 
Maven is set up properly on your system you could, supposedly, just do:

	mvn clean install

Or you could just import it as a Netbeans project by going to:

	Files -> Import Project -> From Zip
	
You may also need to open the project:
	
	Files -> Open Project...
	
And then selecting the original zip file. You may need to download dependencies
by going to Project Explorer, right clicking the Dependency folder, and then
clicking Download Declared Dependencies.

Note that this program does not depend on anything for execution but requires
JUnit for testing and some Guava libraries for pre-written unit tests.

Also, Netbeans doesn't support UAC paths for shell execution which is required
when executing Maven when the project is on a network drive. Thus, it will not
work on Frostburg machine without first moving the project outside the default
projects directory.

# Where is documentation?
To see JavaDoc documentation, you can just go to the
[JavaDoc](target/site/apidocs/index.html).

Then there's the readme. If you're reading this, you've found it!
This is also written as a markdown file. So you could process it as HTML and
make it all pretty.

# File Tree
This project is a relatively typical maven project created in Netbeans.

|   .gitignore
|   nb-configuration.xml
|   nbactions.xml
|   pom.xml
|   tree.txt
|   
+---src
|   +---main
|   |   +---java
|   |   |   \---edu
|   |   |       \---frostburg
|   |   |           +---cosc310
|   |   |           |       Cosc310BigIntCalculator.java
|   |   |           |       Cosc310LLTester.java
|   |   |           |       CoscLLProgram.java
|   |   |           |       
|   |   |           \---Cosc310BigInt
|   |   |               \---skraoofi0
|   |   |                       BigInt.java
|   |   |                       BigIntCalculator.java
|   |   |                       BigIntList.java
|   |   |                       LinkedList.java
|   |   |                       ListFactory.java
|   |   |                       
|   |   \---resources
|   |           big_math.txt
|   |           
|   \---test
|       \---java
|           \---edu
|               \---frostburg
|                   \---Cosc310BigInt
|                       \---skraoofi0
|                               BigIntListTest.java
|                               BigIntTestSuperClass.java
|                               LinkedListTest.java
|                               
\---target
    |   BigIntCalculator-1.0-SNAPSHOT.jar
    |   
    +---classes
    |   |   .netbeans_automatic_build
    |   |   big_math.txt
    |   |   
    |   \---edu
    |       \---frostburg
    |           +---cosc310
    |           |       Cosc310BigIntCalculator.class
    |           |       Cosc310LLTester.class
    |           |       CoscLLProgram.class
    |           |       
    |           \---Cosc310BigInt
    |               \---skraoofi0
    |                       BigInt.class
    |                       BigIntCalculator.class
    |                       BigIntList.class
    |                       LinkedList$1.class
    |                       LinkedList$Item.class
    |                       LinkedList$MainIter.class
    |                       LinkedList.class
    |                       ListFactory.class
    |                       
    +---generated-sources
    |   +---annotations
    |   \---test-annotations
    +---javadoc-bundle-options
    |       javadoc-options-javadoc-resources.xml
    |       package-list
    |       
    +---maven-archiver
    |       pom.properties
    |       
    +---site
    |   \---apidocs
    |       |   allclasses-frame.html
    |       |   allclasses-noframe.html
    |       |   constant-values.html
    |       |   deprecated-list.html
    |       |   help-doc.html
    |       |   index-all.html
    |       |   index.html
    |       |   overview-frame.html
    |       |   overview-summary.html
    |       |   overview-tree.html
    |       |   package-list
    |       |   script.js
    |       |   serialized-form.html
    |       |   stylesheet.css
    |       |   
    |       \---edu
    |           \---frostburg
    |               +---cosc310
    |               |   |   Cosc310BigIntCalculator.html
    |               |   |   Cosc310LLTester.html
    |               |   |   CoscLLProgram.html
    |               |   |   package-frame.html
    |               |   |   package-summary.html
    |               |   |   package-tree.html
    |               |   |   package-use.html
    |               |   |   
    |               |   \---class-use
    |               |           Cosc310BigIntCalculator.html
    |               |           Cosc310LLTester.html
    |               |           CoscLLProgram.html
    |               |           
    |               \---Cosc310BigInt
    |                   \---skraoofi0
    |                       |   BigInt.html
    |                       |   BigIntCalculator.html
    |                       |   BigIntList.html
    |                       |   LinkedList.html
    |                       |   ListFactory.html
    |                       |   package-frame.html
    |                       |   package-summary.html
    |                       |   package-tree.html
    |                       |   package-use.html
    |                       |   
    |                       \---class-use
    |                               BigInt.html
    |                               BigIntCalculator.html
    |                               BigIntList.html
    |                               LinkedList.html
    |                               ListFactory.html
    |                               
    +---surefire
    +---surefire-reports
    |       edu.frostburg.Cosc310BigInt.skraoofi0.BigIntListTest.txt
    |       edu.frostburg.Cosc310BigInt.skraoofi0.LinkedListTest.txt
    |       TEST-edu.frostburg.Cosc310BigInt.skraoofi0.BigIntListTest.xml
    |       TEST-edu.frostburg.Cosc310BigInt.skraoofi0.LinkedListTest.xml
    |       
    \---test-classes
        |   .netbeans_automatic_build
        |   
        \---edu
            \---frostburg
                \---Cosc310BigInt
                    \---skraoofi0
                            BigIntListTest.class
                            BigIntTestSuperClass.class
                            LinkedListTest$CustomTests.class
                            LinkedListTest$GuavaTests$1.class
                            LinkedListTest$GuavaTests.class
                            LinkedListTest.class
                            
