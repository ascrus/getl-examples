package getl.examples.init

import getl.lang.Getl
import groovy.transform.BaseScript

@BaseScript Getl main

// Set default work with objects of group "db"
forGroup 'db'

// Set default repository connection "db:con" for h2 objects in the script
useH2Connection h2Connection('con') {
    // Work under "dba" as administrator
    useLogin 'dba'

    // Define a system table of H2 database users
    def users = h2Table {
        schemaName = 'INFORMATION_SCHEMA'
        tableName = 'USERS'
    }

    // Run the script to create an object in the database
    sql {
        runFile 'resource:/create_db_objects.sql'
    }
}

// Define the structure and create table "Price"
h2Table('prices') {
    field('id') { type = integerFieldType; isKey = true }
    field('name') { type = stringFieldType; isNull = false; length = 50 }
    field('create_date') { type = datetimeFieldType; isNull = false }
    field('price') { type = numericFieldType; isNull = false; length = 9; precision = 2 }
    field('is_active') { type = booleanFieldType; isNull = false }
    field('description') { type = textFieldType }
    create()
}

// Define the structure and create table "Customer"
h2Table('customers') {
    field('id') { type = integerFieldType; isKey = true }
    field('name') { length = 50 }
    field('customer_type') { length = 10 }
    create()
}

// Define the structure and create table "Customer_Phones"
h2Table('customers.phones') {
    tableName = 'customer_phones'
    field('customer_id') { type = integerFieldType; isKey = true }
    field('phone') { length = 50; isKey = true }
    create()
}

// Define the structure and create table "Sales"
h2Table('sales') {
    field('id') { type = bigintFieldType; isKey = true }
    field('price_id') { type = integerFieldType; isNull = false }
    field('customer_id') { type = integerFieldType; isNull = false }
    field('sale_date') { type = datetimeFieldType; isNull = false }
    field('sale_count') { type = bigintFieldType; isNull = false }
    field('sale_sum') { type = numericFieldType; isNull = false; length = 12; precision = 2 }
    field('description') { type = stringFieldType; length = 50 }
    create()
}

logInfo 'All database objects are successfully created!'