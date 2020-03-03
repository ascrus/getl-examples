package getl.examples.repository

import getl.examples.env.InitProcess
import getl.lang.Getl
import getl.utils.FileUtils
import groovy.transform.BaseScript

@BaseScript Getl main

forGroup 'json'

// Register Json connection "Con"
useJsonConnection jsonConnection('con', true) {
    extension = 'json'
    path = "${InitProcess.WorkPath}/events"
    FileUtils.ValidPath(path)
}

// Register Json file "Events" in the repository
json('events', true) {
    rootNode = '.'
    field('id') { type = stringFieldType; isNull = false }
    field('event_time') { type = datetimeFieldType; isNull = false; format = 'yyyy-MM-dd HH:mm:ss' }
    field('event_type') { type = stringFieldType; isNull = false }
    field('counter_id') { type = integerFieldType; isNull = false }
    field('counter_value') { type = bigintFieldType; isNull = false }
}