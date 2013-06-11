package de.agilecoders.wicket.less;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IReferenceHeaderItem;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A resource reference for Less resources.
 * The resources are filtered (stripped comments and whitespace) if there is registered compressor.
 *
 * @author miha
 * @see org.apache.wicket.settings.IResourceSettings#getCssCompressor()
 */
public class LessResourceReference extends CssResourceReference {
    private static final long serialVersionUID = 1L;

    /**
     * Construct.
     *
     * @param scope mandatory parameter
     * @param name  mandatory parameter
     */
    public LessResourceReference(final Class<?> scope, final String name) {
        this(scope, name, null, null, null);
    }


    /**
     * Construct.
     *
     * @param key  mandatory parameter
     */
    public LessResourceReference(final Key key) {
        super(key);
    }

    /**
     * Construct.
     *
     * @param scope  mandatory parameter
     * @param name   mandatory parameter
     * @param locale resource locale
     * @param style  resource style
     */
    public LessResourceReference(final Class<?> scope, final String name, final Locale locale, final String style, final String variation) {
        super(scope, name, locale, style, variation);
    }

    @Override
    public LessPackageResource getResource() {
        return new LessPackageResource(getScope(), getName(), getLocale(), getStyle(), getVariation(), getImports());
    }

    private List<LessPackageResource> getImports() {
        List<LessPackageResource> imports = new ArrayList<LessPackageResource>();

        Iterable <HeaderItem> lessDependencies = getLessDependencies();
        for (HeaderItem lessDependency : lessDependencies) {

            if (lessDependency instanceof IReferenceHeaderItem) {

                IReferenceHeaderItem referenceHeaderItem = (IReferenceHeaderItem) lessDependency;
                ResourceReference reference = referenceHeaderItem.getReference();
                IResource resource = reference.getResource();

                if (resource instanceof LessPackageResource) {
                    LessPackageResource lessPackageResource = (LessPackageResource) resource;
                    imports.add(lessPackageResource);
                }
            }
        }

        return imports;
    }

    private Iterable<HeaderItem> getLessDependencies() {
        List<HeaderItem> lessDependencies = new ArrayList<HeaderItem>();

        for (HeaderItem dep : super.getDependencies()) {

            if (dep instanceof IReferenceHeaderItem) {

                IReferenceHeaderItem referenceHeaderItem = (IReferenceHeaderItem) dep;
                ResourceReference reference = referenceHeaderItem.getReference();

                if (reference instanceof LessResourceReference) {
                    lessDependencies.add(dep);
                }
            }
        }

        return lessDependencies;
    }
}
