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
 * ���Ɖ�̂��߂�SVMP�R�}���h�̒��ۃN���X�ł��D
 */
public abstract class UCExecutionsCore implements ICommand {

  /** ���ID���������߂̃L�[(�l��Long�I�u�W�F�N�g) */
  public static final String LONG_CONTRACT_ID = "LONG_CONTRACT_ID";

  /** ��莞��(������)���������߂̃L�[(�l��String�I�u�W�F�N�g) */
  public static final String STRING_CONTRACT_TIME = "STRING_CONTRACT_TIME";

  /** ����ID���������߂̃L�[(�l��Long�I�u�W�F�N�g) */
  public static final String LONG_ORDER_ID = "LONG_ORDER_ID";

  /** ����ID���������߂̃L�[(�l��String�I�u�W�F�N�g) */
  public static final String STRING_BRAND_NAME = "STRING_BRAND_NAME";

  /** �V�K�ԍϋ敪���������߂̃L�[(�l��Integer�I�u�W�F�N�g) */
  public static final String INT_NEW_REPAY = "INT_NEW_REPAY";

  /** �����敪���������߂̃L�[(�l��Integer�I�u�W�F�N�g) */
  public static final String INT_SELL_BUY = "INT_SELL_BUY";

  /** ���i���������߂̃L�[(�l��Long�I�u�W�F�N�g) */
  public static final String LONG_PRICE = "LONG_PRICE";

  /** ��萔�ʂ��������߂̃L�[(�l��Long�I�u�W�F�N�g) */
  public static final String LONG_VOLUME = "LONG_VOLUME";

  /** �R�}���h�� */
  public static final String CMD_NAME = "Executions";

  /** �ʖ� */
  public static final String CMD_ALIAS = "204";

  /** �R�}���h�̎��s��� */
  protected UCommandStatus fStatus;

  /** ���ʂ��i�[���邽�߂�ArrayList */
  protected ArrayList fExecutionsArray;

  /**
   * �R���X�g���N�^�ł��D
   */
  public UCExecutionsCore() {
    super();
    fExecutionsArray = new ArrayList();
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
    Iterator itr = fExecutionsArray.iterator();
    while (itr.hasNext()) {
      HashMap os = (HashMap) itr.next();
      result += os.get(LONG_CONTRACT_ID) + " "
                 + os.get(STRING_CONTRACT_TIME) + " "
                 + os.get(LONG_ORDER_ID) + " "
                 + os.get(STRING_BRAND_NAME) + " "
                 + os.get(INT_NEW_REPAY) + " "
                 + os.get(INT_SELL_BUY) + " "
                 + os.get(LONG_PRICE) + " "
                 + os.get(LONG_VOLUME);
    }
    return result;
  }

  /**
   * ������Ԃ��܂��D
   * @return �����
   */
  public ArrayList getResults() {
    return fExecutionsArray;
  }

  /**
   * @see cmdCore.ICommand#doIt()
   */
  public abstract UCommandStatus doIt();

  /**
   * @see cmdCore.ICommand#printOn()
   */
  public void printOn() {
    System.out.println("<<Executions>>");
    Iterator itr = fExecutionsArray.iterator();
    while (itr.hasNext()) {
      HashMap os = (HashMap) itr.next();
      System.out.println("ContractID:" + os.get(LONG_CONTRACT_ID) + ","
                          + "ContractTime:" + os.get(STRING_CONTRACT_TIME) + ","
                          + "OrderID:" + os.get(LONG_ORDER_ID) + ","
                          + "BrandName:" + os.get(STRING_BRAND_NAME) + ","
                          + "NewRepay:" + os.get(INT_NEW_REPAY) + ","
                          + "SellBuy:" + os.get(INT_SELL_BUY) + ","
                          + "Price:" + os.get(LONG_PRICE) + ","
                          + "Volume:" + os.get(LONG_VOLUME));
    }
  }
}
