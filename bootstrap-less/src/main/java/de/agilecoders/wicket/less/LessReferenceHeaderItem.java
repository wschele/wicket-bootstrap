package de.agilecoders.wicket.less;

import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IReferenceHeaderItem;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class LessReferenceHeaderItem extends CssReferenceHeaderItem {

    /**
     * Creates a new {@code CSSReferenceHeaderItem}.
     *
     * @param reference      resource reference pointing to the CSS resource
     * @param pageParameters the parameters for this CSS resource reference
     * @param media          the media type for this CSS ("print", "screen", etc.)
     * @param condition      the condition to use for Internet Explorer conditional comments. E.g. "IE 7".
     */
    public LessReferenceHeaderItem(ResourceReference reference, PageParameters pageParameters, String media, String condition) {
        super(reference, pageParameters, media, condition);
    }

    @Override
    public Iterable<? extends HeaderItem> getDependencies() {
        List<HeaderItem> nonLessDependencies = new ArrayList<HeaderItem>();

        for (HeaderItem dep : super.getDependencies()) {

            if (dep instanceof IReferenceHeaderItem) {

                IReferenceHeaderItem referenceHeaderItem = (IReferenceHeaderItem) dep;
                ResourceReference reference = referenceHeaderItem.getReference();

                if (reference instanceof LessResourceReference == false) {
                    nonLessDependencies.add(dep);
                }
            }
        }

        return nonLessDependencies;
    }
}
