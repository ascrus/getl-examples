package getl.examples.postgresql

import getl.examples.patterns.CopyTablesToCsv
import groovy.transform.BaseScript

@BaseScript getl.lang.Getl getl

/**
 * Copy all rows from PostgreSql tables to Csv files
 * <br>(sales data will be unloaded with month partitioning)
 */

// Define Vertica tables
runGroovyClass getl.examples.postgresql.Tables, true

// Copy Vertica tables to Csv files
runGroovyClass CopyTablesToCsv, {
    sourceGroup = 'postgresql'
    salesPart = 'sales.part'
    salesWhere = "Date_Trunc('month', sale_date) = '{month}'"
}