package getl.examples.oracle

import getl.utils.FileUtils

@BaseScript getl.lang.Getl getl

import groovy.transform.BaseScript

/*
Configuration options

Create config file in <project path>/tests/oracle/oracle.conf with syntax:
workPath = '<log and history files directory>'
driverPath = '<oracle jdbc file path>'
connectDatabase = '<oracle database name>'
connectHost = '<oracle node host>'
login = '<oracle user name>'
password = '<oracle user password>'
*/
configuration {
    // Clear content configuration
    clear()

    // Directory of configuration file
    path = (FileUtils.FindParentPath('.', 'src/test/groovy/getl')?:'') + 'tests/oracle'

    // Load configuration file
    load'oracle.conf'

    // Print message to log file and console
    logConfig "Load configuration oracle.conf complete. Use directory \"${configContent.workPath}\"."
}