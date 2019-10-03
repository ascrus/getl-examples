package getl.examples.mssql

import getl.examples.patterns.CopyTablesToCsv
import groovy.transform.BaseScript

@BaseScript getl.lang.Getl getl

/**
 * Copy all rows from Mssql tables to Csv files
 * <br>(sales data will be unloaded with month partitioning)
 */

// Define Mssql tables
runGroovyClass getl.examples.mssql.Tables, true

// Copy Mssql tables to Csv files
runGroovyClass CopyTablesToCsv, {
    sourceGroup = 'mssql'
    salesPart = 'sales.part'
    salesWhere = "DATEFROMPARTS(Year(sale_date), Month(sale_date), 1) = CONVERT(date, '{month}', 20)"
}