package getl.examples.app

import getl.examples.launcher.ExampleRun
import getl.examples.process.sales.GenerateSales

/** Generate and record sales in the database sales table */
class LoadSales extends ExampleRun {
    static void main(def args) {
        Application(this, args)
    }

    @Override
    Object run() {
        // Call the script to write to the sales database table with the generated data
        callScript GenerateSales
    }
}