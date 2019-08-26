/**
 * Create PostgreSQL tables and load data from embedded tables
 */
package getl.examples.postgresql

import getl.lang.Getl
import groovy.transform.BaseScript

@BaseScript Getl main

// Generate sample data in a H2  database
runGroovyClass getl.examples.h2.H2Init, true

// Define PostgreSQL tables
runGroovyClass getl.examples.postgresql.Tables, true

profile("Create PostgreSQL objects") {
    // Run sql script for create schemata and tables
    sql {
        exec 'CREATE SCHEMA IF NOT EXISTS getl_demo;'
        logInfo'Created schema getl_demo.'
    }

    processDatasets(POSTGRESQLTABLE) { tableName ->
        postgresqlTable(tableName) { table ->
            if (!table.exists) {
                // Create table in database
                create ifNotExists: true
                logInfo "Created table $tableName."
            }
            else {
                truncate()
                logInfo "Truncated table $tableName."
            }
        }
    }
}

thread {
    run(listDatasets(POSTGRESQLTABLE)) { tableName ->
        // Copy rows from the embedded table to the PostgreSQL table
        copyRows(embeddedTable(tableName), postgresqlTable(tableName)) {
            done { logInfo "Copied $countRow rows of $tableName from the embedded table to the PostgreSQL table" }
        }
    }
}

thread {
    addThread {
        assert postgresqlTable('prices').countRow() == 7
    }
    addThread {
        assert postgresqlTable('customers').countRow() == 3
    }
    addThread {
        assert postgresqlTable('customers.phones').countRow() == 7
    }
    addThread {
        assert postgresqlTable('sales').countRow() == configContent.countSales
    }

    exec()
}
