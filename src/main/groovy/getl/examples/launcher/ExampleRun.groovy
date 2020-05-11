package getl.examples.launcher

import getl.examples.launcher.sub.Init
import getl.lang.Getl
import getl.utils.FileUtils

/**
 * Launcher of examples
 */
class ExampleRun extends Getl {
    /** Storage path for examples */
    static public final def WorkPath = FileUtils.SystemTempDir() + '/getl_examples'

    @Override
    protected Class useInitClass() { Init }
}