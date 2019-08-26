/**
 * Create MSSQL tables and load data from embedded tables
 */
package getl.examples.mssql

import getl.lang.Getl

@BaseScript Getl main

import groovy.transform.BaseScript

// Generate H2 sample data
runGroovyClass getl.examples.h2.H2Init, true

// Define MSSQL tables
runGroovyClass getl.examples.mssql.Tables, true

profile("Create MSSQL objects") {
    // Run sql script for create schemata and tables
    sql {
        exec false,  """
IF schema_id('getl_demo') IS NULL
BEGIN 
    EXEC sp_executesql N'CREATE SCHEMA getl_demo'
    COMMIT
END"""
        logInfo'Created schema getl_demo.'
    }

    processDatasets(MSSQLTABLE) { tableName ->
        mssqlTable(tableName) { table ->
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
    run(listDatasets(MSSQLTABLE)) { tableName ->
        // Copy rows from the embedded table to the Oracle table
        copyRows(embeddedTable(tableName), mssqlTable(tableName)) {
            done { logInfo "Copied $countRow rows of $tableName from the embedded table to the MSSQL table" }
        }
    }
}

thread {
    addThread {
        assert mssqlTable('prices').countRow() == 7
    }
    addThread {
        assert mssqlTable('customers').countRow() == 3
    }
    addThread {
        assert mssqlTable('customers.phones').countRow() == 7
    }
    addThread {
        assert mssqlTable('sales').countRow() == configContent.countSales
    }

    exec()
}
