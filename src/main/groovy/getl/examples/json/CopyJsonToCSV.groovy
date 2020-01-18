/**
 * Read data from a JSON file and write it into two csv temporary files as master-detail.
 */
package getl.examples.json

import getl.lang.Getl
import groovy.transform.BaseScript

@BaseScript Getl main

// Define json file
json('json:customers', true) {
    rootNode = 'customers'

    field('id') { type = integerFieldType }
    field('name')
    field('phones') { type = objectFieldType }


    // Write json text to temporary file
    fileName = textFile { file ->
        // This file will be storage in temp directory and have randomize file name.
        temporaryFile = true
        // Append text to buffer
        write '''
{
    "customers": [
        {
            "id":1,
            "name":"Customer 1",
            "phones": [
                { "phone": "+7 (001) 100-00-01" },
                { "phone": "+7 (001) 100-00-02" },
                { "phone": "+7 (001) 100-00-03" }
            ]
        },
        {
            "id":2,
            "name":"Customer 2",
            "phones": [
                { "phone": "+7 (001) 200-00-01" },
                { "phone": "+7 (001) 200-00-02" },
                { "phone": "+7 (001) 200-00-03" }
            ]
        },
        {
            "id":3,
            "name":"Customer 3",
            "phones": [
                { "phone": "+7 (001) 300-00-01" },
                { "phone": "+7 (001) 300-00-02" },
                { "phone": "+7 (001) 300-00-03" }
            ]
        }
    ]
}
'''
    }
}

// Define csv temporary file for customers data
csvTempWithDataset('#customers', json('json:customers')) {
    // Used the json fields minus the array phones
    removeField'phones'
}

// Define csv temporary file for customers phones data
csvTemp('#phones', true) {
    // Adding the customer identification field and him phone
    field('customer_id') { type = integerFieldType }
    field('phone')
}

// Generate writer the phones customers to temporary file
copyRows(json('json:customers'), csvTemp('#customers')) {
    // Adding an write to the child table customers_phones
    childs('phones', csvTemp('#phones')) {
        // Processing the child structure phones
        writeRow { addPhone, row ->
            // Copying phones array to the writer in h2 table phones customers
            (row.phones as List<Map>)?.each { phone ->
                addPhone customer_id: row.id, phone: phone.phone
            }
        }

    }

    copyRow()

    logInfo "${destination.updateRows} customers loaded"
    logInfo "${childs('phones').updateRows} customer phones loaded"
}

println 'Customers:'
rowsProcess(csvTemp('#customers')) {
    readRow { println it }
}

println 'Customers phones:'
rowsProcess(csvTemp('#phones')) {
    readRow { println it }
}

unregisterDataset'#*'
