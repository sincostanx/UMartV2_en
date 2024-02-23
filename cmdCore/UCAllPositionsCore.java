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
 * ���ׂĂ̎Q���҂̔����|�W�V�������Ɖ�邽�߂�SVMP�R�}���h�̒��ۃN���X�ł��D
 */
public abstract class UCAllPositionsCore implements ICommand {

  /** �G�[�W�F���g�����������߂̃L�[(�l��String�I�u�W�F�N�g) */
  public static final String STRING_NAME = "STRING_NAME";

  /** ����܂ł̔��茚�ʐ����������߂̃L�[(�l��Long�I�u�W�F�N�g) */
  public static final String LONG_SELL_POSITION = "LONG_SELL_POSITION";

  /** ����܂ł̔������ʐ����������߂̃L�[(�l��Long�I�u�W�F�N�g) */
  public static final String LONG_BUY_POSITION = "LONG_BUY_POSITION";

  /** �R�}���h�� */
  public static final String CMD_NAME = "AllPositions";

  /** �R�}���h�̎��s��� */
  protected UCommandStatus fStatus;

  /** ���ʂ��i�[���邽�߂�ArrayList */
  protected ArrayList fAllPositionsArray;

  /**
   * �R���X�g���N�^�ł��D
   */
  public UCAllPositionsCore() {
    super();
    fAllPositionsArray = new ArrayList();
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
    return true;
  }

  /**
   * @see cmdCore.ICommand#getResultString()
   */
  public String getResultString() {
    String result = "";
    Iterator itr = fAllPositionsArray.iterator();
    while (itr.hasNext()) {
      HashMap os = (HashMap) itr.next();
      result += os.get(STRING_NAME) + " "
          + os.get(LONG_SELL_POSITION) + " "
          + os.get(LONG_BUY_POSITION) + " ";
    }
    return result;
  }

  /**
   * ���ׂĂ̎Q���҂̔����|�W�V������Ԃ��܂��D
   * @return ���ׂĂ̎Q���҂̔����|�W�V����
   */
  public ArrayList getResults() {
    return fAllPositionsArray;
  }

  /**
   * @see cmdCore.ICommand#doIt()
   */
  public abstract UCommandStatus doIt();

  /**
   * @see cmdCore.ICommand#printOn()
   */
  public void printOn() {
    System.out.println("<<AllPositions>>");
    Iterator itr = fAllPositionsArray.iterator();
    while (itr.hasNext()) {
      HashMap os = (HashMap) itr.next();
      System.out.println("Name:" + os.get(STRING_NAME) + ","
                         + "SellPosition:" + os.get(LONG_SELL_POSITION) + ","
                         + "BuyPosition:" + os.get(LONG_BUY_POSITION));
    }
  }
}
