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

import com.architexa.org.eclipse.draw2d.ColorConstants;
import com.architexa.org.eclipse.draw2d.FocusEvent;
import com.architexa.org.eclipse.draw2d.FocusListener;
import com.architexa.org.eclipse.draw2d.IFigure;
import com.architexa.org.eclipse.draw2d.RangeModel;
import com.architexa.org.eclipse.draw2d.geometry.Insets;
import com.architexa.org.eclipse.gef.AccessibleEditPart;
import com.architexa.org.eclipse.gef.ExposeHelper;
import com.architexa.org.eclipse.gef.MouseWheelHelper;
import com.architexa.org.eclipse.gef.editparts.ViewportExposeHelper;
import com.architexa.org.eclipse.gef.editparts.ViewportMouseWheelHelper;
import com.architexa.org.eclipse.gef.internal.InternalImages;
import com.architexa.org.eclipse.gef.palette.PaletteDrawer;
import com.architexa.org.eclipse.gef.palette.PaletteTemplateEntry;
import com.architexa.org.eclipse.gef.ui.palette.PaletteViewerPreferences;

import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IMemento;



/**
 * EditPart for a PaletteDrawer
 * 
 * @author Pratik Shah
 */
public class DrawerEditPart 
	extends PaletteEditPart
{
	
private static final String PROPERTY_EXPANSION_STATE = "expansion"; //$NON-NLS-1$
private static final String PROPERTY_PINNED_STATE = "pinned"; //$NON-NLS-1$

/**
 * Constructor
 * 
 * @param drawer	The PaletteDrawer that this EditPart is representing
 */
public DrawerEditPart(PaletteDrawer drawer) {
	super(drawer);
}

/**
 * @see com.architexa.org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
 */
public IFigure createFigure() {
	DrawerFigure fig = new DrawerFigure(getViewer().getControl()) {
		IFigure buildTooltip() {
			return createToolTip();
		}
	};
	fig.setExpanded(getDrawer().isInitiallyOpen());
	fig.setPinned(getDrawer().isInitiallyPinned());

	fig.getCollapseToggle().addFocusListener(new FocusListener.Stub() {
		public void focusGained(FocusEvent fe) {
			getViewer().select(DrawerEditPart.this);
		}
	});
	
	return fig;
}

/**
 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(Class)
 */
public Object getAdapter(Class key) {
	if (key == ExposeHelper.class) {
		ViewportExposeHelper helper = new ViewportExposeHelper(this);
		helper.setMinimumFrameCount(6);
		helper.setMargin(new Insets(PaletteScrollBar.BUTTON_HEIGHT, 0, 
				PaletteScrollBar.BUTTON_HEIGHT, 0));
		return helper;
	}
	if (key == MouseWheelHelper.class)
		return new ViewportMouseWheelHelper(this);
	return super.getAdapter(key);
}

private PaletteAnimator getPaletteAnimator() {
	return (PaletteAnimator)getViewer()
			.getEditPartRegistry()
			.get(PaletteAnimator.class);
}

/**
 * Convenience method that provides access to the PaletteDrawer that is the model.
 * @return The model PaletteDrawer
 */
public PaletteDrawer getDrawer() {
	return (PaletteDrawer)getPaletteEntry();
}

/**
 * Convenience method to get the DrawerFigure for the model drawer.
 * 
 * @return The DrawerFigure created in {@link #createFigure()}
 */
public DrawerFigure getDrawerFigure() {
	return (DrawerFigure)getFigure();
}

/**
 * @see com.architexa.org.eclipse.gef.GraphicalEditPart#getContentPane()
 */
public IFigure getContentPane() {
	return getDrawerFigure().getContentPane();
}

/**
 * Returns the expansion state of the drawer
 * @return <code>true</code> if the drawer is expanded; false otherwise
 */
public boolean isExpanded() {
	return getDrawerFigure().isExpanded();
}

/**
 * Returns <code>true</code> if the drawer is pinned open.
 * @return boolean
 */
public boolean isPinnedOpen() {
	return getDrawerFigure().isPinnedOpen();
}

/**
 * @see com.architexa.org.eclipse.gef.internal.ui.palette.editparts.PaletteEditPart#nameNeededInToolTip()
 */
protected boolean nameNeededInToolTip() {
	return false;
}

/**
 * @return <code>true</code> if the DrawerFigure can be pinned open.  This is only true
 * when the drawer is expanded and the auto-collapse strategy is
 * <code>PaletteViewerPreferences.COLLAPSE_AS_NEEDED</code>.
 */
public boolean canBePinned() {
	return getDrawerFigure().isPinShowing();
}

protected AccessibleEditPart createAccessible() {
	return new AccessibleGraphicalEditPart() {
		public void getDescription(AccessibleEvent e) {
			e.result = getPaletteEntry().getDescription();
		}

		public void getName(AccessibleEvent e) {
			e.result = getPaletteEntry().getLabel();
		}

		public void getRole(AccessibleControlEvent e) {
			e.detail = ACC.ROLE_TREE;
		}

		public void getState(AccessibleControlEvent e) {
			super.getState(e);
			e.detail |= isExpanded() ? ACC.STATE_EXPANDED : ACC.STATE_COLLAPSED;
		}
	};
}

/**
 * @see com.architexa.org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
 */
protected void refreshVisuals() {	
	getDrawerFigure().setToolTip(createToolTip());

	ImageDescriptor img = getPaletteEntry().getSmallIcon();
	if (img == null) {
		img = InternalImages.DESC_FOLDER_OPEN;
	}
	setImageDescriptor(img);

	getDrawerFigure().setTitle(getPaletteEntry().getLabel());
	getDrawerFigure().setLayoutMode(getPreferenceSource().getLayoutSetting());

	boolean showPin = getPreferenceSource().getAutoCollapseSetting()
					== PaletteViewerPreferences.COLLAPSE_AS_NEEDED;
	getDrawerFigure().showPin(showPin);

	Color background = getDrawer().getDrawerType().equals(
		PaletteTemplateEntry.PALETTE_TYPE_TEMPLATE) ? ColorConstants.listBackground : null;
	getDrawerFigure().getScrollpane().setBackgroundColor(background);
}

/**
 * @see com.architexa.org.eclipse.gef.editparts.AbstractEditPart#register()
 */
protected void register() {
	super.register();
	getPaletteAnimator().addDrawer(this);
	getFigure().addLayoutListener(getPaletteAnimator());
}

public void restoreState(IMemento memento) {
	setExpanded(new Boolean(memento.getString(PROPERTY_EXPANSION_STATE)).booleanValue());
	setPinnedOpen(new Boolean(memento.getString(PROPERTY_PINNED_STATE)).booleanValue());
	RangeModel rModel = getDrawerFigure().getScrollpane().getViewport()
			.getVerticalRangeModel();
	rModel.setMinimum(memento.getInteger(RangeModel.PROPERTY_MINIMUM).intValue());
	rModel.setMaximum(memento.getInteger(RangeModel.PROPERTY_MAXIMUM).intValue());
	rModel.setExtent(memento.getInteger(RangeModel.PROPERTY_EXTENT).intValue());
	rModel.setValue(memento.getInteger(RangeModel.PROPERTY_VALUE).intValue());
	super.restoreState(memento);
}

public void saveState(IMemento memento) {
	memento.putString(PROPERTY_EXPANSION_STATE, new Boolean(isExpanded()).toString());
	memento.putString(PROPERTY_PINNED_STATE, new Boolean(isPinnedOpen()).toString());
	RangeModel rModel = getDrawerFigure().getScrollpane().getViewport()
			.getVerticalRangeModel();
	memento.putInteger(RangeModel.PROPERTY_MINIMUM, rModel.getMinimum());
	memento.putInteger(RangeModel.PROPERTY_MAXIMUM, rModel.getMaximum());
	memento.putInteger(RangeModel.PROPERTY_EXTENT, rModel.getExtent());
	memento.putInteger(RangeModel.PROPERTY_VALUE, rModel.getValue());
	super.saveState(memento);
}

/**
 * Sets the expansion state of the DrawerFigure
 * 
 * @param expanded	<code>true</code> if the drawer is expanded; false otherwise.
 */
public void setExpanded(boolean expanded) {
	getDrawerFigure().setExpanded(expanded);
}

/**
 * @see com.architexa.org.eclipse.gef.internal.ui.palette.editparts.PaletteEditPart#setImageInFigure(Image)
 */
protected void setImageInFigure(Image image) {
	getDrawerFigure().setTitleIcon(image);
}

/**
 * Sets the drawer's pinned state to the specified value.
 * @param pinned <code>true</code> if the drawer should be pinned when opened
 */
public void setPinnedOpen(boolean pinned) {
	getDrawerFigure().setPinned(pinned);
}

/**
 * @see com.architexa.org.eclipse.gef.EditPart#setSelected(int)
 */
public void setSelected(int value) {
	super.setSelected(value);
	getDrawerFigure().getCollapseToggle().requestFocus();
}

/**
 * @see com.architexa.org.eclipse.gef.editparts.AbstractEditPart#unregister()
 */
protected void unregister() {
	getPaletteAnimator().removeDrawer(this);
	super.unregister();
}

}
