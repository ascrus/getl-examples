/**
 * Create Oracle tables and load data from embedded tables
 */
package getl.examples.oracle

import getl.examples.patterns.FillingTablesWithSampleData
import getl.examples.patterns.InitTables
import getl.lang.Getl
import groovy.transform.BaseScript

@BaseScript Getl main

// Generate sample data in a H2  database
runGroovyClass getl.examples.h2.H2Init, true

// Define Oracle tables
runGroovyClass getl.examples.oracle.Tables, true

profile("Create Oracle objects") {
    historypoint('oracle:history') {
        if (!exists) {
            create(true)
            logInfo "Created history point table $it"
        } else {
            truncate()
            logInfo "Truncated history point table $it"
        }
    }
}

// Create Oracle tables
runGroovyClass InitTables, { groupName = 'oracle' }

// Filling data to Oracle tables
runGroovyClass FillingTablesWithSampleData, { sourceGroup = 'samples'; destGroup = 'oracle' }
