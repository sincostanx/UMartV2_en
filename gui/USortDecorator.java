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

import javax.swing.event.*;
import javax.swing.table.*;

/**
 * �e�[�u���\�[�g�p�N���X�DDecorator �p�^�[���K�p�D
 */
class USortDecorator implements TableModel, TableModelListener {
  protected TableModel fRealModel;
  protected int fIndices[];
  public USortDecorator(TableModel model) {
    if (model == null) {
      throw new IllegalArgumentException("null model!");
    }
    this.fRealModel = model;
    fRealModel.addTableModelListener(this);
    allocated();
    //  fIsSortable = new boolean[fRealModel.getColumnCount()];
  }

  public Object getValueAt(int row, int column) {
    return fRealModel.getValueAt(fIndices[row], column);
  }

  public void setValueAt(Object aValue, int row, int column) {
    fRealModel.setValueAt(aValue, fIndices[row], column);
  }

  public void tableChanged(TableModelEvent e) {
    allocated();
  }

  // ����synchronized �͖{���ɂ���̂��H
  public void sort(int column, boolean isAscending) {
    if (getRowCount() == 0) {
      return;
    }
    int direction;
    if (isAscending) {
      direction = 1;
    } else {
      direction = -1;
    }
    int[] indexSrc = new int[fIndices.length]; // ���݂�index�����R�s�[
    System.arraycopy(fIndices, 0, indexSrc, 0, fIndices.length);
    mergeSort(indexSrc, fIndices, 0, fIndices.length, column, direction); //���̊֐��ɂ�� fIndices ���X�V�����B
  }

  /**
   * jdk �� Arrays.java mergeSort �Q�l�B
   * �X�s�[�h�̂��߂ɗ��񂪕K�v�B���̃N���X��p�Ŕėp�֐��ł͂Ȃ��B
   * @param indexSrc ���̔z��
   * @param indexDest �ŏI�I�ȃR�s�[��
   * @param left �z��̌����J�n�_�B���ʂ�0
   * @param right �z��̌����I���_�B���ʂ�indexSrc.length
   * @param column ���������
   * @param direction �������~���� 1: ���� -1:�~��
   */
  protected void mergeSort(int indexSrc[], int indexDest[], int left, int right,
                           int column, int direction) {
    int length = right - left;
    int THRESHOLD = 9;
    // Insertion sort on smallest arrays
    // ������x�������ȗ̈�ł͑}���\�[�g�̕��������B
    if (length < THRESHOLD) {
      for (int i = left; i < right; i++) {
        for (int j = i; j > left
             && direction * compare(indexDest[j - 1], indexDest[j], column) > 0;
             j--) {
          swap(indexDest, j, j - 1);
        }
      }
      return;
    }

    // Recursively sort halves of dest into src
    // �������Ċe�������ċA�I�Ƀ\�[�g
    int destLow = left;
    int destHigh = right;
    int mid = (left + right) >> 1;
    mergeSort(indexDest, indexSrc, left, mid, column, direction);
    mergeSort(indexDest, indexSrc, mid, right, column, direction);

    // If list is already sorted, just copy from src to dest.  This is an
    // optimization that results in faster sorts for nearly ordered lists.
    if (direction * compare(indexSrc[mid - 1], indexSrc[mid], column) <= 0) {
      System.arraycopy(indexSrc, left, indexDest, destLow, length);
      return;
    }

    // Merge sorted halves (now in src) into dest
    for (int i = destLow, p = left, q = mid; i < destHigh; i++) {
      if (q >= right ||
          p < mid && direction * compare(indexSrc[p], indexSrc[q], column) <= 0) {
        indexDest[i] = indexSrc[p++];
      } else {
        indexDest[i] = indexSrc[q++];
      }
    }
  }

  protected void swap(int[] x, int i, int j) {
    int tmp = x[i];
    x[i] = x[j];
    x[j] = tmp;
  }

  protected void swap(int i, int j) {
    int tmp = fIndices[i];
    fIndices[i] = fIndices[j];
    fIndices[j] = tmp;
  }

  public int compare(int i, int j, int column) {
    // Check for nulls.
    Object io = fRealModel.getValueAt(i, column);
    Object jo = fRealModel.getValueAt(j, column);
    if (io == null && jo == null) {
      return 0;
    } else if (io == null) { // Define null less than everything.
      return -1;
    } else if (jo == null) {
      return 1;
    }
    int result = 0;
    try {
      result = ( (Comparable) io).compareTo(jo);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return result;
  }

  private void allocated() {
    fIndices = new int[getRowCount()];
    for (int i = 0; i < fIndices.length; i++) {
      fIndices[i] = i;
    }
  }

  //delegation to TableModel
  public int getRowCount() {
    return fRealModel.getRowCount();
  }

  public int getColumnCount() {
    return fRealModel.getColumnCount();
  }

  public String getColumnName(int i) {
    return fRealModel.getColumnName(i);
  }

  public Class getColumnClass(int i) {
    return fRealModel.getColumnClass(i);
  }

  public boolean isCellEditable(int r, int c) {
    return fRealModel.isCellEditable(r, c);
  }

  public void addTableModelListener(TableModelListener tl) {
    fRealModel.addTableModelListener(tl);
  }

  public void removeTableModelListener(TableModelListener tl) {
    fRealModel.removeTableModelListener(tl);
  }
}
