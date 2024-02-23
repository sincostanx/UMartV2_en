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
 * ���C�߁C�T�[�o�[��Ԃ��Ǘ�����N���X�ł��D
 * �T�[�o�[��Ԃɂ́C������J�n�O���ԑсC������t���ԑсC
 * �񂹎��ԑсC�l�􂢎��ԑсC����㎞�ԑсC���Z���ԑсC
 * ���Z�㎞�ԑт�����܂��D
 */
public class UServerStatus {

  /** ������J�n�O���ԑт�\���萔 */
  public static final int BEFORE_TRADING = 0;

  /** ������t���ԑт�\���萔 */
  public static final int ACCEPT_ORDERS = 1;

  /** �񂹎��ԑт�\���萔 */
  public static final int ITAYOSE = 3;

  /** �l�􂢎��ԑт�\���萔 */
  public static final int MARKING_TO_MARKET = 4;

  /** ����㎞�ԑт�\���萔 */
  public static final int AFTER_MARKING_TO_MARKET = 5;

  /** ���Z���ԑт�\���萔 */
  public static final int SETTLEMENT = 6;

  /** ���Z�㎞�ԑт�\���萔 */
  public static final int AFTER_SETTLEMENT = 7;

  /** �X�[�p�[���[�U�[���O�C���҂����ԑ� */
  public static final int SU_LOGIN = 9;

  /** �T�[�o�[���(���ԑ�)��\��������̔z�� */
  private static final String[] SERVER_STATES = {"BEFORE_TRADING",
      "ACCEPT_ORDERS",
      "RESERVED",
      "ITAYOSE",
      "MARKING_TO_MARKET",
      "AFTER_MARKING_TO_MARKET",
      "SETTLEMENT",
      "AFTER_SETTLEMENT",
      "RESERVERD",
      "SU_LOGIN",
  };

  /** ���݂̓��t (U-Mart��) */
  private int fDate;

  /** ���݂̐� */
  private int fSession;

  /** �T�[�o�[��� (���ԑ�) */
  private int fState;

  /**
   * 	�R���X�g���N�^�ł��D
   */
  public UServerStatus() {
    fState = UServerStatus.SU_LOGIN;
    fDate = 1;
    fSession = 1;
  }

  /**
   * 	�R�s�[�R���X�g���N�^�ł��D
   */
  public UServerStatus(UServerStatus src) {
    fState = src.fState;
    fDate = src.fDate;
    fSession = src.fSession;
  }

  /**
   * �����𐶐����܂��D
   * @return ����
   */
  public Object clone() {
    return new UServerStatus(this);
  }

  /**
   * ������Ԃ��o�͂��܂��D
   */
  public void printOn() {
    System.out.println("Date:" + fDate
                       + ", Session:" + fSession
                       + ", Status:" + getStateString());
  }

  /**
   * �R�s�[���܂��D
   * @param src �R�s�[��
   */
  public void copyFrom(UServerStatus src) {
    fState = src.fState;
    fDate = src.fDate;
    fSession = src.fSession;
  }

  /**
     ������̏�Ԃ�ݒ肵�܂��D
     @param state ������̏��
   */
  public void setState(int state) {
    fState = state;
  }

  /**
     ������̏�Ԃ�Ԃ��܂��D
     @return ������̏��
   */
  public int getState() {
    return fState;
  }

  /**
     ������̏�Ԃ�\���������Ԃ��܂��D
     @return ������̏�Ԃ�\��������
   */
  public String getStateString() {
    return SERVER_STATES[fState];
  }

  /**
   * ���݂̓��t(U-Mart��)��Ԃ��܂��D
   * @return ���݂̓��t(U-Mart��)
   */
  public int getDate() {
    return fDate;
  }

  /**
   * ���݂̓��t(U-Mart��)��ݒ肵�܂��D
   * @param date ���݂̓��t(U-Mart��)
   */
  public void setDate(int date) {
    fDate = date;
  }

  /**
   * ���t��1���i�߂܂��D
   * @return 1���i�߂���̓��t
   */
  public int incrementDate() {
    ++fDate;
    return fDate;
  }

  /**
   * ���݂̐߂�Ԃ��܂��D
   * @return ���݂̐�
   */
  public int getSession() {
    return fSession;
  }

  /**
   * ���݂̐߂�ݒ肵�܂��D
   * @param session ��
   */
  public void setSession(int session) {
    fSession = session;
  }

  /**
   * �߂�1�i�߂܂��D
   * @return 1�i�߂���̐�
   */
  public int incrementSession() {
    ++fSession;
    return fSession;
  }

  /**
   * �߂�1�ɖ߂��܂��D
   * @return �񂹉�
   */
  public int resetSession() {
    fSession = 1;
    return fSession;
  }

}
