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
 * �����Ƃ̎n�l, ���l, ���l, �I�l, �o������ێ����邽�߂̃N���X�ł��D
 */
public class UHistoricalQuote {

  /** ������ */
  private String fBrandName;

  /** ���t */
  private int fDate;

  /** �n�l */
  private long fStartPrice;

  /** ���l */
  private long fHighestPrice;

  /** ���l */
  private long fLowestPrice;

  /** �I�l */
  private long fEndPrice;

  /** �o���� */
  private long fVolume;

  /**
   * UHistoricalQuote�I�u�W�F�N�g�̐���, ���������s���܂��D
   */
  public UHistoricalQuote() {
    fBrandName = null;
    fDate = -1;
    fStartPrice = -1;
    fHighestPrice = -1;
    fLowestPrice = -1;
    fEndPrice = -1;
    fVolume = -1;
  }

  /**
   * UHistoricalQuote�I�u�W�F�N�g�̐���, ���������s���܂��D
   * @param brandName ����
   * @param date ���t
   * @param startPrice �n�l
   * @param highestPrice ���l
   * @param lowestPrice ���l
   * @param endPrice �I�l
   * @param volume �o����
   */
  public UHistoricalQuote(String brandName, int date, 
  		                     long startPrice, long highestPrice, 
  		                     long lowestPrice, long endPrice,
                           long volume) {
    fBrandName = brandName;
    fDate = date;
    fStartPrice = startPrice;
    fHighestPrice = highestPrice;
    fLowestPrice = lowestPrice;
    fEndPrice = endPrice;
    fVolume = volume;
  }

  /**
   * UHistoricalQuote�I�u�W�F�N�g�̕�����Ԃ��܂��D
   * @return UHistoricalQuote�I�u�W�F�N�g�̕���
   */
  public Object clone() {
    return new UHistoricalQuote(fBrandName, fDate, fStartPrice, fHighestPrice,
                                  fLowestPrice, fEndPrice, fVolume);
  }

  /**
   * ���������o�͂��܂��D
   */
  public void printOn() {
    System.out.print("BrandName:" + fBrandName);
    System.out.print(", Date:" + fDate);
    System.out.print(", StartPrice:" + fStartPrice);
    System.out.print(", HighestPrice:" + fHighestPrice);
    System.out.print(", LowestPrice:" + fLowestPrice);
    System.out.print(", EndPrice:" + fEndPrice);
    System.out.println(", Volume:" + fVolume);
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
   * �n�l��Ԃ��܂��D
   * @return �n�l
   */
  public long getStartPrice() {
    return fStartPrice;
  }

  /**
   * ���l��Ԃ��܂��D
   * @return ���l
   */
  public long getHighestPrice() {
    return fHighestPrice;
  }

  /**
   * ���l��Ԃ��܂��D
   * @return ���l
   */
  public long getLowestPrice() {
    return fLowestPrice;
  }

  /**
   * �I�l��Ԃ��܂��D
   * @return �I�l
   */
  public long getEndPrice() {
    return fEndPrice;
  }

  /**
   * �o������Ԃ��܂��D
   * @return �o����
   */
  public long getVolume() {
    return fVolume;
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
   * �n�l��ݒ肵�܂��D
   * @param startPrice �n�l
   */
  public void setStartPrice(long startPrice) {
    fStartPrice = startPrice;
  }

  /**
   * ���l��ݒ肵�܂��D
   * @param highestPrice ���l
   */
  public void setHighestPirce(long highestPrice) {
    fHighestPrice = highestPrice;
  }

  /**
   * ���l��ݒ肵�܂��D
   * @param lowestPrice ���l
   */
  public void setLowestPrice(long lowestPrice) {
    fLowestPrice = lowestPrice;
  }

  /**
   * �I�l��ݒ肵�܂��D
   * @param endPrice �I�l
   */
  public void setEndPrice(long endPrice) {
    fEndPrice = endPrice;
  }

  /**
   * �o������ݒ肵�܂��D
   * @param volume �o����
   */
  public void setVolume(long volume) {
    fVolume = volume;
  }

}
