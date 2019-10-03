/**
 * Generate customers and phones data as XML file
 * <br>P.S. This is script used from h2 installation script.
 */

package getl.examples.xml

import getl.lang.Getl
import groovy.transform.BaseScript

@BaseScript Getl main

configContent.countCustomers = 3
configContent.countCustomerPhones = 7

// Write text to temporary XML file
def xmlFileName = textFile {
    // This file will be storage in temp directory and have randomize file name.
    temporaryFile = true
    // Append text to buffer
    write '''<?xml version="1.0" encoding="UTF-8"?>
<data>
    <header version="1.00" object="Customers">
        <sender time="2019-01-02T01:02:03+03:00"/>
    </header>
    
    <customers>
        <customer customer_type="wholesale">
            <id>1</id>
            <name>Customer 1</name>
            <phones>
                <phone>+7 (001) 100-00-01</phone>
                <phone>+7 (001) 100-00-02</phone>
                <phone>+7 (001) 100-00-03</phone>
            </phones>
        </customer>
        <customer customer_type="retail">
            <id>2</id>
            <name>Customer 2</name>
            <phones>
                <phone>+7 (111) 111-00-11</phone>
                <phone>+7 (111) 111-00-12</phone>
            </phones>
        </customer>
        <customer customer_type="retail">
            <id>3</id>
            <name>Customer 3</name>
            <phones>
                <phone>+7 (222) 222-00-11</phone>
                <phone>+7 (222) 222-00-12</phone>
            </phones>
        </customer>
    </customers>
</data>
'''
}

// Define xml file
xml('xml:customers', true) { xml ->
        fileName = xmlFileName

    rootNode = 'customers.customer'
    defaultAccessMethod = DEFAULT_NODE_ACCESS // Fields values are stored as node value

    attributeField('version') { alias = 'header.@version[0]' }
    attributeField('objecttype') { alias = 'header.@object[0]' }
    attributeField('time') { alias = 'header.sender.@time[0]' }

    field('id') { type = integerFieldType }
    field('name')
    field('customer_type') { alias = '@'} // Customer value are stored as attribute value
    field('phones') { type = objectFieldType } // Phones are stored as array list values and will be manual parsing

    readOpts {
        readAttributes {
            testCase {
                assertEquals('1.00', attributeValue.version)
                assertEquals('Customers', attributeValue.objecttype)
                assertEquals('2019-01-02T01:02:03+03:00', attributeValue.time)
            }
            return true
        }
    }
}