package getl.examples.mssql

import getl.utils.FileUtils

@BaseScript getl.lang.Getl getl

import groovy.transform.BaseScript

/*
Configuration options

Create config file in <project path>/tests/mssql/mssql.conf with syntax:
workPath = '<log and history files directory>'
driverPath = '<mssql jdbc file path>'
connectDatabase = '<mssql database name>'
connectHost = '<mssql node host>'
login = '<mssql user name>'
password = '<mssql user password>'
*/
configuration {
    // Directory of configuration file
    path = configVars.configPath?:'config'

    // Load configuration file
    load'mssql.conf'

    // Print message to log file and console
    logConfig "Load configuration mssql.conf complete. Use directory \"${configContent.workPath}\"."
}