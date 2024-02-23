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
 * ���O�C�����邽�߂�SVMP�R�}���h�̒��ۃN���X�ł��D
 */
abstract public class UCLoginCore implements ICommand {

  /** �R�}���h�� */
  public static final String CMD_NAME = "Login";

  /** �ʖ� */
  public static final String CMD_ALIAS = "101";

  /** �R�}���h�̎��s��� */
  protected UCommandStatus fStatus;

  /** ���[�U�[�� */
  protected String fUserName;

  /** �p�X���[�h */
  protected String fPasswd;

  /**
   * �R���X�g���N�^�ł��D
   */
  public UCLoginCore() {
    super();
    fStatus = new UCommandStatus();
    fUserName = null;
    fPasswd = null;
  }

  /* (non-Javadoc)
   * @see cmdCore.ICommand#getName()
   */
  public String getName() {
    return CMD_NAME;
  }

  /* (non-Javadoc)
   * @see cmdCore.ICommand#isNameEqualTo(java.lang.String)
   */
  public boolean isNameEqualTo(String name) {
    if (name.equalsIgnoreCase(CMD_NAME) || name.equals(CMD_ALIAS)) {
      return true;
    } else {
      return false;
    }
  }

  /* (non-Javadoc)
   * @see cmdCore.ICommand#setArguments(java.util.StringTokenizer)
   */
  public boolean setArguments(StringTokenizer st) {
    try {
      fUserName = st.nextToken();
      fPasswd = st.nextToken();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * ������ݒ肵�܂��D
   * @param userName ���[�U��
   * @param passwd �p�X���[�h
   * @return true:����, false:���s
   */
  public boolean setArguments(String userName, String passwd) {
    fUserName = userName;
    fPasswd = passwd;
    return true;
  }

  /* (non-Javadoc)
   * @see cmdCore.ICommand#printOn()
   */
  public void printOn() {
    if (fStatus.getStatus()) {
      System.out.println("Login as " + fUserName + " succeeded.");
    } else {
      System.out.println("Login as " + fUserName + " failed.");
    }
  }

  /* (non-Javadoc)
   * @see cmdCore.ICommand#getResultString()
   */
  public String getResultString() {
    return null;
  }

}
