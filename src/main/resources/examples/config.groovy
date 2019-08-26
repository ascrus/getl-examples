// Process directory (used to store information generated in examples)
workPath = '<directory path>'

// JDBC drivers directory (directory for storing jar files for used JDBC drivers)
driverPath = '<directory path>'

// Count of generating the sale rows
countSales = 1000

// List of named mail parameters
emailers {
    mail {
        host = '<smtp host>'
        port = 465
        ssl = true
        tls = false
        user = '<login>'
        password = '<password>'
        fromAddress = '<sender email address>'
        toAddress = '<recipient address,recipient address>'
    }
}

// List of named connection parameters
connections {
    hive {
        driverPath = "$driverPath/HiveJDBC4.jar"
        vendor = '<JDBC driver type>' // allow values: apache, hortonworks or cloudera
        connectDatabase = '<database name>'
        connectHost = '<hive master host>:10000'
        login = '<login>'
        hdfsHost = '<hdfs host for bulkload files>'
        hdfsPort = 8022
        hdfsLogin = '<hdfs user>'
        hdfsDir = '<home user catalog>'
    }

    mssql {
        driverPath = "$driverPath/mssql-jdbc-6.2.2.jre8.jar"
        connectDatabase = '<database name>'
        connectHost = '<server host>'
        login = '<login>'
        password = '<password>'
    }

    mysql {
        driverPath = "$driverPath/mysql-connector-java-8.0.16.jar"
        connectDatabase = '<database name>'
        connectHost = '<server host>'
        login = '<login>'
        password = '<password>'
    }

    oracle {
        driverPath = "$driverPath/ojdbc7.jar"
        connectDatabase = '<database name>'
        connectHost = '<server host>'
        login = '<login>'
        password = '<password>'
    }

    postgresql {
        driverPath = "$driverPath/postgresql-42.2.6.jar"
        connectDatabase = '<database name>'
        connectHost = '<server host>'
        login = '<login>'
        password = '<password>'
    }

    vertica {
        driverPath = "$driverPath/vertica-jdbc-9.1.1-0.jar"
        connectDatabase = '<database name>'
        connectHost = '<server host>'
        login = '<login>'
        password = '<password>'
    }
}