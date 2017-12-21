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
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.repository.RequiredRole;

import de.uka.ipd.sdq.componentInternalDependencies.ComponentInternalDependencyRepository;
import edu.kit.ipd.sdq.kamp4is.core.ISArchitectureVersion;
import edu.kit.ipd.sdq.kamp4is.model.fieldofactivityannotations.ISFieldOfActivityAnnotationsRepository;
import edu.kit.ipd.sdq.kampws.server.ArchitectureVersionPersistency;

public class ArchitectureVersionPersistencySandboxTest {
    static final int EXPECTED_COMPONENT_COUNT_SANDBOX = 3;
    static final int EXPECTED_INTERFACE_COUNT_SANDBOX = 3;
    static final String[] EXPECTED_COMPONENT_NAMES_SANDBOX = { "xs-generation-model", "xs-frontend", "xs-server" };
    static final String[] EXPECTED_INTERFACE_NAMES_SANDBOX = { "xs-generation", "xs-frontend", "xs-server" };

    static ArchitectureVersionPersistency bap;

    @BeforeClass
    public static void initializeBaseArchitecture() throws FileNotFoundException {
        bap = new ArchitectureVersionPersistency("src/test/resources/Sandbox", "Sandbox");
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
    public void testGetArchitectureVersion() {
        ISArchitectureVersion version = bap.getArchitectureVersion();
        assertFalse("Sandbox version is null", version == null);
        assertFalse("Sandbox repository is null", version.getRepository() == null);
        assertFalse("Sandbox system is null", version.getSystem() == null);
        assertFalse("Sandbox fieldOfActivityAnnotations is null",
            version.getFieldOfActivityAnnotationsRepository() == null);
        assertFalse("Sandbox componentInternalDependencies is null",
            version.getComponentInternalDependencyRepository() == null);
    }

    @Test
    public void testGetRepository() {
        Repository repo = bap.getArchitectureVersion().getRepository();
        assertFalse("Repository is null", repo == null);

        // compare the components in the repository
        EList<RepositoryComponent> components = repo.getComponents__Repository();
        compareComponentCount(components, EXPECTED_COMPONENT_COUNT_SANDBOX);
        compareComponentNames(components, Arrays.asList(EXPECTED_COMPONENT_NAMES_SANDBOX));
        checkProvidedRoles(components);
        checkRequiredRoles(components);

        // compare the interfaces in the repository
        EList<Interface> interfaces = repo.getInterfaces__Repository();
        compareInterfaceCount(interfaces, EXPECTED_INTERFACE_COUNT_SANDBOX);
        compareInterfaceNames(interfaces, Arrays.asList(EXPECTED_INTERFACE_NAMES_SANDBOX));
    }

    private void checkProvidedRoles(EList<RepositoryComponent> components) {
        for (RepositoryComponent comp : components) {
            EList<ProvidedRole> providedRoles = comp.getProvidedRoles_InterfaceProvidingEntity();
            assertTrue("Unexpected Provided-Role count: " + providedRoles.size() + ", expected: " + 1,
                providedRoles.size() == 1);
        }
    }

    private void checkRequiredRoles(EList<RepositoryComponent> components) {
        for (RepositoryComponent comp : components) {
            EList<RequiredRole> requiredRoles = comp.getRequiredRoles_InterfaceRequiringEntity();
            int expected = 0;
            if (comp.getEntityName().equals(EXPECTED_COMPONENT_NAMES_SANDBOX[1])) {
                expected = 1;
            } else if (comp.getEntityName().equals(EXPECTED_COMPONENT_NAMES_SANDBOX[2])) {
                expected = 2;
            }
            assertTrue("Unexpected Required-Role count: " + requiredRoles.size() + ", expected: " + expected,
                requiredRoles.size() == expected);
        }
    }

    @Test
    public void testGetSystem() {
        org.palladiosimulator.pcm.system.System system = bap.getArchitectureVersion().getSystem();
        assertFalse("System is null", system == null);
        assertTrue(system.getAssemblyContexts__ComposedStructure().size() == EXPECTED_COMPONENT_COUNT_SANDBOX);
    }

    @Test
    public void testGetFieldOfActivityAnnotationsRepository() {
        ISFieldOfActivityAnnotationsRepository foaa = bap.getArchitectureVersion()
            .getFieldOfActivityAnnotationsRepository();
        assertFalse("FieldOfActivityAnnotations is null", foaa == null);

        assertTrue(foaa.getBuildSpecification().getBuildConfigurations().size() == 1);
        assertTrue(foaa.getDevelopmentArtefactSpecification().getSourceFileAggregations().size() == 1);
        assertTrue(foaa.getDevelopmentArtefactSpecification().getSourceFiles().size() == 0);
        assertTrue(foaa.getTestSpecification().getIntegrationTestCases().size() == 1);
        assertTrue(foaa.getTestSpecification().getAcceptanceTestCaseAggregations().size() == 0);
    }

    @Test
    public void testGetComponentInternalDependencyRepository() {
        ComponentInternalDependencyRepository cide = bap.getArchitectureVersion()
            .getComponentInternalDependencyRepository();
        assertFalse("ComponentInternalDependencyRepository is null", cide == null);
        assertTrue(cide.getDependencies().size() == 3);
    }
}
