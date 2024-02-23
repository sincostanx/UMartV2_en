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
 * �^�p�\��⍇�����s��SVMP�R�}���h�̒��ۃN���X�ł��D
 */
public abstract class UCScheduleCore implements ICommand {

  /** �T�[�o�^�p�������������߂̃L�[ */
  public static final String INT_MAX_DAY = "INT_MAX_DAY";

  /** �P���̔񂹉񐔂��������߂̃L�[ */
  public static final String INT_NO_OF_BOARDS = "INT_NO_OF_BOARDS";

  /** �R�}���h�� */
  public static final String CMD_NAME = "Schedule";

  /** �ʖ� */
  public static final String CMD_ALIAS = "";

  /** �R�}���h�̎��s��� */
  protected UCommandStatus fStatus;

  /** ���ʂ��i�[���邽�߂�HashMap */
  protected HashMap fScheduleInfo;

  /**
   * �R���X�g���N�^�ł��D
   */
  public UCScheduleCore() {
    super();
    fStatus = new UCommandStatus();
    fScheduleInfo = new HashMap();
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
   * @see ICommand#getResultString()
   */
  public String getResultString() {
    String result = fScheduleInfo.get(INT_MAX_DAY) + " "
        + fScheduleInfo.get(INT_NO_OF_BOARDS);
    return result;
  }

  /**
   * ���ʂ�Ԃ��܂��D
   * @return ����
   */
  public HashMap getResults() {
    return fScheduleInfo;
  }

  /**
   * @see ICommand#setArguments(StringTokenizer)
   */
  public boolean setArguments(StringTokenizer st) {
    return true;
  }

  /**
   * @see ICommand#doIt()
   */
  public abstract UCommandStatus doIt();

  /**
   * @see ICommand#printOn()
   */
  public void printOn() {
    System.out.println("<<" + CMD_NAME + ">>");
    System.out.println(fScheduleInfo.toString());
  }

}
