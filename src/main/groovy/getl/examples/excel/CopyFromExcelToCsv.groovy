/**
 * Copy Excel file to temporary CSV file
 */
package getl.examples.excel

import getl.data.Field
import getl.lang.Getl
import getl.tfs.TFS
import getl.utils.FileUtils
import groovy.transform.BaseScript

@BaseScript Getl main

def resfileName = '/excel/data.xlsx'

// Copy excel prototype file to tempoprary directory
def excelDestName = FileUtils.FileFromResources(resfileName).path

excel {
    useConnection excelConnection {
        fileName = excelDestName
        listName = 'list1'
        header = true
    }

    copyRows(it, csvTempWithDataset('#excel.data', it))

    assert field('a').type == Field.numericFieldType
    assert field('b').type == Field.numericFieldType
    assert field('c').type == Field.stringFieldType
}

rowsProcess(csvTemp('#excel.data')) {
    readRow { logInfo(it) }
    logInfo "$countRow rows from excel read"
}

unregisterDataset '#*'