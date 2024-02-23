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
 * Read-Write Lock�p�^�[�������������N���X�ł��D
 */
public final class UReadWriteLock {

  /** ����, �ǂݏo�����s���Ă���Reader�X���b�h�̐� */
  private int fReadingReaders = 0;

  /** ����, �ҋ@����Writer�X���b�h�̐� */
  private int fWaitingWriters = 0;

  /** ����, �������ݒ��̃X���b�h�̐� */
  private int fWritingWriters = 0;

  /** �������ݗD�悩? */
  private boolean fPreferWriter = true;

  /**
   * Read Lock���擾���܂��D
   */
  public synchronized void readLock() throws InterruptedException {
    while (fWritingWriters > 0
           || (fPreferWriter && fWaitingWriters > 0)) {
      wait();
    }
    ++fReadingReaders;
  }

  /**
   * Read Lock��������܂��D
   */
  public synchronized void readUnlock() {
    --fReadingReaders;
    fPreferWriter = true;
    notifyAll();
  }

  /**
   * Write Lock���擾���܂��D
   */
  public synchronized void writeLock() throws InterruptedException {
    ++fWaitingWriters;
    try {
      while (fReadingReaders > 0 || fWritingWriters > 0) {
        wait();
      }
    } finally {
      --fWaitingWriters;
    }
    ++fWritingWriters;
  }

  /**
   * Write Lock��������܂��D
   */
  public synchronized void writeUnlock() {
    --fWritingWriters;
    fPreferWriter = false;
    notifyAll();
  }
}
