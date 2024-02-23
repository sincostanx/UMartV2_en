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

import java.io.*;
import java.util.*;

/**
 * �R�}���h�̃T�[�o�[�ł̎��s���ʂ̏�Ԃ�ێ�����N���X�ł��D
 */
public class UCommandStatus {

  /** ���s���ʂ̏�� (true: ����, false: ���s) */
  private boolean fStatus;

  /** �G���[�R�[�h */
  private int fErrorCode;

  /** �G���[���b�Z�[�W */
  private String fErrorMessage;

  /**
   * �R���X�g���N�^�ł��D
   */
  public UCommandStatus() {
  }

  /**
   * br������s���ʏ�Ԃ�ǂݍ���, ��͂��܂��D
   * @param br �X�g���[��
   */
  public void readFrom(BufferedReader br) throws IOException {
    String line = br.readLine();
    StringTokenizer st = new StringTokenizer(line);
    String token = st.nextToken();
    if (token.equals("+ACCEPT")) {
      fStatus = true;
      return;
    } else if (token.equals("+ERROR")) {
      token = st.nextToken();
      fErrorCode = Integer.parseInt(token);
      fErrorMessage = br.readLine();
    } else {
      System.err.println("Error in UCommandStatus.readFrom");
      System.exit(5);
    }
  }

  /**
   * ���s���ʂ̏�Ԃ�ݒ肵�܂��D
   * @param status ���s���ʂ̏��
   */
  public void setStatus(boolean status) {
    fStatus = status;
  }

  /**
   * ���s���ʂ̏�Ԃ�Ԃ��܂��D
   * @return ���s���ʂ̏��
   */
  public boolean getStatus() {
    return fStatus;
  }

  /**
   * �G���[�R�[�h��ݒ肵�܂��D
   * @param code �G���[�R�[�h
   */
  public void setErrorCode(int code) {
    fErrorCode = code;
  }

  /**
   * �G���[�R�[�h��Ԃ��܂��D
   * @return �G���[�R�[�h
   */
  public int getErrorCode() {
    return fErrorCode;
  }

  /**
   * �G���[���b�Z�[�W��ݒ肵�܂��D
   * @param msg �G���[���b�Z�[�W
   */
  public void setErrorMessage(String msg) {
    fErrorMessage = msg;
  }

  /**
   * �G���[���b�Z�[�W��Ԃ��܂��D
   * @return �G���[���b�Z�[�W
   */
  public String getErrorMessage() {
    return fErrorMessage;
  }

}
