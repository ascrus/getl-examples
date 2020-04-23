# Getl project example
This project contains examples of the development of ETL processes for working with data sources based 
on the open source product Getl: [https://github.com/ascrus/getl](Getl)

## Documentation
* See Wiki for English instructions on working with examples: 
[https://github.com/ascrus/getl-examples/wiki](Getl Examples manual)
* See GitHub Documents for Russian instructions on working with examples:
[https://ascrus.github.io/getl-docs/4__primery_getl.htm?ms=AQAQ&st=MA%3D%3D&sct=MA%3D%3D&mw=MjQ0](Getl Examples documentation)

## Source structure
* package main.groovy.getl.examples:
    * app - running application classes
    * data - description of data sources in the repository
    * files - description of file systems in the repository
    * launcher - project utility classes for starting and testing ETL processes
    * pattern - reusable templates
    * process - ETL processes
    * utils - utility libraries
* package test.groovy.getl.examples:
    * data - unit tests of data sources
    * files - unit tests of file system
    * patttern - unit tests of reusable patterns
    * process - unit tests of ETL processes
    * utils - unit tests of utility libraries
    
## Project data initialization
Run class "getl.examples.app.InitProject" from under the IDE or command line.

## Running examples
Run the required class from package "getl.examples.app" from under the IDE or command line.

## Running unit tests
Running the required unit test class in the test module from under the IDE or gradle.
