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
import com.architexa.org.eclipse.draw2d.BorderLayout;
import com.architexa.org.eclipse.draw2d.ColorConstants;
import com.architexa.org.eclipse.draw2d.Figure;
import com.architexa.org.eclipse.draw2d.FocusEvent;
import com.architexa.org.eclipse.draw2d.Graphics;
import com.architexa.org.eclipse.draw2d.ImageFigure;
import com.architexa.org.eclipse.draw2d.ImageUtilities;
import com.architexa.org.eclipse.draw2d.MarginBorder;
import com.architexa.org.eclipse.draw2d.PositionConstants;
import com.architexa.org.eclipse.draw2d.geometry.Insets;
import com.architexa.org.eclipse.draw2d.geometry.Rectangle;
import com.architexa.org.eclipse.draw2d.text.FlowPage;
import com.architexa.org.eclipse.draw2d.text.ParagraphTextLayout;
import com.architexa.org.eclipse.draw2d.text.TextFlow;
import com.architexa.org.eclipse.gef.ui.palette.PaletteMessages;
import com.architexa.org.eclipse.gef.ui.palette.PaletteViewerPreferences;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;



/**
 * A customized figure used to represent entries in the GEF Palette.
 * 
 * @author Pratik Shah
 */
public class DetailedLabelFigure
	extends Figure
{

private static final FontCache FONTCACHE = new FontCache();
private static final Border PAGE_BORDER = new MarginBorder(0, 1, 0, 1);

private SelectableImageFigure image;
private FlowPage page;
private TextFlow nameText, descText;
private Font boldFont;
private boolean selectionState;
private int layoutMode = -1;
private Font cachedFont;

/**
 * Constructor
 */
public DetailedLabelFigure() {
	image = new SelectableImageFigure();
	image.setAlignment(PositionConstants.NORTH);
	page = new FocusableFlowPage();
	page.setBorder(PAGE_BORDER);

	nameText = new TextFlow();
	nameText.setLayoutManager(
		new ParagraphTextLayout(nameText, ParagraphTextLayout.WORD_WRAP_TRUNCATE));

	descText = new TextFlow();
	descText.setLayoutManager(
		new ParagraphTextLayout(descText, ParagraphTextLayout.WORD_WRAP_TRUNCATE));

	page.add(nameText);
	add(image);
	add(page);
	BorderLayout layout = new BorderLayout();
	layout.setHorizontalSpacing(2);
	layout.setVerticalSpacing(0);
	setLayoutManager(layout);
}

/**
* @see com.architexa.org.eclipse.draw2d.Figure#addNotify()
*/
public void addNotify() {
	super.addNotify();
	updateFont(layoutMode);
}

/**
 * Releases any OS resources used by the figure.
 */
protected void dispose() {
	if (boldFont != null) {
		nameText.setFont(null);
		FONTCACHE.checkIn(boldFont);
		boldFont = null;
	}
	if (image != null) {
		image.disposeShadedImage();
	}
}

/**
 * @see com.architexa.org.eclipse.draw2d.IFigure#handleFocusGained(FocusEvent)
 */
public void handleFocusGained(FocusEvent event) {
	super.handleFocusGained(event);
	updateColors();
}

/**
 * @see com.architexa.org.eclipse.draw2d.Figure#handleFocusLost(FocusEvent)
 */
public void handleFocusLost(FocusEvent event) {
	super.handleFocusLost(event);
	updateColors();
}

/**
 * @return whether the name is truncated
 */
public boolean isNameTruncated() {
	return nameText.isTextTruncated();
}

/**
 * @return whether this figure is selected or not
 */
public boolean isSelected() {
	return selectionState;
}

/**
 * @param	s	The description for this entry
 */
public void setDescription(String s) {
	String str = ""; //$NON-NLS-1$
	if (s != null && !s.trim().equals("")  //$NON-NLS-1$
	              && !s.trim().equals(nameText.getText().trim())) {
		str = " " + PaletteMessages.NAME_DESCRIPTION_SEPARATOR //$NON-NLS-1$
				  + " " + s; //$NON-NLS-1$
	}
	if (descText.getText().equals(str)) {
		return;
	}
	descText.setText(str);
}

/**
 * Sets the icon for this figure
 * 
 * @param icon The new image
 */
public void setImage(Image icon) {
	image.setImage(icon);
}

/**
 * @param	layoutMode		the palette layout (any of the
 * 							PaletteViewerPreferences.LAYOUT_XXXX options)
 */
public void setLayoutMode(int layoutMode) {
	updateFont(layoutMode);
	
	if (layoutMode == this.layoutMode)
		return;
	
	this.layoutMode = layoutMode;

	add(page);
	if (descText.getParent() == page)
		page.remove(descText);
	
	BorderLayout layout = (BorderLayout) getLayoutManager();
	if (layoutMode == PaletteViewerPreferences.LAYOUT_COLUMNS) {
		page.setHorizontalAligment(PositionConstants.CENTER);
		layout.setConstraint(image, BorderLayout.TOP);
		layout.setConstraint(page, BorderLayout.CENTER);
	} else if (layoutMode == PaletteViewerPreferences.LAYOUT_ICONS) {
		layout.setConstraint(image, BorderLayout.CENTER);
		remove(page);
	} else if (layoutMode == PaletteViewerPreferences.LAYOUT_LIST) {
		page.setHorizontalAligment(PositionConstants.LEFT);
		layout.setConstraint(image, BorderLayout.LEFT);
		layout.setConstraint(page, BorderLayout.CENTER);
	} else if (layoutMode == PaletteViewerPreferences.LAYOUT_DETAILS) {
		/*
		 * Fix for Bug# 39130
		 * Earlier, descText was only being added to the page if the description was
		 * not an empty String.  Now, it's always added.  This fixes the case mentioned
		 * in 39130.  The undesirable side-effect is that the descText will be added to
		 * the page even when it's empty.  However, that shouldn't affect anything because
		 * the descText will be empty (even in the case where the description is not
		 * empty, but is equal to the name -- see setDescription()).
		 */
		page.add(descText);
		page.setHorizontalAligment(PositionConstants.LEFT);
		layout.setConstraint(image, BorderLayout.LEFT);
		layout.setConstraint(page, BorderLayout.CENTER);
	}
}

/**
 * @param	str		The new name for this entry
 */
public void setName(String str) {
	if (nameText.getText().equals(str)) {
		return;
	}
	nameText.setText(str);
}

/**
 * @param	state	<code>true</code> if this entry is to be set as selected
 */
public void setSelected(boolean state) {
	selectionState = state;
	updateColors();
}

private void updateColors() {
	if (isSelected()) {
		if (hasFocus()) {
			image.useShadedImage();
			setForegroundColor(ColorConstants.menuForegroundSelected);
			setBackgroundColor(ColorConstants.menuBackgroundSelected);
		} else {
			image.disposeShadedImage();
			setForegroundColor(null);
			setBackgroundColor(ColorConstants.button);
		}
	} else {
		image.disposeShadedImage();
		setForegroundColor(null);
		setBackgroundColor(null);
	}
}

private void updateFont(int layout) {
	boolean layoutChanged = (layoutMode != layout);
	boolean fontChanged = (cachedFont == null || cachedFont != getFont());

	cachedFont = getFont();
	if (layoutChanged || fontChanged) {
		if (boldFont != null) {
			FONTCACHE.checkIn(boldFont);
			boldFont = null;
		}
		if (layout == PaletteViewerPreferences.LAYOUT_DETAILS && cachedFont != null)
			boldFont = FONTCACHE.checkOut(cachedFont);
		nameText.setFont(boldFont);
	}
}

private class FocusableFlowPage extends FlowPage {
	protected void paintFigure(Graphics g) {
		if (isSelected()) {
			Rectangle childBounds = null;
			List children = getChildren();
			for (int i = 0; i < children.size(); i++) {
				Figure child = (Figure) children.get(i);
				if (i == 0) {
					childBounds = child.getBounds().getCopy();
				} else {
					childBounds.union(child.getBounds());
				}
			}
			childBounds.expand(new Insets(2, 2, 0, 0));
			translateToParent(childBounds);
			childBounds.intersect(getBounds());
			g.fillRectangle(childBounds);
//			super.paintFigure(g);
			if (DetailedLabelFigure.this.hasFocus()) {
				g.setXORMode(true);
				g.setForegroundColor(ColorConstants.menuBackgroundSelected);
				g.setBackgroundColor(ColorConstants.white);
				g.drawFocus(childBounds.resize(-1, -1));
			}
		} else {
			super.paintFigure(g);
		}
	}
}

private static class SelectableImageFigure extends ImageFigure {
	private Image shadedImage;
	protected void useShadedImage() {
		disposeShadedImage();
		if (super.getImage() != null) {
			ImageData data = ImageUtilities.createShadedImage(super.getImage(), 
					ColorConstants.menuBackgroundSelected);
			shadedImage = new Image(null, data, data.getTransparencyMask());
		}
	}
	protected void disposeShadedImage() {
		if (shadedImage != null) {
			shadedImage.dispose();
			shadedImage = null;
		}
	}
	public Image getImage() {
		if (shadedImage != null)
			return shadedImage;
		return super.getImage();
	}
	public void setImage(Image image) {
		if (image == super.getImage())
			return;
		boolean wasShaded = shadedImage != null;
		disposeShadedImage();
		super.setImage(image);
		if (wasShaded)
			useShadedImage();
	}
}

private static class FontCache {
	private Hashtable table = new Hashtable();
	private static class FontInfo {
		private Font boldFont;
		private int refCount;
	}
	
	/*
	 * Clients should only check in fonts that they checked out from this cache, and should
	 * do only one check-in per checkout. If the given font is not found, a null pointer
	 * exception will be encountered.
	 */
	public void checkIn(Font boldFont) {
		FontInfo info = null;
		Map.Entry entry = null;
		Collection values = table.entrySet();
		for (Iterator iter = values.iterator(); iter.hasNext();) {
			Map.Entry tempEntry = (Map.Entry) iter.next();
			FontInfo tempInfo = (FontInfo)tempEntry.getValue();
			if (tempInfo.boldFont == boldFont) {
				info = tempInfo;
				entry = tempEntry;
				break;
			}
		}
		info.refCount--;
		if (info.refCount == 0) {
			boldFont.dispose();
			table.remove(entry.getKey());
		}
	}
	
	public Font checkOut(Font font) {
		FontInfo info = null;
		FontData key = font.getFontData()[0];
		Object obj = table.get(key);
		if (obj != null) {
			info = (FontInfo)obj;
		} else {
			info = new FontInfo();
			FontData[] boldDatas = font.getFontData();
			for (int i = 0; i < boldDatas.length; i++) {
				boldDatas[i].setStyle(SWT.BOLD);
			}
			info.boldFont = new Font(Display.getCurrent(), boldDatas);
			table.put(key, info);
		}
		info.refCount++;
		return info.boldFont;
	}
}

}
