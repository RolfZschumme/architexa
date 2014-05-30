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

import com.architexa.org.eclipse.draw2d.Border;
import com.architexa.org.eclipse.draw2d.IFigure;
import com.architexa.org.eclipse.draw2d.MarginBorder;
import com.architexa.org.eclipse.draw2d.geometry.Dimension;
import com.architexa.org.eclipse.draw2d.text.FlowPage;
import com.architexa.org.eclipse.draw2d.text.TextFlow;
import com.architexa.org.eclipse.gef.AccessibleEditPart;
import com.architexa.org.eclipse.gef.DragTracker;
import com.architexa.org.eclipse.gef.Request;
import com.architexa.org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import com.architexa.org.eclipse.gef.palette.PaletteContainer;
import com.architexa.org.eclipse.gef.palette.PaletteEntry;
import com.architexa.org.eclipse.gef.palette.PaletteSeparator;
import com.architexa.org.eclipse.gef.tools.SelectEditPartTracker;
import com.architexa.org.eclipse.gef.ui.palette.PaletteMessages;
import com.architexa.org.eclipse.gef.ui.palette.PaletteViewer;
import com.architexa.org.eclipse.gef.ui.palette.PaletteViewerPreferences;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IMemento;



public abstract class PaletteEditPart
	extends AbstractGraphicalEditPart
	implements PropertyChangeListener
{
	
public static final String XML_NAME = "entry"; //$NON-NLS-1$
private static final Border TOOLTIP_BORDER = new MarginBorder(0, 2, 1, 0);
private static ImageCache globalImageCache;
private AccessibleEditPart acc;
private PropertyChangeListener childListener = new PropertyChangeListener() {
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(PaletteEntry.PROPERTY_VISIBLE)) {
			refreshChildren();
		}
	}
};
private Image image;
private ImageDescriptor imgDescriptor;

public PaletteEditPart(PaletteEntry model) {
	setModel(model);
}

/**
 * @see com.architexa.org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
 */
public void activate() {
	super.activate();
	PaletteEntry model = (PaletteEntry)getModel();
	model.addPropertyChangeListener(this);
	traverseChildren(model, true);
}

/**
 * returns the AccessibleEditPart for this EditPart.   This method is called lazily from
 * {@link #getAccessibleEditPart()}.
 */
protected AccessibleEditPart createAccessible() {
	return null;
}

public void createEditPolicies() { }

protected IFigure createToolTip() {
	String message = getToolTipText();
	if (message == null || message.length() == 0)
		return null;

	FlowPage fp = new FlowPage() {
		public Dimension getPreferredSize(int w, int h) {
			Dimension d = super.getPreferredSize(-1, -1);
			if (d.width > 150)
				d = super.getPreferredSize(150, -1);
			return d;
		}
	};
	fp.setOpaque(true);
	fp.setBorder(TOOLTIP_BORDER);
	TextFlow tf = new TextFlow();
	tf.setText(message);
	fp.add(tf);
	return fp;
}

/**
 * @see com.architexa.org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
 */
public void deactivate() {
	PaletteEntry model = (PaletteEntry)getModel();
	model.removePropertyChangeListener(this);
	traverseChildren(model, false);
	if (image != null) {
		image.dispose();
		image = null;
	}
	super.deactivate();
}


protected AccessibleEditPart getAccessibleEditPart() {
	if (acc == null)
		acc = createAccessible();
	return acc;
}

class SingleSelectionTracker extends SelectEditPartTracker {
	SingleSelectionTracker() {
		super(PaletteEditPart.this);
	}
	protected void performSelection() {
		if (hasSelectionOccurred())
			return;
		setFlag(FLAG_SELECTION_PERFORMED, true);
		getCurrentViewer().select(getSourceEditPart());
	}
}

/**
 * @see com.architexa.org.eclipse.gef.editparts.AbstractGraphicalEditPart#getDragTracker(Request)
 */
public DragTracker getDragTracker(Request request) {
	return new SingleSelectionTracker();
}

/**
 * Returns the image cache.
 * The cache is global, and is shared by all palette edit parts. This has the disadvantage
 * that once an image is allocated, it is never freed until the display is disposed. 
 * However, it has the advantage that the same image in different palettes is
 * only ever created once.
 */
protected static ImageCache getImageCache() {
	ImageCache cache = globalImageCache;
	if (cache == null) {
		globalImageCache = cache = new ImageCache();
		Display display = Display.getDefault();
		if (display != null) {
			display.disposeExec(new Runnable() {
				public void run() {
					if (globalImageCache != null) {
						globalImageCache.dispose();
						globalImageCache = null;
					}
				}	
			});
		}
	}
	return cache;
}

/**
 * @see com.architexa.org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
 */
public List getModelChildren() {
	List modelChildren;
	if (getModel() instanceof PaletteContainer)
		modelChildren = new ArrayList(((PaletteContainer)getModel()).getChildren());
	else
		return Collections.EMPTY_LIST;
	
	PaletteEntry prevVisibleEntry = null;
	for (Iterator iter = modelChildren.iterator(); iter.hasNext();) {
		PaletteEntry entry = (PaletteEntry) iter.next();
		if (!entry.isVisible())
			// not visible
			iter.remove();
		else if (entry instanceof PaletteSeparator && prevVisibleEntry == null) 
			// first visible item in a group is a separator, don't need it
			iter.remove();
		else if (entry instanceof PaletteSeparator && prevVisibleEntry instanceof PaletteSeparator)
			// previous visible entry was a separator, don't need it
			iter.remove();
		else
			prevVisibleEntry = entry;
	}
	// check to see if last visible entry was a separator, and thus should be hidden
	if (prevVisibleEntry instanceof PaletteSeparator) 
		modelChildren.remove(prevVisibleEntry);
	
	return modelChildren;
}

protected PaletteEntry getPaletteEntry() {
	return (PaletteEntry)getModel();
}

PaletteViewer getPaletteViewer() {
	return (PaletteViewer)getViewer();
}

protected PaletteViewerPreferences getPreferenceSource() {
	return ((PaletteViewer)getViewer()).getPaletteViewerPreferences();
}

protected IFigure getToolTipFigure() {
	return getFigure();
}

protected String getToolTipText() {
	String text = null;
	PaletteEntry entry = (PaletteEntry)getModel();
	String desc = entry.getDescription();
	boolean needName = nameNeededInToolTip();
	if (desc == null
	  || desc.trim().equals(entry.getLabel())
	  || desc.trim().equals("")) { //$NON-NLS-1$
		if (needName)
			text = entry.getLabel();
	} else {
		if (needName)
			text = entry.getLabel() + " " //$NON-NLS-1$
					+ PaletteMessages.NAME_DESCRIPTION_SEPARATOR + " " + desc; //$NON-NLS-1$
		else
			text = desc;
	}
	if (text != null && text.trim().equals(""))//$NON-NLS-1$
		return null;
	return text;
}

protected boolean nameNeededInToolTip() {
	return getPreferenceSource().getLayoutSetting() 
			== PaletteViewerPreferences.LAYOUT_ICONS;
}

/**
 * @see java.beans.PropertyChangeListener#propertyChange(PropertyChangeEvent)
 */
public void propertyChange(PropertyChangeEvent evt) {
	String property = evt.getPropertyName();
	if (property.equals(PaletteContainer.PROPERTY_CHILDREN)) {
		traverseChildren((List)evt.getOldValue(), false);
		refreshChildren();
		traverseChildren((List)evt.getNewValue(), true);
	} else if (property.equals(PaletteEntry.PROPERTY_LABEL)
			|| property.equals(PaletteEntry.PROPERTY_SMALL_ICON)
			|| property.equals(PaletteEntry.PROPERTY_LARGE_ICON)
			|| property.equals(PaletteEntry.PROPERTY_DESCRIPTION))
		refreshVisuals();
}

public void restoreState(IMemento memento) {
	Iterator iter = getChildren().iterator();
	IMemento[] childMementos = memento.getChildren(XML_NAME);
	int index = 0;
	while (iter.hasNext())
		((PaletteEditPart)iter.next()).restoreState(childMementos[index++]);
}

public void saveState(IMemento memento) {
	Iterator iter = getChildren().iterator();
	while (iter.hasNext())
		((PaletteEditPart)iter.next()).saveState(memento.createChild(XML_NAME));
}

protected void setImageDescriptor(ImageDescriptor desc) {
	if (desc == imgDescriptor)
		return;
	imgDescriptor = desc;
	setImageInFigure(getImageCache().getImage(imgDescriptor));
}

protected void setImageInFigure(Image image) { }

private void traverseChildren(PaletteEntry parent, boolean add) {
	if (!(parent instanceof PaletteContainer))
		return;
	PaletteContainer container = (PaletteContainer)parent;
	traverseChildren(container.getChildren(), add);
}

private void traverseChildren(List children, boolean add) {	
	for (Iterator iter = children.iterator(); iter.hasNext();) {
		PaletteEntry entry = (PaletteEntry) iter.next();
		if (add) {
			entry.addPropertyChangeListener(childListener);
		} else {
			entry.removePropertyChangeListener(childListener);
		}		
	}
}

protected static class ImageCache {
	/** Map from ImageDescriptor to Image */
	private Map images = new HashMap(11);
	
	Image getImage(ImageDescriptor desc) {
		if (desc == null) {
			return null;
		}
		Image img = null;
		Object obj = images.get(desc);
		if (obj != null) {
			img = (Image)obj;
		} else {
			img = desc.createImage();
			images.put(desc, img);
		}
		return img;
	}

	Image getMissingImage() {
		return getImage(ImageDescriptor.getMissingImageDescriptor());
	}
	
	void dispose() {
		for (Iterator i = images.values().iterator(); i.hasNext();) {
			Image img = (Image) i.next();
			img.dispose();
		}
		images.clear();
	}
}

}
