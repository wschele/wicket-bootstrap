package de.schele.wicket.samples.pages;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import de.schele.wicket.samples.panels.validation.SimpleFormModal;
import de.schele.wicket.samples.panels.validation.SimpleFormPanel;

/**
 * @author Alexey Volkov
 * @since 08.11.14
 */
public abstract class BaseValidationPage extends BasePage {

    private static final long serialVersionUID = 8101772374853082900L;

    private final SimpleFormModal modal;

    /**
     * @param parameters current page parameters
     */
    public BaseValidationPage(PageParameters parameters) {
        super(parameters);
        add(newHeader("header"));
        final SimpleFormPanel modalContent = newSimpleFormPanel("content");
        modal = new SimpleFormModal("modal", modalContent);
        add(modal);
        add(newSimpleFormPanel("form"));
        add(newSimpleFormPanel("form-ajax").withAjax());
    }

    protected SimpleFormPanel newSimpleFormPanel(String wicketId) {
        return new SimpleFormPanel(wicketId) {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                super.onSubmit(target);
                modal.close(target);
            }
        };
    }

    @Override
    protected boolean hasNavigation() {
        return true;
    }

    protected WebMarkupContainer newHeader(String wicketId){
        return new WebMarkupContainer(wicketId);
    }
}

