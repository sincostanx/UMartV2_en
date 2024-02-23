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
 * ���鉿�i�ɂ����锄�蒍�����Ɣ�����������ێ�����N���X�ł��D
 * UBoardInformation�N���X�����p���܂��D
 */
public class UBoardInfoElement {

  /** ���i�B������, ���s�����̂Ƃ���-1���ݒ肳��܂��D */
  private long fPrice;

  /** ���蒍���� */
  private long fSellVolume;

  /** ���������� */
  private long fBuyVolume;

  /**
   * UBoardInfoElement�̐�������я�����(���s����,
   * ����/����������:0)���s���܂��D
   */
  public UBoardInfoElement() {
    fPrice = -1;
    fSellVolume = 0;
    fBuyVolume = 0;
  }

  /**
   * UBoardInfoElement�̐�������я��������s���܂��D
   * @param price ���i
   * @param sellVolume ���蒍����
   * @param buyVolume ����������
   */
  public UBoardInfoElement(long price, long sellVolume, long buyVolume) {
    fPrice = price;
    fSellVolume = sellVolume;
    fBuyVolume = buyVolume;
  }

  /**
   * UBoardInfoElement�I�u�W�F�N�g�̕�����Ԃ��܂��D
   * @return UBoardInfoElement�I�u�W�F�N�g
   */
  public Object clone() {
    return new UBoardInfoElement(fPrice, fSellVolume, fBuyVolume);
  }

  /**
   * ���i��Ԃ��܂��D
   * @return ���i
   */
  public long getPrice() {
    return fPrice;
  }

  /**
   * ���蒍������Ԃ��܂��D
   * @return ���蒍����
   */
  public long getSellVolume() {
    return fSellVolume;
  }

  /**
   * ������������Ԃ��܂��D
   * @return ����������
   */
  public long getBuyVolume() {
    return fBuyVolume;
  }

  /**
   * ���i��ݒ肵�܂��D
   * @param price ���i
   */
  public void setPrice(long price) {
    fPrice = price;
  }

  /**
   * ���蒍������ݒ肵�܂��D
   * @param sellVolume ���蒍����
   */
  public void setSellVolume(long sellVolume) {
    fSellVolume = sellVolume;
  }

  /**
   * ���蒍������volume�������܂��D
   * @param volume ���蒍�����̑���
   */
  public void addToSellVolume(long volume) {
    fSellVolume += volume;
  }

  /**
   * ������������ݒ肵�܂��D
   * @param buyVolume ����������
   */
  public void setBuyVolume(long buyVolume) {
    fBuyVolume = buyVolume;
  }

  /**
   * ������������volume�������܂��D
   * @param volume �����������̑���
   */
  public void addToBuyVolume(long volume) {
    fBuyVolume += volume;
  }

  /**
   * ���s����(���i:-1), ����/������������0�ɏ��������܂��D
   */
  public void clear() {
    fPrice = -1;
    fBuyVolume = fSellVolume = 0;
  }

  /**
   * ���������o�͂��܂��D
   */
  public void printOn() {
    System.out.print("Price:" + fPrice);
    System.out.print(", fSellVolume:" + fSellVolume);
    System.out.println(", fBuyVolume:" + fBuyVolume);
  }

}
