package edu.kit.ipd.sdq.kampws.server;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService(name = "ChangeSpecificDependencies")
@SOAPBinding(style = Style.RPC)
public interface ChangeSpecificDependencies {
	
   /**
    * Returns the list of all possible project names in the loaded architecture version.
    * @return List of all possible project names
    */
   @WebMethod
   List<String> getPossibleProjectNames();
	
   /**
    * Returns list of possible change scenarios for a given project.
    * @param projectName Name of the project
    * @return List of change scenarios
    * @throws IllegalArgumentException When name of the project cannot be matched.
    */
   @WebMethod
   List<String> getChangeScenarios(String projectName) throws IllegalArgumentException;
   
	/**
     * Returns a list of dependent projects for a change scenario.
     * @param changedProjects Modified projects of the scenario
     * @return List of dependent projects
	 * @throws IllegalArgumentException When Base Architecture cannot be parsed due to an illegal project name.
     */
   @WebMethod
	List<String> getChangeSpecificDependencies(List<String> changedProjects) throws IllegalArgumentException;
   
   /**
    * Returns the paths of the build specifications for the specified projects.
    * @param projectNames Names of the projects of interest.
    * @return List of build specification paths
    */
   @WebMethod
   List<String> getBuildSpecificationPaths(List<String> projectNames) throws IllegalArgumentException;

}
