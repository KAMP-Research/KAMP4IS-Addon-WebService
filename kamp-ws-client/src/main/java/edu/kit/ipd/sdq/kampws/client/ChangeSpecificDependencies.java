package edu.kit.ipd.sdq.kampws.client;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 3.0.3
 * 2017-10-26T11:10:56.039+02:00
 * Generated source version: 3.0.3
 * 
 */
@WebService(targetNamespace = "http://server.kampws.sdq.ipd.kit.edu/", name = "ChangeSpecificDependencies")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface ChangeSpecificDependencies {

    @WebMethod
    @WebResult(name = "return", targetNamespace = "http://server.kampws.sdq.ipd.kit.edu/", partName = "return")
    public StringArray getBuildSpecificationPaths(
        @WebParam(partName = "arg0", name = "arg0")
        StringArray arg0
    ) throws IllegalArgumentException_Exception;

    @WebMethod
    @WebResult(name = "return", targetNamespace = "http://server.kampws.sdq.ipd.kit.edu/", partName = "return")
    public StringArray getChangeSpecificDependencies(
        @WebParam(partName = "arg0", name = "arg0")
        StringArray arg0
    ) throws IllegalArgumentException_Exception;

    @WebMethod
    @WebResult(name = "return", targetNamespace = "http://server.kampws.sdq.ipd.kit.edu/", partName = "return")
    public StringArray getChangeScenarios(
        @WebParam(partName = "arg0", name = "arg0")
        java.lang.String arg0
    ) throws IllegalArgumentException_Exception;

    @WebMethod
    @WebResult(name = "return", targetNamespace = "http://server.kampws.sdq.ipd.kit.edu/", partName = "return")
    public StringArray getPossibleProjectNames();
}