package getl.examples.init

import getl.examples.repository.Tables
import getl.lang.Getl
import getl.utils.FileUtils

import java.util.logging.Level

class InitRun extends Getl {
    static void main(def args) {
        Application(this, args)
    }

    @Override
    Object run() {
        Getl.Dsl {
            options {
                processTimeLevelLog = Level.FINER
                processTimeDebug = false
                processTimeTracing = true
                sqlEchoLogLevel = Level.INFO
            }

            logging {
                logFileName = "${FileUtils.SystemTempDir()}/getl_examples.{date}.log"
            }

            // Load repository database objects
            runGroovyClass Tables, true

            // Creating a structure in the database
            runGroovyClass CreateDBObjects

            // Generate data for database tables
            runGroovyClass GenerateData
        }
    }
}