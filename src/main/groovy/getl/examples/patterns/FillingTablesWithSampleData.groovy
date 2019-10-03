package getl.examples.patterns

import getl.lang.Getl
import groovy.transform.BaseScript
import groovy.transform.Field

@BaseScript Getl main

@Field String sourceGroup; assert sourceGroup != null
@Field String destGroup; assert destGroup != null

// Thread process
thread {
    // Set list of copied tables
    useList linkDatasets(sourceGroup, destGroup)
    assert !list.isEmpty(), "Destination table in group \"$destGroup\" not found!"

    // Run copy
    runWithElements {
        // Copy rows from the embedded table to the Vertica table
        copyRows(dataset(it.source), jdbcTable(it.destination)) {
            copyRow()
            logInfo "${source.readRows} rows read from $source, ${destination.updateRows} rows write to $destination"
        }
    }

    forGroup destGroup

    addThread {
        assert jdbcTable('prices').countRow() == 7
    }
    addThread {
        assert jdbcTable('customers').countRow() == 3
    }
    addThread {
        assert jdbcTable('customers.phones').countRow() == 7
    }
    addThread {
        assert jdbcTable('sales').countRow() == configContent.countSales
    }

    exec()
}