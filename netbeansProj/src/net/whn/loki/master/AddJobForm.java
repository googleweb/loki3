/**
 *Project: Loki Render - A distributed job queue manager.
 *Version 0.6.2
 *Copyright (C) 2009 Daniel Petersen
 *Created on Aug 8, 2009, 8:09:39 PM
 */
/**
 *This program is free software: you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation, either version 3 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.whn.loki.master;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import net.whn.loki.common.LokiForm;

/**
 *Provides a form for creating a new job
 * @author daniel
 */
public class AddJobForm extends LokiForm {

    /** Creates new form AddJobForm */
    public AddJobForm(MasterForm mHandle) {
        initComponents();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        masterForm = mHandle;
        txtProjectFile.setText(masterForm.getCfg().getProjFile().toString());
        txtOutputDir.setText(masterForm.getCfg().getOutDirFile().toString());
        txtOutputPrefix.setText(masterForm.getCfg().getFilePrefix());
        cbxTileMultiplier.setSelectedIndex(2);
        updateMultiplierText();
    }

    /*BEGIN PRIVATE*/
    private final MasterForm masterForm;

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        projectFileChooser = new javax.swing.JFileChooser();
        jPanel1 = new javax.swing.JPanel();
        cbxJobType = new javax.swing.JComboBox();
        typeLabel = new javax.swing.JLabel();
        nameLabel = new javax.swing.JLabel();
        fileLabel = new javax.swing.JLabel();
        firstFrameLabel = new javax.swing.JLabel();
        lastFrameLabel = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtProjectFile = new javax.swing.JTextField();
        txtFirstFrame = new javax.swing.JTextField();
        txtLastFrame = new javax.swing.JTextField();
        FileBrowseButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtOutputDir = new javax.swing.JTextField();
        OutputBrowseButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtOutputPrefix = new javax.swing.JTextField();
        cancelButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        checkboxEnableTile = new javax.swing.JCheckBox();
        cbxTileMultiplier = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        lblMultiplier = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New Job");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("General"));

        cbxJobType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Blender" }));
        cbxJobType.setToolTipText("select the job type here");

        typeLabel.setText("Type:");

        nameLabel.setText("Name:");

        fileLabel.setText("Project File:");

        firstFrameLabel.setText("First Frame:");

        lastFrameLabel.setText("Last Frame:");

        txtName.setToolTipText("type a unique job name here");

        txtProjectFile.setToolTipText("specify the full path to the project file here");

        txtFirstFrame.setToolTipText("the first frame in the frame range to be rendered (e.g. '1')");

        txtLastFrame.setToolTipText("the last frame in the frame range to be rendered (e.g. '200')");

        FileBrowseButton.setText("Browse");
        FileBrowseButton.setName("FileBrowseButton"); // NOI18N
        FileBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FileBrowseButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Output Directory:");

        txtOutputDir.setToolTipText("the output directory where rendered frames will be placed");

        OutputBrowseButton.setText("Browse");
        OutputBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OutputBrowseButtonActionPerformed(evt);
            }
        });

        jLabel2.setText("Output File Prefix:");

        txtOutputPrefix.setToolTipText("specify a file prefix for the rendered frames (e.g. \"scene1_\" would give \"scene1_0024.jpg\", etc.)");
        txtOutputPrefix.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOutputPrefixActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lastFrameLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(firstFrameLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(fileLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(nameLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(typeLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtLastFrame, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFirstFrame, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtOutputPrefix, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxJobType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtProjectFile, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                    .addComponent(txtOutputDir))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(FileBrowseButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(OutputBrowseButton, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtFirstFrame, txtLastFrame});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtName, txtOutputPrefix});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxJobType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(typeLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtProjectFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fileLabel)
                    .addComponent(FileBrowseButton))
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtOutputDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(OutputBrowseButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtOutputPrefix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(firstFrameLabel)
                    .addComponent(txtFirstFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lastFrameLabel)
                    .addComponent(txtLastFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cancelButton.setText("Cancel");
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        saveButton.setText("Save");
        saveButton.setName("saveButton"); // NOI18N
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Tile rendering"));
        jPanel2.setToolTipText("Tile rendering splits a frame into separate parts for parallel rendering of tiles.");

        checkboxEnableTile.setText("Enabled");
        checkboxEnableTile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkboxEnableTileActionPerformed(evt);
            }
        });

        cbxTileMultiplier.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
        cbxTileMultiplier.setToolTipText("select a multiplier to specify how many tiles parts will be used");
        cbxTileMultiplier.setEnabled(false);
        cbxTileMultiplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxTileMultiplierActionPerformed(evt);
            }
        });

        jLabel3.setText("Multiplier:");

        lblMultiplier.setToolTipText("the total number of tiles parts");
        lblMultiplier.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkboxEnableTile)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbxTileMultiplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblMultiplier)))
                .addContainerGap(392, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(checkboxEnableTile)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cbxTileMultiplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMultiplier))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(saveButton)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, saveButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton)
                    .addComponent(cancelButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Disposes of the form without doing anything.
     * @param evt
     */
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * performs final validation then creates a new job object with the
     * given values and passes it to the masterForm
     * @param evt
     */
    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        boolean valid = true;
        File f;

        //check that the name field isn't empty
        if (txtName.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Please enter a name.");
            valid = false;
            txtName.requestFocusInWindow();
        }

        //check that the project file exists
        try {
            f = new File(txtProjectFile.getText());
            if (!f.canRead() && valid) {
                JOptionPane.showMessageDialog(rootPane,
                        "Please enter a valid project file.");
                valid = false;
                txtProjectFile.requestFocusInWindow();
            }
        } catch (NullPointerException nullEx) {
            JOptionPane.showMessageDialog(rootPane,
                    "Please enter a valid project file.");
            valid = false;
            txtProjectFile.requestFocusInWindow();
        }

        //check that the output dir exists
        try {
            f = new File(txtOutputDir.getText());
            if (!f.canWrite() && valid) {
                JOptionPane.showMessageDialog(rootPane,
                        "Loki cannot write to the output directory.");
                valid = false;
                txtOutputDir.requestFocusInWindow();
            }
        } catch (NullPointerException nullEx) {
            JOptionPane.showMessageDialog(rootPane, "Please enter a valid project file.");
            valid = false;
            txtOutputDir.requestFocusInWindow();
        }

        //check that the frame entries are integers
        try {
            int first = Integer.parseInt(txtFirstFrame.getText());
            int last = Integer.parseInt(txtLastFrame.getText());

            if ((first > last) && valid) {
                JOptionPane.showMessageDialog(rootPane, "The last frame " +
                        "must be the same or larger than the first frame.");
                valid = false;
                txtFirstFrame.requestFocusInWindow();
            }
            if (first < 1 && valid) {
                JOptionPane.showMessageDialog(rootPane, "Frames must be " +
                        "positive numbers.");
                valid = false;
                txtFirstFrame.requestFocusInWindow();
            }
        } catch (NumberFormatException NumEx) {
            if (valid) {
                JOptionPane.showMessageDialog(rootPane, "Please enter valid " +
                        "numbers for first and last frames.");
                valid = false;
                txtFirstFrame.requestFocusInWindow();
            }
        }

        //after all the validation checks
        if (valid) {
            masterForm.addJob(new JobFormInput(
                    cbxJobType.getSelectedItem().toString(),
                    txtName.getText(),
                    txtProjectFile.getText(),
                    txtOutputDir.getText(),
                    txtOutputPrefix.getText(),
                    Integer.parseInt(txtFirstFrame.getText()),
                    Integer.parseInt(txtLastFrame.getText()), 3,
                    checkboxEnableTile.isSelected(),
                    Integer.parseInt(
                    (String) cbxTileMultiplier.getSelectedItem())));
            dispose();
            masterForm.setEnabled(true);

            //'remember' paths for next dialog run
            masterForm.getCfg().setProjFile(
                    new File(txtProjectFile.getText()));
            masterForm.getCfg().setOutDirFile(
                    new File(txtOutputDir.getText()));
            masterForm.getCfg().setFilePrefix(txtOutputPrefix.getText());
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void FileBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FileBrowseButtonActionPerformed
        JFileChooser fileChooser = new JFileChooser(
                masterForm.getCfg().getProjFile());
        fileChooser.setDialogTitle("Select a project file");
        fileChooser.addChoosableFileFilter(new BlendFilter());
        if (fileChooser.showDialog(jLabel1, "Select") == JFileChooser.APPROVE_OPTION) {
            txtProjectFile.setText(
                    fileChooser.getSelectedFile().getPath());
        }
    }//GEN-LAST:event_FileBrowseButtonActionPerformed

    private void updateMultiplierText() {
        Integer multiplier = Integer.parseInt(
                (String) cbxTileMultiplier.getSelectedItem());
        String txt = multiplier.toString() + " * " + multiplier.toString() +
                " = " + Integer.toString(multiplier * multiplier) + " tiles";
        lblMultiplier.setText(txt);
    }

    private void OutputBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OutputBrowseButtonActionPerformed
        JFileChooser dirChooser = new JFileChooser();
        dirChooser.setDialogTitle("Select an output directory");
        dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        dirChooser.setAcceptAllFileFilterUsed(false);
        dirChooser.setCurrentDirectory(masterForm.getCfg().getOutDirFile());
        if (dirChooser.showDialog(jLabel1, "Select") == JFileChooser.APPROVE_OPTION) {
            txtOutputDir.setText(
                    dirChooser.getSelectedFile().getPath());
        }
    }//GEN-LAST:event_OutputBrowseButtonActionPerformed

    private void txtOutputPrefixActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOutputPrefixActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOutputPrefixActionPerformed

    private void checkboxEnableTileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkboxEnableTileActionPerformed
        cbxTileMultiplier.setEnabled(checkboxEnableTile.isSelected());
        lblMultiplier.setEnabled(checkboxEnableTile.isSelected());
    }//GEN-LAST:event_checkboxEnableTileActionPerformed

    private void cbxTileMultiplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxTileMultiplierActionPerformed
        updateMultiplierText();
    }//GEN-LAST:event_cbxTileMultiplierActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton FileBrowseButton;
    private javax.swing.JButton OutputBrowseButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JComboBox cbxJobType;
    private javax.swing.JComboBox cbxTileMultiplier;
    private javax.swing.JCheckBox checkboxEnableTile;
    private javax.swing.JLabel fileLabel;
    private javax.swing.JLabel firstFrameLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lastFrameLabel;
    private javax.swing.JLabel lblMultiplier;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JFileChooser projectFileChooser;
    private javax.swing.JButton saveButton;
    private javax.swing.JTextField txtFirstFrame;
    private javax.swing.JTextField txtLastFrame;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtOutputDir;
    private javax.swing.JTextField txtOutputPrefix;
    private javax.swing.JTextField txtProjectFile;
    private javax.swing.JLabel typeLabel;
    // End of variables declaration//GEN-END:variables
}
