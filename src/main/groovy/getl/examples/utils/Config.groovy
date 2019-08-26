package getl.examples.utils

import getl.lang.Getl
import groovy.transform.BaseScript

@BaseScript Getl main

configuration {
    // Directory of configuration file
    path = configVars.configPath?:'config'

    // Load configuration file
    load'config.groovy'

    // Print message to log file and console
    logConfig "Load configuration config.groovy complete. Use directory \"${configContent.workPath}\"."
}