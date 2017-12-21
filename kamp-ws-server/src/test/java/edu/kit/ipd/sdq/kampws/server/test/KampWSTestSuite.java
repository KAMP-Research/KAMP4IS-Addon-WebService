package edu.kit.ipd.sdq.kampws.server.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
    ArchitectureVersionPersistencySandboxTest.class,
    ArchitectureVersionPersistencyDefaultTest.class,
    ArchitectureVersionUtilsTest.class,
    ChangeSpecificDependenciesImplTest.class,
    ModificationMarksFactoryTest.class,
    ModificationMarksFilterTest.class
})

public class KampWSTestSuite {}
