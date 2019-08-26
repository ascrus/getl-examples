GETL - based package in Groovy, which automates the work of loading and transforming data. His name is an acronym for "Groovy ETL": https://github.com/ascrus/getl

Sample scripts for loading and transforming data from file sources and DBMS using the GETL DSL extension for Groovy.

The examples show how to manage connections to the DBMS and their tables, copy data between different sources, work with local, network and distributed file systems, store parameters for processes and connections in configurations, organize multi-threaded work with data, send mail and much more.

Installation instruction:
<ol>
<li>Create new directory and make it current;</li>
<li>Clone from GIT to current empty directory: "git clone https://github.com/ascrus/getl-examples.git ./";</li>
<li>Import project to IntelliJ IDEA as a gradle project;</li>
<li>Create directory "config" in project directory and copy to this directory the file "src/main/resources/examples/config.groovy";</li>
<li>Edit the file "config/config.groovy", fill in the parameters needed to run the examples;</li>
<li>Run the script "h2.H2Install" to test the examples;</li>
<li>Run the other installation scripts for planned DBMS examples.</li>
</ol>

P.S. For the examples in Idea to work correctly, it is required to set the value "$MODULE_WORKING_DIR$" as the working directory in the configuration of running scripts.