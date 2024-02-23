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

import java.io.*;
import java.util.*;

import javax.swing.*;

import cmdCore.*;

/**
 * ���C���E�C���h�E�̃p�l���̐e�N���X�D�p�����[�^��T�[�o�ƒʐM���� fCommandHashMap �������D
 * �p����ł� dataUpdate() �ƕK�v�Ȃ�� gUpdate() ���I�[�o���C�h����D
 * dataUpdate �̓��f�����X�V����DgUpdate() �͉��̏ꍇ�̂݃��f�����X�V���Crepaint ����D
 * ���C���E�C���h�E�ł͕K�v�ɉ����� dataUpdate �� gUpdate ������D
 * �Ȃ��C���C���E�C���h�E�ł� UPanelFactory ��ʂ��� Upanel ���p�������N���X�𓾂�D
 * �T�u�N���X�F ���ׂẴ^�u�CUTopPanel, UOrderButtonPanel, USubPanel
 */
public class UPanel extends JPanel implements IGUIObserver {

//    protected static HashMap fCommandHashMap;
  protected static UProtocolCore fCProtocol;
  protected static ResourceBundle fRb = UParameters.fRb;
  protected static UParameters fParam = UParameters.getInstance();
  protected String fName = "UPanel";

  public UPanel() {
    // Update List �ɒǉ��D�Ă΂Ȃ���Update����Ȃ��D
    addUpdateObserverList();
    setBorder(BorderFactory.createRaisedBevelBorder());
    setLayout(null);
  }

  public static void setCProtocol(UProtocolCore cp) {
    fCProtocol = cp;
  }

  /** IGUIObserver ���p�����Ă���̂ŁC�K�v�ȏꍇ�ɂ͌p����Ŏ����� addUpdateObserverList ����
   * UpdateManager �ɓo�^���邱�Ƃ�Y��Ȃ����ƁD
   * UPanel ���������� IGUIObserver �𓱓��������߂��̕ӂ̐݌v�͂��܂��Ȃ��Ǝv���D
   * ���_�͌p�����addUpdateObserverList �̍쐬�����v�ł��Ȃ����ƁDabstract �ɂ���΂����񂾂���
   * �Ȃ����p�����JBuilder��GUI�쐬��ʂ��^���ԂɂȂ��Ă��܂����ߒf�O�D���@�𒲂ׂ�C�͂͌��݂Ȃ��D
   * �]�͂�����Β����D
   */
  public void addUpdateObserverList() {};

  public static void setParameters(UParameters p) {
    fParam = p;
  }

  public static UParameters getParameters() {
    return fParam;
  }

  /** Update �����邩�ǂ����B�ʓ|�������׌y���̂��߁B
   * ��{�I�ɂ�isVisivil��dataUpdate���邩�𔻒f���������A�^�u�N���b�N����
   * �����̂��߂�A����I��Update�̂��߂ɋ����I��Update����t���O�𗘗p����B
   * isVisivle�łȂ��ꍇ��Update�������Ƃ���setUpdate�����Ă���ĂԁB
   * ���݂͎��UGUIMainWindow��ChangeListener���ŗ��p���Ă���B
   * */
  public void dataUpdate() {
    /* ���f���̍X�V */
  }

  public void gUpdate() {
    IGUIEvent iev = new IGUIEvent() {
      public void execute() {
        dataUpdate();
      }

      public Runnable getRunnableForInvokeLater() {
        return new Runnable() {
          public void run() {
            repaint();
          }
        };
      }
    };
    UGUIEventQueue.getInstance().addIGUIEvent(iev);
  }

  public String getName() {
    return fName;
  }

  public void writeLog(String filename) throws IOException {
  }
}
