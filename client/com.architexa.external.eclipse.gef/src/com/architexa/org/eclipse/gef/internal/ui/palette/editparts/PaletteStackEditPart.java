/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.architexa.org.eclipse.gef.internal.ui.palette.editparts;

import com.architexa.org.eclipse.draw2d.ActionEvent;
import com.architexa.org.eclipse.draw2d.ActionListener;
import com.architexa.org.eclipse.draw2d.BorderLayout;
import com.architexa.org.eclipse.draw2d.ButtonBorder;
import com.architexa.org.eclipse.draw2d.ButtonModel;
import com.architexa.org.eclipse.draw2d.ChangeEvent;
import com.architexa.org.eclipse.draw2d.ChangeListener;
import com.architexa.org.eclipse.draw2d.Clickable;
import com.architexa.org.eclipse.draw2d.ColorConstants;
import com.architexa.org.eclipse.draw2d.Figure;
import com.architexa.org.eclipse.draw2d.Graphics;
import com.architexa.org.eclipse.draw2d.IFigure;
import com.architexa.org.eclipse.draw2d.StackLayout;
import com.architexa.org.eclipse.draw2d.geometry.Dimension;
import com.architexa.org.eclipse.draw2d.geometry.Insets;
import com.architexa.org.eclipse.draw2d.geometry.Rectangle;
import com.architexa.org.eclipse.gef.GraphicalEditPart;
import com.architexa.org.eclipse.gef.Request;
import com.architexa.org.eclipse.gef.RequestConstants;
import com.architexa.org.eclipse.gef.palette.PaletteEntry;
import com.architexa.org.eclipse.gef.palette.PaletteListener;
import com.architexa.org.eclipse.gef.palette.PaletteStack;
import com.architexa.org.eclipse.gef.palette.ToolEntry;
import com.architexa.org.eclipse.gef.ui.actions.SetActivePaletteToolAction;
import com.architexa.org.eclipse.gef.ui.palette.PaletteViewer;
import com.architexa.org.eclipse.gef.ui.palette.PaletteViewerPreferences;

import java.beans.PropertyChangeEvent;
import java.util.Iterator;

import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;

import org.eclipse.jface.action.MenuManager;



/**
 * The EditPart for a PaletteStack.
 * 
 * @author Whitney Sorenson
 * @since 3.0
 */
public class PaletteStackEditPart 
	extends PaletteEditPart
{
	
private static final Dimension EMPTY_DIMENSION = new Dimension(0, 0);

// listen to changes of clickable tool figure
private ChangeListener clickableListener = new ChangeListener() {
	public void handleStateChanged(ChangeEvent event) {
		if (event.getPropertyName().equals(ButtonModel.MOUSEOVER_PROPERTY))
			arrowFigure.getModel().setMouseOver(activeFigure.getModel().isMouseOver());
		else if (event.getPropertyName().equals(ButtonModel.ARMED_PROPERTY))
			arrowFigure.getModel().setArmed(activeFigure.getModel().isArmed());
	}
};

// listen to changes of arrow figure
private ChangeListener clickableArrowListener = new ChangeListener() {
	public void handleStateChanged(ChangeEvent event) {
		if (event.getPropertyName().equals(ButtonModel.MOUSEOVER_PROPERTY))
			activeFigure.getModel().setMouseOver(arrowFigure.getModel().isMouseOver());
		if (event.getPropertyName().equals(ButtonModel.ARMED_PROPERTY))
			activeFigure.getModel().setArmed(arrowFigure.getModel().isArmed());
	}
};

// listen to see if arrow is pressed
private ActionListener actionListener = new ActionListener() {
	public void actionPerformed(ActionEvent event) {
		openMenu();		
	}
};

// listen to see if active tool is changed in palette
private PaletteListener paletteListener = new PaletteListener() {
	public void activeToolChanged(PaletteViewer palette, ToolEntry tool) {
		if (getStack().getChildren().contains(tool)) {
			if (!arrowFigure.getModel().isSelected())
				arrowFigure.getModel().setSelected(true);
			if (!getStack().getActiveEntry().equals(tool))
				getStack().setActiveEntry(tool);
		} else
			arrowFigure.getModel().setSelected(false);
	}	
};

private Clickable activeFigure;
private RolloverArrow arrowFigure;
private Figure contentsFigure;
private Menu menu;

/**
 * Creates a new PaletteStackEditPart with the given PaletteStack as its model.
 * 
 * @param model the PaletteStack to associate with this EditPart.
 */
public PaletteStackEditPart(PaletteStack model) {
	super(model);
}

/**
 * @see com.architexa.org.eclipse.gef.EditPart#activate()
 */
public void activate() {
	// in case the model is out of sync
	checkActiveEntrySync();
	getPaletteViewer().addPaletteListener(paletteListener);
	super.activate();
}

/**
 * Called when the active entry has changed.
 * 
 * @param oldValue the old model value (can be null)
 * @param newValue the new model value (can be null)
 */
private void activeEntryChanged(Object oldValue, Object newValue) {
	GraphicalEditPart part = null;
	Clickable clickable = null;

	if (newValue != null) {
		part = (GraphicalEditPart)getViewer().getEditPartRegistry().get(newValue);
		clickable = (Clickable)part.getFigure();
		clickable.setVisible(true);
		clickable.addChangeListener(clickableListener);
		activeFigure = clickable;
	}

	if (oldValue != null) {
		part = (GraphicalEditPart)getViewer().getEditPartRegistry().get(oldValue);
		// if part is null, its no longer a child.
		if (part != null) {
			clickable = (Clickable)part.getFigure();
			clickable.setVisible(false);
			clickable.removeChangeListener(clickableListener);
		}
	}
}

private void checkActiveEntrySync() {
	if (activeFigure == null)
		activeEntryChanged(null, getStack().getActiveEntry());
}

/**
 * @see com.architexa.org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
 */
public IFigure createFigure() {
	Figure figure = new Figure() {
		public Dimension getPreferredSize(int wHint, int hHint) {
			if (PaletteStackEditPart.this.getChildren().isEmpty())
				return EMPTY_DIMENSION;
			return super.getPreferredSize(wHint, hHint);
		}
		
		public void paintBorder(Graphics graphics) {
			int layoutMode = getPreferenceSource().getLayoutSetting();
			if (layoutMode == PaletteViewerPreferences.LAYOUT_LIST
			  || layoutMode == PaletteViewerPreferences.LAYOUT_DETAILS)
				return;
			
			Rectangle rect = getBounds().getCopy();

			graphics.translate(getLocation());
			graphics.setBackgroundColor(ColorConstants.listForeground);
			
			// fill the corner arrow
			int[] points = new int[6];
			
			points[0] = rect.width;
			points[1] = rect.height - 5;
			points[2] = rect.width;
			points[3] = rect.height;
			points[4] = rect.width - 5;
			points[5] = rect.height;
		
			graphics.fillPolygon(points);
			
			graphics.translate(getLocation().getNegated());
		}
	};
	figure.setLayoutManager(new BorderLayout());
	
	contentsFigure = new Figure();
	
	StackLayout stackLayout = new StackLayout();
	// make it so the stack layout does not allow the invisible figures to contribute
	// to its bounds
	stackLayout.setObserveVisibility(true);
	contentsFigure.setLayoutManager(stackLayout);
	
	figure.add(contentsFigure, BorderLayout.CENTER);
	
	arrowFigure = new RolloverArrow();
	
	arrowFigure.addChangeListener(clickableArrowListener);
	
	arrowFigure.addActionListener(actionListener);

	int layoutMode = getPreferenceSource().getLayoutSetting();
	if (layoutMode == PaletteViewerPreferences.LAYOUT_LIST
			|| layoutMode == PaletteViewerPreferences.LAYOUT_DETAILS)
		figure.add(arrowFigure, BorderLayout.RIGHT);
	return figure;
}

/**
 * @see com.architexa.org.eclipse.gef.EditPart#deactivate()
 */
public void deactivate() {
	if (activeFigure != null) 
		activeFigure.removeChangeListener(clickableListener);
	arrowFigure.removeActionListener(actionListener);
	arrowFigure.removeChangeListener(clickableArrowListener);
	getPaletteViewer().removePaletteListener(paletteListener);
	super.deactivate();
}

/**
 * @see com.architexa.org.eclipse.gef.EditPart#eraseTargetFeedback(com.architexa.org.eclipse.gef.Request)
 */
public void eraseTargetFeedback(Request request) {	
	Iterator children = getChildren().iterator();
	
	while (children.hasNext()) {
		PaletteEditPart part = (PaletteEditPart)children.next();
		part.eraseTargetFeedback(request);
	}
	super.eraseTargetFeedback(request);
}

/**
 * @see com.architexa.org.eclipse.gef.GraphicalEditPart#getContentPane()
 */
public IFigure getContentPane() {
	return contentsFigure;
}

private PaletteStack getStack() {
	return (PaletteStack)getModel();
}

/**
 * Opens the menu to display the choices for the active entry.
 */
public void openMenu() {	
	MenuManager menuManager = new MenuManager();
	
	Iterator children = getChildren().iterator();
	PaletteEditPart part = null;
	PaletteEntry entry = null;
	while (children.hasNext()) {
		part = (PaletteEditPart)children.next();
		entry = (PaletteEntry)part.getModel();
		
		menuManager.add(new SetActivePaletteToolAction(getPaletteViewer(), 
				entry.getLabel(), entry.getSmallIcon(), 
				getStack().getActiveEntry().equals(entry), (ToolEntry)entry));
	}
	
	menu = menuManager.createContextMenu(getPaletteViewer().getControl());
	
	// make the menu open below the figure
	Rectangle figureBounds = getFigure().getBounds().getCopy();
	getFigure().translateToAbsolute(figureBounds);
	
	Point menuLocation = getPaletteViewer().getControl().toDisplay(
			figureBounds.getBottomLeft().x, figureBounds.getBottomLeft().y);
	
	// remove feedback from the arrow Figure and children figures
	arrowFigure.getModel().setMouseOver(false);
	eraseTargetFeedback(new Request(RequestConstants.REQ_SELECTION));
	
	menu.setLocation(menuLocation);
	menu.addMenuListener(new StackMenuListener(menu, getViewer().getControl().getDisplay()));
	menu.setVisible(true);
}

/**
 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
 */
public void propertyChange(PropertyChangeEvent event) {
	if (event.getPropertyName().equals(PaletteStack.PROPERTY_ACTIVE_ENTRY))
		activeEntryChanged(event.getOldValue(), event.getNewValue());
	else
		super.propertyChange(event);
}

/**
 * @see com.architexa.org.eclipse.gef.editparts.AbstractEditPart#refreshChildren()
 */
protected void refreshChildren() {
	super.refreshChildren();
	Iterator children = getChildren().iterator();
	while (children.hasNext()) {
		PaletteEditPart editPart = (PaletteEditPart)children.next();
		if (!editPart.getFigure().equals(activeFigure))
			editPart.getFigure().setVisible(false);
	}
}

/**
 * @see com.architexa.org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
 */
protected void refreshVisuals() {
	int layoutMode = getPreferenceSource().getLayoutSetting();
	if (layoutMode == PaletteViewerPreferences.LAYOUT_LIST
			|| layoutMode == PaletteViewerPreferences.LAYOUT_DETAILS) {
		if (!getFigure().getChildren().contains(arrowFigure))
			getFigure().add(arrowFigure, BorderLayout.RIGHT);
	} else if (getFigure().getChildren().contains(arrowFigure))
		getFigure().remove(arrowFigure);
}

/**
 * @see com.architexa.org.eclipse.gef.EditPart#showTargetFeedback(com.architexa.org.eclipse.gef.Request)
 */
public void showTargetFeedback(Request request) {
	// if menu is showing, don't show feedback. this is a fix
	// for the occasion when show is called after forced erase
	if (menu != null && !menu.isDisposed() && menu.isVisible())
		return;
		
	Iterator children = getChildren().iterator();
	while (children.hasNext()) {
		PaletteEditPart part = (PaletteEditPart)children.next();
		part.showTargetFeedback(request);
	}
	
	super.showTargetFeedback(request);
}

}

class StackMenuListener 
	implements MenuListener
{

private Menu menu;
private Display d;

/**
 * Creates a new listener to listen to the menu that it used to select the active tool
 * on a stack. Disposes the stack with an asyncExec after hidden is called.
 */
StackMenuListener(Menu menu, Display d) {
	this.menu = menu;
	this.d = d;
}

/**
 * @see org.eclipse.swt.events.MenuListener#menuShown(org.eclipse.swt.events.MenuEvent)
 */
public void menuShown(MenuEvent e) { }

/**
 * @see org.eclipse.swt.events.MenuListener#menuHidden(org.eclipse.swt.events.MenuEvent)
 */
public void menuHidden(MenuEvent e) {
	d.asyncExec(new Runnable() {
		public void run() {
			if (menu != null) {
				if (!menu.isDisposed())
					menu.dispose();
				menu = null;
			}
		}
	});
}

}

class RolloverArrow
	extends Clickable 
{

/**
 * Creates a new Clickable with a TriangleFigure as its child.
 */
RolloverArrow() {
	super(new TriangleFigure());
	setRolloverEnabled(true);
	setBorder(new ButtonBorder(ButtonBorder.SCHEMES.TOOLBAR));
	setOpaque(false);
	setStyle(Clickable.STYLE_BUTTON);
}

/**
 * Draws a checkered pattern to emulate a toggle button that is in the selected state.
 * @param graphics	The Graphics object used to paint
 */
protected void fillCheckeredRectangle(Graphics graphics) {
	// method taken from ToggleButton - because this figure *isn't* a toggle button,
	// but should match the checkered look of the tool's toggle button
	graphics.setBackgroundColor(ColorConstants.button);
	graphics.setForegroundColor(ColorConstants.buttonLightest);
	Rectangle rect = getClientArea(Rectangle.SINGLETON).crop(new Insets(1, 1, 0, 0));
	graphics.fillRectangle(rect.x, rect.y, rect.width, rect.height);
	
	graphics.clipRect(rect);
	graphics.translate(rect.x, rect.y);
	int n = rect.width + rect.height;
	for (int i = 1; i < n; i += 2) {
		graphics.drawLine(0, i, i, 0);
	}
	graphics.restoreState();
}

/**
 * @return false so that the focus rectangle is not drawn.
 */
public boolean hasFocus() {
	return false;
}

/**
 * @see com.architexa.org.eclipse.draw2d.Figure#paintFigure(Graphics)
 */
protected void paintFigure(Graphics graphics) {
	if (isSelected() && isOpaque())
		fillCheckeredRectangle(graphics);
	else
		super.paintFigure(graphics);
}

}

class TriangleFigure
	extends Figure 
{

/**
 * Creates a new TriangleFigure with preferred size 7, -1
 */
TriangleFigure() {
	super();
	setPreferredSize(7, -1);
}

/**
 * @see com.architexa.org.eclipse.draw2d.IFigure#paint(com.architexa.org.eclipse.draw2d.Graphics)
 */
public void paint(Graphics graphics) {
	Rectangle rect = getBounds().getCopy();

	graphics.translate(getLocation());
	graphics.setBackgroundColor(ColorConstants.listForeground);
	
	// fill the arrow
	int[] points = new int[8];
	
	points[0] = 1;
	points[1] = rect.height / 2;
	points[2] = 6;
	points[3] = rect.height / 2;
	points[4] = 3;
	points[5] = 3 + rect.height / 2;
	points[6] = 1;
	points[7] = rect.height / 2;
	
	graphics.fillPolygon(points);
	
	graphics.translate(getLocation().getNegated());
}

}
