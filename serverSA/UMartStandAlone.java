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
package serverSA;

import serverCore.ULoginManager;
import serverCore.UMart;
import serverCore.UMemberList;
import serverCore.UPriceInfoDB;

/**
 * �X�^���h�A�������p�̎�����N���X�ł��D
 */
public class UMartStandAlone extends UMart {

  /**
   * UMartStandAlone�I�u�W�F�N�g�̐�������я��������s���܂��F<br>
   * - �G�[�W�F���g�͒N�����O�C�����Ă��Ȃ����<br>
   * - ���t��1�C�񂹉񐔂�1<br>
   * - ��Ԃ�BEFORE_TRADING<br>
   * @param members ������X�g
   * @param priceInfoDB ���i�n��f�[�^�x�[�X
   * @param startPoint ���i�n��̉��Ԗڂ̃f�[�^�������̃f�[�^�Ƃ��邩
   * @param seed �����̎�
   * @param maxDate �T�[�o�[�^�p����
   * @param noOfSessionsPerDay ���������̐ߐ�
   */
  public UMartStandAlone(UMemberList members, UPriceInfoDB priceInfoDB,
                         int startPoint, long seed,
                         int maxDate, int noOfSessionsPerDay) {
    super(members, priceInfoDB, startPoint, seed,
          maxDate, noOfSessionsPerDay);
  }

  /**
   * @see serverCore.UMart#createLoginManager()
   */
  protected ULoginManager createLoginManager() {
    return new ULoginManager();
  }

}
