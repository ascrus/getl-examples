package getl.examples.env

import getl.lang.Getl
import getl.utils.FileUtils

import java.util.logging.Level

class InitProcess {
    static final def WorkPath = FileUtils.SystemTempDir() + '/getl_examples'

    static void InitOptions(Getl getl) {
        getl.with {
            options {
                processTimeLevelLog = Level.FINER
                processTimeDebug = false
                processTimeTracing = true
                sqlEchoLogLevel = Level.INFO
            }

            logging {
                logFileName = "${InitProcess.WorkPath}/getl_examples.{date}.log"
            }
        }
    }
}