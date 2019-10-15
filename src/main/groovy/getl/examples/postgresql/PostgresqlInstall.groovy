/**
 * Create PostgreSQL tables and load data from embedded tables
 */
package getl.examples.postgresql

import getl.examples.patterns.FillingTablesWithSampleData
import getl.examples.patterns.InitTables
import getl.lang.Getl
import groovy.transform.BaseScript

@BaseScript Getl main

// Generate sample data in a H2  database
runGroovyClass getl.examples.h2.H2Init, true

// Define PostgreSQL tables
runGroovyClass getl.examples.postgresql.Tables, true

profile("Create PostgreSQL objects") {
    // Run sql script for create schemata and tables
    sql(postgresqlConnection('postgresql:con')) {
        exec 'CREATE SCHEMA IF NOT EXISTS getl_demo;'
        logInfo'Created schema getl_demo.'
    }

    // Create history table
    historypoint('postgresql:history') {
        if (!exists) {
            create(true)
            logInfo "Created history point table $it"
        } else {
            truncate()
            logInfo "Truncated history point table $it"
        }
    }
}

// Create PostgreSql tables
runGroovyClass InitTables, { groupName = 'postgresql'; recreateTables = false }

// Filling data to PostgreSql tables
runGroovyClass FillingTablesWithSampleData, { sourceGroup = 'samples'; destGroup = 'postgresql' }
