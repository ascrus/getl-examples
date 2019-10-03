/**
 * Create H2 embedded tables and load random generated data to tables
 */
package getl.examples.h2

import getl.lang.Getl
import getl.utils.DateUtils
import getl.utils.GenerationUtils
import groovy.transform.BaseScript

@BaseScript Getl main

// Define H2 tables
runGroovyClass getl.examples.h2.Tables, true

// Count sale rows
def count_sale_rows = configContent.countSales

// Use a filter when processing a list of objects
forGroup 'samples'

// Create tables
profile("Create h2 tables") {
    processDatasets { name ->
        embeddedTable(name) {
            create()
            logInfo "Created sample table $it"
        }
    }
}

// Price table
embeddedTable('prices') {
    rowsTo {
        // User code
        writeRow { add -> // writer object
            add id: 1, name: 'Apple', create_date: DateUtils.now, price: 60.50, description: 'Not a macintosh.\nThis is fruit.'
            add id: 2, name: 'Pear', create_date: DateUtils.now, price: 90.00, description: null
            add id: 3, name: 'Plum', create_date: DateUtils.now, price: 110.00, description: 'Not a Green Plum.\nThis is fruit.'
            add id: 4, name: 'Cherries', create_date: DateUtils.now, price: 150.10, description: 'Not a china machine.\nThis is fruit.'
            add id: 5, name: 'Melon', create_date: DateUtils.now, price: 30.00, description: null
            add id: 6, name: 'Blackberry', create_date: DateUtils.now, price: 70.90, description: 'Not a phone.\nThis is fruit.'
            add id: 7, name: 'Blueberries', create_date: DateUtils.now, price: 85.00, description: null
        }

        testCase {
            assertEquals(7, countRow)
        }
    }

    logInfo "$updateRows price writen"
}

// Load customers data from generated XML file
runGroovyClass getl.examples.xml.XMLCustomers, true

def countCustomers = configContent.countCustomers
def countCustomerPhones = configContent.countCustomerPhones

// Copy customers rows from xml file to h2 tables customers and customers_phones
copyRows(xml('xml:customers'), embeddedTable('customers')) {
    // Adding an write to the child table customers_phones
    childs('phones', embeddedTable('customers.phones')) {
        // Processing the child structure phones
        writeRow { addPhone, row ->
            // Copying phones array to the writer in h2 table phones customers
            row.phones?.each { phone ->
                addPhone customer_id: row.id, phone: phone?.text()
            }
        }
    }

    copyRow()

    testCase {
        assertEquals(countCustomers, destination.updateRows)
        assertEquals(countCustomerPhones, childs('phones').updateRows)
    }

    logInfo "${destination.updateRows} customers writen"
    logInfo "${childs('phones').updateRows} customer phones writen"
}

// Sales table
embeddedTable('sales') {
    rowsTo {
        // Lookup price map structure
        def priceLookup = embeddedTable('prices').lookup { key = 'id'; strategy = ORDER_STRATEGY }

        // Size of batch saving rows
        destParams.batchSize = 10000

        // User code
        writeRow { add -> // writer object
            def num = 0
            (1..count_sale_rows).each { id ->
                num++
                def price = GenerationUtils.GenerateInt(1, 7)
                def customer = GenerationUtils.GenerateInt(1, 3)
                def sale_date = GenerationUtils.GenerateDate(90)
                def count = GenerationUtils.GenerateInt(1, 99)
                def priceRow = priceLookup.get(price) as Map
                def sum = priceRow.price * count
                def desc = GenerationUtils.GenerateString(50)

                // Append row to destination
                add id: num, price_id: price, customer_id: customer, sale_date: sale_date, sale_count: count, sale_sum: sum,
                        description: desc
            }
        }
    }

    logInfo "$updateRows sales writen"
}