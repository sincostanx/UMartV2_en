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
package cmdCore;

import java.util.*;

/**
 * �R�}���h���`���邽�߂̃C���^�t�F�[�X�ł��D
 * �V�X�e�����̑S�ẴR�}���h�͂��̃C���^�t�F�[�X���������Ȃ���΂Ȃ�܂���D
 */
public interface ICommand {

  /** �������Ԉ���Ă��邱�Ƃ�\���萔 */
  public static final int INVALID_ARGUMENTS = 1;

  /** ��t���Ȃ��R�}���h�ł��邱�Ƃ�\���萔 */
  public static final int UNACCEPTABLE_COMMAND = 2;

  /** ���݂��Ȃ��R�}���h�ł��邱�Ƃ�\���萔 */
  public static final int INVALID_COMMAND = 3;

  /**
   * �R�}���h����Ԃ��܂��D
   * @return �R�}���h��
   */
  public String getName();

  /**
   * ���̃R�}���h�̖��O��name�Ɠ���������Ԃ��܂��D
   * @param name ��r����R�}���h��
   * @return true:�������Cfalse:�������Ȃ�
   */
  public boolean isNameEqualTo(String name);

  /**
   * ���̃R�}���h�ɕK�v�Ȉ�����ݒ肵�܂��D
   * @param st �����Q�iStringTokenizer.nextToken�Ŏ��o����j
   * return true�F�����𐳂����ݒ�ł����ꍇ�Cfalse:���s�����ꍇ
   */
  public boolean setArguments(StringTokenizer st);

  /**
   * �R�}���h�����s���܂��D
   * @return ���s����
   */
  public UCommandStatus doIt();

  /**
   * �R�}���h�̎��s���ʂ�W���o�͂ɕ\�����܂��D
   */
  public void printOn();

  /**
   * �R�}���h�̎��s���ʂ��X�y�[�X��؂�̕�����Ƃ��ĕԂ��܂��D
   * @return ���s���ʂ̕�����
   */
  public String getResultString();

}
