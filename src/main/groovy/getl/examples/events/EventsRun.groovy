package getl.examples.events

import getl.examples.env.InitProcess
import getl.lang.Getl

class EventsRun extends Getl {
    static void main(def args) {
        Application(this, args)
    }

    @Override
    Object run() {
        // Init options
        InitProcess.InitOptions(this)

        // Generate events Json files
        runGroovyClass GenerateFiles

        // Load generated files to Events table
        runGroovyClass ProcessingFiles
    }
}