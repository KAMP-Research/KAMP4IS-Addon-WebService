package edu.kit.ipd.sdq.kampws.server;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.palladiosimulator.pcm.repository.Interface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;

import edu.kit.ipd.sdq.kamp4is.core.ISArchitectureVersion;
import edu.kit.ipd.sdq.kamp4is.model.fieldofactivityannotations.ISBuildConfiguration;
import edu.kit.ipd.sdq.kamp4is.model.fieldofactivityannotations.ISBuildSpecification;

public class ArchitectureVersionUtils {

    private ArchitectureVersionUtils() {}

    private static RepositoryComponent getRepositoryComponentByEntityName(ISArchitectureVersion version,
        String entityName) {
        EList<RepositoryComponent> components = getRepositoryComponents(version);
        RepositoryComponent result = null;
        for (RepositoryComponent c : components) {
            if (c.getEntityName().equals(entityName)) {
                result = c;
                break;
            }
        }
        if (result == null)
            throw new IllegalArgumentException("Encountered invalid name: " + entityName);
        return result;
    }

    private static EList<RepositoryComponent> getRepositoryComponents(ISArchitectureVersion version) {
        Repository repo = version.getRepository();
        return repo.getComponents__Repository();
    }

    private static EList<Interface> getInterfaces(ISArchitectureVersion version) {
        Repository repo = version.getRepository();
        return repo.getInterfaces__Repository();
    }

    private static EList<ISBuildConfiguration> getBuildConfigurations(ISArchitectureVersion version) {
        ISBuildSpecification specification = version.getFieldOfActivityAnnotationsRepository().getBuildSpecification();
        return specification.getBuildConfigurations();
    }

    public static List<String> getChangeScenarios(ISArchitectureVersion version, String entityName) {
        List<String> result = new ArrayList<String>();
        RepositoryComponent component = getRepositoryComponentByEntityName(version, entityName);
        for (ProvidedRole providedRole : component.getProvidedRoles_InterfaceProvidingEntity()) {
            Interface interf = ((OperationProvidedRole) providedRole).getProvidedInterface__OperationProvidedRole();
            result.add(interf.getEntityName());
        }
        return result;
    }

    public static List<String> getPossibleInterfaceNames(ISArchitectureVersion version) {
        List<String> result = new ArrayList<String>();
        EList<Interface> interfaces = getInterfaces(version);

        for (Interface interf : interfaces) {
            result.add(interf.getEntityName());
        }

        return result;
    }

    public static List<String> getPossibleComponentNames(ISArchitectureVersion version) {
        List<String> result = new ArrayList<String>();
        EList<RepositoryComponent> components = getRepositoryComponents(version);

        for (RepositoryComponent comp : components) {
            result.add(comp.getEntityName());
        }

        return result;
    }

    public static void validateInterfaceEntries(ISArchitectureVersion version, List<String> changedProjects)
        throws IllegalArgumentException {
        List<String> possibleProjectNames = getPossibleInterfaceNames(version);
        for (String entry : changedProjects) {
            if (!possibleProjectNames.contains(entry)) {
                throw new IllegalArgumentException("Encountered invalid name: " + entry);
            }
        }
    }

    public static void validateComponentEntries(ISArchitectureVersion version, List<String> changedProjects)
        throws IllegalArgumentException {
        List<String> possibleProjectNames = getPossibleComponentNames(version);
        for (String entry : changedProjects) {
            if (!possibleProjectNames.contains(entry)) {
                throw new IllegalArgumentException("Encountered invalid name: " + entry);
            }
        }
    }

    /**
     * @param version
     * @param projectNames
     * @return
     */
    public static List<String> getBuildSpecificationPaths(ISArchitectureVersion version, List<String> projectNames) {
        List<String> result = new ArrayList<>();
        EList<ISBuildConfiguration> configs = getBuildConfigurations(version);
        EMap<String, ISBuildConfiguration> configsMap = new BasicEMap<String, ISBuildConfiguration>();
        for (ISBuildConfiguration c : configs) {
            configsMap.put(c.getComponent().get(0).getEntityName(), c);
        }

        for (String name : projectNames) {
            if (!configsMap.containsKey(name)) {
                throw new IllegalArgumentException("Encountered invalid name: " + name);
            }
            ISBuildConfiguration config = configsMap.get(name);
            result.add(config.getFilename());
        }

        return result;
    }

}
