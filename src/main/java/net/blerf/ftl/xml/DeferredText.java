package net.blerf.ftl.xml;


/**
 * An interface for elements with "id" attributes to look up.
 *
 * @see net.blerf.ftl.xml.TextLookupUnmarshalListener
 */
public interface DeferredText {

    /**
     * Returns the "id" attribute value, or null.
     */
    String getTextId();

    /**
     * Sets the looked-up text.
     */
    void setResolvedText(String s);

    /**
     * Returns either the looked-up text or the element's own value.
     * <p>
     * TODO: Test to find out which one FTL prioritizes.
     */
    String getTextValue();
}
