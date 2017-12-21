package edu.kit.ipd.sdq.kampws.server.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.junit.BeforeClass;
import org.junit.Test;
import org.palladiosimulator.pcm.repository.Interface;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;

import de.uka.ipd.sdq.componentInternalDependencies.ComponentInternalDependencyRepository;
import edu.kit.ipd.sdq.kamp4is.core.ISArchitectureVersion;
import edu.kit.ipd.sdq.kamp4is.model.fieldofactivityannotations.ISFieldOfActivityAnnotationsRepository;
import edu.kit.ipd.sdq.kampws.server.ArchitectureVersionPersistency;

public class ArchitectureVersionPersistencyDefaultTest {
    static final int EXPECTED_COMPONENT_COUNT_DEFAULT = 26;
    static final int EXPECTED_INTERFACE_COUNT_DEFAULT = 27;
    static final String[] EXPECTED_COMPONENT_NAMES_DEFAULT = { "loc-clusterServices",
        "loc-territoryOptimizer", "loc-vrpSolver", "lgc-roadGraph", "lgc-router",
        "lgc-dataAccess", "xs-frontend", "lgc-renderer", "xs-server-container",
        "lbc", "lgc-servicesMapHelper", "loc-vrpServices", "lgc-dimaCtrl", "xs-runtime",
        "lgc-routerServices", "lgc-rendererServices", "xs-engines", "lgc-dataInformationServices",
        "loc-loadOptimizer", "lgc-dimaServices", "xs-server-services",
        "xs-modules", "xs-generation", "xs-server-systemTest", "lgc-dataServices", "lgc-commonRoutingServicesTasks" };
    static final String[] EXPECTED_INTERFACE_NAMES_DEFAULT = { "loc-clusterServices",
        "loc-territoryOptimizer", "loc-vrpSolver", "lgc-roadGraph", "lgc-router",
        "lgc-dataAccess", "xs-frontend", "lgc-renderer", "xs-server-container",
        "lbc", "lgc-servicesMapHelper", "loc-vrpServices", "lgc-dimaCtrl", "xs-runtime",
        "lgc-routerServices", "lgc-rendererServices", "xs-engines", "lgc-dataInformationServices",
        "loc-loadOptimizer", "lgc-dimaServices", "xs-server-services",
        "xs-modules", "xs-generation", "xs-generation_documentation", "xs-server-systemTest", "lgc-dataServices",
        "lgc-commonRoutingServicesTasks" };

    static ArchitectureVersionPersistency avp;

    @BeforeClass
    public static void initializeBaseArchitecture() throws FileNotFoundException {
        avp = new ArchitectureVersionPersistency();
    }

    @Test
    public void testGetArchitectureVersion() {
        ISArchitectureVersion version = avp.getArchitectureVersion();
        assertFalse("Version is null", version == null);
    }

    @Test
    public void testGetRepository() {
        Repository repo = avp.getArchitectureVersion().getRepository();
        assertFalse("Repository is null", repo == null);
        EList<RepositoryComponent> components = repo.getComponents__Repository();
        compareComponentCount(components, EXPECTED_COMPONENT_COUNT_DEFAULT);
        compareComponentNames(components, Arrays.asList(EXPECTED_COMPONENT_NAMES_DEFAULT));
        EList<Interface> interfaces = repo.getInterfaces__Repository();
        compareInterfaceCount(interfaces, EXPECTED_INTERFACE_COUNT_DEFAULT);
        compareInterfaceNames(interfaces, Arrays.asList(EXPECTED_INTERFACE_NAMES_DEFAULT));
    }

    private void compareComponentCount(EList<RepositoryComponent> components, int expected) {
        assertTrue("Found " + components.size() + " components, expected " + expected, components.size() == expected);
    }

    private void compareComponentNames(EList<RepositoryComponent> components, List<String> expectedComponentNames) {
        List<String> foundNames = new ArrayList<String>();
        for (RepositoryComponent comp : components) {
            String name = comp.getEntityName();
            assertTrue("Unexpected component name encountered: " + name, expectedComponentNames.contains(name));
            assertFalse("Duplicate Name: " + name, foundNames.contains(name));
            foundNames.add(name);
        }
        assertTrue("Not all expected names were found", foundNames.size() == expectedComponentNames.size());
    }

    private void compareInterfaceCount(EList<Interface> interfaces, int expected) {
        assertTrue("Found " + interfaces.size() + " interfaces, expected " + expected, interfaces.size() == expected);
    }

    private void compareInterfaceNames(EList<Interface> interfaces, List<String> expectedInterfaceNames) {
        List<String> foundNames = new ArrayList<String>();
        for (Interface interf : interfaces) {
            String name = interf.getEntityName();
            assertTrue("Unexpected interface name encountered: " + name, expectedInterfaceNames.contains(name));
            assertFalse("Duplicate Name: " + name, foundNames.contains(name));
            foundNames.add(name);
        }
        assertTrue("Not all expected names were found", foundNames.size() == expectedInterfaceNames.size());
    }

    @Test
    public void testGetSystem() {
        org.palladiosimulator.pcm.system.System system = avp.getArchitectureVersion().getSystem();
        assertFalse("System is null", system == null);
    }

    @Test
    public void testGetFieldOfActivityAnnotationsRepository() {
        ISFieldOfActivityAnnotationsRepository foaa = avp.getArchitectureVersion()
            .getFieldOfActivityAnnotationsRepository();
        assertFalse("FieldOfActivityAnnotations is null", foaa == null);
    }

    @Test
    public void testGetComponentInternalDependencyRepository() {
        ComponentInternalDependencyRepository foaa = avp.getArchitectureVersion()
            .getComponentInternalDependencyRepository();
        assertFalse("ComponentInternalDependencyRepository is null", foaa == null);
    }
}
