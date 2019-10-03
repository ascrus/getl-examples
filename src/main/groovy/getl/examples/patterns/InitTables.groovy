package getl.examples.patterns

import groovy.transform.BaseScript
import groovy.transform.Field

@BaseScript getl.lang.Getl main

@Field String groupName; assert groupName != null

// The table mask to process option
forGroup groupName

profile("Create database object with \"$filteringGroup\" group") {
    jdbcTableProcess { tableName ->
        jdbcTable(tableName) {
            if (!exists) {
                // Create table in database
                create ifNotExists: true
                logInfo "Created table $it"
            } else {
                truncate()
                logInfo "Truncated table $it"
            }
        }
    }
}