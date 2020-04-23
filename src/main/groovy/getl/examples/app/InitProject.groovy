package getl.examples.app

import getl.examples.launcher.ExampleRun
import getl.examples.process.events.GenerateEventFiles
import getl.examples.process.events.LoadEventFiles
import getl.examples.process.init.CreateDBObjects
import getl.examples.process.init.GenerateDataToTables
import getl.examples.process.sales.GenerateSales

/**
 * Expand the database for the project with examples and fill the tables with data
 */
class InitProject extends ExampleRun {
    static void main(def args) {
        Application(this, args)
    }

    @Override
    Object run() {
        // Call the script to create the H2 database and its tables
        callScript CreateDBObjects

        // Call the script to fill the database tables with data
        callScript GenerateDataToTables

        // Call the script to write to the sales database table with the generated data
        callScript GenerateSales

        // Call the Json file generation script
        callScript GenerateEventFiles

        // Call the script to upload Json files to the database table
        callScript LoadEventFiles
    }
}