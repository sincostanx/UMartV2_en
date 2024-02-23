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
package cmdCore;

import java.util.*;

/**
 * �����Ɖ�̂��߂�SVMP�R�}���h�̒��ۃN���X�ł��D
 */
public abstract class UCOrderStatusCore implements ICommand {

  /** ����ID���������߂̃L�[(�l��Long�I�u�W�F�N�g) */
  public static final String LONG_ORDER_ID = "LONG_ORDER_ID";

  /** ��������(������)���������߂̃L�[(�l��String�I�u�W�F�N�g) */
  public static final String STRING_ORDER_TIME = "STRING_ORDER_TIME";

  /** ���������������߂̃L�[(�l��String�I�u�W�F�N�g) */
  public static final String STRING_BRAND_NAME = "STRING_BRAND_NAME";

  /** �V�K�ԍϋ敪���������߂̃L�[(�l��Integer�I�u�W�F�N�g) */
  public static final String INT_NEW_REPAY = "INT_NEW_REPAY";

  /** �����敪���������߂̃L�[(�l��Integer�I�u�W�F�N�g) */
  public static final String INT_SELL_BUY = "INT_SELL_BUY";

  /** ���s�w�l�敪���������߂̃L�[(�l��Integer�I�u�W�F�N�g) */
  public static final String INT_MARKET_LIMIT = "INT_MARKET_LIMIT";

  /** ��]������i���������߂̃L�[(�l��Long�I�u�W�F�N�g) */
  public static final String LONG_PRICE = "LONG_PRICE";

  /** ��]������ʂ��������߂̃L�[(�l��Long�I�u�W�F�N�g) */
  public static final String LONG_VOLUME = "LONG_VOLUME";

  /** ��萔�ʂ��������߂̃L�[(�l��Long�I�u�W�F�N�g) */
  public static final String LONG_CONTRACTED_VOLUME = "LONG_CONTRACTED_VOLUME";

  /** ������ʂ��������߂̃L�[(�l��Long�I�u�W�F�N�g) */
  public static final String LONG_CANCEL_VOLUME = "LONG_CANCEL_VOLUME";

  /** �R�}���h�� */
  public static final String CMD_NAME = "OrderStatus";

  /** �ʖ� */
  public static final String CMD_ALIAS = "203";

  /** �R�}���h�̎��s��� */
  protected UCommandStatus fStatus;

  /** ���ʂ��i�[���邽�߂�ArrayList */
  protected ArrayList fStatusArray;

  /**
   * �R���X�g���N�^
   */
  public UCOrderStatusCore() {
    super();
    fStatusArray = new ArrayList();
    fStatus = new UCommandStatus();
  }

  /**
   * @see cmdCore.ICommand#isNameEqualTo(String)
   */
  public boolean isNameEqualTo(String name) {
    if (name.equalsIgnoreCase(CMD_NAME) || name.equals(CMD_ALIAS)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * @see cmdCore.ICommand#getName()
   */
  public String getName() {
    return CMD_NAME;
  }

  /**
   * @see cmdCore.ICommand#setArguments(StringTokenizer)
   */
  public boolean setArguments(StringTokenizer st) {
    return true;
  }

  /**
   * @see cmdCore.ICommand#getResultString()
   */
  public String getResultString() {
    String result = "";
    Iterator itr = fStatusArray.iterator();
    while (itr.hasNext()) {
      HashMap os = (HashMap) itr.next();
      result = os.get(LONG_ORDER_ID) + " "
          + os.get(STRING_ORDER_TIME) + " "
          + os.get(STRING_BRAND_NAME) + " "
          + os.get(INT_NEW_REPAY) + " "
          + os.get(INT_SELL_BUY) + " "
          + os.get(INT_MARKET_LIMIT) + " "
          + os.get(LONG_PRICE) + " "
          + os.get(LONG_VOLUME) + " "
          + os.get(LONG_CONTRACTED_VOLUME) + " "
          + os.get(LONG_CANCEL_VOLUME) + " ";
    }
    return result;
  }

  /**
   * �����Ɖ����Ԃ��D
   * @return �����Ɖ���
   */
  public ArrayList getResults() {
    return fStatusArray;
  }

  /**
   * @see cmdCore.ICommand#doIt()
   */
  public abstract UCommandStatus doIt();

  /**
   * @see cmdCore.ICommand#printOn()
   */
  public void printOn() {
    System.out.println("<<OrderStatus>>");
    Iterator itr = fStatusArray.iterator();
    while (itr.hasNext()) {
      HashMap os = (HashMap) itr.next();
      System.out.println("OrderID:" + os.get(LONG_ORDER_ID) + ","
                         + "OrderTime:" + os.get(STRING_ORDER_TIME) + ","
                         + "BrandName:" + os.get(STRING_BRAND_NAME) + ","
                         + "NewRepay:" + os.get(INT_NEW_REPAY) + ","
                         + "SellBuy:" + os.get(INT_SELL_BUY) + ","
                         + "MarketLimit:" + os.get(INT_MARKET_LIMIT) + ","
                         + "Price:" + os.get(LONG_PRICE) + ","
                         + "Volume:" + os.get(LONG_VOLUME) + ","
                         + "ContractVolume:" + os.get(LONG_CONTRACTED_VOLUME)
                         + ","
                         + "CancelVolume:" + os.get(LONG_CANCEL_VOLUME));
    }
  }
}
