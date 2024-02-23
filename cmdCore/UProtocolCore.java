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
 * SVMP�R�}���h�I�u�W�F�N�g�W����ێ�����v���g�R���N���X�ł��D
 * SVMP�R�}���h�����L�[�Ƃ��Ďg���ēK�؂�SVMP�I�u�W�F�N�g���擾���邱�Ƃ��ł��܂��D
 */
public abstract class UProtocolCore {

	/** SVMP�R�}���h�I�u�W�F�N�g�̃f�[�^�x�[�X */
  protected HashMap fCommandHash;

  /**
   * �R���X�g���N�^�ł��D
   */
  public UProtocolCore() {
    fCommandHash = new HashMap();
  }

  /**
   * SVMP�R�}���h�I�u�W�F�N�g�̃f�[�^�x�[�X��Ԃ��܂��D
   * @return SVMP�R�}���h�I�u�W�F�N�g�̃f�[�^�x�[�X(HashMap)
   */
  public HashMap getCommandHashMap() {
    return fCommandHash;
  }

  /**
   * �R�}���h����cmdStr��SVMP�R�}���h�I�u�W�F�N�g��Ԃ��܂��D
   * @param cmdStr �R�}���h��
   * @return SVMP�R�}���h�I�u�W�F�N�g
   */
  public ICommand getCommand(String cmdStr) {
    return (ICommand)fCommandHash.get(cmdStr);
  }
}
