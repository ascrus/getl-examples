package getl.examples.init

import getl.examples.env.InitProcess
import getl.examples.repository.Db
import getl.lang.Getl
import getl.utils.FileUtils

import java.util.logging.Level

class InitRun extends Getl {
    static void main(def args) {
        Application(this, args)
    }

    @Override
    Object run() {
        // Init options
        InitProcess.InitOptions(this)

        // Creating a structure in the database
        runGroovyClass CreateDBObjects

        // Generate data for database tables
        runGroovyClass GenerateData
    }
}