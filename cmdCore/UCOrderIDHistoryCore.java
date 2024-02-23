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
 * ����ID�������擾���邽�߂�SVMP�R�}���h�̒��ۃN���X�ł��D
 */
public abstract class UCOrderIDHistoryCore implements ICommand {

  /** �R�}���h�� */
  public static final String CMD_NAME = "OrderIDHistory";

  /** �R�}���h�̎��s���ʂ̏�� */
  protected UCommandStatus fCommandStatus;

  /** ����ID�����𒲂ׂ������[�U�[��ID */
  protected int fTargetUserID;

  /** �ߋ��񂹉��񕪂̒���ID���~�������H */
  protected int fNoOfSteps;

  /** ����ID���� */
  protected ArrayList fOrderIDHistory;

  /**
   * �R���X�g���N�^
   */
  public UCOrderIDHistoryCore() {
    super();
    fCommandStatus = new UCommandStatus();
    fOrderIDHistory = new ArrayList();
    fTargetUserID = -1;
    fNoOfSteps = 0;
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
   * @see cmdCore.ICommand#getResultString()
   */
  public String getResultString() {
    String result = "";
    Iterator itr = fOrderIDHistory.iterator();
    while (itr.hasNext()) {
      Long id = (Long) itr.next();
      result += id.toString() + " ";
    }
    return result;
  }

  /**
   * ������ݒ肵�܂��D
   * @param targetUserID ���[�UID
   * @param noOfSteps�@�ߋ��񂹉��񕪂̒���ID���~�������H
   */
  public void setArguments(int targetUserID, int noOfSteps) {
    fTargetUserID = targetUserID;
    fNoOfSteps = noOfSteps;
  }

  /**
   * @see cmdCore.ICommand#setArguments(StringTokenizer)
   */
  public boolean setArguments(StringTokenizer st) {
    if (st.hasMoreTokens()) {
      fTargetUserID = Integer.parseInt(st.nextToken());
      fNoOfSteps = Integer.parseInt(st.nextToken());
      return true;
    } else {
      return false;
    }
  }

  /**
   * @see cmdCore.ICommand#printOn()
   */
  public void printOn() {
    System.out.println(getResultString());
  }

  /**
   * ����ID�̗�����Ԃ��܂��D
   * @return ����ID�̗���
   */
  public ArrayList getOrderIDHistory() {
    return fOrderIDHistory;
  }

}
