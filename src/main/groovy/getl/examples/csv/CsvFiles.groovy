package getl.examples.csv

import getl.utils.BoolUtils
import groovy.transform.BaseScript
import groovy.transform.Field

@BaseScript getl.lang.Getl getl

/**
 * Define CSV files for load and unload data from Vertica tables
 */

@Field String sourceGroup; assert sourceGroup != null
@Field boolean packToGz = false

forGroup 'csv'

// Connection for Csv files
useCsvConnection csvConnection('con', true) {
    fieldDelimiter = '\t' // field delimiter char
    escaped = true // used escape coding for " and \n characters
    codePage = 'UTF-8' // write as utf-8 code page
    isGzFile = packToGz // pack files to GZ
    path = "${configContent.workPath}/csv" // path for csv files
    extension = 'csv'
    createPath = true // create path if not exist
    autoSchema = true // create schema files for csv files
}

processDatasets(sourceGroup + ':*', LISTJDBCTABLECLASSES) { tableName -> // Process source table
    def csvName = parseName(tableName).objectName
    csvWithDataset(csvName, dataset(tableName), true) { // Define Csv dataset for default connection on source table base
        fileName = csvName
    }
}