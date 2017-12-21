package edu.kit.ipd.sdq.kampws.server.test;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.sdq.kamp4is.core.ISArchitectureVersion;
import edu.kit.ipd.sdq.kamp4is.core.ISChangePropagationAnalysis;
import edu.kit.ipd.sdq.kamp4is.model.modificationmarks.ISChangePropagationStep;
import edu.kit.ipd.sdq.kamp4is.model.modificationmarks.ISModificationRepository;
import edu.kit.ipd.sdq.kampws.server.ArchitectureVersionPersistency;
import edu.kit.ipd.sdq.kampws.server.ModificationMarksFactory;
import edu.kit.ipd.sdq.kampws.server.ModificationMarksFilter;

public class ModificationMarksFilterTest {

    static ArchitectureVersionPersistency architectureVersionPersistency;
    static ModificationMarksFactory modificationMarksGenerator;
    static ISChangePropagationAnalysis propagationAnalysis;
    static ISArchitectureVersion version;

    @Before
    public void initialize() throws FileNotFoundException {
        architectureVersionPersistency = new ArchitectureVersionPersistency();
        modificationMarksGenerator = new ModificationMarksFactory(architectureVersionPersistency);
        propagationAnalysis = new ISChangePropagationAnalysis();
        version = architectureVersionPersistency.getArchitectureVersion();
    }

    @Test
    public void testCreateResponse_MultipleEntries() {
        List<String> example = new ArrayList<String>();
        example.add("xs-frontend");
        example.add("xs-server-services");

        // add target model
        ISModificationRepository modifications = modificationMarksGenerator.createModificationMarks(example);
        version.setInternalModificationRepository(modifications);

        // execute KAMP
        propagationAnalysis.runChangePropagationAnalysis(version);
        ISModificationRepository newModifications = (ISModificationRepository) version.getModificationRepository();
        EList<ISChangePropagationStep> steps = newModifications.getISChangePropagationSteps();
        assertTrue(steps.size() > 0);

        List<String> response = ModificationMarksFilter.filterModifiedComponents(newModifications);
        assertTrue("expected: " + 4 + " found: " + response.size(), response.size() == 4);
        assertTrue(response.contains("xs-frontend"));
        assertTrue(response.contains("xs-server-services"));
        assertTrue(response.contains("xs-server-container"));
        assertTrue(response.contains("xs-server-systemTest"));

    }

    @Test
    public void testCreateResponse_SingleEntry_frontend() {
        List<String> example = new ArrayList<String>();
        example.add("xs-frontend");

        // add target model
        ISModificationRepository modifications = modificationMarksGenerator.createModificationMarks(example);
        version.setInternalModificationRepository(modifications);

        // execute KAMP
        propagationAnalysis.runChangePropagationAnalysis(version);
        ISModificationRepository newModifications = (ISModificationRepository) version.getModificationRepository();
        EList<ISChangePropagationStep> steps = newModifications.getISChangePropagationSteps();
        assertTrue(steps.size() > 0);

        List<String> response = ModificationMarksFilter.filterModifiedComponents(newModifications);
        assertTrue("expected: " + 4 + " found: " + response.size(), response.size() == 4);
        assertTrue(response.contains("xs-frontend"));
        assertTrue(response.contains("xs-server-services"));
        assertTrue(response.contains("xs-server-container"));
        assertTrue(response.contains("xs-server-systemTest"));

    }

    @Test
    public void testCreateResponse_SingleEntry_lbc() {
        List<String> example = new ArrayList<String>();
        example.add("lbc");

        // add target model
        ISModificationRepository modifications = modificationMarksGenerator.createModificationMarks(example);
        version.setInternalModificationRepository(modifications);

        // execute KAMP
        propagationAnalysis.runChangePropagationAnalysis(version);
        ISModificationRepository newModifications = (ISModificationRepository) version.getModificationRepository();
        EList<ISChangePropagationStep> steps = newModifications.getISChangePropagationSteps();
        assertTrue(steps.size() > 0);

        List<String> response = ModificationMarksFilter.filterModifiedComponents(newModifications);
        assertTrue("expected: " + 22 + " found: " + response.size(), response.size() == 22);
        assertTrue(response.contains("lgc-servicesMapHelper"));
        assertTrue(response.contains("lgc-roadGraph"));
        assertTrue(response.contains("xs-server-services"));
        assertTrue(response.contains("xs-server-container"));
        assertTrue(response.contains("xs-server-systemTest"));

    }
}
