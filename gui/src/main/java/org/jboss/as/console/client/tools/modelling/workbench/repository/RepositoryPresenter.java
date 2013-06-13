/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.jboss.as.console.client.tools.modelling.workbench.repository;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import org.jboss.as.console.client.tools.modelling.workbench.ActivateEvent;
import org.jboss.as.console.client.tools.modelling.workbench.PassivateEvent;
import org.jboss.as.console.client.tools.modelling.workbench.ReifyEvent;
import org.jboss.as.console.client.tools.modelling.workbench.ResetEvent;
import org.jboss.as.console.mbui.marshall.Marshaller;
import org.jboss.ballroom.client.widgets.window.DefaultWindow;
import org.jboss.dmr.client.ModelNode;

/**
 * Lists the available interaction units and let the user create new interaction units.
 *
 * Events fired:
 * <ul>
 *     <li>Reify</li>
 * </ul>
 *
 * @author Harald Pehl
 * @date 10/30/2012
 */
public class RepositoryPresenter extends PresenterWidget<RepositoryPresenter.MyView>
{

    public interface MyView extends View
    {
        void setPresenter(RepositoryPresenter presenter);
    }


    @Inject
    public RepositoryPresenter(final EventBus eventBus, final MyView view)
    {
        super(eventBus, view);
    }

    @Override
    protected void onBind()
    {
        super.onBind();
        getView().setPresenter(this);
    }

    public void visualize(final Sample sample)
    {
        DialogVisualization visualization = new DialogVisualization(sample.getDialog());
        DefaultWindow window = new DefaultWindow("Dialog: "+sample.getDialog().getId());
        window.setWidth(800);
        window.setHeight(600);
        window.setModal(true);
        window.setWidget(new ScrollPanel(visualization.getChart()));
        window.center();
    }

    public void marshall(final Sample sample)
    {
        Marshaller m = new Marshaller();
        ModelNode node = m.marshall(sample.getDialog());


        HTMLPanel htmlPanel = new HTMLPanel("<pre>"+node.toString()+"</pre>");
        htmlPanel.setStyleName("fill-layout-width");

        DefaultWindow window = new DefaultWindow("Wireformat: "+sample.getDialog().getId());
        window.setWidth(800);
        window.setHeight(600);
        window.setModal(true);
        window.setWidget(new ScrollPanel(htmlPanel));
        window.center();
    }

    public void reify(final Sample sample)
    {
        ReifyEvent.fire(this, new ReifyEvent(sample));
    }

    public void activate(final Sample sample)
    {
        ActivateEvent.fire(this, new ActivateEvent(sample));
    }

    public void reset(Sample sample) {
        ResetEvent.fire(this, new ResetEvent(sample));
    }

    public void passivate(Sample sample) {
        PassivateEvent.fire(this, new PassivateEvent(sample));
    }
}
