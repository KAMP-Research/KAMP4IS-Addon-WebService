package edu.kit.ipd.sdq.kampws.server;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import javax.jws.WebService;

import org.apache.log4j.Logger;

import edu.kit.ipd.sdq.kamp4is.core.ISAbstractChangePropagationAnalysis;
import edu.kit.ipd.sdq.kamp4is.core.ISArchitectureVersion;
import edu.kit.ipd.sdq.kamp4is.core.ISChangePropagationAnalysis;
import edu.kit.ipd.sdq.kamp4is.model.modificationmarks.ISChangePropagationDueToDataDependencies;
import edu.kit.ipd.sdq.kamp4is.model.modificationmarks.ISModificationRepository;

@WebService(endpointInterface = "edu.kit.ipd.sdq.kampws.server.ChangeSpecificDependencies")
public class ChangeSpecificDependenciesImpl implements ChangeSpecificDependencies {
    private ArchitectureVersionPersistency architectureVersionPersistency;
    private ISAbstractChangePropagationAnalysis<ISArchitectureVersion, ISChangePropagationDueToDataDependencies> propagationAnalysis;
    private ModificationMarksFactory modificationMarksFactory;

    final static Logger LOGGER = Logger.getLogger(ChangeSpecificDependenciesImpl.class);

    public ChangeSpecificDependenciesImpl() throws FileNotFoundException {
        LOGGER.debug("Initializing ChangeSpecificDependencies");
        architectureVersionPersistency = new ArchitectureVersionPersistency();
        modificationMarksFactory = new ModificationMarksFactory(architectureVersionPersistency);
        propagationAnalysis = new ISChangePropagationAnalysis();
    }

    /*
     * (non-Javadoc)
     * @see edu.kit.ipd.sdq.kampws.server.ChangeSpecificDependencies#
     * getChangeSpecificDependencies(java.util.List)
     */
    @Override
    public List<String> getChangeSpecificDependencies(List<String> changedProjects) throws IllegalArgumentException {

        // step 1: get base architecture
        ISArchitectureVersion version = architectureVersionPersistency.getArchitectureVersion();

        // step 1.1: check input
        ArchitectureVersionUtils.validateInterfaceEntries(version, changedProjects);

        // step 2: add the change scenario as modification marks
        ISModificationRepository modifications = modificationMarksFactory.createModificationMarks(changedProjects);
        version.setInternalModificationRepository(modifications);

        // step 3: execute KAMP on the version
        propagationAnalysis.runChangePropagationAnalysis(version);

        // step 4: generate response from modified modification marks
        List<String> response = ModificationMarksFilter
            .filterModifiedComponents((ISModificationRepository) version.getModificationRepository());

        // restore version, because the ChangePropagationAnalysis corrupts it
        architectureVersionPersistency.reloadArchitectureVersion();

        return response;
    }

    /*
     * (non-Javadoc)
     * @see edu.kit.ipd.sdq.kampws.server.ChangeSpecificDependencies#
     * getChangeScenarios(java.util.List)
     */
    @Override
    public List<String> getChangeScenarios(String projectName) throws IllegalArgumentException {
        ISArchitectureVersion version = architectureVersionPersistency.getArchitectureVersion();
        ArchitectureVersionUtils.validateComponentEntries(version, Arrays.asList(projectName));
        List<String> response = ArchitectureVersionUtils.getChangeScenarios(version, projectName);
        return response;
    }

    /*
     * (non-Javadoc)
     * @see edu.kit.ipd.sdq.kampws.server.ChangeSpecificDependencies#
     * getPossibleProjectNames()
     */
    @Override
    public List<String> getPossibleProjectNames() {
        ISArchitectureVersion version = architectureVersionPersistency.getArchitectureVersion();
        List<String> response = ArchitectureVersionUtils.getPossibleComponentNames(version);
        return response;
    }

    /*
     * (non-Javadoc)
     * @see edu.kit.ipd.sdq.kampws.server.ChangeSpecificDependencies#
     * getBuildSpecificationPaths(java.util.List)
     */
    @Override
    public List<String> getBuildSpecificationPaths(List<String> projectNames) throws IllegalArgumentException {
        ISArchitectureVersion version = architectureVersionPersistency.getArchitectureVersion();
        ArchitectureVersionUtils.validateComponentEntries(version, projectNames);
        List<String> response = ArchitectureVersionUtils.getBuildSpecificationPaths(version, projectNames);
        return response;
    }

}
