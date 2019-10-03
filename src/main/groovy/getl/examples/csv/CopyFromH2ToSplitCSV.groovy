/**
 *  Write data divided into portions of files and then read such files as a single data set
 */

package getl.examples.csv

import getl.lang.Getl
import groovy.transform.BaseScript

@BaseScript Getl main

// Generate sample data in a H2  database
runGroovyClass getl.examples.h2.H2Init, true

def salesTable = embeddedTable('samples:sales')
def countSales = configContent.countSales as Integer

// Create csv temporary dataset based on sales
def salesFile = csvTempWithDataset('#sales', salesTable) {
    // File reading options
    readOpts {
        isSplit = true // Reads a chunked file
        isValid = false // Check constraints when reading a file
    }

    // File writing options
    writeOpts {
        def cur = 0
        def div = (countSales / 4).intValue()
        splitFile { cur++; cur % div == 0 } // The definition that the data record should be moved to the next file
    }
}

// Copy sample data from table to file
copyRows(salesTable, salesFile)
logInfo "${salesFile.writeRows} write to $salesFile with ${salesFile.countWritePortions} partition (${salesFile.countWriteCharacters} characters)"

// Get a list of recorded files by mask
def files = csvTempConnection().listFiles {
    // Use file mask type
    type = fileType
    // Use sort by name
    sort = nameSort
    // Use file name mask plus chunk number
    mask = "${salesFile.fileName}[.][0-9]+"
}
testCase {
    assertEquals(salesFile.countWritePortions, files.size())
}
logInfo "Detected ${files.collect { File f -> f.name + '(' + f.size() + ' bytes)' }} files"

// Read split data files
rowProcess(salesFile) {
    readRow { row ->
        testCase {
            assertTrue(row.price_id in (1..7))
        }
    }
    testCase {
        assertEquals(salesFile.countWritePortions, salesFile.countReadPortions)
    }
    logInfo "$countRow read from $salesFile with ${salesFile.countReadPortions} partition"
}

unregisterDataset '#*'