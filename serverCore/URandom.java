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
 * �����������N���X�ł��D
 */
public class URandom {

  /** �����̎� */
  private static long fSeed;

  /** ���������� */
  private static Random fRandom = null;

  /**
   * �����̎���V�X�e�������ŏ��������������I�u�W�F�N�g��Ԃ��܂��D
   * @return �����I�u�W�F�N�g
   */
  public static Random getInstance() {
    if (fRandom == null) {
      fSeed = System.currentTimeMillis();
      fRandom = new Random(fSeed);
    }
    return fRandom;
  }

  /**
   * �����̎��ݒ肵�܂��D
   * @param seed �����̎�
   */
  public static void setSeed(long seed) {
    fSeed = seed;
    fRandom = new Random(fSeed);
  }

  /**
   * �����̎��Ԃ��܂��D
   * @return �����̎�
   */
  public static long getSeed() {
    return fSeed;
  }

}
