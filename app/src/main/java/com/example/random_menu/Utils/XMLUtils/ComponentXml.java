package com.example.random_menu.Utils.XMLUtils;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;



@Root(name="component")
public class ComponentXml {
    @Attribute
    public Integer id;
    @Attribute
    public String name;
    @Attribute
    public String comment;
    @Attribute
    public String count;

}
