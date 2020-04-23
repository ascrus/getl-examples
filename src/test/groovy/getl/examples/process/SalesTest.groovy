package getl.examples.process

import getl.examples.data.Db
import getl.examples.launcher.ExampleRun
import getl.examples.launcher.ExampleTest
import getl.examples.process.sales.GenerateSales
import org.junit.Test

class SalesTest extends ExampleTest {
    @Test
    void testGenerateSales() {
        ExampleRun.Dsl {
            // Call the script for registering objects in the repository
            callScripts Db

            // Set login for connection
            h2Connection('db:con') { useLogin 'writer' }

            // Clear table "sales"
            h2Table('db:sales') {
                truncate()
                assertEquals(0, countRow())
            }

            // Calling the script for generating and writing sales to the table
            callScript GenerateSales, [count_generate_rows: 1000]

            // Check the number of records
            h2Table('db:sales') {
                assertEquals(1000, countRow())
            }

            // Calling again the script for generating and writing sales to the table
            callScript GenerateSales, [count_generate_rows: 1000]

            // Check the number of records
            h2Table('db:sales') {
                assertEquals(2000, countRow())
            }
        }
    }
}