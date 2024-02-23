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
package serverNet;

import serverCore.*;

/**
 * �l�b�g���[�N���œ��삷�������ł��D
 */
public class UMartNetwork extends UMart {

  /** �T�[�o�[�\�P�b�g�̃|�[�g�ԍ��̃f�t�H���g�l */
  public static final int DEFAULT_PORT = 5010;

  /** �T�[�o�[�\�P�b�g�̃|�[�g�ԍ� */
  private int fPort = DEFAULT_PORT;

  /**
   * �f�t�H���g�|�[�g��UMartNetwork�I�u�W�F�N�g�̐�������я��������s���܂��F<br>
   * - �G�[�W�F���g�͒N�����O�C�����Ă��Ȃ����<br>
   * - ���t��1�C�߂�1<br>
   * - ��Ԃ�BEFORE_TRADING<br>
   * @param members ������X�g
   * @param priceInfoDB ���i�n��f�[�^�x�[�X
   * @param startPoint ���i�n��̊J�n�|�C���g
   * @param seed �����̎�
   * @param maxDate �������
   * @param noOfSessionsPerDay 1��������̐ߐ�
   */
  public UMartNetwork(UMemberList members, UPriceInfoDB priceInfoDB,
                      int startPoint, long seed,
                      int maxDate, int noOfSessionsPerDay) {
    this(members, priceInfoDB, startPoint, seed, maxDate, noOfSessionsPerDay, DEFAULT_PORT);
  }

  /**
   * UMartNetwork�I�u�W�F�N�g�̐�������я��������s���܂��F<br>
   * - �G�[�W�F���g�͒N�����O�C�����Ă��Ȃ����<br>
   * - ���t��1�C�߂�1<br>
   * - ��Ԃ�BEFORE_TRADING<br>
   * @param members ������X�g
   * @param priceInfoDB ���i�n��f�[�^�x�[�X
   * @param startPoint ���i�n��̊J�n�|�C���g
   * @param seed �����̎�
   * @param maxDate �������
   * @param noOfSessionsPerDay 1��������̐ߐ�
   * @param port �T�[�o�[�\�P�b�g�̃|�[�g�ԍ�
   */
  public UMartNetwork(UMemberList members, UPriceInfoDB priceInfoDB,
                      int startPoint, long seed,
                      int maxDate, int noOfSessionsPerDay,
                      int port) {
    super(members, priceInfoDB, startPoint, seed, maxDate, noOfSessionsPerDay);
    fPort = port;
  }

  /**
   * @see serverCore.UMart#createLoginManager()
   */
  protected ULoginManager createLoginManager() {
    return new ULoginManagerNetwork(this);
  }
  
  /**
   * ���O�C���}�l�[�W�����N�����܂��D
   */
  public void startLoginManager() {
    ((ULoginManagerNetwork)fLoginManager).startLoop(fPort);
  }

}
