/**
 * Copy files from directories to other location
 */

package getl.examples.files

import getl.lang.Getl
import getl.utils.FileUtils
import groovy.transform.BaseScript

@BaseScript Getl main

def resFile = FileUtils.FileFromResources('/files/file.txt')

// Register history table in repository
def history = embeddedTable('#history', true) { tableName = 'download_history' }

files {
    // Set root path
    rootPath = systemTempPath

    // Set local directory to OS temporary directory
    localDirectory = systemTempPath

    createDir 'files'
    changeDirectory 'files'
    (1..3).each { dirNum ->
        createDir "dir.$dirNum"
        changeDirectory "dir.$dirNum"
        (1..5).each { fileNum ->
            upload(resFile.name)
            rename(resFile.name, "file.${fileNum}.txt")
        }
        changeDirectoryUp()
    }

    rootPath = "${systemTempPath}/files"

    // Build list of files with parent directory
    buildListFiles('{dir}/{file}') {
        recursive = true // analyze subdirectories
        historyTable = history // use history table
        createHistoryTable = true // create history table
    }

    // Create local subdirectory
    createLocalDir'process.files'
    // Cd to local subdirectiry
    changeLocalDirectory'process.files'

    // Download files to local subdirectory
    downloadListFiles {
        historyTable = history // use history table
        saveDirectoryStructure = true // create analog subdirectories structure
        filterFiles = "Upper(FILENAME) LIKE '%.TXT'" // download only text files
        orderFiles = ['FILEPATH', 'FILENAME'] // download as sorted by path and file name
        downloadFile { logInfo "Download file ${it.filepath}/${it.filename}"} // print downloaded file name to log
    }

    // Repeat build list of files with parent directory (validation fixing files to history table)
    buildListFiles('{dir}/{file}') {
        recursive = true // analyze subdirectories
        historyTable = history // use history table
    }

    // The file list must be empty because they are in history
    testCase {
        assertTrue(fileList.rows().isEmpty())
    }

    // Change current local directory to root
    changeLocalDirectoryToRoot()

    // Remove process temporary files and directory
    FileUtils.DeleteFolder "${currentLocalDir()}/process.files"
}

logInfo 'History rows: '
rowProcess(history) {
    readRow { logInfo it }
}

unregisterDataset '#*'