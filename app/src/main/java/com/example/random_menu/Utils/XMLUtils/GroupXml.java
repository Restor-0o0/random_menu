package com.example.random_menu.Utils.XMLUtils;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;



@Root(name="group")
public class GroupXml {
    @Attribute
    public Integer id;
    @Attribute
    public String name;
    @Attribute
    public String comment;
    @ElementList(name = "element",inline = true,required = false)
    public List<ElementXml> elements;
}
