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

import java.util.*;

/**
 * ������{�����ɓ��������N���X�D0�����l�̐擪�ɂ���ꍇ��
 * �������l���Ă���D
 */
public class UStringNumberElement extends UTwoPartsElement {
  /**
   * p0342001 �� p342001 �ƕ\������Ă��܂��D�\�[�g�̐�����ۂ��߂ɁC�擪��0�͕�����̕��� �܂߂�D
   */
  public UStringNumberElement(String s) {
    //�O���� null �͂�₱�����̂œ���Ȃ��D�����Ȃ��Ă��󕶎��������D
    fPrefix_ = s.replaceAll("[1-9]+[0-9]*\\b", ""); // �Ō�̐��������������D�擪��0�ȊO
    //�㕔��
    String suf = s.replaceAll("^.*\\D0*", ""); // �ŏ��̐����ȊO�����������D������0*�͊܂ށD

    if (suf.equals("")) {
      //�����������Ȃ��ꍇ�͑�񕔕��͂Ȃ�
      fSuffix_ = null;
    } else {
      fSuffix_ = Long.valueOf(suf);
    }
  }

  /**
   * �e�N���X���I�[�o�[���C�h�D������0����������̂�Ԃ��D
   * �������Ȃ��ƁCa01 > a10 �ɂȂ��Ă��܂��D
   *
   * @return
   */
  public Object getPrefix() {
    String s = ( (String) fPrefix_).replaceAll("0+\\b", "");
    return s;
  }

  public static void main(String[] args) {
    UStringNumberElement s1 = new UStringNumberElement("art001");
    UStringNumberElement s2 = new UStringNumberElement("art0001");
    UStringNumberElement s3 = new UStringNumberElement("art100");
    System.out.println(s1);
    System.out.println(s1.getPrefix());
    System.out.println(s1.getSuffix());
    System.out.println(s2);
    System.out.println(s2.getPrefix());
    System.out.println(s2.getSuffix());
    System.out.println(s3);
    System.out.println(s3.getPrefix());
    System.out.println(s3.getSuffix());
    Object[] array = new Object[3];
    array[0] = s3;
    array[1] = s1;
    array[2] = s2;
    Arrays.sort(array);
    for (int i = 0; i < array.length; i++) {
      System.out.println(array[i]);
    }

  }
}