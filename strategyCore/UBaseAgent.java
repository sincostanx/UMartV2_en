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
package strategyCore;

import java.io.*;
import java.util.*;

import cmdCore.*;

/**
 * �}�V���G�[�W�F���g�̃��[�g�N���X�ł��D
 * �S�Ẵ}�V���G�[�W�F���g��UBaseAgent���p�����āC
 * doActions���\�b�h��setParameters���\�b�h���I�[�o�[���C�h����K�v������܂��D
 * �T�[�o�Ƃ̒ʐM�ɂ́CgetUmcp���\�b�h��CProtocolCore�I�u�W�F�N�g���擾������C
 * CProtocolCore�I�u�W�F�N�g����SVMP�R�}���h�I�u�W�F�N�g���擾����
 * �K�؂Ȉ�����ݒ肵����Ɏ��s���܂��D
 */
public class UBaseAgent {

  /** ���O�C���� */
  protected String fLoginName;

  /** �p�X���[�h */
  protected String fPasswd;

  /** ���ۂ̖��O�iHuman�̏ꍇ�F�����CMachine�̏ꍇ�F�N���X���j */
  protected String fRealName;

  /** ���[�UID */
  protected int fUserID;

  /** �����̎� */
  protected int fSeed;

  /** ���� */
  protected Random fRandom;

  /** �v���g�R���E���C�u���� */
  protected UProtocolCore fUmcp;

  /** �V�X�e���p�����[�^ */
  protected String[] fSystemParameters;

  /** �o�̓X�g���[�� */
  protected OutputStream fOutputStream = null;

  /** fOutputStream���琶�������PrintWriter�I�u�W�F�N�g */
  protected PrintWriter fPrintWriter;

  /**
   * UBaseAgent�I�u�W�F�N�g�̐�������я��������s���܂��D
   * @param loginName ���O�C����
   * @param passwd �p�X���[�h
   * @param realName ���ۂ̖��O�iHuman�̏ꍇ�F�����CMachine�̏ꍇ�F�N���X���j
   * @param seed �����̎�
   */
  public UBaseAgent(String loginName, String passwd, String realName,
                       int seed) {
    fLoginName = loginName;
    fPasswd = passwd;
    fRealName = realName;
    fSeed = seed;
    fRandom = new Random(seed);
    fUmcp = null;
    fUserID = -1;
    fSystemParameters = new String[0];
    setOutputStream(System.out); // �f�t�H���g�ł́C�W���o�͂𗘗p����D
  }

  /**
   * �G�[�W�F���g�̃V�X�e���p�����[�^��ݒ肵�܂��D
   * ���̃��\�b�h���I�[�o�[���C�h����ꍇ�C�K���C�ŏ���super.setParameters��ǂ�ł��������D
   * @param args �V�X�e���p�����[�^
   */
  public void setParameters(String[] args) {
    fSystemParameters = new String[args.length];
    message("**** Parameters given by CSV file ****");
    for (int i = 0; i < args.length; ++i) {
      fSystemParameters[i] = args[i];
      message(args[i]);
    }
    message("****************************************");
  }

  /**
   * ���b�Z�[�W���o�͂��܂��D
   * @param msg String ���b�Z�[�W
   */
  public void message(String msg) {
    if (fPrintWriter != null) {
      fPrintWriter.println(msg);
      fPrintWriter.flush();
    }
  }
  
  /**
   * ���b�Z�[�W�����s�Ȃ��ŏo�͂��܂��D
   * @param msg ���b�Z�[�W
   */
  public void print(String msg) {
    if (fPrintWriter != null) {
      fPrintWriter.print(msg);
      fPrintWriter.flush();
    }  	
  }

  /**
   * ���b�Z�[�W�����s����ŏo�͂��܂��D
   * @param msg ���b�Z�[�W
   */
  public void println(String msg) {
    if (fPrintWriter != null) {
      fPrintWriter.println(msg);
      fPrintWriter.flush();
    }  	
  }

  /**
   * ��O���o�͂��܂��D
   * @param ex Exception ��O
   */
  public void message(Exception ex) {
    if (fPrintWriter != null) {
      ex.printStackTrace(fPrintWriter);
    }
  }

  /**
   * �o�̓X�g���[����ݒ肵�܂��D
   * @param os �o�̓X�g���[��
   */
  public void setOutputStream(OutputStream os) {
    fOutputStream = os;
    if (fOutputStream == null) {
      fPrintWriter = null;
    } else {
      fPrintWriter = new PrintWriter(os, true);
    }
  }

  /**
   * �o�̓X�g���[����Ԃ��܂��D
   * @return �o�̓X�g���[��
   */
  public OutputStream getOutputStream() {
    return fOutputStream;
  }

  /**
   * �G�[�W�F���g�̃V�X�e���p�����[�^��Ԃ��܂��D
   * @return �V�X�e���p�����[�^
   */
  public String[] getParameters() {
    return fSystemParameters;
  }

  /**
   * CProtocol�I�u�W�F�N�g��ݒ肵�܂��D
   * @param umcp CProtocol�I�u�W�F�N�g
   */
  public void setCProtocol(UProtocolCore umcp) {
    fUmcp = umcp;
  }

  /**
   * �����˗��C�L�����Z���Ȃǂ̓�����s���܂��D
   * @param date ��
   * @param session �ߐ�
   * @param serverState �T�[�o�[���
   * @param maxDate �^�p����
   * @param noOfSessionsPerDay ���������̐ߐ�
   */
  public void doActions(int date, int session, int serverState,
                          int maxDate, int noOfSessionsPerDay) {
  }

  /**
   * �v���g�R���E���C�u������Ԃ��܂��D
   * @return �v���g�R���E���C�u�����D
   */
  public UProtocolCore getUmcp() {
    return fUmcp;
  }

  /**
   * �p�X���[�h��Ԃ��܂��D
   * @return �p�X���[�h
   */
  public String getPasswd() {
    return fPasswd;
  }

  /**
   * �p�X���[�h��ݒ肵�܂��D
   * @param passwd �p�X���[�h
   */
  public void setPasswd(String passwd) {
    fPasswd = passwd;
  }

  /**
     ���[�UID��Ԃ��܂��D
     @return ���[�UID�D
   */
  public int getUserID() {
    return fUserID;
  }

  /**
   * ���[�U�[ID��ݒ肵�܂��D
   * @param userID ���[�U�[ID
   */
  public void setUserID(int userID) {
    fUserID = userID;
  }

  /**
   * �����̎��Ԃ��܂��D
   * @return �����̎�
   */
  public int getSeed() {
    return fSeed;
  }

  /**
   * �����̎��ݒ肵�܂��D
   * @param seed �����̎�
   */
  public void setSeed(int seed) {
    fSeed = seed;
    fRandom = new Random(seed);
  }

  /**
   * ���O�C������Ԃ��܂��D
   * @return ���O�C����
   */
  public String getLoginName() {
    return fLoginName;
  }

  /**
   * ���ۂ̖��O�iHuman�̏ꍇ�F�����CMachine�̏ꍇ�F�N���X���j��Ԃ��܂��D
   * @return ���ۂ̖��O
   */
  public String getRealName() {
    return fRealName;
  }

  /**
   * ���O�C������ݒ肵�܂��D
   * @param string ���O�C����
   */
  public void setLoginName(String string) {
    fLoginName = string;
  }

  /**
   * ���ۂ̖��O�iHuman�̏ꍇ�F�����CMachine�̏ꍇ�F�N���X���j��ݒ肵�܂��D
   * @param string ���ۂɖ��O
   */
  public void setRealName(String string) {
    fRealName = string;
  }
  
  /**
   * �����I�u�W�F�N�g��Ԃ��܂��D
   * @return �����I�u�W�F�N�g
   */
  public Random getRandom() {
  	return fRandom;
  }

}
