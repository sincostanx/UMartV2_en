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
 * �T�[�o�o�ߖ⍇���̂��߂�SVMP�R�}���h�̒��ۃN���X�ł��D
 */
public abstract class UCServerDateCore implements ICommand {

  /** �o�ߓ������������߂̃L�[ */
  public static final String INT_DAY = "INT_DAY";

  /** ���݂̔񂹉񐔂��������߂̃L�[ */
  public static final String INT_BOARD_NO = "INT_BOARD_NO";

  /** �R�}���h�� */
  public static final String CMD_NAME = "ServerDate";

  /** �ʖ� */
  public static final String CMD_ALIAS = "";

  /** �R�}���h�̎��s��� */
  protected UCommandStatus fStatus;

  /** ���ʂ��i�[���邽�߂�HashMap */
  protected HashMap fServerDateInfo;

  /**
   * �R���X�g���N�^�ł��D
   */
  public UCServerDateCore() {
    super();
    fStatus = new UCommandStatus();
    fServerDateInfo = new HashMap();
  }

  /**
   * 	@see cmdCore.ICommand#isNameEqualTo(String)
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
   * @see cmdCore.ICommand#doIt()
   */
  public abstract UCommandStatus doIt();

  /**
   * @see cmdCore.ICommand#printOn()
   */
  public void printOn() {
    System.out.println("<<" + CMD_NAME + ">>");
    System.out.println(fServerDateInfo.toString());
  }

  /**
   * @see cmdCore.ICommand#getResultString()
   */
  public String getResultString() {
    String result = fServerDateInfo.get(INT_DAY) + " "
        + fServerDateInfo.get(INT_BOARD_NO);
    return result;
  }

  /**
   * ���ʂ�Ԃ��܂��D
   * @return ����
   */
  public HashMap getResults() {
    return fServerDateInfo;
  }

}
