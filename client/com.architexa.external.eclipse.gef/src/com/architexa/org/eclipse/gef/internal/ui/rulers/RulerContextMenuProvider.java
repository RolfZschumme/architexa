/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.architexa.org.eclipse.gef.internal.ui.rulers;

import com.architexa.org.eclipse.gef.ContextMenuProvider;
import com.architexa.org.eclipse.gef.EditPartViewer;
import com.architexa.org.eclipse.gef.ui.actions.CreateGuideAction;
import com.architexa.org.eclipse.gef.ui.actions.GEFActionConstants;

import org.eclipse.jface.action.IMenuManager;


/**
 * @author Pratik Shah
 */
public class RulerContextMenuProvider 
	extends ContextMenuProvider 
{
public RulerContextMenuProvider(EditPartViewer viewer) {
	super(viewer);
}
public void buildContextMenu(IMenuManager menu) {
	GEFActionConstants.addStandardActionGroups(menu);
	menu.appendToGroup(GEFActionConstants.GROUP_ADD, new CreateGuideAction(getViewer()));
}

}