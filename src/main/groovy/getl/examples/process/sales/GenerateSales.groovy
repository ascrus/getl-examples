package getl.examples.process.sales

import getl.examples.data.Db
import getl.examples.launcher.ExampleRun
import getl.utils.GenerationUtils
import groovy.transform.BaseScript
import groovy.transform.Field

//noinspection GroovyUnusedAssignment
@BaseScript ExampleRun main

// Count rows to generate sales
@Field Long count_generate_rows = 100000

void check() {
    assert count_generate_rows?:0 > 0
}

// Load repository database objects
runGroovyClass Db, true

forGroup 'db'

// Set default repository connection "db:con" for h2 objects in the script
useH2Connection h2Connection('con') {
    // Work under "writer" as user
    useLogin 'writer'
}

// Work with table "Sales"
h2Table('sales') {
    def seq = sequence('sales')

    // Generate and write data to the sales table
    rowsTo {
        // Lookup price map structure
        def priceLookup = h2Table('prices').lookup { key = 'id'; strategy = ORDER_STRATEGY }

        // Size of batch saving rows
        destParams.batchSize = 1000

        // Code for inserting records into a table
        writeRow { add -> // Write descriptor
            (1..count_generate_rows).each { num ->
                def id = seq.nextValueFast
                def price = GenerationUtils.GenerateInt(1, 7)
                def customer = GenerationUtils.GenerateInt(1, 3)
                def sale_date = GenerationUtils.GenerateDate(90)
                def count = GenerationUtils.GenerateInt(1, 99)
                def priceRow = priceLookup.get(price) as Map
                def sum = priceRow.price * count
                def desc = GenerationUtils.GenerateString(50)

                // Append row to destination
                add id: id, price_id: price, customer_id: customer, sale_date: sale_date, sale_count: count, sale_sum: sum,
                        description: desc
            }
        }
    }

    logInfo "$updateRows rows are inserted in table \"Sales\", total ${countRow()} rows in table"
}