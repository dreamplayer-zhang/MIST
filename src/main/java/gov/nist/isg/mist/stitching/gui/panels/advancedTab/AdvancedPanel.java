// ================================================================
//
// Disclaimer: IMPORTANT: This software was developed at the National
// Institute of Standards and Technology by employees of the Federal
// Government in the course of their official duties. Pursuant to
// title 17 Section 105 of the United States Code this software is not
// subject to copyright protection and is in the public domain. This
// is an experimental system. NIST assumes no responsibility
// whatsoever for its use by other parties, and makes no guarantees,
// expressed or implied, about its quality, reliability, or any other
// characteristic. We would appreciate acknowledgement if the software
// is used. This software can be redistributed and/or modified freely
// provided that any derivative works bear some notice that they are
// derived from it, and any modified versions bear some notice that
// they have been modified.
//
// ================================================================

// ================================================================
//
// Author: tjb3
// Date: Apr 18, 2014 12:53:27 PM EST
//
// Time-stamp: <Apr 18, 2014 12:53:27 PM tjb3>
//
//
// ================================================================

package gov.nist.isg.mist.stitching.gui.panels.advancedTab;

import gov.nist.isg.mist.stitching.gui.components.helpDialog.HelpDocumentationViewer;
import gov.nist.isg.mist.stitching.gui.components.textfield.TextFieldInputPanel;
import gov.nist.isg.mist.stitching.gui.components.textfield.textFieldModel.DblModel;
import gov.nist.isg.mist.stitching.gui.components.textfield.textFieldModel.IntModel;
import gov.nist.isg.mist.stitching.gui.params.StitchingAppParams;
import gov.nist.isg.mist.stitching.gui.params.interfaces.GUIParamFunctions;
import gov.nist.isg.mist.stitching.lib.log.Debug;
import gov.nist.isg.mist.stitching.lib.log.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Creates the advanced options panel
 * 
 * @author Tim Blattner
 * @version 1.0
 * 
 */
public class AdvancedPanel extends JPanel implements GUIParamFunctions, ActionListener {

  private static String repeatabilityHelp = "During optimization, uses the user-specified "
      + "repeatability of the stage. Leave this field blank to use the default. "
      + "This value represents the uncertainty that the microscope stage has "
      + "related to the mechanics that move the stage. Setting this value may "
      + "increase the search space that is used to find the correct translation "
      + "between neighboring images.";


  private static String horizontalOverlapHelp = "During optimization, uses the "
      + "user-specified horizontal overlap for computing the repeatability of "
      + "the stage. Leave this field blank to use the default. Modifying this "
      + "field will aid in correcting translations that have low correlation in "
      + "the horizontal direction. By default we compute the horizontal overlap "
      + "based on the translations of the highest correlated tiles.";

  private static String verticalOverlapHelp = "During optimization, uses the "
      + "user-specified vertical overlap for computing the repeatability of the "
      + "stage. Leave this field blank to use the default. Modifying this field "
      + "will aid in correcting translations that have low correlation in the "
      + "vertical direction. By default we compute the vertical overlap based on "
      + "the translations of the highest correlated tiles.";
  
  private static String overlapUncertaintyHelp = "During optimization, uses the "
      + "user-specified overlap uncertainty for computing the repeatability of the "
      + "stage. Leave this field blank to use the default. Modifying this field "
      + "will aid in correcting translations where the overlap uncertainty should be increased."
      + " TIP: Value should not exceed 20.0, default is set at 5.0";
  
  

  private static final long serialVersionUID = 1L;
  
  private TextFieldInputPanel<Integer> numFFTPeaks;
  private TextFieldInputPanel<Integer> maxRepeatability;
  private TextFieldInputPanel<Double> horizontalOverlap;
  private TextFieldInputPanel<Double> verticalOverlap;
  private TextFieldInputPanel<Double> overlapUncertainty;
  

  private ParallelOptPane parallelOptions;
  private JComboBox loggingLevel;
  private JComboBox debugLevel;


  /**
   * Initializes the advanced options panel
   */
  public AdvancedPanel() {

    this.loggingLevel = new JComboBox(Log.LogType.values());
    this.debugLevel = new JComboBox(Debug.DebugType.values());

    this.loggingLevel.setSelectedItem(Log.getLogLevel());
    this.debugLevel.setSelectedItem(Debug.getDebugLevel());

    this.numFFTPeaks =
        new TextFieldInputPanel<Integer>("Number of FFT Peaks", "", 
            new IntModel(1, 100, true));
    
    this.maxRepeatability =
        new TextFieldInputPanel<Integer>("Stage Repeatability", "", new IntModel(1,
            Integer.MAX_VALUE, true), repeatabilityHelp);

    this.horizontalOverlap =
        new TextFieldInputPanel<Double>("Horizontal overlap", "",
            new DblModel(0.0, 100.0, true), horizontalOverlapHelp);
    this.verticalOverlap =
        new TextFieldInputPanel<Double>("Vertical overlap", "", new DblModel(0.0, 100.0, true),
            verticalOverlapHelp);
    
    this.overlapUncertainty =
        new TextFieldInputPanel<Double>("Overlap uncertainty", "", new DblModel(0.0, 100.0, true),
            overlapUncertaintyHelp);

    
    this.parallelOptions = new ParallelOptPane();

    setFocusable(false);

    initControls();
    initListeners();

  }

  private void initListeners()
  {
    this.loggingLevel.addActionListener(this);
    this.debugLevel.addActionListener(this);
  }

  private void initControls() {

    JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

    JPanel vertPanel = new JPanel(new GridBagLayout());

    GridBagConstraints c = new GridBagConstraints();

    JButton qButton = new JButton("Help?");
    
    qButton.addActionListener(new HelpDocumentationViewer("advanced-parameters-optional-"));
    
    c.anchor = GridBagConstraints.NORTHEAST;
    c.gridy = 0;
    vertPanel.add(qButton, c);
    c.fill = GridBagConstraints.HORIZONTAL;
    c.anchor = GridBagConstraints.LINE_START;

    c.gridy = 1;
    vertPanel.add(this.maxRepeatability, c);

    c.gridy = 2;
    vertPanel.add(this.horizontalOverlap, c);

    c.gridy = 3;
    vertPanel.add(this.verticalOverlap, c);

    c.gridy = 4;
    vertPanel.add(this.overlapUncertainty, c);

    c.gridy = 5;
    vertPanel.add(this.numFFTPeaks, c);


    JPanel logPanel = new JPanel(new GridBagLayout());
    this.loggingLevel = new JComboBox(Log.LogType.values());
    c.gridy = 0;
    logPanel.add(new JLabel("Log Level"), c);
    c.gridy = 1;
    logPanel.add(this.loggingLevel, c);

    this.debugLevel = new JComboBox(Debug.DebugType.values());
    c.gridy = 0;

    c.insets = new Insets(0, 10, 0, 0);
    logPanel.add(new JLabel("Debug Level"), c);
    c.gridy = 1;
    logPanel.add(this.debugLevel, c);

    c.gridy = 6;
    c.insets = new Insets(0,0,0,0);
    vertPanel.add(logPanel,c);


    
    c.gridy = 7;
    vertPanel.add(this.parallelOptions.getStitchingTypePanel(), c);

    c.gridy = 8;
    vertPanel.add(this.parallelOptions.getProgramPanel(), c);

    mainPanel.add(vertPanel);
    add(mainPanel);
  }

  @Override
  public void loadParamsIntoGUI(StitchingAppParams params) {
    this.numFFTPeaks.setValue(params.getAdvancedParams().getNumFFTPeaks());
    this.maxRepeatability.setValue(params.getAdvancedParams().getRepeatability());
    this.horizontalOverlap.setValue(params.getAdvancedParams().getHorizontalOverlap());
    this.verticalOverlap.setValue(params.getAdvancedParams().getVerticalOverlap());
    this.overlapUncertainty.setValue(params.getAdvancedParams().getOverlapUncertainty());
    this.parallelOptions.loadParamsIntoGUI(params);

    Log.LogType logType = params.getLogParams().getLogLevel();

    if (logType == null)
      logType = Log.LogType.MANDATORY;

    Debug.DebugType debugType = params.getLogParams().getDebugLevel();

    if (debugType == null)
      debugType = Debug.DebugType.NONE;

    this.loggingLevel.setSelectedItem(logType);
    this.debugLevel.setSelectedItem(debugType);

  }



  @Override
  public boolean checkAndParseGUI(StitchingAppParams params) {
    if (checkGUIArgs()) {
      saveParamsFromGUI(params, false);
      return true;
    }
    return false;
  }

  @Override
  public boolean checkGUIArgs() {

    return this.parallelOptions.checkGUIArgs();
  }

  private boolean loadingParams = false;

  @Override
  public void enableLoadingParams() {
    this.loadingParams = true;
    this.numFFTPeaks.enableIgnoreErrors();
    this.maxRepeatability.enableIgnoreErrors();
    this.horizontalOverlap.enableIgnoreErrors();
    this.verticalOverlap.enableIgnoreErrors();
    this.overlapUncertainty.enableIgnoreErrors();
    this.parallelOptions.enableLoadingParams();

  }

  @Override
  public void disableLoadingParams() {
    this.loadingParams = false;
    this.numFFTPeaks.disableIgnoreErrors();
    this.maxRepeatability.disableIgnoreErrors();
    this.horizontalOverlap.disableIgnoreErrors();
    this.verticalOverlap.disableIgnoreErrors();
    this.overlapUncertainty.disableIgnoreErrors();
    this.parallelOptions.disableLoadingParams();
  }

  @Override
  public boolean isLoadingParams() {
    return this.loadingParams;
  }

  @Override
  public void saveParamsFromGUI(StitchingAppParams params, boolean isClosing) {
    params.getAdvancedParams().setRepeatability(this.maxRepeatability.getValue());
    params.getAdvancedParams().setHorizontalOverlap(this.horizontalOverlap.getValue());
    params.getAdvancedParams().setVerticalOverlap(this.verticalOverlap.getValue());
    params.getAdvancedParams().setNumFFTPeaks(this.numFFTPeaks.getValue());
    params.getAdvancedParams().setOverlapUncertainty(this.overlapUncertainty.getValue());

    Log.LogType logLevel = this.getLogLevel();
    Debug.DebugType debugLevel = this.getDebugLevel();

    params.getLogParams().setLogLevel(logLevel);
    params.getLogParams().setDebugLevel(debugLevel);

    this.parallelOptions.saveParamsFromGUI(params, isClosing);
  }

  /**
   * Gets the log level
   * @return the log level
   */
  public Log.LogType getLogLevel() {
    return (Log.LogType)this.loggingLevel.getSelectedItem();
  }

  /**
   * Gets the debug level
   * @return the debug level
   */
  public Debug.DebugType getDebugLevel() {
    return (Debug.DebugType)this.debugLevel.getSelectedItem();
  }


  @Override
  public void actionPerformed(ActionEvent arg0) {
    Object src = arg0.getSource();
    if (src instanceof JComboBox) {
      JComboBox action = (JComboBox) src;

      if (action.equals(this.loggingLevel)) {
        Log.setLogLevel((Log.LogType)action.getSelectedItem());
      } else if (action.equals(this.debugLevel)) {
        Debug.setDebugLevel((Debug.DebugType)action.getSelectedItem());
      }
    }
  }



}
