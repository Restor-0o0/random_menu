package com.example.random_menu.Utils.XMLUtils;

import android.database.Cursor;
import android.util.Log;


import com.example.random_menu.DB.MainBaseContract;
import com.example.random_menu.Data.Component;
import com.example.random_menu.Data.Element;
import com.example.random_menu.Data.Group;
import com.example.random_menu.Reposetory.InterfaceReposetoryComponents;
import com.example.random_menu.Reposetory.InterfaceReposetoryElements;
import com.example.random_menu.Reposetory.InterfaceReposetoryGroups;
import com.example.random_menu.Reposetory.ReposetoryComponents;
import com.example.random_menu.placeholder.ElemPlaceholderContent;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.PersistenceException;
import org.simpleframework.xml.core.Persister;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class XmlManager implements InterfaceXmlManager {
    private InterfaceReposetoryGroups reposetoryGroups;
    private InterfaceReposetoryElements reposetoryElements;
    private InterfaceReposetoryComponents reposetoryComponents;


    @Inject
    public XmlManager(
            InterfaceReposetoryGroups reposetoryGroups,
            InterfaceReposetoryElements reposetoryElements,
            InterfaceReposetoryComponents reposetoryComponents
    )
    {
        this.reposetoryGroups = reposetoryGroups;
        this.reposetoryElements = reposetoryElements;
        this.reposetoryComponents = reposetoryComponents;
    }
    @Override
    public XMLWrapper exportSelectedElements(Group selectedGroup,List<Element> SelectesElements) throws Exception{
        Group groupItem = selectedGroup;
        if (groupItem != null) {

            XMLWrapper xmlExport = new XMLWrapper();
            List<GroupXml> groups = new ArrayList<>();
            GroupXml xmlGroup = new GroupXml();


            xmlGroup.id = groupItem.id;
            xmlGroup.name = groupItem.name;
            xmlGroup.comment = groupItem.comment;

            List<ElementXml> elements = new ArrayList<>();

            for (Element elem : SelectesElements) {

                ElementXml xmlElem = new ElementXml();

                xmlElem.id = elem.id;
                xmlElem.name = elem.name;
                xmlElem.comment = elem.comment;
                xmlElem.priority = elem.priority;
                List<ComponentXml> components = new ArrayList<>();

                List<Component> componentsList = reposetoryComponents.loadComponentsData(Integer.valueOf(xmlElem.id));
                if (componentsList.size() > 0) {
                    for(Component comp : componentsList){
                        ComponentXml xmlComponent = new ComponentXml();

                        xmlComponent.id = comp.id;
                        xmlComponent.name = comp.name;
                        xmlComponent.comment = comp.comment;
                        xmlComponent.count = comp.quantity;

                        components.add(xmlComponent);
                    }
                }
                xmlElem.components = components;
                elements.add(xmlElem);
            }
            xmlGroup.elements = elements;
            groups.add(xmlGroup);


            xmlExport.groups = groups;
            return xmlExport;
        }
    return new XMLWrapper();

    }

    @Override
    public XMLWrapper exportSelectedGroups(List<Group> SelectesGroups) throws Exception{
        XMLWrapper xmlExport = new XMLWrapper();
        List<GroupXml> groups = new ArrayList<>();
        for (Group currentGroup : SelectesGroups) {
            GroupXml xmlGroup = new GroupXml();

            xmlGroup.id = currentGroup.id;
            xmlGroup.name = currentGroup.name;
            xmlGroup.comment = currentGroup.comment;

            List<ElementXml> elements = new ArrayList<>();

            List<Element> elementsList = reposetoryElements.getAllElements(Integer.valueOf(currentGroup.id));
            if (elementsList.size() > 0) {

                for(Element elem: elementsList) {

                    ElementXml xmlElem = new ElementXml();

                    xmlElem.id = elem.id;
                    xmlElem.name = elem.name;
                    xmlElem.comment = elem.comment;
                    xmlElem.priority = elem.priority;
                    List<ComponentXml> components = new ArrayList<>();

                    List<Component> componentsList = reposetoryComponents.loadComponentsData(Integer.valueOf(xmlElem.id));
                    if (componentsList.size() > 0) {
                        for(Component comp: componentsList) {
                            ComponentXml xmlComponent = new ComponentXml();

                            xmlComponent.id = comp.id;
                            xmlComponent.name = comp.name;
                            xmlComponent.comment = comp.comment;
                            xmlComponent.count = comp.quantity;

                            components.add(xmlComponent);
                        }
                    }
                    xmlElem.components = components;
                    elements.add(xmlElem);
                }

            }
            xmlGroup.elements = elements;
            groups.add(xmlGroup);
        }

        xmlExport.groups = groups;
        return xmlExport;

    }
    @Override
    public String groupsToXml(XMLWrapper xmlResult) throws Exception{

            Serializer serializer = new Persister();
            StringWriter writer = new StringWriter();
            serializer.write(xmlResult, writer);
            //String xml = writer.toString();
            //Log.d("SimpleXML", xml);
            return writer.toString();

    }
    @Override
    public XMLWrapper xmlToClass(String xmlString) throws Exception{

        Serializer serializer = new Persister();
        XMLWrapper root = serializer.read(XMLWrapper.class, xmlString);
        Log.d("RESULTTTT", String.valueOf(root.groups.get(0).name));

        return root;
    }
    @Override
    public void importGroupsIntoDB(XMLWrapper xmlResult,Integer maxPriority) throws Exception{
        int groupID,elemID;
        if(xmlResult.groups != null) {
            for (GroupXml group : xmlResult.groups) {
                groupID = (int) reposetoryGroups.add(group.name, group.comment,maxPriority+1);
                maxPriority+=1;
                if (group.elements != null) {


                    for (ElementXml elem : group.elements) {
                        elemID = (int) reposetoryElements.add(elem.name, elem.comment, Integer.valueOf(elem.priority));
                        reposetoryElements.addGroupLink(elemID, groupID);
                        if (elem.components != null) {
                            for (ComponentXml component : elem.components) {
                                reposetoryComponents.addComponent(
                                        Integer.valueOf(elem.id),
                                        component.name,
                                        component.comment,
                                        component.count);
                            }
                        }

                    }
                }
            }
        }

    }
    @Override
    public void importElementsIntoDB(XMLWrapper xmlWrapper,Group selectedGroup) throws Exception{
        int groupID = selectedGroup.id;
        int elemID;
        if(xmlWrapper.groups != null) {
            for (GroupXml group : xmlWrapper.groups) {
                if (group.elements != null) {
                    for (ElementXml elem : group.elements) {
                        elemID = (int) reposetoryElements.add(elem.name, elem.comment, Integer.valueOf(elem.priority));
                        reposetoryElements.addGroupLink(elemID, groupID);
                        if (elem.components != null) {
                            for (ComponentXml component : elem.components) {
                                reposetoryComponents.addComponent(
                                        Integer.valueOf(elem.id),
                                        component.name,
                                        component.comment,
                                        component.count);
                            }
                        }

                    }
                }
            }
        }


    }
}
