/**
 * Define Oracle tables
 */
package getl.examples.oracle

import groovy.transform.BaseScript
import getl.lang.Getl

@BaseScript Getl main

// Load configuration file
runGroovyClass getl.examples.utils.Config, true

forGroup 'oracle'

// Oracle database connection
useOracleConnection oracleConnection('con', true) {
    useConfig 'oracle'
    sqlHistoryFile = "${configContent.workPath}/oracle.{date}.sql"
}

// History table for incremental load
historypoint('history', true) {
    schemaName = connection.schemaName
    tableName = 'history_point'
    saveMethod = mergeSave
}

// Price table
oracleTable('prices', true) {
    tableName = 'prices'
    field('id') { type = integerFieldType; isKey = true }
    field('name') { type = stringFieldType; isNull = false; length = 50 }
    field('create_date') { type = datetimeFieldType; isNull = false }
    field('price') { type = numericFieldType; isNull = false; length = 9; precision = 2 }
    field('description') { type = textFieldType }
}

// Customers table
oracleTable('customers', true) { table ->
    tableName = 'customers'
    field('id') { type = integerFieldType; isKey = true }
    field('name') { length = 50 }
    field('customer_type') { length = 10 }
}

// Customer phones table
oracleTable('customers.phones', true) { table ->
    tableName = 'customer_phones'
    field('customer_id') { type = integerFieldType; isKey = true }
    field('phone') { length = 50; isKey = true }
}

// Sales table
oracleTable('sales', true) {
    tableName = 'sales'
    field('id') { type = bigintFieldType; isKey = true }
    field('price_id') { type = integerFieldType; isNull = false }
    field('customer_id') { type = integerFieldType; isNull = false }
    field('sale_date') { type = datetimeFieldType; isNull = false }
    field('sale_count') { type = bigintFieldType; isNull = false }
    field('sale_sum') { type = numericFieldType; isNull = false; length = 12; precision = 2 }

    readOpts {
        hints = 'PARALLEL'
    }
}

// Query to get a list of months of sales
query('sales.part', true) {
    setQuery """SELECT DISTINCT Trunc(sale_date, 'month') as month 
                FROM ${oracleTable('sales').fullTableName}
                ORDER BY month"""
}