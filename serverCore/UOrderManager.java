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
 * �S�Ẵ��[�U�[�̒����Ƃ��̗������Ǘ�����N���X�ł��D
 * �����́C���[�U�[���Ƃ�UOrderArray��p���ĊǗ�����Ă��܂��D
 * ����C���������́CArrayList��p���Ĉꊇ�Ǘ�����Ă��܂��D
 */
public class UOrderManager {

  /** �������� */
  private ArrayList fOrderHistory;

  /** �e���[�U�[�̒������Ǘ�����UOrderArray�I�u�W�F�N�g�̃x�N�^ */
  private Vector fOrderArrays;

  /** ����ID�𔭐����邽�߂̃J�E���^ */
  static private long fOrderID = 1;

  /** ����񂹊��ԓ��̒������\�[�g���邽�߂̗��������� */
  private Random fRandom;

  /**
   * UOrderManager�����������܂��D
   */
  public UOrderManager() {
    fOrderArrays = new Vector();
    fOrderHistory = new ArrayList();
    fRandom = URandom.getInstance();
  }

  /**
   * ����ID��Ԃ��܂��D�S������ʂ��Ē���ID���d�Ȃ�Ȃ��悤�ɁC
   * ���̒���ID�𗘗p����Ƃ悢�ł��D
   * @return ����ID
   */
  public static long getOrderID() {
    long result = fOrderID;
    ++fOrderID;
    return result;
  }

  /**
   * ���̐����̗�����Ԃ��܂��D
   * @return ���̐����̗���
   */
  public final int getRandomInteger() {
    return Math.abs(fRandom.nextInt());
  }

  /**
   * userID���������o�[�̑S�Ă̒������������܂��D
   * @param userID ���������������������o�[�̃��[�U�[ID
   */
  public void cancelAllOrdersOfMember(int userID) {
    UOrderArray orderArray = getOrderArray(userID);
    if (orderArray == null) {
      System.out.print("Member" + userID + " cannot be found ");
      System.out.println("in UOrderManager.cancelAllOrdersOfMember");
      return;
    }
    Enumeration orders = orderArray.getUncontractedOrders().elements();
    while (orders.hasMoreElements()) {
      UOrder o = (UOrder) orders.nextElement();
      o.cancel();
      if (o.getContractVolume() > 0) {
        orderArray.getContractedOrders().addElement(o);
      }
    }
    orderArray.getUncontractedOrders().removeAllElements();
  }

  /**
   * userID���������o�[�̑S�Ă̒�����Vector�ɓ���ĕԂ��܂��D
   * @param userID ���[�U�[ID
   * @return �w�肳�ꂽ�����o�[�̑S�Ă̒������܂�Vector
   */
  public Vector getAllOrders(int userID) {
    Vector result = new Vector();
    UOrderArray orderArray = getOrderArray(userID);
    if (orderArray == null) {
      System.out.print("Member" + userID + " cannot be found ");
      System.out.println("in UOrderManager.getExecutions");
      return result;
    }
    Enumeration contOrders = orderArray.getContractedOrders().elements();
    while (contOrders.hasMoreElements()) {
      UOrder o = (UOrder) contOrders.nextElement();
      result.addElement(o);
    }
    Enumeration uncontOrders = orderArray.getUncontractedOrders().elements();
    while (uncontOrders.hasMoreElements()) {
      UOrder o = (UOrder) uncontOrders.nextElement();
      result.addElement(o);
    }
    return result;
  }

  /**
   * userID���������o�[��orderID�Ŏw�肳��钍�����������܂��D
   * @param userID ���[�U�[ID
   * @param orderID ����ID
   * @return �������ꂽ�����D�w�肵�����������݂��Ȃ��ꍇ�Cnull��Ԃ��܂��D
   */
  public UOrder cancelOrder(int userID, long orderID) {
    // �w�肳�ꂽorderID�̒�����������Ȃ����null��Ԃ�
    UOrderArray orderArray = getOrderArray(userID);
    if (orderArray == null) {
      System.out.print("Member" + userID + " cannot be found ");
      System.out.println("in UOrderManager.cancelOrder");
      return null;
    }
    Vector uncontractedOrders = orderArray.getUncontractedOrders();
    int size = uncontractedOrders.size();
    int targetIndex = -1;
    for (int i = 0; i < size; ++i) {
      UOrder o = (UOrder) uncontractedOrders.elementAt(i);
      if (o.getOrderID() == orderID) {
        targetIndex = i;
        break;
      }
    }
    if (targetIndex < 0) {
      return null;
    }
    UOrder order = (UOrder) uncontractedOrders.remove(targetIndex);
    order.cancel();
    if (order.getContractVolume() > 0) {
      orderArray.getContractedOrders().addElement(order);
    }
    return order;
  }

  /**
   * ���ׂẴ����o�[�̑S�Ă̒�������菜���܂��D
   */
  public void removeAllOrders() {
    Enumeration e = fOrderArrays.elements();
    while (e.hasMoreElements()) {
      UOrderArray array = (UOrderArray) e.nextElement();
      array.removeAllContractedOrders();
      array.removeAllUncontractedOrders();
    }
  }

  /**
   * �S�����̗�������Y��������Ԃ��܂��D
   * @param orderID ����ID
   * @return ����
   */
  public UOrder getOrderFromHistory(long orderID) {
    int index = (int) orderID - 1;
    if (index < 0 || index >= fOrderHistory.size()) {
      return null;
    }
    return (UOrder) fOrderHistory.get(index);
  }

  /**
   * �����𗚗��֓o�^���܂��D
   * @param o ����
   */
  public void registerOrderToHistory(UOrder o) {
    fOrderHistory.add(o);
    UOrderArray orderArray = getOrderArray(o.getUserID());
    orderArray.getHistory().add(o);
  }

  /**
   * userID�̒����Ǘ��N���XUOrderArray�𐶐����܂��D
   * @param userID ���[�U�[ID
   * @return �����Ftrue, ���s: false (���ꃁ���o�[�����݂���ꍇ)
   */
  public boolean createOrderArray(int userID) {
    Enumeration e = fOrderArrays.elements();
    while (e.hasMoreElements()) {
      UOrderArray x = (UOrderArray) e.nextElement();
      if (x.getUserID() == userID) {
        System.err.println("The same UserID exists.");
        return false;
      }
    }
    fOrderArrays.addElement(new UOrderArray(userID));
    return true;
  }

  /**
   * ������ǉ����܂��D
   * @param o �ǉ�����
   * @return �����Ftrue, ���s: false (�Ή����郁���o�[��������Ȃ��Ƃ�)
   */
  public boolean addOrder(UOrder o) {
    Enumeration e = fOrderArrays.elements();
    while (e.hasMoreElements()) {
      UOrderArray x = (UOrderArray) e.nextElement();
      if (x.getUserID() == o.getUserID()) {
        x.addOrder(o);
        return true;
      }
    }
    return false;
  }

  /**
   * �����Ŏw�肳�ꂽ������V���ɍ쐬���܂��D
   * ���s�����̏ꍇ, �������i��0�ɐݒ肳��܂��D
   * �ʏ�, ����UOrder.createOrder���\�b�h�ŐV�K�������쐬���Ă���,
   * UOrder.addOrder���\�b�h��p���Ē�����ǉ����܂��D
   * @param userID ���[�U�[ID
   * @param userName ���[�U��
   * @param brandName ������
   * @param sellBuy �����敪 (��: UOrder.SELL, ��: UOrder.BUY)
   * @param marketLimit ���s�w�l�敪 (���s: UOrder.MARKET, �w�l: UOrder.LIMIT)
   * @param newRepay �V�K�ԍϋ敪 (�V�K: UOrder.NEW, �ԍ�: UOrder.LIMIT )
   * @param price �������i
   * @param volume ��������
   * @param date ������
   * @param session ������
   * @return ����
   */
  public UOrder createOrder(int userID, String userName, String brandName,
                            int sellBuy, int marketLimit, int newRepay, 
                            long price, long volume, int date, int session) {
    UOrder o = new UOrder();
    o.setUserID(userID);
    o.setUserName(userName);
    o.setBrandName(brandName);
    o.setOrderID(UOrderManager.getOrderID());
    o.setTime(new Date());
    o.setDate(date);
    o.setSession(session);
    o.setSellBuy(sellBuy);
    o.setMarketLimit(marketLimit);
    o.setNewRepay(newRepay);
    if (marketLimit == UOrder.MARKET) {
      o.setPrice(0);
    } else {
      o.setPrice(price);
    }
    o.setVolume(volume);
    o.setRandomInteger(this.getRandomInteger());
    return o;
  }

  /**
   * �����Ŏw�肳�ꂽ�����o�[�̒����W����Ԃ��܂��D
   * @param userID ���[�U�[ID
   * @return �����W�����Ǘ�����UOrderArray�I�u�W�F�N�g�D
   *          �Ή����郁���o�[��UOrderArray�I�u�W�F�N�g��
   *          ���݂��Ȃ��ꍇ, null��Ԃ��̂Œ��ӂ��邱�ƁD
   */
  public UOrderArray getOrderArray(int userID) {
    Enumeration e = fOrderArrays.elements();
    while (e.hasMoreElements()) {
      UOrderArray x = (UOrderArray) e.nextElement();
      if (x.getUserID() == userID) {
        return x;
      }
    }
    return null;
  }

  /**
   * ���l���̃����o�[��UOrderArray���Ǘ����Ă��邩�Ԃ��܂��D
   * @return �Ǘ����Ă���UOrderArray�̐�
   */
  public int getNoOfOrderArrays() {
    return fOrderArrays.size();
  }

  /**
   * �Ǘ����Ă���UOrderArray��񋓂��邽�߂�Enumeration��Ԃ��܂��D
   * @return �Ǘ����Ă���UOrderArray��񋓂��邽�߂�Enumeration
   */
  public Enumeration getOrderArrays() {
    return fOrderArrays.elements();
  }

}
