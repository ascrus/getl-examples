/**
 * Create Mysql tables and load data from embedded tables
 */
package getl.examples.mysql

import getl.examples.patterns.FillingTablesWithSampleData
import getl.examples.patterns.InitTables

@BaseScript getl.lang.Getl getl

import groovy.transform.BaseScript

// Generate H2 sample data
runGroovyClass getl.examples.h2.H2Init, true

// Define MySQL tables
runGroovyClass getl.examples.mysql.Tables, true

profile("Create MySQL objects") {
    // Run sql script for create schemata and tables
    sql(mysqlConnection('mysql:con')) {
        exec 'CREATE SCHEMA IF NOT EXISTS getl_demo;'
        logInfo'Created schema getl_demo.'
    }

    historypoint('mysql:history') {
        if (!exists) {
            create(true)
            logInfo "Created history point table $it"
        } else {
            truncate()
            logInfo "Truncated history point table $it"
        }
    }
}

// Create MySQL tables
runGroovyClass InitTables, { groupName = 'mysql' }

// Filling data to MySQL tables
runGroovyClass FillingTablesWithSampleData, { sourceGroup = 'samples'; destGroup = 'mysql' }
