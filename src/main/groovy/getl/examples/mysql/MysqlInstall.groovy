/**
 * Create Mysql tables and load data from embedded tables
 */
package getl.examples.mysql

@BaseScript getl.lang.Getl getl

import groovy.transform.BaseScript

// Generate H2 sample data
runGroovyClass getl.examples.h2.H2Init, true

// Define MySQL tables
runGroovyClass getl.examples.mysql.Tables, true

profile("Create MySQL objects") {
    // Run sql script for create schemata and tables
    sql {
        exec 'CREATE SCHEMA IF NOT EXISTS getl_demo;'
        logInfo'Created schema getl_demo.'
    }

    processDatasets(MYSQLTABLE) { tableName ->
        mysqlTable(tableName) { table ->
            if (!table.exists) {
                // Create table in database
                create()
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
    run(listDatasets(MYSQLTABLE)) { tableName ->
        // Copy rows from the embedded table to the MySQL table
        copyRows(embeddedTable(tableName), mysqlTable(tableName)) {
            done { logInfo "Copied $countRow rows of $tableName from the embedded table to the MySQL table" }
        }
    }
}

thread {
    addThread {
        assert mysqlTable('prices').countRow() == 7
    }
    addThread {
        assert mysqlTable('customers').countRow() == 3
    }
    addThread {
        assert mysqlTable('customers.phones').countRow() == 7
    }
    addThread {
        assert mysqlTable('sales').countRow() == configContent.countSales
    }

    exec()
}
