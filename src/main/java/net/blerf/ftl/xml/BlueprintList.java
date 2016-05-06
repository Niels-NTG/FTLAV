package net.blerf.ftl.xml;

import javax.xml.bind.annotation.*;
import java.util.List;


@XmlRootElement(name="blueprintList")
@XmlAccessorType(XmlAccessType.FIELD)
public class BlueprintList {

	@XmlAttribute
	private String name;

	@XmlElement(name="name")
	private List<String> items;

}
