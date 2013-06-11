package de.agilecoders.wicket.less;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;

	public HomePage(final PageParameters parameters) {
		super(parameters);

		add(new Label("version", getApplication().getFrameworkSettings().getVersion()));

		// TODO Add your page's components here

    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(new LessReferenceHeaderItem(new RootLessReference(), null, null, null));
    }

    private static class RootLessReference extends LessResourceReference {

        public RootLessReference() {
            super(HomePage.class, "resources/root.less");
        }

        @Override
        public Iterable<? extends HeaderItem> getDependencies() {
            List<HeaderItem> dependencies = new ArrayList<HeaderItem>();
            dependencies.add(CssHeaderItem.forReference(new CssResourceReference(HomePage.class, "resources/mixins.less")));
            return dependencies;
        }
    }

}
