package getl.examples.init

import getl.examples.repository.Db
import getl.lang.Getl
import groovy.transform.BaseScript

@BaseScript Getl main

// Load repository database objects
runGroovyClass Db, true

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

sequence('sales') { seq ->
    sql(seq.currentJDBCConnection) {
        exec "CREATE SEQUENCE ${seq.fullName} INCREMENT BY ${seq.cache} CACHE ${seq.cache} NO CYCLE"
    }
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

// Define the structure and create table "Events"
h2Table('events') {
    field('id') { type = stringFieldType; length = 50; isKey = true }
    field('event_time') { type = datetimeFieldType; isNull = false }
    field('event_type') { type = stringFieldType; length = 20; isNull = false }
    field('counter_id') { type = integerFieldType; isNull = false }
    field('counter_value') { type = bigintFieldType; isNull = false }
    create()
}

// Create history points s_Points table
historypoint('points') { create() }

logInfo 'All database objects are successfully created!'