package getl.examples.vertica

import getl.utils.FileUtils
import groovy.transform.BaseScript
import getl.examples.h2.Install as H2Install

@BaseScript getl.lang.Getl getl

/**
 * Create Vertica tables and load data from H2 tables to Vertica
 */

assert FileUtils.FileFromResources('fileutils/file.txt') != null
assert FileUtils.FileFromResources('lang/vertica/init.sql') != null

// Generate sample data in a H2  database
runGroovyClass H2Install, true

// Define Vertica tables
runGroovyClass getl.examples.vertica.Tables, true

profile("Create Vertica objects") {
    // Run sql script for create schemata and tables
    sql {
        loadResource 'lang/vertica/init.sql'
        runSql false
        logInfo'Created schema getl_demo.'
    }

    historypoint('vertica.demo') {
        if (!exists) {
            create(true)
            logInfo 'History point table created.'
        }
        else {
            truncate()
            logInfo 'History point table cleared.'
        }
    }

    processDatasets(VERTICATABLE) { tableName ->
        verticaTable(tableName) { table ->
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
    run(listDatasets(VERTICATABLE)) { tableName ->
        // Copy rows from the embedded table to the Vertica table
        copyRows(embeddedTable(tableName), verticaTable(tableName)) {
            bulkLoad = true
            done { logInfo "Copied $countRow rows of $tableName from the embedded table to the Vertica table" }
        }
    }
}

thread {
    addThread {
        assert verticaTable('prices').countRow() == 7
    }
    addThread {
        assert verticaTable('customers').countRow() == 3
    }
    addThread {
        assert verticaTable('customers.phones').countRow() == 7
    }
    addThread {
        assert verticaTable('sales').countRow() == 250000
    }

    exec()
}
