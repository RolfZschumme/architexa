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
package com.architexa.org.eclipse.gef.ui.palette;

import com.architexa.org.eclipse.gef.internal.ui.palette.editparts.DrawerEditPart;

import org.eclipse.jface.action.Action;


/**
 * An action that can be used to pin the given drawer open.
 * 
 * @author Pratik Shah
 */
public class PinDrawerAction extends Action {
	
private DrawerEditPart drawer;

/**
 * Constructor
 * 
 * @param	drawer	The EditPart for the drawer that this action pins/unpins
 */
public PinDrawerAction (DrawerEditPart drawer) {
	this.drawer = drawer;
	setChecked(drawer.isPinnedOpen());
	setEnabled(drawer.isExpanded());
	setText(PaletteMessages.PINNED);
}

/**
 * Toggles the pinned open status of the drawer.
 * 
 * @see org.eclipse.jface.action.Action#run()
 */
public void run() {
	drawer.setPinnedOpen(!drawer.isPinnedOpen());
}

}
