package getl.examples.process.events

import getl.examples.data.Db
import getl.examples.data.Json
import getl.examples.files.EventsStorage
import getl.examples.launcher.ExampleRun
import groovy.transform.BaseScript

//noinspection GroovyUnusedAssignment
@BaseScript ExampleRun main

// Define repository objects
callScripts Db, Json, EventsStorage

// Using writer login
h2Connection('db:con') { useLogin 'writer' }

files('events') {
    // Save file processing history to table Events_History
    story = h2Table('db:events_history')
    // Create history table if not exist
    createStory = true
}

// Processing events files
fileProcessing(files('events')) {
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

logInfo "Loading event files completed successfully."

// Runs after the script finishes.
void done() {
    // Turn off history
    files('events') { story = null }
}