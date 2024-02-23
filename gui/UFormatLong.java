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
package gui;

import java.text.*;

/**
 * �e�[�u���ŕ\���ƃ\�[�g���̔�r�@�𕪗����邽�߂̃N���X�D
 * �\����"�C"��؂�ŁC��r�͐��l�ōs���D
 */

public class UFormatLong implements Comparable {
  protected Long fNum_;
  public UFormatLong() {
    fNum_ = null;
  }

  public UFormatLong(Long val) {
    fNum_ = val;
  }

  public UFormatLong(Number val) {
    fNum_ = new Long(val.longValue());
  }

  public UFormatLong(String val) {
    fNum_ = new Long(val);
  }

  public UFormatLong(long val) {
    fNum_ = new Long(val);
  }

  /*
   * ���݂� UFormatLong �ȊO�̌^�Ƃ̔�r�͍l���Ă��Ȃ��B����N���X�̃`�F�b�N�������
   * �d���Ȃ邽�߁B�L���X�g�������Ȃ��ꍇ�͑z��O�B�ꉞ try catch �ŗ����Ȃ��悤�ɂ���B
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(Object uNum) {
    // null �͍ŏ�
    if (uNum == null) {
      return 1;
    }
    int result = 0;
    try {
      Long lNum = ( (UFormatLong) uNum).getNum();
      // null �͍ŏ�
      if (fNum_ == null && lNum == null) {
        return 0;
      } else if (fNum_ != null && lNum == null) {
        return 1;
      } else if (fNum_ == null && lNum != null) {
        return -1;
      }
      result = fNum_.compareTo( ( (UFormatLong) uNum).getNum());
    } catch (Exception e) {
      System.err.println("\"uNum\" of compareTo(Object uNum) in UFormatLong.java must be an instance of UFormatLong!");
      e.printStackTrace();
    }

    return result;
  }

  public String toString() {
    if (fNum_ == null) {
      return "";
    }
    return NumberFormat.getNumberInstance().format(fNum_);
  }

  /**
   * @return
   */
  public Long getNum() {
    return fNum_;
  }

  /**
   * @return
   */
  public long longValue() {
    return fNum_.longValue();
  }

  /**
   * @param val
   */
  public void setNum(Long val) {
    fNum_ = val;
  }

  public void setNum(long val) {
    fNum_ = new Long(val);
  }

  public void setNum(int val) {
    fNum_ = new Long(val);
  }
}
