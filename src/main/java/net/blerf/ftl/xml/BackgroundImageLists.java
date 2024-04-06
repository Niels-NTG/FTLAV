package net.blerf.ftl.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "imageLists")
@XmlAccessorType(XmlAccessType.FIELD)
public class BackgroundImageLists {

    @XmlElement(name = "imageList")
    private List<BackgroundImageList> imageLists = new ArrayList<>();
}
