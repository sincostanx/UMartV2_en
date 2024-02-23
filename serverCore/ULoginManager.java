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

import java.util.*;

/**
 * �S�����o�[�̃��O�C�����Ǘ����܂��D
 */

public class ULoginManager {

  /** �S�����o�[�̃��O�C����Ԃ��i�[���邽�߂̃x�N�^ */
  private Vector fLoginStatusArray;

  /**
   * ULoginStatusManager�I�u�W�F�N�g�̐�������я��������s���܂��D
   */
  public ULoginManager() {
    fLoginStatusArray = new Vector();
  }

  /**
   * �����ŗ^�����郆�[�U�[���ɂ��������ĐV�������O�C����Ԃ�
   * ����, �o�^���s���܂��D������, ���ꃆ�[�U�[���̐ڑ���Ԃ���邱�Ƃ�
   * �ł��܂���D
   * @param id ���[�U�[ID
   * @return true: ����, false: ���s
   */
  public boolean creatLoginStatus(int id) {
    Enumeration e = fLoginStatusArray.elements();
    while (e.hasMoreElements()) {
      ULoginStatus x = (ULoginStatus) e.nextElement();
      if (x.getUserID() == id) {
        System.out.print("ERROR:ULoginStatusManager::createLoginStatus: " + id);
        System.err.println(" has already existed.");
        return false;
      }
    }
    fLoginStatusArray.addElement(new ULoginStatus(id));
    return true;
  }

  /**
   * �����ŗ^�����郆�[�U�[ID�̃��O�C����Ԃ�Ԃ��܂��D
   * ������Ȃ��ꍇ��, null��Ԃ��܂��D
   * @param userID ���[�U�[ID
   * @return �Ή����郍�O�C����ԃI�u�W�F�N�g�B������, �Ή����鎖������
   *         ������Ȃ��ꍇ��null�B
   */
  public ULoginStatus findLoginStatus(int userID) {
    Enumeration e = fLoginStatusArray.elements();
    while (e.hasMoreElements()) {
      ULoginStatus x = (ULoginStatus) e.nextElement();
      if (x.getUserID() == userID) {
        return x;
      }
    }
    return null;
  }

  /**
   * �o�^����Ă���S�Ẵ��O�C����Ԃ��܂ރx�N�^��Ԃ��܂��D
   * @return ���O�C����Ԃ��܂ރx�N�^
   */
  public Vector getLoginStatuses() {
    return fLoginStatusArray;
  }

}
