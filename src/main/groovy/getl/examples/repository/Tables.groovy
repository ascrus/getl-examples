package getl.examples.repository

import getl.lang.Getl
import getl.utils.FileUtils
import groovy.transform.BaseScript

@BaseScript Getl main

configuration {
    // Load the list of logins to the database from the configuration file in the resources
    load 'resource:/logins.db.conf'
}

// Register a connection to H2 database in the repository and set it by default for H2 objects of this script
useH2Connection h2Connection('db:con', true) {
    // Specify to place the database file in the pace of the OS directory (if it is not there, it will be created)
    connectDatabase = FileUtils.SystemTempDir() + '/getl_examples'

    // Specify the configuration section in which logins to the database are stored
    loginsConfigStore = 'db_logins'

    // Set default schema for tables
    schemaName = 'demo'
}

// Set default work with objects of group "db"
forGroup 'db'

// Register Price table in the repository
h2Table('prices', true) {
    tableName = 'prices'
    field('id') { type = integerFieldType; isKey = true }
    field('name') { type = stringFieldType; isNull = false; length = 50 }
    field('create_date') { type = datetimeFieldType; isNull = false }
    field('price') { type = numericFieldType; isNull = false; length = 9; precision = 2 }
    field('is_active') { type = booleanFieldType; isNull = false }
    field('description') { type = textFieldType }
}

// Register Customer table in the repository
h2Table('customers', true) {
    tableName = 'customers'
    field('id') { type = integerFieldType; isKey = true }
    field('name') { length = 50 }
    field('customer_type') { length = 10 }
}

// Register Customer_Phones table in the repository
h2Table('customers.phones', true) {
    tableName = 'customer_phones'
    field('customer_id') { type = integerFieldType; isKey = true }
    field('phone') { length = 50; isKey = true }
}

// Register Sales table in the repository
h2Table('sales', true) {
    tableName = 'sales'
    field('id') { type = bigintFieldType; isKey = true }
    field('price_id') { type = integerFieldType; isNull = false }
    field('customer_id') { type = integerFieldType; isNull = false }
    field('sale_date') { type = datetimeFieldType; isNull = false }
    field('sale_count') { type = bigintFieldType; isNull = false }
    field('sale_sum') { type = numericFieldType; isNull = false; length = 12; precision = 2 }
    field('description') { type = stringFieldType; length = 50 }
}
