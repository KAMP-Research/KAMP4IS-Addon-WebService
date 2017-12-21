package edu.kit.ipd.sdq.kampws.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.eclipse.emf.common.util.EList;

import edu.kit.ipd.sdq.kamp4is.model.modificationmarks.ISChangePropagationDueToInterfaceDependencies;
import edu.kit.ipd.sdq.kamp4is.model.modificationmarks.ISChangePropagationStep;
import edu.kit.ipd.sdq.kamp4is.model.modificationmarks.ISIntracomponentPropagation;
import edu.kit.ipd.sdq.kamp4is.model.modificationmarks.ISModificationRepository;
import edu.kit.ipd.sdq.kamp4is.model.modificationmarks.ISModifyComponent;

/**
 * Provides methods to filter Modification Marks Repositories for specific entries.
 * @author Milena Neumann
 *
 */
public class ModificationMarksFilter {

	/**
	 * Filters for the names of the modified Components in the Modification Repository.
	 * @param ModificationRepository Modification Repository to be filtered for changed Components names
	 * @return List of names of components marked as modified
	 */
    public static List<String> filterModifiedComponents(ISModificationRepository modificationMarks) {
        HashSet<String> resultSet = new HashSet<String>();
        EList<ISChangePropagationStep> modificationSteps = modificationMarks.getISChangePropagationSteps();
        for(ISChangePropagationStep step : modificationSteps) {            	
            
            if(step instanceof ISChangePropagationDueToInterfaceDependencies) {
                ISChangePropagationDueToInterfaceDependencies cpid = (ISChangePropagationDueToInterfaceDependencies) step;
                EList<ISModifyComponent> mods = cpid.getComponentModifications();            
                for(ISModifyComponent mod : mods) {
                    resultSet.add(mod.getAffectedElement().getEntityName());
                }
            } else if (step instanceof ISIntracomponentPropagation) {
                ISIntracomponentPropagation icp = (ISIntracomponentPropagation) step;
                EList<ISModifyComponent> mods = icp.getComponentModifications();
                for(ISModifyComponent mod : mods) {
                	
                    resultSet.add(mod.getAffectedElement().getEntityName());
                }
            }
        }    
        
        return new ArrayList<String>(resultSet);
    }

}
