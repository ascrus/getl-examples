/**
 * Create Vertica tables and load data from embedded tables
 */
package getl.examples.vertica

import getl.examples.patterns.*
import getl.lang.Getl
import groovy.transform.BaseScript

@BaseScript Getl main

// Generate sample data in a H2  database
runGroovyClass getl.examples.h2.H2Init, true

// Define Vertica tables
runGroovyClass getl.examples.vertica.Tables, true

profile("Create Vertica schema") {
    // Run sql script for create schemata and tables
    sql(verticaConnection('vertica:con')) {
        exec 'CREATE SCHEMA IF NOT EXISTS getl_demo;'
        logInfo'Created schema getl_demo'
    }

    // Create history table
    historypoint('vertica:history') {
        if (!exists) {
            create(true)
            logInfo "Created history point table $it"
        } else {
            truncate()
            logInfo "Truncated history point table $it"
        }
    }
}

// Create Vertica tables
runGroovyClass InitTables, { groupName = 'vertica' }

// Filling data to Vertica tables
runGroovyClass FillingTablesWithSampleData, { sourceGroup = 'samples'; destGroup = 'vertica' }
