/**
 * Send HTML mail with attachment file to specified email
 */

package getl.examples.utils

import getl.utils.FileUtils
import groovy.transform.BaseScript

@BaseScript getl.lang.Getl getl

// Load configuration file
runGroovyClass getl.examples.utils.Config, true

// Mail commands
mail {
    // Use parameters from emailers.mail configurarion section
    config = 'mail'

    subject = 'Test mail send'
    isHtml = true
    message = '<HTML><BODY><H1>Message</H1>This is test send from getl lang</BODY></HTML>'
    attachment = FileUtils.FileFromResources('files/dir1/file1.txt', 'src/main/resources')

    send()

    logInfo('Send message complete!')
}