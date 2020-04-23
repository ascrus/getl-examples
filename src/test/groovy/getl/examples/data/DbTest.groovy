package getl.examples.data

import getl.examples.app.InitProject
import getl.examples.launcher.ExampleRun
import getl.examples.launcher.ExampleTest
import org.junit.Test

class DbTest extends ExampleTest {
    @Test
    void testConnection() {
        ExampleRun.Dsl {
            // Call the script for registering database objects in the repository
            callScripts Db

            // Test logins
            assertNotNull(findConnection('db:con'))
            h2Connection('db:con') {
                useLogin 'dba'
                connect()

                useLogin 'reader'
                connect()

                useLogin 'writer'
                connect()

                shouldFail {
                    useLogin 'unknown'
                    connect()
                }
            }
        }
    }

    @Test
    void testTables() {
        ExampleRun.Dsl {
            // Reinit project
            callScript InitProject

            // Check tables in the database schema
            h2Connection('db:con') {
                useLogin 'reader'
                def requiredTables = ['prices', 'customers', 'customer_phones', 'sales', 'events'].sort()
                def tablesList = retrieveDatasets(schemaName: 'demo').collect { table -> table.tableName.toLowerCase() }
                tablesList = tablesList.findAll { table -> table in requiredTables }
                assertEquals(requiredTables, tablesList)
            }

            // Check repository tables
            processJdbcTables('db:*') { name ->
                h2Table(name) {
                    assertTrue(exists)
                    retrieveFields()
                    assertTrue(field.size() > 0)
                    assertTrue(countRow() > 0)
                }
            }
        }
    }
}