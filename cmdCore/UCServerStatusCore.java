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
 * �T�[�o��Ԃ��擾���邽�߂�SVMP�R�}���h�̒��ۃN���X�ł��D
 */
public abstract class UCServerStatusCore implements ICommand {

  /** �R�}���h�� */
  public static final String CMD_NAME = "ServerStatus";

  /** �R�}���h�̎��s���ʂ̏�� */
  protected UCommandStatus fCommandStatus;

  /** �T�[�o�[��� */
  protected HashMap fData;

  /** ���݂̓��t�iU-Mart��)���������߂̃L�[ */
  public static final String INT_DATE = "INT_DATE";

  /** ���݂̔񂹉񐔂��������߂̃L�[ */
  public static final String INT_BOARD_NO = "INT_BOARD_NO";

  /** ���ԑт��������߂̃L�[ */
  public static final String INT_STATE = "INT_STATE";

  /**
   * �R���X�g���N�^�ł��D
   */
  public UCServerStatusCore() {
    super();
    fCommandStatus = new UCommandStatus();
    fData = new HashMap();
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
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /*
   * (non-Javadoc)
   * @see cmdCore.ICommand#printOn()
   */
  public void printOn() {
    System.out.println(fData.toString());
  }

  /**
   * �T�[�o��Ԃ�Ԃ��D
   * @return �T�[�o���
   */
  public HashMap getData() {
    return fData;
  }

  /*
   * (non-Javadoc)
   * @see cmdCore.ICommand#getResultString()
   */
  public String getResultString() {
    String returnStr = "";
    returnStr += fData.get(UCServerStatusCore.INT_DATE).toString() + "\n";
    returnStr += fData.get(UCServerStatusCore.INT_BOARD_NO).toString() + "\n";
    returnStr += fData.get(UCServerStatusCore.INT_STATE).toString() + "\n";
    return returnStr;
  }

}
