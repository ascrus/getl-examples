package getl.examples.utils

import getl.lang.Getl
import groovy.transform.BaseScript
import groovy.transform.Field

@BaseScript Getl getl

@Field String tableName; assert tableName != null

logInfo "Table $tableName has " + embeddedTable(tableName).countRow() + " rows."