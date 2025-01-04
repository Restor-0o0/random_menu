package com.example.random_menu.Utils.XMLUtils;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;


import java.util.List;



@Root(name="element")
public class Element {
    @Attribute
    public String id;
    @Attribute
    public String name;
    @Attribute
    public String comment;
    @ElementList(name = "component",inline = true,required = false)
    public List<Component> components;
}
