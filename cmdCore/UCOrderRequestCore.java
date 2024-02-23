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
 * �����˗��̂��߂�SVMP�R�}���h�̒��ۃN���X�ł��D
 */
public abstract class UCOrderRequestCore implements ICommand {

  /** ����ID���������߂̃L�[(�l��Long�I�u�W�F�N�g) */
  public static final String LONG_ORDER_ID = "LONG_ORDER_ID";

  /** ��������(������)���������߂̃L�[(�l��String�I�u�W�F�N�g) */
  public static final String STRING_ORDER_TIME = "STRING_ORDER_TIME";

  /** �R�}���h�� */
  public static final String CMD_NAME = "OrderRequest";

  /** �ʖ� */
  public static final String CMD_ALIAS = "201";

  /** ������ */
  protected String fBrandName;

  /** �V�K�ԍϋ敪 */
  protected int fNewRepay;

  /** �����敪 */
  protected int fSellBuy;

  /** ���s�w�l�敪 */
  protected int fMarketLimit;

  /** ��]������i */
  protected long fPrice;

  /** ��]������� */
  protected long fVolume;

  /** �R�}���h�̎��s��� */
  protected UCommandStatus fStatus;

  /** ���ʂ��i�[���邽�߂�HashMap */
  protected HashMap fRequestInfo;

  /**
   * �R���X�g���N�^�ł��D
   */
  public UCOrderRequestCore() {
    super();
    fBrandName = "";
    fNewRepay = -1;
    fSellBuy = -1;
    fMarketLimit = -1;
    fPrice = -1;
    fVolume = -1;
    fRequestInfo = new HashMap();
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
    try {
      fBrandName = st.nextToken();
      fNewRepay = Integer.parseInt(st.nextToken());
      fSellBuy = Integer.parseInt(st.nextToken());
      fMarketLimit = Integer.parseInt(st.nextToken());
      fPrice = Long.parseLong(st.nextToken());
      fVolume = Long.parseLong(st.nextToken());
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   * �R�}���h���s�ɕK�v�Ȉ�����ݒ肵�܂��D
   * @param brandName ������
   * @param newRepay �V�K�ԍϋ敪
   * @param sellBuy �����敪
   * @param marketLimit�@���s�w�l�敪
   * @param price ��]������i
   * @param volume�@��]�������
   */
  public void setArguments(String brandName, int newRepay, int sellBuy,
                           int marketLimit, long price, long volume) {
    fBrandName = brandName;
    fNewRepay = newRepay;
    fSellBuy = sellBuy;
    fMarketLimit = marketLimit;
    fPrice = price;
    fVolume = volume;
  }

  /**
   * @see cmdCore.ICommand#getResultString()
   */
  public String getResultString() {
    String result = fRequestInfo.get(LONG_ORDER_ID) + " "
        + fRequestInfo.get(STRING_ORDER_TIME);
    return result;
  }

  /**
   * �����˗�����Ԃ��܂��D
   * @return �����˗����
   */
  public HashMap getResults() {
    return fRequestInfo;
  }

  /**
   * @see cmdCore.ICommand#doIt()
   */
  public abstract UCommandStatus doIt();

  /**
   * @see cmdCore.ICommand#printOn()
   */
  public void printOn() {
    System.out.println("<<OrderRequest " + fBrandName + fNewRepay + fSellBuy
                       + fMarketLimit + fPrice + fVolume + ">>");
    System.out.println("OrderID:" + fRequestInfo.get(LONG_ORDER_ID) + ","
                       + "OrderTime:" + fRequestInfo.get(STRING_ORDER_TIME));
  }
}
