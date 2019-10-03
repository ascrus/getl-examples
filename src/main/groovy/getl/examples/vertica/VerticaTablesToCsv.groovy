package getl.examples.vertica

import getl.examples.patterns.CopyTablesToCsv
import groovy.transform.BaseScript

@BaseScript getl.lang.Getl getl

/**
 * Copy all rows from Vertica tables to Csv files
 * <br>(sales data will be unloaded with month partitioning)
 */

// Define Vertica tables
runGroovyClass getl.examples.vertica.Tables, true

// Copy Vertica tables to Csv files
runGroovyClass CopyTablesToCsv, {
    sourceGroup = 'vertica'
    salesPart = 'sales.part'
    packToGz = true
    salesWhere = "Trunc(sale_date, 'month') = '{month}'"
}