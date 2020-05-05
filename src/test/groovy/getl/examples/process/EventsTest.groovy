package getl.examples.process

import getl.examples.data.Db
import getl.examples.files.EventsStorage
import getl.examples.launcher.ExampleRun
import getl.examples.launcher.ExampleTest
import getl.examples.process.events.GenerateEventFiles
import getl.examples.process.events.LoadEventFiles
import org.junit.Test

class EventsTest extends ExampleTest {
    @Test
    void testFillEvents() {
        ExampleRun.Dsl {
            // Call the script for registering objects in the repository
            callScripts Db, EventsStorage

            // Set login for connection
            h2Connection('db:con') { useLogin 'writer' }

            // Clear table "events"
            h2Table('db:events') {
                truncate()
                assertEquals(0, countRow())
            }

            // Clear table "events"
            h2Table('db:events_history') {
                truncate()
                assertEquals(0, countRow())
            }

            // Clear history point
            historypoint('db:points') { clearValue('events') }

            // Clear events storage
            fileCleaner(files('events')) {
                useSourcePath { mask = '{date}/*' }
                removeFiles = true
                removeEmptyDirs = true
            }

            // Check that the events storage is empty
            files('events') {
                assertEquals(0, list().size())
            }

            // Call script for generating event Json files
            callScript GenerateEventFiles, [countGenerateDays: 3, countGenerateFilesinDay: 3]

            // Verify that 9 json files are generated
            files('events') {
                buildListFiles {
                    useMaskPath {
                        mask = '{date}/events_*_{num}.json'
                        // Defina mask variables
                        variable('date') { type = dateFieldType; format = 'yyyy-MM-dd' }
                        variable('num') { type = integerFieldType; length = 3 }
                    }
                    recursive = true
                }
                assertEquals(9, fileList.countRow())
            }

            // Call the script again
            callScript GenerateEventFiles, [ countGenerateDays: 3, countGenerateFilesinDay: 3]


            // Verify that another 9 files are generated
            files('events') {
                buildListFiles {
                    useMaskPath {
                        mask = '{date}/events_*_{num}.json'
                        // Defina mask variables
                        variable('date') { type = dateFieldType; format = 'yyyy-MM-dd' }
                        variable('num') { type = integerFieldType; length = 3 }
                    }
                    recursive = true
                }
                assertEquals(18, fileList.countRow())
            }

            // Call load events
            callScript LoadEventFiles
            assertEquals(1800, h2Table('db:events').countRow())
        }
    }
}