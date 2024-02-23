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
 * �ߋ��̌�������₢���킹��SVMP�R�}���h�̒��ۃN���X�ł��D
 */
public abstract class UCAccountHistoryCore implements ICommand {

  /** �R�}���h�� */
  public static final String CMD_NAME = "AccountHistory";

  /** �R�}���h�ւ̈����F�Ώۃ��[�U�[�̃��[�U�[ID�i-1�F�������g�j */
  protected int fTargetUserId;

  /** �R�}���h�ւ̈����F�ߋ��������̏�񂪕K�v���H�i-1�F�S�Ă̏��j */
  protected long fNoOfDays;

  /** �R�}���h�̎��s���ʂ̏�� */
  protected UCommandStatus fCommandStatus;

  /** �ߋ��̌������̔z�� */
  protected ArrayList fArray;

  /** ���������������߂̃L�[ */
  public static final String HASHMAP_DATA = "HASHMAP_DATA";

  /** ���[�U�[�����������߂̃L�[ */
  public static final String STRING_NAME = "STRING_NAME";

  /** ���[�U�[ID���������߂̃L�[ */
  public static final String INT_USER_ID = "INT_USER_ID";

  /** �X�e�b�v�����������߂̃L�[ */
  public static final String INT_STEP = "INT_STEP";

  /** ���������v���������߂̃L�[ */
  public static final String LONG_UNREALIZED_PROFIT = "LONG_UNREALIZED_PROFIT";

  /** ���|�W�V�����̍��v���������߂̃L�[ */
  public static final String LONG_SELL_POSITION = "LONG_SELL_POSITION";

  /** ���|�W�V�����̍��v���������߂̃L�[ */
  public static final String LONG_BUY_POSITION = "LONG_BUY_POSITION";

  /**
   * �R���X�g���N�^�ł��D
   */
  public UCAccountHistoryCore() {
    super();
    fCommandStatus = new UCommandStatus();
    fArray = new ArrayList();
  }

  /*
   * (non-Javadoc)
   * @see cmdCore.ICommand#isNameEqualTo(java.lang.String)
   */
  public boolean isNameEqualTo(String name) {
    if (name.equalsIgnoreCase(CMD_NAME)) {
      return true;
    } else {
      return false;
    }
  }

  /*
   * (non-Javadoc)
   * @see cmdCore.ICommand#getName()
   */
  public String getName() {
    return CMD_NAME;
  }

  /*
   * (non-Javadoc)
   * @see cmdCore.ICommand#setArguments(java.util.StringTokenizer)
   */
  public boolean setArguments(StringTokenizer st) {
    try {
      fTargetUserId = Integer.parseInt(st.nextToken());
      fNoOfDays = Long.parseLong(st.nextToken());
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * �R�}���h�����s���邽�߂ɕK�v��������ݒ肷��D
   * @param targetUserId ���[�UID
   * @param noOfDays �������̏�񂪕K�v���H
   */
  public void setArguments(int targetUserId, long noOfDays) {
    fTargetUserId = targetUserId;
    fNoOfDays = noOfDays;
  }

  /*
   * (non-Javadoc)
   * @see cmdCore.ICommand#printOn()
   */
  public void printOn() {
    System.out.println(fArray.toString());
  }

  /**
   * �R�}���h�����s�������ʂł���ߋ��̌������(HashMap�^)���i�[���ꂽ�z���Ԃ��D
   * @return �ߋ��̌������(HashMap�^)���i�[���ꂽ�z��
   */
  public ArrayList getData() {
    return fArray;
  }

  /*
   * (non-Javadoc)
   * @see cmdCore.ICommand#getResultString()
   */
  public String getResultString() {
    String returnStr = "";
    Iterator fArrayItr = fArray.iterator();
    while (fArrayItr.hasNext()) {
      HashMap data = (HashMap) fArrayItr.next();
      returnStr += data.get(UCAccountHistoryCore.STRING_NAME).toString() + "\n";
      returnStr += data.get(UCAccountHistoryCore.INT_USER_ID).toString() + "\n";
      returnStr += data.get(UCAccountHistoryCore.INT_STEP).toString() + "\n";
      returnStr += data.get(UCAccountHistoryCore.LONG_UNREALIZED_PROFIT).toString() + "\n";
      returnStr += data.get(UCAccountHistoryCore.LONG_SELL_POSITION).toString() + "\n";
      returnStr += data.get(UCAccountHistoryCore.LONG_BUY_POSITION).toString() + "\n";
    }
    return returnStr;
  }

}
