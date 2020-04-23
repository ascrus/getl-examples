package getl.examples.files

import getl.examples.files.EventsStorage
import getl.examples.launcher.ExampleRun
import getl.examples.launcher.ExampleTest
import getl.utils.FileUtils
import org.junit.Test

class EventsStorageTest extends ExampleTest {
    @Test
    void testFileManager() {
        ExampleRun.Dsl {
            // Call the script for registering objects in the repository
            callScripts EventsStorage

            // Checking registration in the repository
            assertNotNull(findFilemanager('events'))

            files('events') {
                // Checking for a directory
                assertTrue(FileUtils.ExistsFile(rootPath, true))

                // Checking upload access
                textFile {
                    temporaryFile = true
                    fileName = "$localDirectory/test.txt"
                    write 'test'
                }
                upload('test.txt')
                assertTrue(existsFile('test.txt'))

                // Checking download access
                removeLocalFile('test.txt')
                download('test.txt')
                assertEquals('test', new File("$localDirectory/test.txt").text)

                // Check write permission
                removeFile('test.txt')
                assertFalse(existsFile('test.txt'))
            }
        }
    }
}