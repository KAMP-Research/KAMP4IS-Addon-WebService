package edu.kit.ipd.sdq.kampws.server.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.sdq.kamp4is.model.modificationmarks.ISModificationRepository;
import edu.kit.ipd.sdq.kamp4is.model.modificationmarks.ISModifyInterface;
import edu.kit.ipd.sdq.kamp4is.model.modificationmarks.ISSeedModifications;
import edu.kit.ipd.sdq.kampws.server.ArchitectureVersionPersistency;
import edu.kit.ipd.sdq.kampws.server.ModificationMarksFactory;

public class ModificationMarksFactoryTest {

    static ModificationMarksFactory mmf;

    @Before
    public void initialize() throws FileNotFoundException {
        mmf = new ModificationMarksFactory(new ArchitectureVersionPersistency());
    }

    @Test
    public void testCreateModificationMarks_SingleEntry() {
        String expected = "xs-frontend";
        List<String> example = new ArrayList<String>();
        example.add(expected);
        ISModificationRepository result = mmf.createModificationMarks(example);
        assertFalse(result == null);
        ISSeedModifications seedModifications = result.getSeedModifications();
        assertFalse(seedModifications == null);
        EList<ISModifyInterface> interfaceMods = seedModifications.getInterfaceModifications();
        assertTrue("expected: " + 1 + " got: " + interfaceMods.size(), interfaceMods.size() == 1);
        ISModifyInterface mod = interfaceMods.get(0);
        assertTrue("expected: " + expected + " found: " + mod.getAffectedElement().getEntityName(),
            mod.getAffectedElement().getEntityName().equals(expected));
    }

    @Test
    public void testCreateModificationMarks_MultipleEntries() {
        String expected1 = "xs-frontend";
        String expected2 = "xs-server-services";
        List<String> example = new ArrayList<String>();
        example.add(expected1);
        example.add(expected2);
        ISModificationRepository result = mmf.createModificationMarks(example);
        assertFalse(result == null);
        ISSeedModifications seedModifications = result.getSeedModifications();
        assertFalse(seedModifications == null);
        EList<ISModifyInterface> interfaceMods = seedModifications.getInterfaceModifications();
        assertTrue("expected: " + 2 + " got: " + interfaceMods.size(), interfaceMods.size() == 2);
        ISModifyInterface mod = interfaceMods.get(0);
        assertTrue("expected: " + expected1 + " found: " + mod.getAffectedElement().getEntityName(),
            mod.getAffectedElement().getEntityName().equals(expected1));
        mod = interfaceMods.get(1);
        assertTrue("expected: " + expected2 + " found: " + mod.getAffectedElement().getEntityName(),
            mod.getAffectedElement().getEntityName().equals(expected2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateModificationMarks_InvalidInput() {
        String expected = "wrong-frontend";
        List<String> example = new ArrayList<String>();
        example.add(expected);
        // expecting exception here:
        @SuppressWarnings("unused")
        ISModificationRepository result = mmf.createModificationMarks(example);
    }

}
