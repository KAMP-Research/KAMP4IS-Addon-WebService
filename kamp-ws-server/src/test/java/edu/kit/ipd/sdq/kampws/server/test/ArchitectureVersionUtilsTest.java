/**
 * 
 */
package edu.kit.ipd.sdq.kampws.server.test;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.kit.ipd.sdq.kamp4is.core.ISArchitectureVersion;
import edu.kit.ipd.sdq.kampws.server.ArchitectureVersionPersistency;
import edu.kit.ipd.sdq.kampws.server.ArchitectureVersionUtils;

/**
 * @author Milena Neumann
 */
public class ArchitectureVersionUtilsTest {

    static ArchitectureVersionPersistency avp;

    static ISArchitectureVersion version;

    @BeforeClass
    public static void initialize() throws FileNotFoundException {
        avp = new ArchitectureVersionPersistency();
        version = avp.getArchitectureVersion();
    }

    @Before
    public void reload() {
        avp.reloadArchitectureVersion();
        version = avp.getArchitectureVersion();
    }

    @Test
    public void testGetChangeScenarios_ProjectWithoutShortcut() {
        List<String> result = ArchitectureVersionUtils.getChangeScenarios(version, "xs-runtime");
        assertTrue("Expected: " + 1 + " found: " + result.size(), result.size() == 1);
        assertTrue(result.contains("xs-runtime"));
    }

    @Test
    public void testGetChangeScenarios_ProjectWithShortcut() {
        List<String> result = ArchitectureVersionUtils.getChangeScenarios(version, "xs-generation");
        assertTrue("Expected: " + 2 + " found: " + result.size(), result.size() == 2);
        assertTrue(result.contains("xs-generation"));
        assertTrue(result.contains("xs-generation_documentation"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetChangeScenarios_Invalid() {
        ArchitectureVersionUtils.getChangeScenarios(version, "invalid");
    }

    @Test
    public void testGetPossibleComponentNames() {
        List<String> result = ArchitectureVersionUtils.getPossibleComponentNames(version);
        assertTrue("Expected: " + 26 + " found: " + result.size(), result.size() == 26);
        assertTrue(result.contains("xs-frontend"));
        assertTrue(result.contains("xs-generation"));
        assertTrue(result.contains("lbc"));
    }

    @Test
    public void testValidateComponents() {
        List<String> entries = ArchitectureVersionUtils.getPossibleComponentNames(version);
        ArchitectureVersionUtils.validateComponentEntries(version, entries);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateComponents_InvalidEntry() {
        List<String> entries = ArchitectureVersionUtils.getPossibleComponentNames(version);
        entries.add("invalidEntry");
        ArchitectureVersionUtils.validateComponentEntries(version, entries);
    }

    @Test
    public void testGetPossibleInterfaceNames() {
        List<String> result = ArchitectureVersionUtils.getPossibleInterfaceNames(version);
        assertTrue("Expected: " + 27 + " found: " + result.size(), result.size() == 27);
        assertTrue(result.contains("xs-frontend"));
        assertTrue(result.contains("xs-generation"));
        assertTrue(result.contains("lbc"));
    }

    @Test
    public void testValidateInterfaces() {
        List<String> entries = ArchitectureVersionUtils.getPossibleInterfaceNames(version);
        ArchitectureVersionUtils.validateInterfaceEntries(version, entries);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateInterfaces_InvalidEntry() {
        List<String> entries = ArchitectureVersionUtils.getPossibleInterfaceNames(version);
        entries.add("invalidEntry");
        ArchitectureVersionUtils.validateInterfaceEntries(version, entries);
    }

    @Test
    public void testGetBuildSpecificationPaths() {
        List<String> example = new ArrayList<String>();
        example.add("xs-frontend");
        example.add("xs-engines");
        List<String> result = ArchitectureVersionUtils.getBuildSpecificationPaths(version, example);
        assertTrue("Expected: " + 2 + " found: " + result.size(), result.size() == 2);
        assertTrue(result.contains("xs/frontend/pom.xml"));
        assertTrue(result.contains("xs/engines/pom.xml"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBuildSpecificationPaths_Invalid() {
        List<String> example = new ArrayList<String>();
        example.add("xs-frontend");
        example.add("invalid");
        ArchitectureVersionUtils.getBuildSpecificationPaths(version, example);
    }
}
