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

import java.util.*;

import java.awt.*;
import java.awt.event.*;

import cmdCore.*;

/**
 * �`���[�g�\���^�u. 
 */
public class UServerManagerChartPanel extends UPanel {

  protected String fSpotLineName = fRb.getString("CURRENT_SPOT_PRICE");
  protected String fFutureLineName = fRb.getString("CURRENT_U-MART_PRICE");
  protected Color fSpotLineColor = Color.blue;
  protected Color fFutureLineColor = Color.red;
  protected int fCurrentStep = 0;
  protected ArrayList fSpotPriceData = new ArrayList();
  protected ArrayList fFuturePriceData = new ArrayList();
  protected UGraph fGraph = new UGraph();
  protected UGraphYrangePanel fGraphYrangePanel = new UGraphYrangePanel();
  protected UGraphXrangePanel fGraphXrangePanel = new UGraphXrangePanel();
  protected USeperateWindowButton fSeperateWindowButton = new
      USeperateWindowButton();

  public UServerManagerChartPanel() {
    fName = fRb.getString("CHART");
    try {
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    // UGraph�̐ݒ�
    fGraph.getGraph().add(new UGraphData(fSpotLineName, fSpotLineColor));
    fGraph.getGraph().add(new UGraphData(fFutureLineName, fFutureLineColor));
    fGraph.setBackground(Color.white);
    fGraph.setBounds(new Rectangle(10, 15, 670, 340));
    fGraph.setFixedMaxX(fParam.getDays() * fParam.getBoardPerDay());
    this.add(fGraph, null);
    // UGraphYrangePanel�̐ݒ�
    fGraphYrangePanel.setGraph(fGraph);
    fGraphYrangePanel.setBounds(new Rectangle(15, 370, 188, 132));
    this.add(fGraphYrangePanel, null);
    // UGraphXrangePanel�̐ݒ�
    fGraphXrangePanel.setGraph(fGraph);
    fGraphXrangePanel.setBounds(new Rectangle(213, 370, 186, 79));
    this.add(fGraphXrangePanel, null);
    // USeperateWindowButton�̐ݒ�
    fSeperateWindowButton.setGraph(fGraph);
    fSeperateWindowButton.setSubWindowTitle(fName);
    fSeperateWindowButton.setBounds(new Rectangle(223, 462, 162, 33));
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
    if (step <= fCurrentStep) {
      return;
    }
    Iterator iter;
    UCSpotPriceCore cSpotPrice = (UCSpotPriceCore) fCProtocol.getCommand(
        UCSpotPriceCore.CMD_NAME);
    // step-fCurrentStep ������SpotPrice�f�[�^��v��
    cSpotPrice.setArguments(new StringTokenizer("j30" + " " +
                                                String.valueOf(step -
        fCurrentStep)));
    cSpotPrice.doIt();
    ArrayList spotSeries = cSpotPrice.getResults();
    for (int i = spotSeries.size() - 1; i >= 0; --i) {
      HashMap elem = (HashMap) spotSeries.get(i);
      double d = (double) ( (Long) elem.get(UCSpotPriceCore.LONG_PRICE)).
          longValue();
      //���ӁI�I�I�I�ύX�K�v�D���݂� -1 �ɓ���ȈӖ�����D
      if (d > 0.0) {
        fSpotPriceData.add(new Double(d));
      } else {
        fSpotPriceData.add(new Double(Double.NaN));
      }
    }
    ( (UGraphData) fGraph.getGraph().get(0)).setData(fSpotPriceData);
    UCFuturePriceCore cFuturePrice = (UCFuturePriceCore) fCProtocol.
        getCommand("FuturePrice");
    // step-fCurrentStep ������ FuturePrice�f�[�^��v��
    cFuturePrice.setArguments(new StringTokenizer("j30" + " " +
                                                  String.valueOf(step -
        fCurrentStep)));
    cFuturePrice.doIt();
    ArrayList futuresSeries = cFuturePrice.getResults();
    for (int i = futuresSeries.size() - 1; i >= 0; --i) {
      HashMap elem = (HashMap) futuresSeries.get(i);
      double d = ( (Long) elem.get(UCFuturePriceCore.LONG_PRICE)).doubleValue();
      //���ӁI�I�I�I�ύX�K�v�D�����̏ꍇ�̒�`������D
      if (d > 0) {
        fFuturePriceData.add(new Double(d));
      } else {
        fFuturePriceData.add(new Double(Double.NaN));
      }
    }
    ( (UGraphData) fGraph.getGraph().get(1)).setData(fFuturePriceData);
    fCurrentStep = step; // �X�V�͍Ōザ��Ȃ��ƃO���t���A�b�v�f�[�g����Ȃ�
  }

  public void repaint() {
    if (super.isVisible()) {
      super.repaint();
    }
    if (fSeperateWindowButton != null) {
      fSeperateWindowButton.repaintSubWindow();
    }
  }

}
