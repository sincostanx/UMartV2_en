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

/**
 * �\�[�g�\�ȃe�[�u���N���X. 
 * Swing �� Thread safe �łȂ����� Exception ��f�����Ƃ���D
 */
public class UTable extends JTable {

  protected USortDecorator fDecorator;

  //  protected boolean fIsAscending = true;
  //�e��Ńf�t�H���g�̏����~�������߂���悤�ɂ���B
  protected boolean[] fDefaultAscending;

  protected boolean[] fCurrentAscending;

  //�e�񂪃\�[�g�\���ǂ���
  protected boolean[] fIsSortable;

  protected int fSortedColumn = -1;

  protected JTableHeader fTableHeader;

  protected MouseListener fSortMouseListener;

  //Table Header �̃}�E�X���X�i�[�ꎞ�ޔ�p
  protected MouseListener[] fMouseListenerArrayOfHeader;

  //�N���b�N���̎��ԁD
  protected long fClickTime = 0;

  //  �N���b�N��, fClickDeltaTime �Ԃ� Mouse Event ���󂯕t���Ȃ��D
  protected long fClickDeltaTime = 500; //�~���b

  //setModel�̎��ԁD
  protected long fSetModelTime = 0;

  //  setModel ��, fSetModelDeltaTime �Ԃ� Table �̍X�V�����Ȃ��D
  //�ɒ[�ɑ����X�V�͕K�� Exception ��f�����߁D
  protected long fSetModelDeltaTime = 500; //�~���b

  /**
   * �N���b�N�ɂ��\�[�g���������� MouseListener �͖̂����N���X���g���Ă��������낢��ύX�̕K�v�������ē����N���X���B
   * �ł��A���͂��̕K�v���Ȃ��Ȃ��������̂܂܂ɂ��Ă����B
   */
  protected class UMouseListener extends MouseAdapter {
    public void mouseClicked(MouseEvent e) {
      //�N���b�N���ꂽ���Ԃ��L�^
      long currentTime = System.currentTimeMillis();
      //�N���b�N����͍ă\�[�g���Ȃ��B
      if (currentTime - fClickTime < fClickDeltaTime) {
        fClickTime = currentTime;
        return;
      }
      fClickTime = currentTime;
      //���݃\�[�g������������@���Ȃ��̂�Ctrl+Click �Ń\�[�g�������B
      int ctrlPressed = e.getModifiers() & InputEvent.CTRL_MASK;
      if (ctrlPressed != 0) {
        fSortedColumn = -1;
        return;
      }
      if (getRowCount() < 2) { //0,1 �̓\�[�g�̕K�v�Ȃ�
        return;
      }
      TableColumnModel tcm = getColumnModel();
      int columnNum = tcm.getColumnIndexAtX(e.getX());
      //Sortable �łȂ���͏I���B
      if (!fIsSortable[columnNum]) {
        return;
      }
      //JTableHeader �� default ��2�� MouseListener �������Ă���̂� Click Event ��
      //�������邽�߂ɑS���폜�D�A���N���b�N�΍�D
      //removeAllMouseListenerFromHeader();
      int mc = convertColumnIndexToModel(columnNum);
      int shiftPressed = e.getModifiers() & InputEvent.SHIFT_MASK;
      if (shiftPressed == 0) {
        fCurrentAscending[columnNum] = fDefaultAscending[columnNum];
      } else {
        fCurrentAscending[columnNum] = !fDefaultAscending[columnNum];
      }
      fDecorator.sort(mc, fCurrentAscending[columnNum]);
      fSortedColumn = mc;
      //��ō폜���� MouseListener ��߂��DfClickDeltaTime �o���Ȃ��Ƃ��߁D
      //TODO MouseEvent ���󂯂Ȃ��悤�ɂ��邢�����@�Ȃ��́I�H
      //returnAllMouseListenerToHeader();
    }
  };

  public UTable() {
    super();
  }

  public UTable(Object[][] data, Object[] header) {
    super(data, header);
    fDefaultAscending = new boolean[getColumnCount()];
    fCurrentAscending = new boolean[getColumnCount()];
    fIsSortable = new boolean[getColumnCount()];
    Arrays.fill(fDefaultAscending, true);
    Arrays.fill(fCurrentAscending, true);
    Arrays.fill(fIsSortable, true);
    fTableHeader = getTableHeader();
    //      setModel(getModel());
    addMouseListenerToHeader();
  }

  public UTable(Vector data, Vector header) {
    super(data, header);
    fDefaultAscending = new boolean[getColumnCount()];
    fCurrentAscending = new boolean[getColumnCount()];
    fIsSortable = new boolean[getColumnCount()];
    Arrays.fill(fDefaultAscending, true);
    Arrays.fill(fCurrentAscending, true);
    Arrays.fill(fIsSortable, true);
    fTableHeader = getTableHeader();
    //      setModel(getModel());
    addMouseListenerToHeader();
  }

  /*
   * TODO �Ȃ����͂킩��Ȃ����Cconstracta ������� setModel �����Ă΂�Ă���D super(data,header)
   * �ŌĂ΂�Ă���H ���ł��낤??
   */
  public void setModel(TableModel dataModel) {
    long currentTime = System.currentTimeMillis();
    //setModel����͍�setModel���Ȃ��D
    if (currentTime - fSetModelTime < fSetModelDeltaTime) {
      return;
    }
    fSetModelTime = currentTime;
    // �h���b�O���͉��������ɏI���B
    if (fTableHeader != null && fTableHeader.getDraggedDistance() != 0) {
      return;
    }

    //�ύX�����̕ω���ۑ����邽�ߌ��݂̔z����o���Ă����āC���f�����Z�b�g��������
    //���ёւ��Ă���D�G���K���g���Ɍ����邪����ȊO�ǂ�����΂�����������Ȃ������D
    //�����2003�N�̃S�[���f���E�B�[�N���Ԃꂽ...
    int column = getColumnCount();
    int[] order = new int[column];
    for (int i = 0; i < column; i++) {
      order[i] = convertColumnIndexToModel(i);
    }
    fDecorator = new USortDecorator(dataModel);
    //�ߋ��̃\�[�g�����󂯌p���D!= null �̏����͂�낵���Ȃ��Ȃ��D
    //if (fSortedColumn >= 0 && fCurrentAscending != null) {
    if (fSortedColumn >= 0 && fCurrentAscending != null) {
      fDecorator.sort(fSortedColumn, fCurrentAscending[fSortedColumn]);
    }
    super.setModel(fDecorator);
    //�ߋ��̗����V�����e�[�u���ɔ��f�D
    for (int i = 0; i < column; i++) {
      super.moveColumn(convertColumnIndexToView(order[i]), i);
    }
  }

  public Class getColumnClass(int column) {
    Object target = null;
    //�e�[�u���ɂ�null�������̂ŁA���߂�null�ȊO���ł�܂Œ��ׂ�B
    //�������A�����̃I�u�W�F�N�g������ꍇ�͑z�肵�Ȃ��B
    for (int i = 0; i < fDecorator.getRowCount(); i++) {
      if (fDecorator.getValueAt(i, column) != null) {
        target = fDecorator.getValueAt(i, column);
        break;
      }
    }
    if (target == null) {
      return java.lang.Object.class;
    }
    Class c = target.getClass();
    if (c == java.lang.String.class) {
      return java.lang.Number.class; // String ���E��
    } else if (c == java.lang.Number.class) {
      return java.lang.Number.class;
    } else if (c == gui.UFormatLong.class) {
      return java.lang.Number.class;
    } else if (c == gui.UFormatDouble.class) {
      return java.lang.Number.class;
    } else if (c == UTwoPartsElement.class) {
      return java.lang.Number.class;
    } else {
      return c;
    }
  }

  public boolean isCellEditable(int r, int c) {
    return false;
  }

  protected void addMouseListenerToHeader() {
    if (fTableHeader == null) {
      return;
    }
    fSortMouseListener = new UMouseListener();
    fTableHeader.addMouseListener(fSortMouseListener);
  }

  /**
   * ��i �̃f�t�H���g�����~���𓾂�B
   *
   * @param i
   * @return true: ���� false: �~��
   */
  protected boolean getDefaultAscendingAt(int i) {
    return fDefaultAscending[i];
  }

  /**
   * ��̃f�t�H���g�����~�����Z�b�g
   *
   * @param b
   *            ��S���̏����L�����z�� true: ���� false: �~��
   */
  protected void setDefaultAscending(boolean ba[]) {
    fDefaultAscending = ba;
  }

  /**
   * ��� i �̃f�t�H���g�����~�����Z�b�g
   *
   * @param i
   *            �Z�b�g�����
   * @param b
   *            �����~�����e true: ���� false: �~��
   */
  protected void setDefaultAscendingAt(int i, boolean b) {
    fDefaultAscending[i] = b;
  }

  /**
   * �� i ���\�[�g�\���ǂ���
   *
   * @param i
   *            ��
   * @return
   */
  protected boolean getSortableAt(int i) {
    return fIsSortable[i];
  }

  /**
   * �� i ���\�[�g�\���ǂ����Z�b�g����B
   *
   * @param i
   *            ��
   * @param b
   *            true: �\�[�g�� false: �\�[�g�s��
   * @return
   */
  protected void setSortableAt(int i, boolean b) {
    fIsSortable[i] = b;
  }

  public void removeAllMouseListenerFromHeader() {
    if (fTableHeader == null) {
      return;
    }
    //MouseListener �ꎞ�ޔ��D
    fMouseListenerArrayOfHeader = fTableHeader.getMouseListeners();
    for (int i = 0; i < fMouseListenerArrayOfHeader.length; i++) {
      fTableHeader.removeMouseListener(fMouseListenerArrayOfHeader[i]);
    }
  }

  public void returnAllMouseListenerToHeader() {
    if (fTableHeader == null) {
      return;
    }
    for (int i = 0; i < fMouseListenerArrayOfHeader.length; i++) {
      fTableHeader.addMouseListener(fMouseListenerArrayOfHeader[i]);
    }
  }

  public static void main(String args[]) {
    int[][] priceData = { {1960, 20, 15}
        , {2000, 22, 21}
        , {2004, 21, 11}
        , {2010, 12, 1}
        , {2002, 32, 4}
        , {2050, -2, 7}
        , {1, 2, 3}
    }; // 6x3
    String[] header = {"Price", "Sell", "Buy", "Name"};
    Object[][] data = new Object[priceData.length][priceData[0].length + 1];
    Object[] toph = new Object[4];
    int i, j;
    for (i = 0; i < priceData.length; i++) {
      for (j = 0; j < priceData[0].length; j++) {
        data[i][j] = new Integer(priceData[i][j]);
      }
    }
    data[0][0] = new JButton();
    for (i = 0; i < priceData.length; i++) {
      Integer itmp = new Integer(i * 2 + 1);
      data[i][3] = "member" + itmp.toString();
    }
    for (j = 0; j < header.length; j++) {
      toph[j] = header[j];
    }
    UTable t = new UTable(data, toph);
    JFrame f = new JFrame();
    f.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
    f.getContentPane().add(new JScrollPane(t));
    f.setBounds(new Rectangle(300, 300, 450, 300));
    f.setVisible(true);
  }

}