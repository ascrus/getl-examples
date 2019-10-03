/**
 * Copy data from table to csv files
*/
package getl.examples.patterns

import getl.examples.csv.CsvFiles
import getl.lang.Getl
import getl.utils.DateUtils
import groovy.transform.BaseScript
import groovy.transform.Field

@BaseScript Getl main

@Field String sourceGroup; assert sourceGroup != null
@Field String salesPart; assert salesPart != null
@Field String salesWhere; assert salesWhere != null
@Field boolean packToGz = false

// Define Csv files with source tables structure
runGroovyClass CsvFiles, true, { sourceGroup = this.sourceGroup; packToGz = this.packToGz }

// Unload data from all tables without the sales table
thread {
    useList linkDatasets(sourceGroup, 'csv') { it != 'sales' }
    runWithElements {
        def file = csv(it.destination) { // Modify file definition (set file name)
            fileName = sourceGroup + '.' + fileName
        }

        copyRows(jdbcTable(it.source), csv(it.destination)) {
            copyRow()
            logInfo "$countRow rows copy from $source to $destination"
        }
    }
}

// Unload data from the sales tables with month partition
thread {
    forGroup sourceGroup

    // Get list of month sales partition
    useList query(salesPart).rows()
    logInfo "Found ${list.size()} month partitions from sales table"
    run(3) { sale_day -> // Run thread on one month partition
        def sales = jdbcTable('sales') { // Modify sales table definition (set filter)
            readOpts {
                where = this.salesWhere
            }
            queryParams.month = DateUtils.FormatDate('yyyy-MM-dd', sale_day.month)
        }
        def file = csv('csv:sales') { // Modify sales file definition (set file name)
            fileName = sourceGroup + '.' + fileName + '.' + DateUtils.FormatDate('yyyyMMdd', sale_day.month)
        }

        copyRows(sales, file) { // Copy rows from one month partition to one csv file
            copyRow()
            logInfo "$countRow rows copied from $source to $destination"
        }
    }
}