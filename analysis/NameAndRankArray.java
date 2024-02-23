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

/**
 * ���O�C�����ƃ����N�̔z����܂Ƃ߂��N���X�ł��D
 */
public class NameAndRankArray implements Comparable{

	/** ���O�C���� */
	private String name_="";
	
	/** �����N�̔z�� */
	private RankArray rankArray_=null;
	
	/** �Ō�Ɍv�Z����p���[�g�����N�D1�ȏ�D */
	private int paretoRank_ = 0;

	/**
	 * �R���X�g���N�^�ł��D
	 * @param name ���O�C����
	 * @param array �����N�̔z��
	 */
	public NameAndRankArray(String name,RankArray array){
		this.name_ = name;
		this.rankArray_ = array;
	}

	/**
	 * ���O�C������Ԃ��D
	 * @return name ���O�C����
	 */
	public String getName() {
		return name_;
	}
	/**
	 * �����N�̔z��
	 * @return rankArray �����N�̔z��
	 */
	public RankArray getRankArray() {
		return rankArray_;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return getParetoRank()+","+getName()+","+getRankArray().toString();
	}
	
	/**
	 * �p���[�g�����N��Ԃ��܂��D
	 * @return paretoRank �p���[�g�����N
	 */
	public int getParetoRank() {
		return paretoRank_;
	}
	
	/**
	 * �p���[�g�����N��ݒ肵�܂��D
	 * @param paretoRank �p���[�g�����N
	 */
	public void setParetoRank(int paretoRank) {
		paretoRank_ = paretoRank;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(T)
	 */
	public int compareTo(Object o) {
		NameAndRankArray nr = (NameAndRankArray)o;
		if(getParetoRank() > nr.getParetoRank()){
			return 1;
		}else if(getParetoRank() > nr.getParetoRank()){
			return -1;
		}else {
			return 0;
		}
	}
}
