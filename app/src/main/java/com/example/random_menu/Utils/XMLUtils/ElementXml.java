package com.example.random_menu.Utils.XMLUtils;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;


import java.util.List;



@Root(name="element")
public class ElementXml {
    @Attribute
    public Integer id;
    @Attribute
    public String name;
    @Attribute
    public String comment;
    @Attribute
    public Integer priority;
    @ElementList(name = "component",inline = true,required = false)
    public List<ComponentXml> components;
}
