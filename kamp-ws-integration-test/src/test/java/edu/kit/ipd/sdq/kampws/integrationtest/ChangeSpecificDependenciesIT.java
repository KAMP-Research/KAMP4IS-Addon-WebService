package edu.kit.ipd.sdq.kampws.integrationtest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.kit.ipd.sdq.kampws.client.ChangeSpecificDependencies;
import edu.kit.ipd.sdq.kampws.client.ChangeSpecificDependenciesService;
import edu.kit.ipd.sdq.kampws.client.IllegalArgumentException_Exception;
import edu.kit.ipd.sdq.kampws.client.StringArray;

public final class ChangeSpecificDependenciesIT {

    private static final String WSDL_LOCATION = "http://localhost:8085/kamp-ws-server/services/changeSpecificDependencies?wsdl";

    private static final Logger LOGGER = Logger.getLogger(ChangeSpecificDependenciesIT.class);

    static ChangeSpecificDependencies csd;

    @BeforeClass
    public static void initialize() {
        LOGGER.debug("Initializing Client against WSDL location: " + WSDL_LOCATION);
        ChangeSpecificDependenciesService service = null;
        try {
            service = new ChangeSpecificDependenciesService(new URL(WSDL_LOCATION));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail();
        }
        csd = service.getChangeSpecificDependenciesPort();
        assumeTrue(csd != null);
    }

    @Test
    public void testGetPossibleProjectNames() {
        LOGGER.debug("testGetPossibleProjectNames() started:");
        assertFalse("Service not initialized", csd == null);

        List<String> response = csd.getPossibleProjectNames().getItem();
        LOGGER.debug("Response from WS: " + response.toString());
        assertTrue("Expected: " + 26 + " found: " + response.size(), response.size() == 26);
        assertTrue(response.contains("xs-frontend"));
        assertTrue(response.contains("xs-generation"));
        assertTrue(response.contains("lbc"));
        LOGGER.debug("testGetPossibleProjectNames() passed.");
    }

    @Test
    public void testGetPossibleChangeScenariosForProject() {
        LOGGER.debug("testGetPossibleChangeScenariosForProject() started:");
        assertFalse("Service not initialized", csd == null);
        List<String> response = null;
        try {
            response = csd.getChangeScenarios("xs-generation").getItem();
            LOGGER.debug("Response from WS: " + response.toString());
        } catch (IllegalArgumentException_Exception e) {
            e.printStackTrace();
            fail("Unexpected Exception");
        }
        assertTrue("Expected: " + 2 + " found: " + response.size(), response.size() == 2);
        assertTrue(response.contains("xs-generation"));
        assertTrue(response.contains("xs-generation_documentation"));
        LOGGER.debug("testGetPossibleChangeScenariosForProject() passed.");
    }

    @Test
    public void testGetChangeSpecificDependencies() {
        LOGGER.debug("testGetChangeSpecificDependencies() started:");
        assertFalse("Service not initialized", csd == null);
        StringArray example = new StringArray();
        example.getItem().add("xs-frontend");
        ArrayList<String> response = null;
        try {
            response = (ArrayList<String>) csd.getChangeSpecificDependencies(example).getItem();

        } catch (IllegalArgumentException_Exception e) {
            e.printStackTrace();
            fail("Unexpected Exception");
        }
        LOGGER.debug("Response from WS: " + response.toString());
        assertTrue("Expected: " + 4 + " found: " + response.size(), response.size() == 4);
        String[] expectedEntries = { "xs-frontend", "xs-server-services", "xs-server-container",
            "xs-server-systemTest" };
        for (String entry : expectedEntries) {
            assertTrue("Expected Entry " + entry + " not found.", response.contains(entry));
        }
        LOGGER.debug("testGetPossibleChangeScenariosForProject() passed.");
    }

    @Test
    public void testGetChangeSpecificDependencies_model() {
        LOGGER.debug("testGetChangeSpecificDependencies() started:");
        assertFalse("Service not initialized", csd == null);
        StringArray example = new StringArray();
        example.getItem().add("xs-generation");
        ArrayList<String> response = null;
        try {
            response = (ArrayList<String>) csd.getChangeSpecificDependencies(example).getItem();

        } catch (IllegalArgumentException_Exception e) {
            LOGGER.error("Unexpected Exception!", e);
        }
        LOGGER.debug("Response from WS: " + response.toString());
        assertTrue("Expected: " + 25 + " found: " + response.size(), response.size() == 25);
        String[] expectedEntries = { "xs-frontend", "xs-server-services", "xs-server-container",
            "xs-server-systemTest" };
        for (String entry : expectedEntries) {
            assertTrue("Expected Entry " + entry + " not found.", response.contains(entry));
        }
        LOGGER.debug("testGetPossibleChangeScenariosForProject() passed.");
    }

    @Test
    public void testGetChangeSpecificDependencies_documentation() {
        LOGGER.debug("testGetChangeSpecificDependencies() started:");
        assertFalse("Service not initialized", csd == null);
        StringArray example = new StringArray();
        example.getItem().add("xs-generation_documentation");
        ArrayList<String> response = null;
        try {
            response = (ArrayList<String>) csd.getChangeSpecificDependencies(example).getItem();

        } catch (IllegalArgumentException_Exception e) {
            LOGGER.error("Unexpected Exception!", e);
        }
        LOGGER.debug("Response from WS: " + response.toString());
        assertTrue("Expected: " + 5 + " found: " + response.size(), response.size() == 5);
        String[] expectedEntries = { "xs-generation", "xs-frontend", "xs-server-services", "xs-server-container",
            "xs-server-systemTest" };
        for (String entry : expectedEntries) {
            assertTrue("Expected Entry " + entry + " not found.", response.contains(entry));
        }
        LOGGER.debug("testGetPossibleChangeScenariosForProject() passed.");
    }

    @Test
    public void testGetBuildSpecificationPaths() {
        LOGGER.debug("testGetBuildSpecificationPaths() started:");
        StringArray example = new StringArray();
        example.getItem().add("xs-generation");
        example.getItem().add("xs-frontend");
        example.getItem().add("xs-server-services");
        example.getItem().add("xs-server-container");
        example.getItem().add("xs-server-systemTest");
        ArrayList<String> response = null;
        try {
            response = (ArrayList<String>) csd.getBuildSpecificationPaths(example).getItem();
        } catch (IllegalArgumentException_Exception e) {
            LOGGER.error("Unexpected Exception!", e);
        }
        assertTrue("Expected: " + 5 + " found: " + response.size(), response.size() == 5);
        assertTrue(response.contains("xs/generation/pom.xml"));
        assertTrue(response.contains("xs/frontend/pom.xml"));
        assertTrue(response.contains("xs/server/services/pom.xml"));
        assertTrue(response.contains("xs/server/container/pom.xml"));
        assertTrue(response.contains("xs/server/systemTest/pom.xml"));

    }

}