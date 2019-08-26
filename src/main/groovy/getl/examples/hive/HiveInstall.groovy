/**
 * Create Hive tables and load data from embedded tables
 */
package getl.examples.hive

import getl.lang.Getl
import groovy.transform.BaseScript

@BaseScript Getl main

// Generate H2 sample data
runGroovyClass getl.examples.h2.H2Init, true

// Define Hive tables
runGroovyClass getl.examples.hive.Tables, true

profile("Create Hive objects") {
    // Run sql script for create schemata and tables
    sql {
        exec 'CREATE SCHEMA IF NOT EXISTS getl_demo;'
        logInfo'Created schema getl_demo.'
    }

    // Create or truncate tables
    processDatasets(HIVETABLE) { tableName ->
        hiveTable(tableName) { table ->
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
    run(listDatasets(HIVETABLE)) { tableName ->
        // Copy rows from the embedded table to the Hive table
        copyRows(embeddedTable(tableName), hiveTable(tableName)) {
            bulkLoad = true
            done { logInfo "Copied $countRow rows of $tableName from the embedded table to the Hive table" }
        }
    }
}

thread {
    addThread {
        assert hiveTable('prices').countRow() == 7
    }
    addThread {
        assert hiveTable('customers').countRow() == 3
    }
    addThread {
        assert hiveTable('customers.phones').countRow() == 7
    }
    addThread {
        assert hiveTable('sales').countRow() == configContent.countSales
    }

    exec()
}
