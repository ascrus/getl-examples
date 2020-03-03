package getl.examples.repository

import getl.examples.env.InitProcess
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
    connectDatabase = "${InitProcess.WorkPath}/db"
    FileUtils.ValidFilePath(connectDatabase)

    // Specify the configuration section in which logins to the database are stored
    loginsConfigStore = 'db_logins'

    // Set default schema for tables
    schemaName = 'demo'

    // Write sql command to log
    sqlHistoryFile = "${InitProcess.WorkPath}/db.{date}.sql"
}

// Set default work with objects of group "db"
forGroup 'db'

// Register Price table in the repository
h2Table('prices', true) {
    tableName = 'prices'
}

// Register Customer table in the repository
h2Table('customers', true) {
    tableName = 'customers'
}

// Register Customer_Phones table in the repository
h2Table('customers.phones', true) {
    tableName = 'customer_phones'
}

// Register Sales sequence in the repository
sequence('sales', true) {
    name = 's_sales'
    cache = 100
}

// Register Sales table in the repository
h2Table('sales', true) {
    tableName = 'sales'
}

// Register Events table in the repository
h2Table('events', true) {
    tableName = 'events'
}

// Register Events history point manager in the repository
historypoint('points', true) {
    tableName = 's_points'
    saveMethod = mergeSave
}

// Register Events_History table in the repository
h2Table('events_history', true) {
    tableName = 's_events_history'
}