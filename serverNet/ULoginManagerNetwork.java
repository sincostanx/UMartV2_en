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
package serverNet;

import java.net.*;
import serverCore.ULoginManager;

/**
 * �l�b�g���[�N�z���Ƀ��O�C������G�[�W�F���g��������悤��ULoginManager���@�\�g�������N���X�ł��D
 */
public class ULoginManagerNetwork extends ULoginManager implements Runnable {

  /** �N���C�A���g����̐ڑ��v�����󂯕t���邽�߂̃T�[�o�[�\�P�b�g */
  private ServerSocket fServerSocket;

  /** �X���b�h�I�u�W�F�N�g */
  private Thread fThread;

  /** �I���t���O */
  private boolean fDoneFlag;

  /** �l�b�g���[�N��UMart�N���X�ւ̎Q�� */
  UMartNetwork fUMartNetwork;

  /**
   * �R���X�g���N�^�ł��D
   * @param umart UMartNetwork�I�u�W�F�N�g
   */
  public ULoginManagerNetwork(UMartNetwork umart) {
    super();
    fUMartNetwork = umart;
    fThread = null;
    fServerSocket = null;

  }

  /**
   * ���[�U�[����̐ڑ��v�����Ď����郁�C�����[�v�ł��D
   */
  public void run() {
    fDoneFlag = false;
    try {
      while (!fDoneFlag) {
        Socket s = fServerSocket.accept();
        new UAgentForNetworkClient(s, fUMartNetwork);
      }
    } catch (Exception e) {
      System.out.println("Exception: " + e);
    }
    fThread = null;
  }

  /**
   * �l�b�g���[�N�E�G�[�W�F���g����̐ڑ���t���J�n���܂��D
   * @param port �ڑ���t�|�[�g
   */
  public void startLoop(int port) {
    try {
      fServerSocket = new ServerSocket(port);
    } catch (Exception e) {
      System.err.println("Error: " + e);
      System.exit(5);
    }
    fThread = new Thread(this);
    fThread.start();
  }

  /**
   * ���C�����[�v���I�����܂��D
   */
  public void stopLoop() {
    fDoneFlag = false;
  }

}
