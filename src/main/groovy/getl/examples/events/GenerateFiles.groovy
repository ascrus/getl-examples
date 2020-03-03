package getl.examples.events

import getl.examples.repository.Db
import getl.examples.repository.Json
import getl.lang.Getl
import getl.utils.DateUtils
import getl.utils.FileUtils
import getl.utils.GenerationUtils
import getl.utils.StringUtils
import groovy.json.JsonGenerator
import groovy.json.StreamingJsonBuilder
import groovy.transform.BaseScript
import groovy.transform.Field

@BaseScript Getl main

// Script parameters
@Field countGenerateDays = 10
@Field countGenerateFilesinDay = 10
@Field countGenerateEventsInFile = 100
@Field countThreads = 3

// Check script parameters
void check() {
    assert (countGenerateDays?:0) > 0
    assert (countGenerateFilesinDay?:0) > 0
    assert (countGenerateEventsInFile?:0) > 0
    assert (countThreads?:0) > 0
}

// Define database objects
runGroovyClass Db, true
// Using writer login
h2Connection('db:con') { useLogin 'writer' }

// Get last generated date
def lastDate = historypoint('db:points').lastValue('events').value as Date
if (lastDate == null) lastDate = DateUtils.ParseDate('yyyy-MM-dd', '2009-12-31')
logInfo "Generate events after ${DateUtils.FormatDate(lastDate)} ..."

// Define json objects
runGroovyClass Json, true
// Filter by group "json"
forGroup 'json'

// Generate randomize filling code
def genEvents = GenerationUtils.GenerateRandomRow(json('events'), ['id', 'event_time'],
        ['_abs_': true, event_type: [list: ['start_session', 'change_session', 'finish_session', 'kill_session']],
         counter_id: [minValue: 1, maxValue: 99]])

// Generate files with threads mode
thread {
    // Offset days
    useList (1..countGenerateDays)
    // Count threads
    countProc = countThreads
    // Json path
    def jsonPath = jsonConnection('con').path
    run { Integer offsDay ->
        // Generate date
        def genDate = DateUtils.AddDate('dd', offsDay, lastDate)
        // Formated generate date
        def genStr = DateUtils.FormatDate('yyyy-MM-dd', genDate)

        profile("Generate on $genStr date") {
            // Path for generate file
            def genPath = jsonConnection('con').path + '/' + genStr
            FileUtils.ValidPath(genPath)

            // Set path on generated date
            jsonConnection('con').path = "$jsonPath/$genStr"

            // Generate events files
            (1..countGenerateFilesinDay).each { num ->
                json('events') {
                    // Create Json writer
                    fileName = "events_${genStr}_${StringUtils.AddLedZeroStr(num, 3)}"
                    def writer = new File(fullFileName()).newWriter(codePage)
                    def builder = new StreamingJsonBuilder(writer,
                            new JsonGenerator.Options().timezone(TimeZone.default.ID).dateFormat('yyyy-MM-dd HH:mm:ss').build())

                    // Generate rows
                    def rows = [] as List<Map>
                    (1..countGenerateEventsInFile).each {
                        // Create row and setting id and event time
                        def row = [:]
                        row.id = StringUtils.RandomStr()
                        row.event_time = GenerationUtils.GenerateDateTime(genDate, 86399)

                        // Generate randon values to row
                        genEvents(row)

                        // Added rows to list
                        rows << row
                    }

                    // Save list to file
                    builder(rows)
                    writer.close()
                    logInfo "Json ${fullFileName()} generated"
                }

                historypoint('db:points').saveValue('events', genDate)
            }
        }
    }
}

def newDate = historypoint('db:points').lastValue('events').value as Date
logInfo "Events generated successfully on ${DateUtils.FormatDate(newDate)}."
