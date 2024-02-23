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
package serverCore;

/**
 * �e���[�U�[(�����o�[)�ɑ΂���ڑ����̃N���C�A���g�����Ǘ����܂��D
 * U-Mart�V�X�e���ł́C���ꃆ�[�U���������O�C�����邱�Ƃ������Ă��邽�߁C
 * �ڑ����̃N���C�A���g����ێ����Ă��܂��D
 */

public class ULoginStatus {

  /** �ڑ����̃N���C�A���g�� */
  private int fNoOfLoginAgents;

  /** ���[�U�[ID */
  private int fUserID;

  /**
   * ULoginStatus�I�u�W�F�N�g�𐶐�, ���������܂��D
   * @param userID ���[�U�[ID
   */
  public ULoginStatus(int userID) {
    fUserID = userID;
    fNoOfLoginAgents = 0;
  }

  /**
   * ���[�U�[ID��Ԃ��܂��D
   * @return ���[�U�[ID
   */
  public int getUserID() {
    return fUserID;
  }

  /**
   * ���O�C�����̃��[�U�[����1���������܂��D
   */
  synchronized public void incrementNoOfLoginAgents() {
    fNoOfLoginAgents++;
    // System.out.println("No of userID" + fUserID + " is " + fNoOfLoginAgents);
  }

  /**
   * ���O�C�����̃��[�U�[����1�������炵�܂��D
   */
  synchronized public void decrementNoOfLoginAgents() {
    if (fNoOfLoginAgents-- < 0) {
      System.out.println("Agent number is minus");
      System.exit(1);
    }
    System.out.println("No of userID" + fUserID + " is " + fNoOfLoginAgents);
  }

  /**
   * ���O�C�����̃��[�U�[����Ԃ��܂��D
   * @return ���O�C�����̃��[�U�[��
   */
  synchronized public int getNoOfLoginAgents() {
    return fNoOfLoginAgents;
  }

}
