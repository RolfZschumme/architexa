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
package com.architexa.org.eclipse.gef.ui.actions;

import com.architexa.org.eclipse.gef.EditPart;
import com.architexa.org.eclipse.gef.internal.GEFMessages;
import com.architexa.org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import com.architexa.org.eclipse.gef.palette.PaletteTemplateEntry;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;


/**
 * Copies the currently selected template in the palatte to the system clipboard. 
 * @author Eric Bordeau
 */
public class CopyTemplateAction 
	extends WorkbenchPartAction
	implements ISelectionChangedListener
{

private Object template;

/**
 * Constructs a new CopyTemplateAction.  You must manually add this action to the palette
 * viewer's list of selection listeners.  Otherwise, this action's enabled state won't be
 * updated properly.
 * 
 * @param editor the workbench part
 * @see com.architexa.org.eclipse.gef.ui.actions.EditorPartAction#EditorPartAction(IEditorPart)
 */
public CopyTemplateAction(IEditorPart editor) {
	super(editor);
	setId(ActionFactory.COPY.getId());
	setText(GEFMessages.CopyAction_Label);
}

/**
 * Returns whether the selected EditPart is a TemplateEditPart.
 * @return whether the selected EditPart is a TemplateEditPart
 */
protected boolean calculateEnabled() {
	return template != null;
}

/**
 * @see com.architexa.org.eclipse.gef.ui.actions.EditorPartAction#dispose()
 */
public void dispose() {
	template = null;
}

/**
 * Sets the default {@link Clipboard Clipboard's} contents to be the currently selected
 * template.
 */
public void run() {
	Clipboard.getDefault().setContents(template);
}

/**
 * Sets the selected EditPart and refreshes the enabled state of this action.
 * 
 * @see ISelectionChangedListener#selectionChanged(SelectionChangedEvent)
 */
public void selectionChanged(SelectionChangedEvent event) {
	ISelection s = event.getSelection();
	if (!(s instanceof IStructuredSelection))
		return;
	IStructuredSelection selection = (IStructuredSelection)s;
	template = null;
	if (selection != null && selection.size() == 1) {
		Object obj = selection.getFirstElement();
		if (obj instanceof EditPart) {
			Object model = ((EditPart)obj).getModel();
			if (model instanceof CombinedTemplateCreationEntry)
				template = ((CombinedTemplateCreationEntry)model).getTemplate();
			else if (model instanceof PaletteTemplateEntry)
				template = ((PaletteTemplateEntry)model).getTemplate();
		}
	}
	refresh();
}

}
