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
import javax.swing.*;
import javax.swing.table.*;

import cmdCore.*;

public class UBoardTablePanel extends UPanel implements IOrderObserver {
  /** �e�[�u���̃w�b�_ */
  Object[] fBoardHeaderData = {
      fRb.getString("SELL"), fRb.getString("PRICE"), fRb.getString("BUY")};
  Vector fBoardHeader = new Vector();
  Vector fBoardInformation = new Vector();
  UTable fBoardTable;
  JScrollPane fBoardTablePane = new JScrollPane();
  //JPanel fBoardTablePanel = new JPanel();
  JRadioButton fTableTypeButton1 = new JRadioButton();
  JRadioButton fTableTypeButton2 = new JRadioButton();
  JRadioButton fTableTypeButton3 = new JRadioButton();
  ButtonGroup fTableButtonGroup = new ButtonGroup();
  JButton fTableUpdateButton = new JButton();
  JLabel fCurrentOrderBookLabel = new JLabel();
  UValueLabel fMarketSellVolumeLabel = new UValueLabel();
  UValueLabel fMarketBuyVolumeLabel = new UValueLabel();
  JLabel fMarketSellLabel = new JLabel();
  JLabel fMarketBuyLabel = new JLabel();

  protected static final int TABLE_SIMPLE = 0;
  protected static final int TABLE_SUM = 1;
  protected static final int TABLE_STANDARD = 2;
  protected int fTableType = TABLE_STANDARD;

  /**
   * interface IGUIObserver �������̂� UGUIUpdateManager ��
   * addDayObserver �� addStepObserver ���ĂԕK�v����D���ׂĂ�Upanel�ŕK�v�D
   */
  public void addUpdateObserverList() {
    UGUIUpdateManager.addAlwaysObserver(this);
  }

  public UBoardTablePanel() {
    fName = fRb.getString("ORDER_BOOK");
    try {
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void jbInit() throws Exception {
    UOrderButtonPanel.addObserver(this);
    this.setLayout(null);
    // ������
    fCurrentOrderBookLabel.setHorizontalAlignment(SwingConstants.CENTER);
    fCurrentOrderBookLabel.setHorizontalTextPosition(SwingConstants.CENTER);
    fCurrentOrderBookLabel.setText(fRb.getString("CURRENT_ORDER_BOOK"));
    fCurrentOrderBookLabel.setBounds(new java.awt.Rectangle(43, 4, 126, 22));
    for (int i = 0; i < fBoardHeaderData.length; i++) {
      fBoardHeader.add(fBoardHeaderData[i]);
    }
    fBoardTable = new UTable(new Vector(), fBoardHeader);
    fBoardTablePane.getViewport().setBackground(Color.white);
    fBoardTablePane.setBorder(BorderFactory.createRaisedBevelBorder());
    fBoardTablePane.setBounds(new java.awt.Rectangle(6, 28, 196, 249));
    this.setBorder(BorderFactory.createEtchedBorder());
    this.setBounds(new java.awt.Rectangle(6, 7, 288, 283));
    this.setLayout(null);
    fTableTypeButton1.setText(fRb.getString("SIMPLE"));
    fTableTypeButton1.setForeground(Color.black);
    fTableTypeButton1.setSelected(false);
    fTableTypeButton1.setBounds(new java.awt.Rectangle(207, 29, 78, 25));
    fTableTypeButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        fTableType = TABLE_SIMPLE;
        gUpdate();
        // Table �݂̂̍X�V�ł悢�D
        updateOrderObserver();
      }
    });
    fTableTypeButton2.setText(fRb.getString("SUM"));
    fTableTypeButton2.setForeground(Color.black);
    fTableTypeButton2.setSelected(false);
    fTableTypeButton2.setBounds(new java.awt.Rectangle(207, 69, 78, 25));
    fTableTypeButton2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        fTableType = TABLE_SUM;
        //gUpdate();
        // Table �݂̂̍X�V�ł悢�D
        updateOrderObserver();
      }
    });
    fTableTypeButton3.setText(fRb.getString("STANDARD"));
    fTableTypeButton3.setForeground(Color.black);
    fTableTypeButton3.setSelected(true);
    fTableTypeButton3.setBounds(new java.awt.Rectangle(207, 108, 78, 25));
    fTableTypeButton3.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        fTableType = TABLE_STANDARD;
        //gUpdate();
        // Table �݂̂̍X�V�ł悢�D
        updateOrderObserver();
      }
    });
    fTableUpdateButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        //gUpdate();
        // Table �݂̂̍X�V�ł悢�D
        updateOrderObserver();
      }
    });

    fTableButtonGroup.add(fTableTypeButton1);
    fTableButtonGroup.add(fTableTypeButton2);
    fTableButtonGroup.add(fTableTypeButton3);
    fTableUpdateButton.setBounds(new java.awt.Rectangle(204, 247, 79, 27));
    fTableUpdateButton.setText(fRb.getString("UPDATE"));
    fTableUpdateButton.setForeground(Color.black);
    this.add(fBoardTablePane, null);
    this.add(fCurrentOrderBookLabel, null);
    this.add(fTableTypeButton2, null);
    this.add(fTableTypeButton1, null);
    this.add(fTableTypeButton3, null);
    this.add(fTableUpdateButton, null);
    fBoardTablePane.getViewport().add(fBoardTable, null);

    fMarketSellLabel.setText(fRb.getString("MARKET_SELL"));
    fMarketSellLabel.setBounds(new java.awt.Rectangle(208, 154, 68, 15));
    fMarketBuyLabel.setText(fRb.getString("MARKET_BUY"));
    fMarketBuyLabel.setBounds(new java.awt.Rectangle(208, 199, 68, 15));

    fMarketSellVolumeLabel.setText("");
    fMarketSellVolumeLabel.setBounds(new java.awt.Rectangle(208, 174, 68, 15));
    fMarketBuyVolumeLabel.setText("");
    fMarketBuyVolumeLabel.setBounds(new java.awt.Rectangle(208, 219, 68, 15));

    this.add(fMarketSellLabel, null);
    this.add(fMarketSellVolumeLabel, null);
    this.add(fMarketBuyVolumeLabel, null);
    this.add(fMarketBuyLabel, null);
    addComponentListener(new ComponentAdapter() {
      public void componentShown(ComponentEvent ce) {
        gUpdate();
      }
    });
  }

  public synchronized void dataUpdate() {
    UCBoardInformationCore cBoardInformation = (UCBoardInformationCore)
        fCProtocol
        .getCommand(UCBoardInformationCore.CMD_NAME);
    UCommandStatus status = cBoardInformation.doIt();
    fBoardInformation = new Vector(); // JTable�p�̃f�[�^
    if (!status.getStatus()) {
      fBoardTable.setModel(new DefaultTableModel(fBoardInformation,
                                                 fBoardHeader));
      return;
    }
    HashMap result = cBoardInformation.getResults();
    ArrayList array = (ArrayList) result
        .get(UCBoardInformationCore.ARRAYLIST_BOARD);
    /*
     * 2�dVector�Ȃ̂�Market�̈������ʓ|�Ȃ̂�Iterator�͎g�p���Ȃ��D printData
     * �ɂ͈�ԏ��Market���C���̌� Limit ��񂪑���
     */
    Vector printData = makePrintData(array);
    if (fTableType == TABLE_SIMPLE) {
      fBoardInformation = makeSimpleTableModel(printData);
    } else if (fTableType == TABLE_SUM) {
      fBoardInformation = makeSumTableModel(printData);
    } else if (fTableType == TABLE_STANDARD) {
      fBoardInformation = makeStandardTableModel(printData);
    } else {
      System.err.println("Invalid fTableType!");
    }

    fMarketSellVolumeLabel.setText( ( (UFormatLong) ( (Vector) printData.get(0))
                                     .get(0)).toString());
    fMarketBuyVolumeLabel.setText( ( (UFormatLong) ( (Vector) printData.get(0))
                                    .get(2)).toString());
  }

  /**
   * JTable�̃��f���́C�e�s�ł���Vector��v�f�Ƃ���Vector�ŕ\�����D
   * UCBoardInformation���瓾������́CHashMap��ArrayList�ł��邽�߁C�����Vector��Vector�֕ϊ��������̂�Ԃ��D
   *
   * @param ����ArrayList�ŕ\�����ꂽ���
   * @return �ɂ�����e���i�̏����i�[����Vector(�v�f0:��������, �v�f1:���i, �v�f2:��������)��v�f�Ƃ���Vector
   */
  private Vector makePrintData(ArrayList array) {
    Vector printData = new Vector();
    Iterator iter = array.iterator();
    while (iter.hasNext()) {
      HashMap os = (HashMap) iter.next();
      Vector tmpBoard = new Vector();
      tmpBoard
          .add(new UFormatLong( (Long) os.get(UCBoardInformationCore.
                                              LONG_SELL_VOLUME)));
      tmpBoard.add(new UFormatLong( (Long) os.get(UCBoardInformationCore.
                                                  LONG_PRICE)));
      tmpBoard.add(new UFormatLong( (Long) os.get(UCBoardInformationCore.
                                                  LONG_BUY_VOLUME)));
      printData.add(tmpBoard);
    }
    return printData;
  }

  private Vector makeSimpleTableModel(Vector printData) {
    Vector table = new Vector();
    //Market ���͕ύX���Ȃ����ƂɕύX�B
    //    ((Vector)printData.get(0)).set(1, fRb.getString("MARKET"));
    //    table.add((Vector)printData.get(0));
    for (int i = printData.size() - 1; i >= 1; i--) {
      table.add( (Vector) printData.get(i));
    }
    return table;
  }

  private long[] makeSumOfBuy(Vector printData) {
    // �ȉ��CMarket ��Sell��Buy�ɕ�����̂�+1
    long[] buySum = new long[printData.size() + 1];
    buySum[printData.size()] = ( (UFormatLong) ( (Vector) printData.get(0)).get(
        2))
        .longValue();
    // Buy��Market����ԏ�.
    long tmpSum = buySum[printData.size()];
    for (int i = printData.size() - 1; i >= 1; i--) {
      long vol = ( (UFormatLong) ( (Vector) printData.get(i)).get(2)).longValue();
      // 2��buy���
      tmpSum += vol;
      buySum[i] = tmpSum;
    }
    return buySum;
  }

  private long[] makeSumOfSell(Vector printData) {
    // �ȉ��CMarket ��Sell��Buy�ɕ�����̂�+1
    long[] sellSum = new long[printData.size() + 1];
    sellSum[0] = ( (UFormatLong) ( (Vector) printData.get(0)).get(0)).longValue();
    //Sell��Market����ԉ�.
    long tmpSum = sellSum[0];
    for (int i = 1; i < printData.size(); i++) {
      long vol = ( (UFormatLong) ( (Vector) printData.get(i)).get(0)).longValue();
      //0 ��sell���
      tmpSum += vol;
      sellSum[i] = tmpSum;
    }
    return sellSum;
  }

  private Vector makeSumTableModel(Vector printData) {
    /*
     * �Ƃɂ������G�Ȃ̂� �EMarket �̎�舵�����ʁD �E�T�[�o�̃f�[�^�͒l�i���������\���͒l�i�~���D�܂��Cbuy �� sell
     * �ŕ������Ⴄ�D buy -> �l�i�����������ɑ�����. -> ����������ݐς��Ƃ� -> printData �t sell ->
     * �l�i�����������ɑ�����. -> ����������ݐς��Ƃ� -> printData ��
     */
    Vector table = new Vector();
    long[] buySum = makeSumOfBuy(printData);
    long[] sellSum = makeSumOfSell(printData);
    // Market ���̓e�[�u���Ɏ����Ȃ����ƂɕύX
    /*
     * Vector firstLine = new Vector(); firstLine.add("-"); // Sell �͂Ȃ�
     * firstLine.add(fRb.getString("MARKET_BUY")); firstLine.add(new
     * Long(buySum[printData.size()])); table.add(firstLine);
     */
    for (int i = printData.size() - 1; i >= 1; i--) {
      Vector tmpV = new Vector();
      tmpV.add(new UFormatLong(sellSum[i]));
      tmpV.add( ( (Vector) printData.get(i)).get(1));
      tmpV.add(new UFormatLong(buySum[i]));
      table.add(tmpV);
    }
    /*
     * Vector lastLine = new Vector(); lastLine.add(new Long(sellSum[0]));
     * lastLine.add(fRb.getString("MARKET_SELL")); lastLine.add("-"); //
     * Buy�͂Ȃ� table.add(lastLine);
     */
    return table;
  }

  private Vector makeStandardTableModel(Vector printData) {
    Vector table = new Vector();
    long[] buySum = makeSumOfBuy(printData);
    long[] sellSum = makeSumOfSell(printData);
    for (int i = printData.size() - 1; i >= 1; i--) {
      Vector tmpV = new Vector();
      //�e�[�u���� Sell Price Buy �̏�
      if (buySum[i] - sellSum[i] > 0) {
        tmpV.add(null);
        tmpV.add( ( (Vector) printData.get(i)).get(1)); // price
        tmpV.add(new UFormatLong(buySum[i] - sellSum[i]));
      } else if (buySum[i] - sellSum[i] < 0) {
        tmpV.add(new UFormatLong(sellSum[i] - buySum[i]));
        tmpV.add( ( (Vector) printData.get(i)).get(1)); // price
        tmpV.add(null);
      } else {
        tmpV.add(new UFormatLong(0));
        tmpV.add( ( (Vector) printData.get(i)).get(1)); // price
        tmpV.add(new UFormatLong(0));
      }
      table.add(tmpV);
    }
    return table;
  }

  public void gUpdate() {
    if (!isVisible()) {
      return;
    }
    IGUIEvent iev = new IGUIEvent() {
      public void execute() {
        dataUpdate();
      }

      public Runnable getRunnableForInvokeLater() {
        return new Runnable() {
          public void run() {
            fBoardTable.setModel(new DefaultTableModel(
                fBoardInformation, fBoardHeader));
            repaint();
          }
        };
      }
    };
    UGUIEventQueue.getInstance().addIGUIEvent(iev);
  }

  public void updateOrderObserver() {
    if (!isVisible()) {
      return;
    }
    IGUIEvent iev = new IGUIEvent() {
      public void execute() {
        dataUpdate(); // �������ɍX�V���K�v
      }

      public Runnable getRunnableForInvokeLater() {
        return new Runnable() {
          public void run() {
            fBoardTable.setModel(new DefaultTableModel(
                fBoardInformation, fBoardHeader));
            repaint();
          }
        };
      }
    };
    UGUIEventQueue.getInstance().addIGUIEvent(iev);
  }
} //  @jve:decl-index=0:visual-constraint="10,10"
