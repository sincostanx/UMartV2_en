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

/**
 * �e�[�u���ŕ\���ƃ\�[�g���̔�r�@�𕪗����邽�߂̃N���X�B
 * prefix �� suffix ��2����Ȃ�\���������B
 * �Ⴆ�΁Aagent0, agent100 �Ȃǂ𐳊m�ɕ\���������ꍇ�ɂ�
 * "agent"+Integer �Ƃ���Ηǂ��B ��r�� prefix ���D�悳��A
 * prefix ����v�����ꍇ�̂� suffix �̌��ʂ��e��
 */
public class UTwoPartsElement implements Comparable {
  protected Object fPrefix_;
  protected Object fSuffix_;

  /**
   *
   */
  public UTwoPartsElement() {
    fPrefix_ = null;
    fSuffix_ = null;
  }

  public UTwoPartsElement(Object pre, Object suf) {
    fPrefix_ = pre;
    fSuffix_ = suf;
  }

  /**
   * �������������̏ꍇ�������ƍl������̂ŃR���X�g���N�^������Ă����B
   * @param pre
   * @param num
   */
  public UTwoPartsElement(Object pre, char num) {
    fPrefix_ = pre;
    fSuffix_ = new Long(num);
  }

  /**
   * �������������̏ꍇ�������ƍl������̂ŃR���X�g���N�^������Ă����B
   * @param pre
   * @param num
   */
  public UTwoPartsElement(Object pre, int num) {
    fPrefix_ = pre;
    fSuffix_ = new Long(num);
  }

  /**
   * �������������̏ꍇ�������ƍl������̂ŃR���X�g���N�^������Ă����B
   * @param pre
   * @param num
   */
  public UTwoPartsElement(Object pre, long num) {
    fPrefix_ = pre;
    fSuffix_ = new Long(num);
  }

  /*
   * ���݂� UTwoPartsElement �ȊO�̌^�Ƃ̔�r�͍l���Ă��Ȃ��B����N���X�̃`�F�b�N�������
   * �d���Ȃ邽�߁B�L���X�g�������Ȃ��ꍇ�͑z��O�B�ꉞ try catch �ŗ����Ȃ��悤�ɂ���B
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(Object u2Parts) {
    int result = 0;
    Object pre = null, suf = null;
    try {
      pre = ( (UTwoPartsElement) u2Parts).getPrefix();
      suf = ( (UTwoPartsElement) u2Parts).getSuffix();
    } catch (Exception e) {
      System.err.println(
          "\"u2Parts\" of compareTo(Object u2Parts) in UTwoPartsElement.java must be an instance of UTwoPartsElement!");
      e.printStackTrace();
    }
    try {
      //			null �͍ŏ�
      if (getPrefix() == null && pre == null) {
        return 0;
      } else if (getPrefix() != null && pre == null) {
        return 1;
      } else if (getPrefix() == null && pre != null) {
        return -1;
      }
      result = ( (Comparable) getPrefix()).compareTo( (Comparable) pre);
      if (result == 0) {
        if (getSuffix() == null && suf == null) {
          return 0;
        } else if (getSuffix() != null && suf == null) {
          return 1;
        } else if (getSuffix() == null && suf != null) {
          return -1;
        }
        result = ( (Comparable) getSuffix()).compareTo( (Comparable) suf);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  public String toString() {
    String p, s;
    if (fPrefix_ == null) {
      p = "";
    } else {
      p = fPrefix_.toString();
    }

    if (fSuffix_ == null) {
      s = "";
    } else {
      s = fSuffix_.toString();
    }
    return p.toString() + s.toString();
  }

  /*
      public static void main(String[] args) {
          UTwoPartsElement[] array = new UTwoPartsElement[10];
          String pre = "agent";
          String[] str = new String[10];
          Integer[] num = new Integer[10];
          for (int i = 0; i < 10; i++) {
              num[i] = new Integer(i + i % 2 * 100);
              str[i] = pre + num[i];
              array[i] = new UTwoPartsElement(pre, num[i]);
          }
          array[9] = new UTwoPartsElement();
                  array[9].setPrefix(pre);
                  array[9].setSuffix(null);
          Arrays.sort(str);
          for (int i = 0; i < str.length; i++) {
              System.out.println(str[i]);
          }
          System.err.println("array:");
          Arrays.sort(array);
          for (int i = 0; i < array.length; i++) {
              System.out.println(i + " " + array[i]);
          }
      }
   */
  /**
   * @return
   */
  public Object getPrefix() {
    return fPrefix_;
  }

  /**
   * @return
   */
  public Object getSuffix() {
    return fSuffix_;
  }

  /**
   * @param object
   */
  public void setPrefix(Object object) {
    fPrefix_ = object;
  }

  /**
   * @param object
   */
  public void setSuffix(Object object) {
    fSuffix_ = object;
  }

  /**
   * @param num
   */
  public void setSuffix(char num) {
    fSuffix_ = new Long(num);
  }

  /**
   * @param num
   */
  public void setSuffix(int num) {
    fSuffix_ = new Long(num);
  }

  /**
   * @param object
   */
  public void setSuffix(long num) {
    fSuffix_ = new Long(num);
  }
}
