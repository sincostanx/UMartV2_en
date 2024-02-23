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
 * �񂹂��Ƃ̖�艿�i�Ɩ�萔�ʂ�ێ����邽�߂̃N���X�ł��D
 * �܂�, ���Ȃ��̐߂ɂ��Ă͔���C�z�Ɣ����C�z��
 * �ێ����Ă��܂��D
 */

public class UTodaysQuote {

  /** ������ */
  private String fBrandName;

  /** �� */
  private int fDate;

  /** �� */
  private int fSession;

  /** ��艿�i */
  private long fPrice;

  /** ��萔�� */
  private long fVolume;

  /** ����C�z */
  private long fAskedQuotation;

  /** �����C�z */
  private long fBidQuotation;

  /**
   * UTodaysQuote�I�u�W�F�N�g�̐���, ���������s���܂��D
   */
  public UTodaysQuote() {
    fBrandName = null;
    fDate = -1;
    fSession = -1;
    fPrice = -1;
    fVolume = -1;
    fAskedQuotation = -1;
    fBidQuotation = -1;
  }

  /**
   * UTodaysQuote�I�u�W�F�N�g�̐���, ���������s���܂��D
   * @param brandName ������
   * @param date ���t
   * @param session ��
   * @param price ��艿�i
   * @param volume ��萔��
   * @param askedQuotation ����C�z
   * @param bidQuotation �����C�z
   */
  public UTodaysQuote(String brandName, int date, int session, long price,
                       long volume, long askedQuotation, long bidQuotation) {
    fBrandName = brandName;
    fDate = date;
    fSession = session;
    fPrice = price;
    fVolume = volume;
    fAskedQuotation = askedQuotation;
    fBidQuotation = bidQuotation;
  }

  /**
   * UTodaysQuote�I�u�W�F�N�g�̕�����Ԃ��܂��D
   * @return UTodaysQuote�I�u�W�F�N�g�̕���
   */
  public Object clone() {
    return new UTodaysQuote(fBrandName, fDate, fSession, fPrice,
                              fVolume, fAskedQuotation, fBidQuotation);
  }

  /**
   * ���������o�͂��܂��D
   */
  public void printOn() {
    System.out.print("BrandName:" + fBrandName);
    System.out.print(", Date:" + fDate);
    System.out.print(", BoardNo:" + fSession);
    System.out.print(", Price:" + fPrice);
    System.out.print(", Volume:" + fVolume);
    System.out.print(", AskedQuotation:" + fAskedQuotation);
    System.out.println(", BidQuotation:" + fBidQuotation);
  }

  /**
   * ��������Ԃ��܂��D
   * @return ������
   */
  public String getBrandName() {
    return fBrandName;
  }

  /**
   * ���t��Ԃ��܂��D
   * @return ���t
   */
  public int getDate() {
    return fDate;
  }

  /**
   * �߂�Ԃ��܂��D
   * @return ��
   */
  public int getSession() {
    return fSession;
  }

  /**
   * ��艿�i��Ԃ��܂��D
   * @return ��艿�i
   */
  public long getPrice() {
    return fPrice;
  }

  /**
   * ��萔�ʂ�Ԃ��܂��D
   * @return ��萔��
   */
  public long getVolume() {
    return fVolume;
  }

  /**
   * ����C�z��Ԃ��܂��D
   * @return ����C�z
   */
  public long getAskedQuotation() {
    return fAskedQuotation;
  }

  /**
   * �����C�z��Ԃ��܂��D
   * @return �����C�z
   */
  public long getBidQuotation() {
    return fBidQuotation;
  }

  /**
   * ��������ݒ肵�܂��D
   * @param brandName ������
   */
  public void setBrandName(String brandName) {
    fBrandName = brandName;
  }

  /**
   * ���t��ݒ肵�܂��D
   * @param date ���t
   */
  public void setDate(int date) {
    fDate = date;
  }

  /**
   * �߂�ݒ肵�܂��D
   * @param session ��
   */
  public void setSession(int session) {
    fSession = session;
  }

  /**
   * ��艿�i��ݒ肵�܂��D
   * @param price ��艿�i
   */
  public void setPrice(long price) {
    fPrice = price;
  }

  /**
   * ��萔�ʂ�ݒ肵�܂��D
   * @param volume ��萔��
   */
  public void setVolume(long volume) {
    fVolume = volume;
  }

  /**
   * ����C�z��ݒ肵�܂��D
   * @param askedQuotation ����C�z
   */
  public void setAskedQuotation(long askedQuotation) {
    fAskedQuotation = askedQuotation;
  }

  /**
   * �����C�z��ݒ肵�܂��D
   * @param bidQuotation �����C�z
   */
  public void setBidQuotation(long bidQuotation) {
    fBidQuotation = bidQuotation;
  }

}
