package getl.examples.postgresql

import getl.utils.FileUtils

@BaseScript getl.lang.Getl getl

import groovy.transform.BaseScript

/*
Configuration options

Create config file in <project path>/tests/postgresql/postgresql.conf with syntax:
workPath = '<log and history files directory>'
driverPath = '<postgresql jdbc file path>'
connectDatabase = '<postgresql database name>'
connectHost = '<postgresql node host>'
login = '<postgresql user name>'
password = '<postgresql user password>'
*/
configuration {
    // Clear content configuration
    clear()

    // Directory of configuration file
    path = (FileUtils.FindParentPath('.', 'src/test/groovy/getl')?:'') + 'tests/postgresql'

    // Load configuration file
    load'postgresql.conf'

    // Print message to log file and console
    logConfig "Load configuration postgresql.conf complete. Use directory \"${configContent.workPath}\"."
}