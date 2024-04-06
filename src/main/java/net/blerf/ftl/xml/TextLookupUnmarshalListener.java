package net.blerf.ftl.xml;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;


/**
 * Substitutes string values using an existing lookup Map.
 * <p>
 * After each DeferredText field is unmarshalled, this will call
 * setResolvedText() on it, unescaping newlines in the process.
 *
 * @see net.blerf.ftl.xml.DeferredText
 */
@Slf4j
public class TextLookupUnmarshalListener extends Unmarshaller.Listener {

    private final Map<String, String> lookupMap;

    public TextLookupUnmarshalListener() {
        lookupMap = new HashMap<>();
    }

    /**
     * After construction, get this Map and put entries into it.
     */
    public Map<String, String> getLookupMap() {
        return lookupMap;
    }

    @Override
    public void afterUnmarshal(Object target, Object parent) {

        if (target instanceof DeferredText) {
            DeferredText deferredText = (DeferredText) target;

            if (deferredText.getTextId() != null) {
                String textId = deferredText.getTextId();

                if (lookupMap.containsKey(textId)) {
                    String resolvedText = lookupMap.get(textId).replace("\\n", "\n");
                    deferredText.setResolvedText(resolvedText);
                    log.debug("found deferredText {}", deferredText);
                } else {
                    log.warn("Text lookup failed for id: {}", textId);
                }
            }
        }
    }
}
