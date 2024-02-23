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
 * �ߋ��̓����ł̉��i���ڂ��擾���邽�߂�SVMP�R�}���h�̒��ۃN���X�ł��D
 */
public abstract class UCHistoricalQuotesCore implements ICommand {

  /** ���������������߂̃L�[(�l��String�I�u�W�F�N�g) */
  public static final String STRING_BRAND_NAME = "STRING_BRAND_NAME";

  /** ���t���������߂̃L�[(�l��Integer�I�u�W�F�N�g) */
  public static final String INT_DATE = "INT_DATE";

  /** �n�l���������߂̃L�[(�l��Long�I�u�W�F�N�g) */
  public static final String LONG_START_PRICE = "LONG_START_PRICE";

  /** ���l���������߂̃L�[(�l��Long�I�u�W�F�N�g) */
  public static final String LONG_HIGHEST_PRICE = "LONG_HIGHEST_PRICE";

  /** ���l���������߂̃L�[(�l��Long�I�u�W�F�N�g) */
  public static final String LONG_LOWEST_PRICE = "LONG_LOWEST_PRICE";

  /** �I�l���������߂̃L�[(�l��Long�I�u�W�F�N�g) */
  public static final String LONG_END_PRICE = "LONG_END_PRICE";

  /** �o�������������߂̃L�[(�l��Long�I�u�W�F�N�g) */
  public static final String LONG_VOLUME = "LONG_VOLUME";

  /** �R�}���h�� */
  public static final String CMD_NAME = "HistoricalQuotes";

  /** ������ */
  protected String fBrandName;

  /** �K�v�ȏ��̓��� */
  protected int fNoOfDays;

  /** �R�}���h�̎��s��� */
  protected UCommandStatus fStatus;

  /** ���ʂ��i�[���邽�߂�ArrayList */
  protected ArrayList fQuotesArray;

  /**
   * �R���X�g���N�^�ł��D
   */
  public UCHistoricalQuotesCore() {
    super();
    fBrandName = "";
    fNoOfDays = -1;
    fQuotesArray = new ArrayList();
    fStatus = new UCommandStatus();
  }

  /**
   * @see cmdCore.ICommand#isNameEqualTo(String)
   */
  public boolean isNameEqualTo(String name) {
    if (name.equalsIgnoreCase(CMD_NAME)) {
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
      fNoOfDays = Integer.parseInt(st.nextToken());
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   * �R�}���h���s�ɕK�v�Ȉ�����ݒ肵�܂��D
   * @param brandName ������
   * @param noOfDays ����
   */
  public void setArguments(String brandName, int noOfDays) {
    fBrandName = brandName;
    fNoOfDays = noOfDays;
  }

  /**
   * @see cmdCore.ICommand#getResultString()
   */
  public String getResultString() {
    String result = "";
    Iterator itr = fQuotesArray.iterator();
    while (itr.hasNext()) {
      HashMap os = (HashMap) itr.next();
      result += os.get(STRING_BRAND_NAME) + " "
          + os.get(INT_DATE) + " "
          + os.get(LONG_START_PRICE) + " "
          + os.get(LONG_HIGHEST_PRICE) + " "
          + os.get(LONG_LOWEST_PRICE) + " "
          + os.get(LONG_END_PRICE) + " "
          + os.get(LONG_VOLUME) + " ";
    }
    return result;
  }

  /**
   * �ߋ��̉��i���ڂ�Ԃ��܂��D
   * @return �ߋ��̉��i����
   */
  public ArrayList getResults() {
    return fQuotesArray;
  }

  /**
   * @see cmdCore.ICommand#doIt()
   */
  public abstract UCommandStatus doIt();

  /**
   * @see cmdCore.ICommand#printOn()
   */
  public void printOn() {
    System.out.println("<<HistoricalQuotes " + fBrandName + " " + fNoOfDays
                       + ">>");
    Iterator itr = fQuotesArray.iterator();
    while (itr.hasNext()) {
      HashMap os = (HashMap) itr.next();
      System.out.println("BrandName:" + os.get(STRING_BRAND_NAME) + ","
                         + "Date:" + os.get(INT_DATE) + ","
                         + "StartPrice:" + os.get(LONG_START_PRICE) + ","
                         + "HighestPrice:" + os.get(LONG_HIGHEST_PRICE) + ","
                         + "LowestPrice:" + os.get(LONG_LOWEST_PRICE) + ","
                         + "EndPrice:" + os.get(LONG_END_PRICE) + ","
                         + "Volume:" + os.get(LONG_VOLUME));
    }
  }
}
