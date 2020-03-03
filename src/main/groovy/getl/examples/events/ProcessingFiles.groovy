package getl.examples.events

import getl.examples.repository.Db
import getl.examples.repository.Json
import getl.lang.Getl
import groovy.transform.BaseScript

@BaseScript Getl main

// Define database objects
runGroovyClass Db, true
// Using writer login
h2Connection('db:con') { useLogin 'writer' }

// Define json objects
runGroovyClass Json, true

// Define source file managers
files('#events', true) {
    // Set the path to the source events file storage directory
    rootPath = jsonConnection('json:con').path

    // Save file processing history to table Events_History
    story = h2Table('db:events_history')
    // Create history table if not exist
    createStory = true
}

// Processing events files
fileProcessing(files('#events')) {
    // Remove processed file
    removeFiles = true
    // Remove empty directory after processing files
    removeEmptyDirs = true

    // Set search mask to process files
    useSourcePath {
        mask = '{date}/events_*_{num}.json'
        // Defina mask variables
        variable('date') { type = dateFieldType; format = 'yyyy-MM-dd' }
        variable('num') { type = integerFieldType; length = 3 }
    }

    // Count thread processing
    countOfThreadProcessing = 4
    // Group file processing in threads by date
    threadGroupColumns = ['date']
    // Process files in a group sorted by number
    order = ['num']

    processFile { inf ->
        logFine "Parse file ${inf.attr.filepath}/${inf.attr.filename} ..."
        json('json:events') {
            // Set file path for json connection
            currentJSONConnection.path = inf.file.parent
            // Set file name for json file
            fileName = inf.file.name
        }
        // Copy rows from json file to events table
        copyRows(json('json:events'), h2Table('db:events'))
        // Confirm success of file processing operation
        inf.result = inf.completeResult

        logInfo "Parsed file ${inf.attr.filepath}/${inf.attr.filename}, ${h2Table('db:events').updateRows} rows loaded"
    }
}

unregisterDataset('#*')

logInfo "Loading event files completed successfully."