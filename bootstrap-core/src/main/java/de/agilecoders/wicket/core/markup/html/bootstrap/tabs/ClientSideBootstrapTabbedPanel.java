package de.agilecoders.wicket.core.markup.html.bootstrap.tabs;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

/**
 * <p>
 * 	A "pure" client side stateless tabs component. You use it as you would use {@link TabbedPanel}, 
 * 	but instead of generating links that trigger server round trips id does generates just "client
 * 	side" links. 
 * </p>
 * 
 * @author Ernesto Reinaldo Barreiro (reiern70@gmailcom)
 */
public class ClientSideBootstrapTabbedPanel<T extends ITab> extends BootstrapTabbedPanel<T> {

    /**
     * Constructor.
     *
     * @param id The component id
     * @param tabs A list of all tabs
     */
    public ClientSideBootstrapTabbedPanel(String id, final List<T> tabs) {
        this(id, tabs, null);
    }

    /**
     * Constructor.
     *
     * @param id  The component id
     * @param tabs A list of all tabs
     * @param activeTabIndexModel The model saying which tab is the current active one
     */
    public ClientSideBootstrapTabbedPanel(String id, final List<T> tabs, IModel<Integer> activeTabIndexModel) {
        super(id, tabs, activeTabIndexModel);

    }

    // creates tabs panel.
    private WebMarkupContainer createTabPanel(String id, T tab, final int tabIndex, final IModel<Integer> activeTabIndexModel, String tabPanelId) {
        WebMarkupContainer tabPanel = new WebMarkupContainer(id);
        tabPanel.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                int activeTab = activeTabIndexModel != null ? activeTabIndexModel.getObject() : 0;
                boolean isActive = (tabIndex == activeTab);
                return "tab" + tabIndex + (isActive ? " active" : "");
            }
        }));
        WebMarkupContainer link = newLink("link", tabIndex);
        tabPanel.add(link);
        link.add(newTitle("title", wrap(tab.getTitle()), tabIndex));
        return tabPanel;
    }

    /**
     * Override to create a different title label.
     *
     * @param id
     * @param title The label title
     * @param tabIndex The index of the tab
     * @return
     */
    @Override
    protected Component newTitle(final String id, IModel<?> title, final int tabIndex) {
        return new Label(id, title);
    }

    /**
     * Override to create a different tab's link.
     *
     * @param id
     * @param tabIndex The index of the tab
     * @return
     */
    @Override
    protected WebMarkupContainer newLink(final String id, final int tabIndex) {
        return new WebMarkupContainer(id) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onComponentTag(final ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("data-toggle", "tab");
                tag.put("href", "#" + findParent(LoopItem.class).getIndex());
            }
        };
    }

    /**
     * Override to create a different tabs content's container.
     *
     * @param id The component id fo the content's container
     * @param tabs
     *@param activeTabIndexModel @return The content's container
     */
    protected WebMarkupContainer newPanel(final String id, List<T> tabs, IModel<Integer> activeTabIndexModel) {
        WebMarkupContainer panel = new WebMarkupContainer(id);
        panel.add(AttributeAppender.append("data-blah", "dryn"));
        RepeatingView panels = new RepeatingView("panels");
        panel.add(panels);

        int tabIndex = 0;
        for (T tab: tabs) {
            if (tab.isVisible()) {
                WebMarkupContainer panel2 = createContentContainer(panels.newChildId(), tab, tabIndex, activeTabIndexModel);
                panels.add(panel2);
                tabIndex++;
            }
        }
        return panel;
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();

        WebMarkupContainer panelsContainer = newPanel(TAB_PANEL_ID, getTabs(), (IModel<Integer>) getDefaultModel());
        addOrReplace(panelsContainer);
    }

    // creates tabs contents panel.
    private WebMarkupContainer createContentContainer(String id, T tab, final int tabIndex, final IModel<Integer> activeTabIndexModel) {
        WebMarkupContainer panel = tab.getPanel(id);
        panel.setRenderBodyOnly(false);
        panel.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                int activeTab = activeTabIndexModel!=null? activeTabIndexModel.getObject():0;
                boolean isActive = (tabIndex == activeTab);
                return "tab" + tabIndex + (isActive?" tab-pane fade in active":" tab-pane fade");
            }
        }));
        panel.setOutputMarkupId(true);
        return panel;
    }

    /**
     * Override to returns a different tabs container.
     *
     * @param id The component id of the tabs container
     * @return The tabs container
     */
    @Override
    protected WebMarkupContainer newTabsContainer(final String id) {
        return new WebMarkupContainer(id) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onComponentTag(final ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("class", getTabContainerCssClass());
            }
        };
    }

    /**
     * Override to return a different CSS class for tabs container.
     * @return The CSS class for tabs container
     */
    @Override
    protected String getTabContainerCssClass() {
        return "nav nav-tabs";
    }

}
