package com.example.random_menu.Utils.XMLUtils;

import com.example.random_menu.Data.Element;
import com.example.random_menu.Data.Group;

import org.simpleframework.xml.core.PersistenceException;

import java.util.List;

public interface InterfaceXmlManager {
    void importGroupsIntoDB(XMLWrapper xmlResult,Integer maxPriority) throws Exception;
    void importElementsIntoDB(XMLWrapper xmlWrapper,Group selectedGroup) throws Exception;
    XMLWrapper xmlToClass(String xmlString) throws Exception;
    String groupsToXml(XMLWrapper xmlResult) throws Exception;
    XMLWrapper exportSelectedGroups(List<Group> SelectesGroups) throws Exception;
    XMLWrapper exportSelectedElements(Group selectedGroup,List<Element> SelectesElements) throws Exception;

}
