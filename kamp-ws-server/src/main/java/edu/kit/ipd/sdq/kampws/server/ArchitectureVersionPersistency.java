package edu.kit.ipd.sdq.kampws.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryPackage;
import org.palladiosimulator.pcm.repository.util.RepositoryResourceFactoryImpl;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.system.SystemPackage;
import org.palladiosimulator.pcm.system.util.SystemResourceFactoryImpl;

import de.uka.ipd.sdq.componentInternalDependencies.ComponentInternalDependenciesPackage;
import de.uka.ipd.sdq.componentInternalDependencies.ComponentInternalDependencyRepository;
import de.uka.ipd.sdq.componentInternalDependencies.util.ComponentInternalDependenciesResourceFactoryImpl;
import edu.kit.ipd.sdq.kamp4is.core.ISArchitectureVersion;
import edu.kit.ipd.sdq.kamp4is.model.fieldofactivityannotations.ISFieldOfActivityAnnotationsPackage;
import edu.kit.ipd.sdq.kamp4is.model.fieldofactivityannotations.ISFieldOfActivityAnnotationsRepository;
import edu.kit.ipd.sdq.kamp4is.model.fieldofactivityannotations.util.ISFieldOfActivityAnnotationsResourceFactoryImpl;
import edu.kit.ipd.sdq.kamp4is.model.modificationmarks.ISModificationmarksPackage;

public class ArchitectureVersionPersistency {
    public static final String FILEEXTENSION_REPOSITORY = "repository";
    public static final String FILEEXTENSION_SYSTEM = "system";
    public static final String FILEEXTENSION_FIELDOFACTIVITYANNOTATIONS = "fieldofactivityannotations";
    public static final String FILEEXTENSION_INTERNALMODIFICATIONMARK = "modificationmarks";
    public static final String FILEEXTENSION_COMPONENTINTERNALDEPENDENCIES = "componentinternaldependencies";

    final static Logger LOGGER = Logger.getLogger(ArchitectureVersionPersistency.class);

    private Properties prop = new Properties();

    private String folderPath;
    private String name;

    private ISArchitectureVersion architectureVersion;

    public ArchitectureVersionPersistency(String folderPath, String name) throws FileNotFoundException {
        LOGGER.debug("Called Base Architecture Constructor with Parameters " + folderPath + " and " + name + ".");
        loadMetaClassPackages();
        registerExtensions();
        File file = Paths.get(folderPath).toAbsolutePath().normalize().toFile();
        if (!file.exists() || !file.isDirectory()) {
            throw new FileNotFoundException("Invalid Path: " + file.getAbsolutePath());
        }
        loadArchitectureVersion(URI.createFileURI(file.getAbsolutePath()), name);
    }

    public ArchitectureVersionPersistency() throws FileNotFoundException {
        loadPropertiesFile();
        loadMetaClassPackages();
        registerExtensions();
        File file = Paths.get(folderPath).toAbsolutePath().normalize().toFile();
        if (!file.exists() || !file.isDirectory()) {
            throw new FileNotFoundException("Invalid Path: " + file.getAbsolutePath());
        }
        loadArchitectureVersion(URI.createFileURI(file.getAbsolutePath()), name);
    }

    private void loadPropertiesFile() {
        InputStream input = null;

        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            input = classLoader.getResourceAsStream("config.properties");

            // load a properties file
            prop.load(input);

            name = prop.getProperty("architecture.version.name");
            folderPath = prop.getProperty("architecture.version.folder");

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void loadMetaClassPackages() {
        RepositoryPackage.eINSTANCE.eClass();
        SystemPackage.eINSTANCE.eClass();
        ISFieldOfActivityAnnotationsPackage.eINSTANCE.eClass();
        ComponentInternalDependenciesPackage.eINSTANCE.eClass();
        ISModificationmarksPackage.eINSTANCE.eClass();
    }

    private void registerExtensions() {
        Map<String, Object> extensionToFactoryMap = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap();
        extensionToFactoryMap.put(FILEEXTENSION_REPOSITORY, new RepositoryResourceFactoryImpl());
        extensionToFactoryMap.put(FILEEXTENSION_SYSTEM, new SystemResourceFactoryImpl());
        extensionToFactoryMap.put(FILEEXTENSION_FIELDOFACTIVITYANNOTATIONS,
            new ISFieldOfActivityAnnotationsResourceFactoryImpl());
        extensionToFactoryMap.put(FILEEXTENSION_COMPONENTINTERNALDEPENDENCIES,
            new ComponentInternalDependenciesResourceFactoryImpl());
    }

    public void reloadArchitectureVersion() {
        File file = Paths.get(folderPath).toAbsolutePath().normalize().toFile();
        loadArchitectureVersion(URI.createFileURI(file.getAbsolutePath()), name);
    }

    public void loadArchitectureVersion(URI folderPath, String name) {
        String repositoryFilePath = name + "." + FILEEXTENSION_REPOSITORY;
        String systemFilePath = name + "." + FILEEXTENSION_SYSTEM;
        String foaaFilePath = name + "." + FILEEXTENSION_FIELDOFACTIVITYANNOTATIONS;
        String cideFilePath = name + "." + FILEEXTENSION_COMPONENTINTERNALDEPENDENCIES;

        ResourceSet resourceSet = new ResourceSetImpl();
        Repository repository = (Repository) loadEmfModelFromFile(folderPath, repositoryFilePath, resourceSet);
        System system = (System) loadEmfModelFromFile(folderPath, systemFilePath, resourceSet);
        ISFieldOfActivityAnnotationsRepository fieldOfActivityRepository = (ISFieldOfActivityAnnotationsRepository) loadEmfModelFromFile(
            folderPath, foaaFilePath, resourceSet);
        ComponentInternalDependencyRepository componentInternalDependencyRepository = (ComponentInternalDependencyRepository) loadEmfModelFromFile(
            folderPath, cideFilePath, resourceSet);

        EcoreUtil.resolveAll(resourceSet);
        architectureVersion = new ISArchitectureVersion(name, repository, system, fieldOfActivityRepository, null,
            componentInternalDependencyRepository);
    }

    private EObject loadEmfModelFromFile(URI loadURI, String filePath, ResourceSet resourceSet) {
        if (filePath != null)
            loadURI = loadURI.appendSegment(filePath);
        try {
            Resource resource = resourceSet.createResource(loadURI);

            // ((ResourceImpl) resource).setIntrinsicIDToEObjectMap(new
            // HashMap<String, EObject>());
            Map<Object, Object> loadOptions = setupLoadOptions(resource);
            resource.load(loadOptions);
            if (!resource.getContents().isEmpty()) {
                return resource.getContents().get(0);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    private static Map<Object, Object> setupLoadOptions(Resource resource) {
        Map<Object, Object> loadOptions = ((XMLResourceImpl) resource).getDefaultLoadOptions();
        loadOptions.put(XMLResource.OPTION_DEFER_ATTACHMENT, Boolean.TRUE);
        loadOptions.put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);
        loadOptions.put(XMLResource.OPTION_USE_DEPRECATED_METHODS, Boolean.TRUE);
        loadOptions.put(XMLResource.OPTION_USE_PARSER_POOL, new XMLParserPoolImpl());
        loadOptions.put(XMLResource.OPTION_USE_XML_NAME_TO_FEATURE_MAP, new HashMap<Object, Object>());
        return loadOptions;
    }

    public ISArchitectureVersion getArchitectureVersion() {
        return architectureVersion;
    }

}
