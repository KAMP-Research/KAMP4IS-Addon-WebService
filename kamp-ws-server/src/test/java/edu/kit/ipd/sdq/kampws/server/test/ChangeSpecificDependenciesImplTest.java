package edu.kit.ipd.sdq.kampws.server.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.sdq.kampws.server.ChangeSpecificDependenciesImpl;

public class ChangeSpecificDependenciesImplTest {

    static ChangeSpecificDependenciesImpl csd;

    @Before
    public void initialize() throws FileNotFoundException {
        csd = new ChangeSpecificDependenciesImpl();
    }

    @Test
    public void testGetChangeSpecificDependencies_MultipleEntries() {
        List<String> example = new ArrayList<String>();
        example.add("xs-frontend");
        example.add("xs-server-services");
        ArrayList<String> response;
        response = (ArrayList<String>) csd.getChangeSpecificDependencies(example);
        assertTrue("Expected: " + 4 + " found: " + response.size(), response.size() == 4);
        assertTrue(response.contains("xs-frontend"));
        assertTrue(response.contains("xs-server-services"));
        assertTrue(response.contains("xs-server-container"));
        assertTrue(response.contains("xs-server-systemTest"));
    }

    @Test
    public void testGetChangeSpecificDependencies_SingleEntry() {
        List<String> example = new ArrayList<String>();
        example.add("xs-frontend");
        ArrayList<String> response;
        response = (ArrayList<String>) csd.getChangeSpecificDependencies(example);
        assertTrue("Expected: " + 4 + " found: " + response.size(), response.size() == 4);
        assertTrue(response.contains("xs-frontend"));
        assertTrue(response.contains("xs-server-services"));
        assertTrue(response.contains("xs-server-container"));
        assertTrue(response.contains("xs-server-systemTest"));
    }

    @Test
    public void testGetChangeSpecificDependencies_Documentation() {
        List<String> example = new ArrayList<String>();
        example.add("xs-generation_documentation");
        ArrayList<String> response;
        response = (ArrayList<String>) csd.getChangeSpecificDependencies(example);
        assertTrue("Expected: " + 5 + " found: " + response.size(), response.size() == 5);
        assertTrue(response.contains("xs-frontend"));
        assertTrue(response.contains("xs-server-services"));
        assertTrue(response.contains("xs-server-container"));
        assertTrue(response.contains("xs-server-systemTest"));
    }

    @Test
    public void testGetChangeSpecificDependencies_Model() {
        List<String> example = new ArrayList<String>();
        example.add("xs-generation");
        ArrayList<String> response;
        response = (ArrayList<String>) csd.getChangeSpecificDependencies(example);
        assertTrue("Expected: " + 25 + " found: " + response.size(), response.size() == 25);
        assertTrue(response.contains("xs-frontend"));
        assertTrue(response.contains("xs-server-services"));
        assertTrue(response.contains("xs-server-container"));
        assertTrue(response.contains("xs-server-systemTest"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetChangeSpecificDependencies_InvalidInput() {
        List<String> example = new ArrayList<String>();
        example.add("wrong-frontend");
        @SuppressWarnings("unused")
        ArrayList<String> response = (ArrayList<String>) csd.getChangeSpecificDependencies(example);
        fail(); // this should never be reached as line above throws exception.
    }

    @Test
    public void testGetPossibleChangeScenariosForProject_ProjectWithoutShortcut() {
        ArrayList<String> response = (ArrayList<String>) csd.getChangeScenarios("xs-frontend");
        assertTrue("Expected: " + 1 + " found: " + response.size(), response.size() == 1);
        assertTrue(response.contains("xs-frontend"));
    }

    @Test
    public void testGetPossibleChangeScenariosForProject_ProjectWithShortcut() {
        ArrayList<String> response = (ArrayList<String>) csd.getChangeScenarios("xs-generation");
        assertTrue("Expected: " + 2 + " found: " + response.size(), response.size() == 2);
        assertTrue(response.contains("xs-generation"));
        assertTrue(response.contains("xs-generation_documentation"));
    }

    @Test
    public void testGetPossibleProjectNames() {
        ArrayList<String> response = (ArrayList<String>) csd.getPossibleProjectNames();
        assertTrue("Expected: " + 26 + " found: " + response.size(), response.size() == 26);
        assertTrue(response.contains("xs-frontend"));
        assertTrue(response.contains("xs-generation"));
        assertTrue(response.contains("lbc"));
    }

    @Test
    public void testGetBuildSpecificationPaths() {
        List<String> example = new ArrayList<String>();
        example.add("xs-generation");
        example.add("xs-frontend");
        example.add("xs-server-services");
        example.add("xs-server-container");
        example.add("xs-server-systemTest");
        ArrayList<String> response = (ArrayList<String>) csd.getBuildSpecificationPaths(example);
        assertTrue("Expected: " + 5 + " found: " + response.size(), response.size() == 5);
        assertTrue(response.contains("xs/generation/pom.xml"));
        assertTrue(response.contains("xs/frontend/pom.xml"));
        assertTrue(response.contains("xs/server/services/pom.xml"));
        assertTrue(response.contains("xs/server/container/pom.xml"));
        assertTrue(response.contains("xs/server/systemTest/pom.xml"));

    }
}
