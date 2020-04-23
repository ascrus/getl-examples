package getl.examples.launcher.sub

import getl.examples.launcher.ExampleRun
import java.util.logging.Level

/**
 * Getl application parameter initialization class
 */
class Init extends ExampleRun {
    @Override
    Object run() {
        options {
            processTimeLevelLog = Level.FINER
            processTimeDebug = false
            processTimeTracing = true
            sqlEchoLogLevel = Level.INFO
        }

        logging {
            // To set the log file name, use the global variable "WorkPath" from the launcher
            logFileName = "$WorkPath/getl_examples.{date}.log"
        }
    }
}