package getl.examples.files

import getl.examples.data.Db
import getl.examples.data.Json
import getl.examples.launcher.ExampleRun
import groovy.transform.BaseScript

//noinspection GroovyUnusedAssignment
@BaseScript ExampleRun main

// Call the script for registering objects in the repository
callScripts Json

// Define source file managers
files('events', true) {
    // Set the path to the source events file storage directory
    rootPath = jsonConnection('json:con').path
}