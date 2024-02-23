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
 * UPriceInfoDB�ŗp�������X�e�b�v���̉��i������舵���N���X�ł��D
 * ���C�߁C�������i�C�敨���i��ێ����Ă��܂��D
 */
public class UPriceInfo {

	/** ���i���������Ă��Ȃ��Ƃ��ɗp����萔 */
  public static final long INVALID_PRICE = -1;

  /** �� */
  private int fDate;

  /** �� */
  private int fSession;

  /** �������i */
  private long fSpotPrice;

  /** �敨���i�B������, ���i�����݂��Ȃ��ꍇ��, -1���ݒ肳��܂��D*/
  private long fFuturePrice;

  /**
   * UPriceInfo�I�u�W�F�N�g�̐����Ə��������s���܂��D
   */
  public UPriceInfo() {
    fDate = 0;
    fSession = 0;
    fSpotPrice = UPriceInfo.INVALID_PRICE;
    fFuturePrice = UPriceInfo.INVALID_PRICE;
  }

  /**
   * �������i��Ԃ��܂��D
   * @return �������i
   */
  public long getSpotPrice() {
    return fSpotPrice;
  }

  /**
   * �敨���i��Ԃ��܂��D
   * @return �敨���i�B���i�����݂��Ȃ��ꍇ��, -1���Ԃ�܂��D
   */
  public long getFuturePrice() {
    return fFuturePrice;
  }

  /**
   * �������i��ݒ肵�܂��D
   * @param spotPrice �������i
   */
  public void setSpotPrice(long spotPrice) {
    fSpotPrice = spotPrice;
  }

  /**
   * �敨���i��ݒ肵�܂��D
   * @param futurePrice �敨���i�B���i�����݂��Ȃ��ꍇ�� -1��ݒ肵�܂��D
   */
  public void setFuturePrice(long futurePrice) {
    fFuturePrice = futurePrice;
  }

  /**
   * �߂�Ԃ��܂��D
   * @return ��
   */
  public int getSession() {
    return fSession;
  }

  /**
   * ����Ԃ��܂��D
   * @return ��
   */
  public int getDate() {
    return fDate;
  }

  /**
   * �߂�ݒ肵�܂��D
   * @param session ��
   */
  public void setSession(int session) {
    fSession = session;
  }

  /**
   * ����ݒ肵�܂��D
   * @param date ��
   */
  public void setDate(int date) {
    fDate = date;
  }

}
