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
package analysis;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

/**
 * �p���[�g�����L���O�����߂邽�߂̃N���X�ł��D
 * ���݂�, �ő�C���ρC�����C�j�Y���̂S�Ɍ��߂����ɂȂ��Ă��܂��D
 * �V�����w�W���o�Ă���΁C�Ή�����\��������܂��D
 */
public class ParetoRanking {
	
	/** �w�W�ɑ΂��ă����N�����N���X�̔z�� */
	private AbstractRankMaker[] rankMaker_;

	/** �p���[�g�����N�̔z�� */
	private ArrayList paretoRankingList_ = new ArrayList();

	/** �w�W�ɑ΂��ă����N�����N���X�̐� */
	private int maxRankMaker_ = 4;

	/** �V�~�����[�V������ */
	private int simulationNum_ = 0;

	/** �G�[�W�F���g�� */
	private int totalAgentNum_ = 0;

	/**
	 * �R���X�g���N�^�ł��D
	 * @param dirs �����f�B���N�g��
	 */
	public ParetoRanking(String[] dirs) {
		simulationNum_ = dirs.length;
		rankMaker_ = new AbstractRankMaker[maxRankMaker_];
		// �ő�
		rankMaker_[0] = new MaxProfitRankMaker();
		// ����
		rankMaker_[1] = new MeanProfitRankMaker();
		// ����
		rankMaker_[2] = new WinningAverageRankMaker();
		// �j�Y��
		rankMaker_[3] = new BankruptcyRateRankMaker();
		SettlementReader sr = new SettlementReader();
		// dirs �ȉ��̃f�B���N�g���ɂ��� Settlement_account.csv ��ǂݍ��ށD
		ArrayList list = sr.getAllSettlementHashList(dirs);
		for (int i = 0; i < rankMaker_.length; i++) {
			rankMaker_[i].setAllResult(list);
		}
		// ���O�C�����𗘗p���āC���O�Ƌ�� RankArray ������ NameAndRankArray �� ArrayList �����D
		ArrayList allName = rankMaker_[0].getAllLoginName();
		totalAgentNum_ = allName.size();
		Iterator iter = allName.iterator();
		while (iter.hasNext()) {
			String name = (String) iter.next();
			//          RankArray �͗v�f�� maxRankMaker_ �̃x�N�g���D
			NameAndRankArray nr = new NameAndRankArray(name, new RankArray(
					maxRankMaker_));
			paretoRankingList_.add(nr);
		}

		for (int i = 0; i < rankMaker_.length; i++) {
			HashMap tmpMap = rankMaker_[i].getRank();
			// tmpMap �� key ���S���O�C�����D
			for (Iterator iterator = paretoRankingList_.iterator(); iterator
			.hasNext();) {
				NameAndRankArray nr = (NameAndRankArray) iterator.next();
				// �w�W rankMaker_[i] �Ɋւ��郍�O�C���� name �̃����N�D
				int rank = ((Integer) (tmpMap.get(nr.getName()))).intValue();
				nr.getRankArray().setRankAt(i, rank);
			}
		}
	}

	/**
	 * �p���[�g�����N�̃��X�g��Ԃ��܂��D
	 * @return paretoRankingMap �p���[�g�����N�̃��X�g
	 */
	public ArrayList getParetoRankingList() {
		return paretoRankingList_;
	}

	/**
	 * �w�W�ɑ΂��ă����N�����N���X�̐���Ԃ��܂��D
	 * @return maxRankMaker �w�W�ɑ΂��ă����N�����N���X�̐�
	 */
	public int getMaxRankMaker() {
		return maxRankMaker_;
	}

	/**
	 * �V�~�����[�V��������Ԃ��܂��D
	 * @return simulationNum �V�~�����[�V������
	 */
	public int getSimulationNum() {
		return simulationNum_;
	}

	/**
	 * �G�[�W�F���g����Ԃ��܂��D
	 * @return totalAgentNum �G�[�W�F���g��
	 */
	public int getTotalAgentNum() {
		return totalAgentNum_;
	}

	/**
	 * �p���[�g�����L���O���v�Z���܂��D
	 */
	public void calcParetoRank() {
		//      �G�[�W�F���g�����Ȃ���΂�邱�ƂȂ��D
		if (getTotalAgentNum() == 0) {
			return;
		}
		//      �G�[�W�F���g����C�Ȃ炱���ŏI��D
		if (getTotalAgentNum() == 1) {
			NameAndRankArray nr0 = (NameAndRankArray) paretoRankingList_.get(0);
			RankArray preRankArray = nr0.getRankArray();
			nr0.setParetoRank(1);
			return;
		}

		int currentRank = 1;
		boolean[] alreadySelected = new boolean[paretoRankingList_.size()];
		Arrays.fill(alreadySelected, false);
		//2 �C�ȏ�̂Ƃ�

		while (isContainsFalse(alreadySelected) == true) { // �܂��I�΂�Ă��Ȃ��z������D
			ArrayList numArray = new ArrayList(); // ����I�Ԕԍ���ۑ�����D
			for (int i = 0; i < paretoRankingList_.size(); i++) {
				if (alreadySelected[i] == true) {
					//���ɑI�΂�Ă���D
					continue;
				}
				boolean isDominated = false; // �܂��͗D�z����Ă��Ȃ��ƍl����D
				NameAndRankArray nr = (NameAndRankArray) paretoRankingList_
				.get(i);
				RankArray rankArray = nr.getRankArray();
				for (int j = 0; j < paretoRankingList_.size(); j++) {
					if (alreadySelected[j] == true || i == j) {
						//���ɑI�΂�Ă��邩�������g�Ƃ̔�r���D
						continue;
					}
					NameAndRankArray nrTarget = (NameAndRankArray) paretoRankingList_
					.get(j);
					RankArray rankArrayTarget = nrTarget.getRankArray();
					//�������C���ł� Target �ɗD�z����Ă��܂��ΏI��D
					// ��p���[�g�𓯃����N�ɂ���ƁC�j�Y�����قƂ�� 0 �Ȃ̂ŊF�������N�ɂȂ��Ă��܂��D
					if (rankArrayTarget.isDominate(rankArray)) {
						isDominated = true;
						break;
					}
				}
				// ���ݎc���Ă���N�ɂ��D�z����Ă��Ȃ���΁C���̌��D
				if (isDominated == false) {
					numArray.add(new Integer(i));
				}
			}
			// �K���C�ЂƂ͑I�΂��͂��D�I�΂�Ă��Ȃ��Ƃ��͖������[�v�ɗ�����̂ŋ����I���D
			if(numArray.size() == 0){
				System.err.println("Something is wrong! Infinite loop was happed in calcParetoRank(), ParetoRanking.java");
				System.exit(-1);
			}
			Iterator iter = numArray.iterator();
			while (iter.hasNext()) {
				Integer num = (Integer) iter.next();
				// �����N���Z�b�g���āC�I�΂ꂽ�t���O�𗧂Ă�D
				NameAndRankArray nr = (NameAndRankArray) paretoRankingList_
				.get(num.intValue());
				nr.setParetoRank(currentRank);
				alreadySelected[num.intValue()] = true;
			}
			currentRank += numArray.size(); // �����N������I�񂾕��������₷�D
		}
	}

	private boolean isContainsFalse(boolean[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == false) {
				return true;
			}
		}
		return false;
	}

	/**
	 * ��ʂɌ��ʂ��o�͂��܂��D
	 *
	 */
	public void printResult(){
		StringBuffer usingIndex = new StringBuffer();
		for (int i = 0; i < rankMaker_.length; i++) {
			usingIndex.append(rankMaker_[i].getName()+",");
		}
		usingIndex.deleteCharAt(usingIndex.lastIndexOf(","));
		System.out.println("ParetoRank,AgentName,"+usingIndex);
		Collections.sort(paretoRankingList_);
		Iterator iter = paretoRankingList_.iterator();
		while (iter.hasNext()) {
			NameAndRankArray element = (NameAndRankArray) iter.next();
			System.out.println(element);
		}
	}

	/**
	 * �o�̓X�g���[���Ɍ��ʂ��o�͂��܂��D
	 * @param pw �o�̓X�g���[��
	 */
	public void printResult(PrintWriter pw){
		StringBuffer usingIndex = new StringBuffer();
		for (int i = 0; i < rankMaker_.length; i++) {
			usingIndex.append(rankMaker_[i].getName()+",");
		}
		usingIndex.deleteCharAt(usingIndex.lastIndexOf(","));
		pw.println("ParetoRank,AgentName,"+usingIndex);
		Collections.sort(paretoRankingList_);
		Iterator iter = paretoRankingList_.iterator();
		while (iter.hasNext()) {
			NameAndRankArray element = (NameAndRankArray) iter.next();
			pw.println(element);
		}
	}

	/**
	 * ���C�����\�b�h�ł��D
	 * @param args �����f�B���N�g��
	 */
	public static void main(String[] args) {
		if(args.length < 1){
			System.err.println("Usage: java analysis.ParetoRanking dir1 dir2 ... ");
			System.exit(1);
		}
		ParetoRanking pr = new ParetoRanking(args);
		pr.calcParetoRank();
		pr.printResult();
	}
}
