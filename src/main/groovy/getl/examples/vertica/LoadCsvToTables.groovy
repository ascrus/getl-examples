package getl.examples.vertica

import getl.examples.csv.CsvFiles
import groovy.transform.BaseScript

@BaseScript getl.lang.Getl getl

// Define Vertica tables
runGroovyClass getl.examples.vertica.Tables, true
// Define Csv files
runGroovyClass CsvFiles, true, { sourceGroup = 'vertica'; packToGz = true }

thread {
    useList listJdbcTables('vertica:*')
    run(1) { tableName ->
        def objectName = parseName(tableName).objectName
        verticaTable(tableName) {
            truncate()

            bulkLoadCsv(csv("csv:$objectName")) {
                inheritFields = true
                if (objectName == 'sales') {
                    files = 'vertica.sales.{month}.csv.gz'
                }
            }
        }
    }
}