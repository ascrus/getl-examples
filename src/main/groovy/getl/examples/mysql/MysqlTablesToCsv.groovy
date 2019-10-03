package getl.examples.mysql

import getl.examples.patterns.CopyTablesToCsv
import groovy.transform.BaseScript

@BaseScript getl.lang.Getl getl

/**
 * Copy all rows from Mуsql tables to Csv files
 * <br>(sales data will be unloaded with month partitioning)
 */

// Define Mysql tables
runGroovyClass getl.examples.mysql.Tables, true

// Copy Mуsql tables to Csv files
runGroovyClass CopyTablesToCsv, {
    sourceGroup = 'mysql'
    salesPart = 'sales.part'
    salesWhere = "CONVERT(DATE_FORMAT(sale_date, '%Y-%m-01'), date) = CONVERT('{month}', date)"
}