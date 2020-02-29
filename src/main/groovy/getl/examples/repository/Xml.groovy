package getl.examples.repository

import getl.lang.Getl
import getl.utils.FileUtils
import groovy.transform.BaseScript

@BaseScript Getl main

// Register Xml file "Customers" in the repository
xml('xml:customers', true) { xml ->
    fileName = FileUtils.ResourceFileName('resource:/customers.xml')

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
                assertEquals('1.00', xml.attributeValue.version)
                assertEquals('Customers', xml.attributeValue.objecttype)
                assertEquals('2019-01-02T01:02:03+03:00', xml.attributeValue.time)
            }
            return true
        }
    }
}