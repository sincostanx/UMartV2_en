/**
 * Copyright (c) 2001-2008 U-Mart Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * ---------------------------------------------------------------------
 */
package gui;

import java.io.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import cmdCore.*;

/**
 * �|�W�V�����\���p�^�u�D
 */
public class UPositionPanel extends UPanel {

  protected String fBuyLineName = fRb.getString("BUY_VOLUME");
  protected String fSellLineName = fRb.getString("SELL_VOLUME");
  protected Color fBuyLineColor = Color.blue;
  protected Color fSellLineColor = Color.red;
  protected double fInitMaxY = 300;
  protected double fInitMinY = 0;
  protected int fTargetUserID = -1; // -1 �͎������g�̏��p
  protected int fStep = 0;
  protected UGraph fGraph = new UGraph();
  protected UGraphYrangePanel fGraphYrangePanel = new UGraphYrangePanel();
  protected UGraphXrangePanel fGraphXrangePanel = new UGraphXrangePanel();
  protected USeperateWindowButton fSeperateWindowButton = new
      USeperateWindowButton();
  protected ArrayList fBuyVolumeData = new ArrayList();
  protected ArrayList fSellVolumeData = new ArrayList();
  protected ArrayList fPositionLog = new ArrayList();

  public UPositionPanel() {
    super();
    // Update List �ɒǉ��D�Ă΂Ȃ���Update����Ȃ��D
    addUpdateObserverList();
    fName = fRb.getString("POSITION");
    fBuyVolumeData.add(new Point2D.Double(0.0, 0.0));
    fSellVolumeData.add(new Point2D.Double(0.0, 0.0));
    try {
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    // UGraph�̐ݒ�
    fGraph.setBounds(new Rectangle(10, 15, 520, 253));
    fGraph.setFixedMaxY(fInitMaxY);
    fGraph.setFixedMinY(fInitMinY);
    fGraph.setLayout(null);
    fGraph.getGraph().add(new UGraphData(fBuyLineName, fBuyLineColor));
    fGraph.getGraph().add(new UGraphData(fSellLineName, fSellLineColor));
    fGraph.setBackground(Color.white);
    fGraph.setYAuto(true); // fGraphYrangePanel �� JCheckBox ���ύX�̕K�v����D
    fGraph.setFixedMaxX(fParam.getDays() * fParam.getBoardPerDay());
    this.add(fGraph, null);
    // UGraphYrangePanel�̐ݒ�
    fGraphYrangePanel.setBounds(new Rectangle(537, 11, 185, 134));
    fGraphYrangePanel.setGraph(fGraph);
    fGraphYrangePanel.setYAuto(true);
    this.add(fGraphYrangePanel, null);
    // UGraphXrangePanel�̐ݒ�
    fGraphXrangePanel.setBounds(new Rectangle(537, 151, 185, 79));
    fGraphXrangePanel.setGraph(fGraph);
    this.add(fGraphXrangePanel, null);
    // USeperateWindowButton�̐ݒ�
    fSeperateWindowButton.setBounds(new Rectangle(544, 236, 171, 30));
    fSeperateWindowButton.setGraph(fGraph);
    fSeperateWindowButton.setSubWindowTitle(fName);
    this.add(fSeperateWindowButton, null);
    addComponentListener(new ComponentAdapter() {
      public void componentShown(ComponentEvent ce) {
        gUpdate();
      }
    });
  }

  /**
   * interface IGUIObserver �������̂� UGUIUpdateManager ��
   * addDayObserver �� addStepObserver ���ĂԕK�v����D���ׂĂ�Upanel�ŕK�v�D
   */
  public void addUpdateObserverList() {
    UGUIUpdateManager.addStepObserver(this);
  }

  public void dataUpdate() {
    if (!isVisible() && !fSeperateWindowButton.isSubWindowAvailable()) {
      return;
    }
    int step = UGUIUpdateManager.getStep();
    if (step <= fStep) {
      return;
    }
    UCAccountHistoryCore cHistory = (UCAccountHistoryCore) fCProtocol.
        getCommand(UCAccountHistoryCore.CMD_NAME);
    cHistory.setArguments(fTargetUserID, step - fStep);
    cHistory.doIt();
    ArrayList dataArray = cHistory.getData(); // HashMap �� ArrayList ���Ԃ�
    for (int i = dataArray.size() - 1; i >= 0; --i) {
      HashMap hm = (HashMap) dataArray.get(i);
      fPositionLog.add(hm);
      int s = ( (Integer) hm.get(UCAccountHistoryCore.INT_STEP)).intValue();
      long buyPosition = ( (Long) hm.get(UCAccountHistoryCore.LONG_BUY_POSITION)).
          longValue();
      long sellPosition = ( (Long) hm.get(UCAccountHistoryCore.
                                          LONG_SELL_POSITION)).longValue();
      fBuyVolumeData.add(new Point2D.Double(s, buyPosition));
      fSellVolumeData.add(new Point2D.Double(s, sellPosition));
    }
    fStep = step;
    ArrayList array = fGraph.getGraph();
    ( (UGraphData) array.get(0)).setData(fBuyVolumeData);
    ( (UGraphData) array.get(1)).setData(fSellVolumeData);
  }

  public int getTargetUserID() {
    return fTargetUserID;
  }

  public void setTargetUserID(int id) {
    fTargetUserID = id;
  }

  public void writeLog(String basename) throws IOException {
    int step = UGUIUpdateManager.getStep();
    UCAccountHistoryCore cHistory = (UCAccountHistoryCore) fCProtocol.
        getCommand(UCAccountHistoryCore.CMD_NAME);
    cHistory.setArguments(fTargetUserID, step);
    cHistory.doIt();
    ArrayList dataArray = cHistory.getData(); // HashMap �� ArrayList ���Ԃ�
    String filename = basename + "_position.csv";
    PrintWriter pw = new PrintWriter(new FileWriter(filename));
    pw.println("step,buy,sell");
    for (int i = dataArray.size() - 1; i >= 0; --i) {
      HashMap hm = (HashMap) dataArray.get(i);
      pw.print(hm.get(UCAccountHistoryCore.INT_STEP).toString() + ",");
      pw.print(hm.get(UCAccountHistoryCore.LONG_BUY_POSITION).toString() + ",");
      pw.print(hm.get(UCAccountHistoryCore.LONG_SELL_POSITION).toString());
      pw.println();
    }
    pw.close();
  }

  public void repaint() {
    if (super.isVisible()) {
      super.repaint();
    }
    // �Ɨ��E�B���h�E�̉�ʍX�V
    if (fSeperateWindowButton != null) {
      fSeperateWindowButton.repaintSubWindow();
    }
  }

}
