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

// Copy excel prototype file to tempoprary directory
def excelPrototypeName = FileUtils.FileFromResources('excel/data.xlsx', 'src/main/resources').path
def excelDestName = "${TFS.systemPath}/data.xlsx"
FileUtils.CopyToFile(excelPrototypeName, excelDestName, true)
new File(excelDestName).deleteOnExit()

excel { dataset ->
    connection = excelConnection {
        fileName = excelDestName
        listName = 'list1'
        header = true
    }

    copyRows(dataset, csvTempWithDataset('demo', dataset))

    assert field('a').type == Field.numericFieldType
    assert field('b').type == Field.numericFieldType
    assert field('c').type == Field.stringFieldType
}

rowProcess(csvTemp('demo')) {
    process { logInfo(it) }
}