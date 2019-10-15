/**
 * Create MSSQL tables and load data from embedded tables
 */
package getl.examples.mssql

import getl.examples.patterns.FillingTablesWithSampleData
import getl.examples.patterns.InitTables
import getl.lang.Getl

@BaseScript Getl main

import groovy.transform.BaseScript

// Generate H2 sample data
runGroovyClass getl.examples.h2.H2Init, true

// Define MSSQL tables
runGroovyClass getl.examples.mssql.Tables, true

profile("Create MSSQL objects") {
    // Run sql script for create schemata and tables
    sql(mssqlConnection('mssql:con')) {
        exec false,  """
IF schema_id('getl_demo') IS NULL
BEGIN 
    EXEC sp_executesql N'CREATE SCHEMA getl_demo'
    COMMIT
END"""
        logInfo'Created schema getl_demo.'
    }

    historypoint('mssql:history') {
        if (!exists) {
            create(true)
            logInfo "Created history point table $it"
        } else {
            truncate()
            logInfo "Truncated history point table $it"
        }
    }
}

// Create MSSQL tables
runGroovyClass InitTables, { groupName = 'mssql'; recreateTables = false }

// Filling data to MSSQL tables
runGroovyClass FillingTablesWithSampleData, { sourceGroup = 'samples'; destGroup = 'mssql' }
