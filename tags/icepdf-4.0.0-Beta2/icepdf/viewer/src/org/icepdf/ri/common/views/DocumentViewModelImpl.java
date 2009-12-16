/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEpdf 3.0 open source software code, released
 * May 1st, 2009. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2009 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */
package org.icepdf.ri.common.views;

import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.PageTree;
import org.icepdf.core.views.swing.PageViewComponentImpl;
import org.icepdf.core.views.swing.AbstractPageViewComponent;

import javax.swing.*;
import java.util.ArrayList;

/**
 * <p>Default Swing implementation of the AbstractDocumentViewModel class.  The
 * constructor for this class constructs the needed PageViewComponentImpl objects
 * and associates a reference to the parent JScrollPane.</p>
 * <p/>
 * <p>Swing specific setup is handle by this class.</p>
 *
 * @since 2.5
 */
public class DocumentViewModelImpl extends AbstractDocumentViewModel {

    public DocumentViewModelImpl(Document document, JScrollPane parentScrollPane) {
        // construct abstract parent
        super(document);

        // load the page components into the layout
        PageViewComponentImpl pageViewComponentImpl = null;
        PageTree pageTree = document.getPageTree();
        int numberOfPages = document.getNumberOfPages();
        int avgPageWidth = 0;
        int avgPageHeight = 0;

        // add components for every page in the document
        pageComponents = new ArrayList<AbstractPageViewComponent>(numberOfPages);
        for (int i = 0; i < numberOfPages; i++) {
            // also a way to pass in an average document size.
            if (i < MAX_PAGE_SIZE_READ_AHEAD) {
                pageViewComponentImpl =
                        new PageViewComponentImpl(this, pageTree, i, parentScrollPane);
                avgPageWidth += pageViewComponentImpl.getPreferredSize().width;
                avgPageHeight += pageViewComponentImpl.getPreferredSize().height;
            } else if (i > MAX_PAGE_SIZE_READ_AHEAD) {
                pageViewComponentImpl =
                        new PageViewComponentImpl(this, pageTree, i,
                                parentScrollPane,
                                avgPageWidth, avgPageHeight);
            }
            // calculate average page size
            else if (i == MAX_PAGE_SIZE_READ_AHEAD) {
                avgPageWidth /= (MAX_PAGE_SIZE_READ_AHEAD);
                avgPageHeight /= (MAX_PAGE_SIZE_READ_AHEAD);
                pageViewComponentImpl =
                        new PageViewComponentImpl(this, pageTree, i,
                                parentScrollPane,
                                avgPageWidth, avgPageHeight);
            }
            pageComponents.add(pageViewComponentImpl);
        }
    }
}
