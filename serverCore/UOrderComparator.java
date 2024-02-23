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
 * UOrder�I�u�W�F�N�g��������������
 * �\�[�g���邽�߂ɗ��p����Comparator�ł��D
 */
public class UOrderComparator implements Comparator {

  /**
   * �����`��(���s����C�w�l�C���s�����̏�)�C���i(����)�C
   * ���蔃��(����C�����̏�)�C�񂹊���(���蒍���͏����C����������
   * �~��), �񂹊��Ԃ����������Ɋւ��Ă̓����_���Ɏ����������Œ�����
   * �\�[�g���邽�߂ɗ��p����܂��D
   * @param o1 ����1
   * @param o2 ����2
   * @return ����o1 < o2�Ȃ��-1, o1 > o2�Ȃ��+1
   */
  public int compare(Object o1, Object o2) {
    UOrder u1 = (UOrder) o1;
    UOrder u2 = (UOrder) o2;
    int u1M = getMarketLimitPriority(u1);
    int u2M = getMarketLimitPriority(u2);
    if (u1M < u2M) {
      return -1;
    } else if (u1M > u2M) {
      return 1;
    }
    if (u1.getPrice() < u2.getPrice()) {
      return -1;
    } else if (u1.getPrice() > u2.getPrice()) {
      return 1;
    }
    if (u1.getSellBuy() < u2.getSellBuy()) {
      return -1;
    } else if (u1.getSellBuy() > u2.getSellBuy()) {
      return 1;
    }
    if (u1.getSellBuy() == UOrder.SELL) {
      if (u1.getSession() < u2.getSession()) {
        return -1;
      } else if (u1.getSession() > u2.getSession()) {
        return 1;
      }
    } else {
      if (u1.getSession() < u2.getSession()) {
        return 1;
      } else if (u1.getSession() > u2.getSession()) {
        return -1;
      }
    }
    if (u1.getRandomNumber() < u2.getRandomNumber()) {
      return -1;
    } else {
      return 1;
    }
  }

  /**
   * �����`���ɂ��D�揇�ʂ�Ԃ��܂��D
   * @param o ����
   * @return �����u���s����v�Ȃ��0, �u�w�l�v�Ȃ��1, �u���s�����v�Ȃ��2
   */
  private final int getMarketLimitPriority(UOrder o) {
    if (o.getMarketLimit() == UOrder.MARKET) {
      if (o.getSellBuy() == UOrder.SELL) {
        return 0;
      } else {
        return 2;
      }
    } else {
      return 1;
    }
  }

}
