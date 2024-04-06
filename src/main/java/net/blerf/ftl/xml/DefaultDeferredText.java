package net.blerf.ftl.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;


@XmlAccessorType(XmlAccessType.FIELD)
public class DefaultDeferredText implements DeferredText {

    @XmlValue
    private final String ownText;

    @XmlAttribute(name = "id")
    private final String textId = null;

    @XmlTransient
    private String resolvedText = null;


    public DefaultDeferredText() {
        this("");
    }

    public DefaultDeferredText(String ownText) {
        this.ownText = ownText;
    }

    /**
     * Returns the "id" attribute value, or null.
     */
    @Override
    public String getTextId() {
        return textId;
    }

    /**
     * Sets the looked-up text.
     */
    public void setResolvedText(String s) {
        resolvedText = s;
    }

    /**
     * Returns either the looked-up text or the element's own value.
     */
    @Override
    public String getTextValue() {
        return (resolvedText != null ? resolvedText : ownText);
    }

    @Override
    public String toString() {
        return getTextValue();
    }
}
