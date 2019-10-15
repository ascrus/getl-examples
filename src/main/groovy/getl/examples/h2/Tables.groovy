/**
 * Define H2 embedded tables
 */
package getl.examples.h2

import getl.lang.Getl
import groovy.transform.BaseScript

@BaseScript Getl main

// Load configuration file
runGroovyClass getl.examples.utils.Config, true

forGroup 'samples'

// Price table
embeddedTable('prices', true) {
    tableName = 'prices'
    field('id') { type = integerFieldType; isKey = true }
    field('name') { type = stringFieldType; isNull = false; length = 50 }
    field('create_date') { type = datetimeFieldType; isNull = false }
    field('price') { type = numericFieldType; isNull = false; length = 9; precision = 2 }
    field('is_active') { type = booleanFieldType; isNull = false }
    field('description') { type = textFieldType }
}

// Customers table
embeddedTable('customers', true) {
    tableName = 'customers'
    field('id') { type = integerFieldType; isKey = true }
    field('name') { length = 50 }
    field('customer_type') { length = 10 }
}

// Customer_Phones table
embeddedTable('customers.phones', true) {
    tableName = 'customer_phones'
    field('customer_id') { type = integerFieldType; isKey = true }
    field('phone') { length = 50; isKey = true }
}

// Sales table
embeddedTable('sales', true) {
    tableName = 'sales'
    field('id') { type = bigintFieldType; isKey = true }
    field('price_id') { type = integerFieldType; isNull = false }
    field('customer_id') { type = integerFieldType; isNull = false }
    field('sale_date') { type = datetimeFieldType; isNull = false }
    field('sale_count') { type = bigintFieldType; isNull = false }
    field('sale_sum') { type = numericFieldType; isNull = false; length = 12; precision = 2 }
    field('description') { type = stringFieldType; length = 50 }
}
