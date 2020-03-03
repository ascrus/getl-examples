package getl.examples.init

import getl.examples.repository.Db
import getl.examples.repository.Xml
import getl.lang.Getl
import getl.utils.DateUtils
import getl.utils.GenerationUtils
import groovy.transform.BaseScript

@BaseScript Getl main

// Count customers
final def countCustomers = 3
// Count of customer phones
final def countCustomerPhones = 7

// Load repository database objects
runGroovyClass Db, true

// Set default work with objects of group "db"
forGroup 'db'

// Set default repository connection "db:con" for h2 objects in the script
useH2Connection h2Connection('con') {
    // Work under "writer" as user
    useLogin 'writer'
}

// Work with table "Price"
h2Table('prices') {
    // Generate and write data to table "Price"
    rowsTo {
        // Code for inserting records into a table
        writeRow { add -> // Write descriptor
            add id: 1, name: 'Apple', create_date: DateUtils.now, price: 60.50, is_active: true, description: 'Not a macintosh.\nThis is fruit.'
            add id: 2, name: 'Pear', create_date: DateUtils.now, price: 90.00, is_active: true, description: null
            add id: 3, name: 'Plum', create_date: DateUtils.now, price: 110.00, is_active: true, description: 'Not a Green Plum.\nThis is fruit.'
            add id: 4, name: 'Cherries', create_date: DateUtils.now, price: 150.10, is_active: true, description: 'Not a china machine.\nThis is fruit.'
            add id: 5, name: 'Melon', create_date: DateUtils.now, price: 30.00, is_active: true, description: null
            add id: 6, name: 'Blackberry', create_date: DateUtils.now, price: 70.90, is_active: true, description: 'Not a phone.\nThis is fruit.'
            add id: 7, name: 'Blueberries', create_date: DateUtils.now, price: 85.00, is_active: true, description: null
        }
    }

    // Calling Unit Tests
    testCase {
        assertEquals(7, updateRows)
    }

    logInfo "$updateRows rows are inserted in table \"Price\""
}

// Load repository Xml objects
runGroovyClass Xml, true

// Copy customer data from the Xml resource file "customers.xml" to tables "Customers" and "Customers_Phones"
copyRows(xml('xml:customers'), h2Table('customers')) {
    // Adding an write to the child table "Customers_Phones"
    childs('phones', h2Table('customers.phones')) {
        // Processing the child structure phones
        writeRow { addPhone, row ->
            // Copying phones array to the writer in h2 table phones customers
            row.phones?.each { phone ->
                addPhone customer_id: row.id, phone: phone?.text()
            }
        }
    }

    // Copy data
    copyRow()

    // Calling Unit Tests
    testCase {
        assertEquals(countCustomers, destination.updateRows)
        assertEquals(countCustomerPhones, childs('phones').updateRows)
    }

    logInfo "${destination.updateRows} rows are inserted in table \"Customers\""
    logInfo "${childs('phones').updateRows} rows are inserted in table \"Customers_Phones\""
}

logInfo 'All database objects are successfully filled with data!'
