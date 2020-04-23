package getl.examples.launcher

import getl.examples.launcher.sub.Init
import getl.lang.Getl
import getl.test.GetlDslTest

/**
 * Unit test examples launch class
 */
class ExampleTest extends GetlDslTest {
    @Override
    Class<Getl> useInitClass() { Init }

    @Override
    Class<Getl> useGetlClass() { ExampleRun }
}