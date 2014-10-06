# What is this?
It adds, subtracts, and multiplies numbers. But they can be big numbers.

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
The code has has javadoc so you can build it and view it.

Then there's the readme. If you're reading this, you've found it!
This is also written as a markdown file. So you could process it as HTML and
make it all pretty.