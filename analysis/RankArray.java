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

import java.util.Arrays;

/**
 * �����N��ۑ�����z��̃N���X�ł��D
 */
public class RankArray{
	
	/** �����N��ۑ�����z�� */
	private int[] rank_;
	
	/** �ۑ����郉���N�� */
	private int size_ = 0;

	/**
	 * �R���X�g���N�^�ł��D
	 * @param size �����N�̐�
	 */
	public RankArray(int size){
		size_ = size;
		rank_ = new int[size_];
		Arrays.fill(rank_,0);
	}
	
	/**
	 * ���ׂẴ����N���D��Ă���ꍇ���`�F�b�N���܂��D
	 * �ЂƂł������N�����������������false��Ԃ��̂ŁC��p���[�g�p�ɂȂ��Ă��܂��D
	 * @param oa �����N�̔z��
	 * @return true:���ׂẴ����N���D��Ă���Cfalse:�ЂƂł������N��������������
	 */
	public boolean isDominateForWeakPareto(RankArray oa){
		for (int i = 0; i < getSize(); i++) {
			// �ЂƂł������N����������������΂��߁D
			if( getRankAt(i) >= oa.getRankAt(i)){
				return false;
			}
		}
		return true;
	}

//	���g�� oa �ɗD�z���Ă��邩�H     
	
	/**
	 * ���g��oa�����D�z���Ă��邩���`�F�b�N���܂��D
	 * @param oa ��r�Ώ�
	 * @return true:�D�z���Ă���, false:�D�z���Ă��Ȃ�
	 */
	public boolean isDominate(RankArray oa){
		//���ׂē������Ƃ��`�F�b�N�p
		boolean isSuperior = false;
		for (int i = 0; i < getSize(); i++) {
			// �ЂƂł������N��������ΗD��ł͂Ȃ��D
			if( getRankAt(i) > oa.getRankAt(i)){
				return false;
			}
			//�D��ł��邽�߂ɂ͏��Ȃ��Ƃ��ЂƂ̓����N����D
			if(getRankAt(i) < oa.getRankAt(i)){
				isSuperior = true;
			}            
		}
		if(isSuperior){
			// ����������N���Ȃ��C���Ȃ��Ƃ��ЂƂ͗D��Ă���D
			return true;
		}else{
			// ���҂����S�ɓ������ꍇ�D
			return false;
		}
	}    

	/**
	 * i�Ԗڂ̃����N��Ԃ��D
	 * @param i �Y����
	 * @return rank �����N
	 */
	public int getRankAt(int i) {
		return rank_[i];
	}
	
	/**
	 * i�ԖڂɃ����N��ݒ肷��D
	 * @param index �Y����
	 * @param rank �����N
	 */
	public void setRankAt(int index, int rank) {
		rank_[index]=rank;
	}
	
	/**
	 * �z��̑傫����Ԃ��D
	 * @return size �z��̑傫��
	 */
	public int getSize() {
		return size_;
	}
	
	/**
	 * �z��̑傫����ݒ肷��D
	 * @param size�@�z��̑傫��
	 */
	public void setSize(int size) {
		size_ = size;
		rank_ = new int[size];
		Arrays.fill(rank_,0);        
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < rank_.length; i++) {
			sb.append(rank_[i]+",");
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o){
		RankArray or = (RankArray)o;
		for (int i = 0; i < or.getSize(); i++) {
			if(or.getRankAt(i)!=getRankAt(i)){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * �����N�̔z���Ԃ��D
	 * @return �����N�̔z��
	 */
	public int[] getRank() {
		return rank_;
	}
	
	/**
	 * �����N�̔z���ݒ肷��D
	 * @param rank �����N�̔z��
	 */
	public void setRank(int[] rank) {
		rank_ = rank;
		size_ = rank.length;
	}
	
}

