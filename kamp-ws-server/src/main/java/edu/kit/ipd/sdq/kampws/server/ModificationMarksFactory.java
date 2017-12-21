package edu.kit.ipd.sdq.kampws.server;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.palladiosimulator.pcm.repository.Interface;

import edu.kit.ipd.sdq.kamp4is.model.modificationmarks.ISModificationRepository;
import edu.kit.ipd.sdq.kamp4is.model.modificationmarks.ISModificationmarksFactory;
import edu.kit.ipd.sdq.kamp4is.model.modificationmarks.ISModifyInterface;
import edu.kit.ipd.sdq.kamp4is.model.modificationmarks.ISSeedModifications;

/**
 * Provides methods to create ModificationMarks.
 * @author Milena Neumann
 *
 */
public class ModificationMarksFactory {

	ArchitectureVersionPersistency architectureVersionPersistency;
    ISModificationmarksFactory factory;
        
    public ModificationMarksFactory(ArchitectureVersionPersistency architectureVersionPersistency) {
        this.architectureVersionPersistency = architectureVersionPersistency;
        factory = ISModificationmarksFactory.eINSTANCE;
    }

    /**
     * Returns ModificationRepository containing entries corresponding to changed Projects.
     * @param changedProjects List of changed Projects
     * @return ModificationRepository containing entries corresponding to changed Projects
     */
    public ISModificationRepository createModificationMarks(List<String> changedProjects) {        
        ISSeedModifications seed = createSeedModifications(changedProjects);
        ISModificationRepository result = factory.createISModificationRepository();
        result.setSeedModifications(seed);
        return result;
    }

    private ISSeedModifications createSeedModifications(List<String> changedProjects) {
        ISSeedModifications seed = factory.createISSeedModifications();
        EList<ISModifyInterface> modList = seed.getInterfaceModifications();
        
        for(String str : changedProjects) {
            ISModifyInterface mod = factory.createISModifyInterface();
            Interface interf = getCorrespondingInterface(str);
            if(interf == null) {
                throw new IllegalArgumentException("The following name cannot be resolved: " + str);
            }
            mod.setAffectedElement(interf);
            modList.add(mod);
        }
        return seed;
    }

    private Interface getCorrespondingInterface(String interfaceName) {
        EList<Interface> interfaces = architectureVersionPersistency.getArchitectureVersion().getRepository().getInterfaces__Repository();
        for(Interface interf : interfaces) {
            if(interf.getEntityName().equals(interfaceName)) {
                return interf;
            }
        }
        return null;
    }

    
    
}
