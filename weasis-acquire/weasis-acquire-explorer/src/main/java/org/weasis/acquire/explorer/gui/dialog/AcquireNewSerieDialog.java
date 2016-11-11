/*******************************************************************************
 * Copyright (c) 2016 Weasis Team and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nicolas Roduit - initial API and implementation
 *******************************************************************************/
package org.weasis.acquire.explorer.gui.dialog;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.weasis.acquire.explorer.Messages;
import org.weasis.acquire.explorer.core.bean.Serie;
import org.weasis.acquire.explorer.gui.central.AcquireTabPanel;
import org.weasis.core.api.media.data.ImageElement;

@SuppressWarnings("serial")
public class AcquireNewSerieDialog extends JDialog implements PropertyChangeListener {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AcquireNewSerieDialog.class);


    private final JTextField serieName = new JTextField();
    private JOptionPane optionPane;

    private AcquireTabPanel acquireTabPanel;
    private List<ImageElement> medias;

    public AcquireNewSerieDialog(AcquireTabPanel acquireTabPanel, final List<ImageElement> medias) {
        this.acquireTabPanel = acquireTabPanel;
        this.medias = medias;
        optionPane = new JOptionPane(initPanel(), JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null,
            AcquireImportDialog.OPTIONS, AcquireImportDialog.OPTIONS[0]);
        optionPane.addPropertyChangeListener(this);

        setContentPane(optionPane);
        setModal(true);
        pack();
    }

    private JPanel initPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel question = new JLabel(Messages.getString("AcquireNewSerieDialog.enter_name")); //$NON-NLS-1$
        panel.add(question, BorderLayout.NORTH);

        panel.add(serieName, BorderLayout.CENTER);

        return panel;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object action = evt.getNewValue();
        boolean close = true;
        if (action != null) {
            if (AcquireImportDialog.OPTIONS[0].equals(action)) {
                if (serieName.getText() != null && !serieName.getText().isEmpty()) {
                    acquireTabPanel.moveElements(new Serie(serieName.getText()), medias);
                } else {
                    JOptionPane.showMessageDialog(this, Messages.getString("AcquireImportDialog.add_name_msg"), //$NON-NLS-1$
                        Messages.getString("AcquireImportDialog.add_name_title"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
                    optionPane.setValue(AcquireImportDialog.REVALIDATE);
                    close = false;
                }
            } else if (action.equals(AcquireImportDialog.REVALIDATE)) {
                close = false;
            }
            if (close) {
                clearAndHide();
            }
        }
    }

    public void clearAndHide() {
        serieName.setText(null);
        setVisible(false);
    }

}