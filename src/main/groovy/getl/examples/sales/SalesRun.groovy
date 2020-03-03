package getl.examples.sales

import getl.examples.env.InitProcess
import getl.lang.Getl

class SalesRun extends Getl {
    static void main(def args) {
        Application(this, args)
    }

    @Override
    Object run() {
        // Init options
        InitProcess.InitOptions(this)

        // Generate sales rows
        runGroovyClass Sales
    }
}