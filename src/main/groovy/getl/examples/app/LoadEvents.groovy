package getl.examples.app

import getl.examples.launcher.ExampleRun
import getl.examples.process.events.GenerateEventFiles

/** Generate events in Json files and load these files into the database event table */
class LoadEvents extends ExampleRun{
    static void main(def args) {
        Application(this, args)
    }

    @Override
    Object run() {
        // Call the Json file generation script
        callScript GenerateEventFiles

        // Call the script to upload Json files to the database table
        callScript LoadEvents
    }
}