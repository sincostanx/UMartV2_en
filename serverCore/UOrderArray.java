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
package serverCore;

import java.util.*;

/**
 * ���[�U�[��l���̒����W�����Ǘ�����N���X�ł��D
 * ������, ���S�ɖ�肵��������, ����蕪���c���Ă��钍���ɕ����ĊǗ�����܂��D
 */
public class UOrderArray {

  /** ���[�U�[ID */
  private int fUserID;

  /** ���ς݂̒����W�� */
  private Vector fContractedOrders;

  /** �����̒����W�� */
  private Vector fUncontractedOrders;

  /** �������� */
  private ArrayList fHistory;

  /**
   * userID�������[�U�[��UOrderArray���쐬���܂��D
   * @param userID ���[�U�[ID
   */
  public UOrderArray(int userID) {
    fUserID = userID;
    fContractedOrders = new Vector();
    fUncontractedOrders = new Vector();
    fHistory = new ArrayList();
  }

  /**
   * ����������Ԃ��܂��D
   * @return ���������̃��X�g
   */
  public ArrayList getHistory() {
    return fHistory;
  }

  /**
   * userID��Ԃ��܂��D
   * @return ���[�U�[ID
   */
  public int getUserID() {
    return fUserID;
  }

  /**
   * ��蕪�̒����W����Ԃ��܂��D
   * @return ��蕪�̒����W��
   */
  public Vector getContractedOrders() {
    return fContractedOrders;
  }

  /**
   * ����蕪�̒����W����Ԃ��܂��D
   * @return ����蕪�̒����W��
   */
  public Vector getUncontractedOrders() {
    return fUncontractedOrders;
  }

  /**
   * ������ǉ�����D���̃��\�b�h��, �ǉ����ꂽ������
   * ����蕪���܂ނ��`�F�b�N��, �K�؂ɕ��ނ��܂��D
   * @param o ����
   */
  public void addOrder(UOrder o) {
    if (o.getUncontractOrderVolume() > 0) {
      fUncontractedOrders.addElement(o);
    } else if (o.getUncontractOrderVolume() == 0) {
      fContractedOrders.addElement(o);
    } else {
      System.out.println("Order volume is under 0");
      System.exit(1);
    }
  }

  /**
   * ��蕪�̒�����S�Ď�菜���܂��D
   */
  public void removeAllContractedOrders() {
    fContractedOrders.removeAllElements();
  }

  /**
   * ����蕪�̒�����S�Ď�菜���܂��D
   */
  public void removeAllUncontractedOrders() {
    fUncontractedOrders.removeAllElements();
  }

  /**
   * ��蕪�̒�������Ԃ��܂��D
   * @return ��蕪�̒�����
   */
  public int getNoOfContractedOrders() {
    return fContractedOrders.size();
  }

  /**
   * ����蕪�̒�������Ԃ��܂��D
   * @return ����蕪�̒�����
   */
  public int getNoOfUncontractedOrders() {
    return fUncontractedOrders.size();
  }

  /**
   * index�Ŏw�肳����蕪�̒�����Ԃ��܂��D
   * @param index �Y����
   * @return ����
   */
  public UOrder getContractedOrderAt(int index) {
    return (UOrder) fContractedOrders.elementAt(index);
  }

  /**
   * index�Ŏw�肳��関��蕪�̒�����Ԃ��܂��D
   * @param index �Y����
   * @return ����
   */
  public UOrder getUncontractedOrderAt(int index) {
    return (UOrder) fUncontractedOrders.elementAt(index);
  }

  /**
   * �e�X�g�p���C�����\�b�h�ł��D
   */
  public static void main(String args[]) {
    UOrderArray uo = new UOrderArray(1);
    for (int i = 0; i < 10; ++i) {
      UOrder o = new UOrder();
      o.setUserID(i + 1);
      o.setBrandName("j300119991200000");
      o.setOrderID(i + 1);
      o.setDate(i + 1);
      o.setSession(i % 3 + 1);
      o.setSellBuy(UOrder.SELL);
      o.setMarketLimit(UOrder.MARKET);
      o.setNewRepay(UOrder.REPAY);
      o.setPrice(2500 + i * 10);
      o.setVolume(i + 20);
      o.setRandomInteger(1234);
      o.setContractVolume(i + 2);
      o.setCancelVolume(i * 2);
      uo.addOrder(o);
    }
    System.out.println("UserID : " + uo.getUserID());
    System.out.println("Contracted Order Number is " + uo.getNoOfContractedOrders());
    System.out.println("Uncontracted Order Number is " + uo.getNoOfUncontractedOrders());
    uo.getContractedOrderAt(0).printOn(System.out);
  }
}
