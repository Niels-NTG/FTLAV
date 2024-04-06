package net.blerf.ftl.xml.ship;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * An XmlAdapter for ShipChassis Explosions.
 * <p>
 * Explosion tags wrap a sequence of gibs. Gib tags have an irksome suffix tacked on
 * (gib1, gib2, gib3, ...).
 *
 * @see ShipChassis
 */
public class ExplosionAdapter extends XmlAdapter<Object, ShipChassis.Explosion> {

    private DocumentBuilder documentBuilder;
    private JAXBContext jaxbContext;


    public ExplosionAdapter() {
    }

    public ExplosionAdapter(JAXBContext jaxbContext) {
        this();
        this.jaxbContext = jaxbContext;
    }


    private DocumentBuilder getDocumentBuilder() throws Exception {
        if (documentBuilder == null) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            documentBuilder = dbf.newDocumentBuilder();
        }
        return documentBuilder;
    }

    private JAXBContext getJAXBContext(Class<?> type) throws Exception {
        if (jaxbContext == null) {
            return JAXBContext.newInstance(type);
        }
        return jaxbContext;
    }

    @Override
    public Object marshal(ShipChassis.Explosion explosion) throws Exception {
        if (explosion == null) return null;

        Document explosionDoc = getDocumentBuilder().newDocument();
        Element explosionElement = explosionDoc.createElement("explosion");
        explosionDoc.appendChild(explosionElement);

        for (int i = 0; i < explosion.gibs.size(); i++) {
            Document childDoc = getDocumentBuilder().newDocument();

            QName childRoot = new QName(String.format("gib%d", i + 1));

            @SuppressWarnings("unchecked")
            JAXBElement<ShipChassis.Gib> jaxbElement = new JAXBElement(childRoot, ShipChassis.Gib.class, explosion.gibs.get(i));

            Marshaller marshaller = getJAXBContext(ShipChassis.Gib.class).createMarshaller();
            marshaller.marshal(jaxbElement, explosionElement);
        }

        return explosionDoc.getDocumentElement();
    }

    @Override
    public ShipChassis.Explosion unmarshal(Object elementObj) throws Exception {
        if (elementObj == null || elementObj instanceof Element == false) return null;

        @SuppressWarnings("unchecked")
        Element element = (Element) elementObj;

        ShipChassis.Explosion explosion = new ShipChassis.Explosion();

        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node childNode = children.item(i);

            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                @SuppressWarnings("unchecked")
                Element childElement = (Element) childNode;

                if (childElement.getLocalName().matches("^gib[0-9]*$")) {

                    DOMSource source = new DOMSource(childElement);
                    Unmarshaller unmarshaller = getJAXBContext(ShipChassis.Gib.class).createUnmarshaller();
                    JAXBElement<ShipChassis.Gib> jaxbElement = unmarshaller.unmarshal(source, ShipChassis.Gib.class);

                    explosion.gibs.add(jaxbElement.getValue());
                }
            }
        }

        return explosion;
    }
}
