package getl.examples.oracle

import getl.examples.patterns.CopyTablesToCsv
import groovy.transform.BaseScript

@BaseScript getl.lang.Getl getl

/**
 * Copy all rows from Oracle tables to Csv files
 * <br>(sales data will be unloaded with month partitioning)
 */

// Define Oracle tables
runGroovyClass getl.examples.oracle.Tables, true

// Copy Oracle tables to Csv files
runGroovyClass CopyTablesToCsv, {
    sourceGroup = 'oracle'
    salesPart = 'sales.part'
    packToGz = false
    salesWhere = "Trunc(sale_date, 'month') = TO_DATE('{month}', 'YYYY-MM-DD')"
}