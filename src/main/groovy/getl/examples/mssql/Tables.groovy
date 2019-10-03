/**
 * Define MSSQL tables
 */
package getl.examples.mssql

import getl.lang.Getl

@BaseScript Getl main

import groovy.transform.BaseScript

// Load configuration file
runGroovyClass getl.examples.utils.Config, true

forGroup 'mssql'

// MSSQL database connection
useMssqlConnection mssqlConnection('con', true) {
    // Use parameters from [connections.mssql] section
    useConfig 'mssql'
    schemaName = 'getl_demo'
    sqlHistoryFile = "${configContent.workPath}/mssql.{date}.sql"
}

// History table for incremental load
historypoint('history', true) {
    schemaName = connection.schemaName
    tableName = 'history_point'
    saveMethod = mergeSave
}

// Price table
mssqlTable('prices', true) {
    tableName = 'prices'
    field('id') { type = integerFieldType; isKey = true }
    field('name') { type = stringFieldType; isNull = false; length = 50 }
    field('create_date') { type = datetimeFieldType; isNull = false }
    field('price') { type = numericFieldType; isNull = false; length = 9; precision = 2 }
    field('description') { type = textFieldType }
}

// Customers table
mssqlTable('customers', true) { table ->
    tableName = 'customers'
    field('id') { type = integerFieldType; isKey = true }
    field('name') { length = 50 }
    field('customer_type') { length = 10 }
}

// Customer phones table
mssqlTable('customers.phones', true) { table ->
    tableName = 'customer_phones'
    field('customer_id') { type = integerFieldType; isKey = true }
    field('phone') { length = 50; isKey = true }
}

// Sales table
mssqlTable('sales', true) {
    tableName = 'sales'
    field('id') { type = bigintFieldType; isKey = true }
    field('price_id') { type = integerFieldType; isNull = false }
    field('customer_id') { type = integerFieldType; isNull = false }
    field('sale_date') { type = datetimeFieldType; isNull = false }
    field('sale_count') { type = bigintFieldType; isNull = false }
    field('sale_sum') { type = numericFieldType; isNull = false; length = 12; precision = 2 }

    readOpts {
        withHint = 'NOLOCK'
    }
}

// Query to get a list of months of sales
query('sales.part', true) {
    setQuery """SELECT DISTINCT DATEFROMPARTS(Year(sale_date), Month(sale_date), 1) as month  
                FROM ${mssqlTable('sales').fullTableName}
                ORDER BY month"""
}