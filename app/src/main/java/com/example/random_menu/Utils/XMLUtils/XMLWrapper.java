package com.example.random_menu.Utils.XMLUtils;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;


@Root(name = "root")
public class XMLWrapper {
    @ElementList(name = "group",inline = true,required = false)
    public List<Group> groups;
}

