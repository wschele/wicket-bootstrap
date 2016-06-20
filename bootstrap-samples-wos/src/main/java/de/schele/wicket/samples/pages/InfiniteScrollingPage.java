package de.schele.wicket.samples.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import de.schele.wicket.samples.panels.pagination.InfinitePaginationPanel;

/**
 *
 */
@MountPath(value = "/infinitescroll")
public class InfiniteScrollingPage extends WebPage {

    /**
     * Constructor.
     *
     * @param parameters The query parameters
     */
    public InfiniteScrollingPage(final PageParameters parameters) {
        super(parameters);

        add(new InfinitePaginationPanel("infinite"));
    }
}
