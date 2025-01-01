package com.example.random_menu.Utils.XMLUtils;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "root")
public class XMLWrapper {
    @XmlElement(name = "group")
    private List<Group> groups;

}

