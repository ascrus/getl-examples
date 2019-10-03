/**
 * Create Hive tables and load data from embedded tables
 */
package getl.examples.hive

import getl.examples.patterns.FillingTablesWithSampleData
import getl.examples.patterns.InitTables
import getl.lang.Getl
import groovy.transform.BaseScript

@BaseScript Getl main

// Generate H2 sample data
runGroovyClass getl.examples.h2.H2Init, true

// Define Hive tables
runGroovyClass getl.examples.hive.Tables, true

profile("Create Hive objects") {
    // Run sql script for create schemata and tables
    sql(hiveConnection('hive:con')) {
        exec 'CREATE SCHEMA IF NOT EXISTS getl_demo;'
        logInfo'Created schema getl_demo.'
    }
}

// Create Hive tables
runGroovyClass InitTables, { groupName = 'hive' }

// Filling data to Hive tables
runGroovyClass FillingTablesWithSampleData, { sourceGroup = 'samples'; destGroup = 'hive' }
