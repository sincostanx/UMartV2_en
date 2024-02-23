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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

/**
 * ���ʂ̃n�b�V���}�b�v����C�ЂƂ̎w�W�ɑ΂��ă����N�����N���X�ł��D
 */
public abstract class AbstractRankMaker {
	/** su ���܂߂邩�H */
	private boolean isSuValid = false;

	/** HashMap(���郆�[�U�̌���)�� ArrayList(���̎��s�̑S����)�� ArrayList */ 
	private ArrayList allResult_ = null;

	/**
	 * ���O��Ԃ��܂��D
	 */ 
	abstract public String getName();

	/**
	 * �����N��Ԃ��܂��D
	 * @return ���[�U���� key �����N�� value �Ƃ��� HashMap
	 */
	abstract public HashMap getRank();

	/**
	 * �S�Ẵ��O�C�����[�U��Ԃ��܂��D
	 * @return �S�Ẵ��O�C�����[�U�̃��X�g
	 */
	public ArrayList getAllLoginName(){
		ArrayList result = new ArrayList();
		Iterator iter1 = allResult_.iterator();
		while (iter1.hasNext()) {
			ArrayList array = (ArrayList) iter1.next();
			Iterator iter2 = array.iterator();
			while (iter2.hasNext()) {
				HashMap map = (HashMap) iter2.next();
				if(result.contains(map.get("LoginName"))){
					continue;
				}
				if(map.get("LoginName").equals("su") && !isSuValid()){
					continue;
				}
				result.add(map.get("LoginName"));
			}
		}
		return result; 
	}

	/**
	 * �G�[�W�F���g�� name �� key �Ɋւ���S�l��Ԃ��܂��D
	 * @param name �G�[�W�F���g��
	 * @param key �L�[
	 */
	public ArrayList getAllValueOf(String name, String key){
		ArrayList result = new ArrayList();
		Iterator iter1 = allResult_.iterator();
		while (iter1.hasNext()) {
			ArrayList array = (ArrayList) iter1.next();
			Iterator iter2 = array.iterator();
			while (iter2.hasNext()) {
				HashMap map = (HashMap) iter2.next();
				if(name.equals(map.get("LoginName"))){
					result.add(map.get(key));
				}
			}
		}        
		return result;
	}

	/**
	 * ������̐��l����Ȃ� ArrayList �̍ő��Ԃ��܂��D
	 * @param list ������̐��l����Ȃ�ArrayList
	 * @return�@�ő�l
	 */
	public double getMax(ArrayList list){
		if(list == null || list.size() == 0){
			return Double.NaN;
		}
		double max = Double.NEGATIVE_INFINITY;
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			double num = Double.parseDouble( (String) iter.next());
			if(num > max){
				max = num;
			}
		}
		return max;
	}

	/**
	 * ������̐��l����Ȃ� ArrayList �̍ŏ���Ԃ��܂��D
	 * @param list ������̐��l����Ȃ� ArrayList
	 * @return �ŏ��l
	 */    
	public double getMin(ArrayList list){
		if(list == null || list.size() == 0){
			return Double.NaN;
		}
		double min = Double.POSITIVE_INFINITY;
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			double num = Double.parseDouble( (String) iter.next());
			if(num < min){
				min = num;
			}
		}
		return min;
	}

	/**
	 * ������̐��l����Ȃ� ArrayList �̕��ς�Ԃ��܂��D
	 * @param list ������̐��l����Ȃ� ArrayList
	 * @return ���ϒl
	 */    
	public double getMean(ArrayList list){
		if(list == null || list.size() == 0){
			return Double.NaN;
		}
		double mean = 0;
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			mean += Double.parseDouble( (String) iter.next());
		}
		return mean/list.size();
	}

	/**
	 * ���l�z��������N�ɕϊ����܂��D
	 * @param array ���l�z��
	 * @param isDesc �~����
	 * @return rank �����N�z��
	 */    
	public int[] convertToRank(double[] array, boolean isDesc){
		if(array == null || array.length == 0){
			return null;
		}
		int[] rank = new int[array.length];
		Arrays.fill(rank,1); // �S�����N��1��
		for (int i = 0; i < array.length-1; i++) {
			for (int j = i+1; j < array.length; j++) {
				if(isDesc){ // �~���̏ꍇ�D�傫�������ǂ�.
					if(array[i] > array[j]){
						rank[j]++; // ���������̃����N�𑝂₷�D
					}else if(array[i] < array[j]){
						rank[i]++;                        
					}
					// �������ꍇ�͉������Ȃ��D
				}else{ // �����̏ꍇ�D�����������ǂ�.
					if(array[i] < array[j]){
						rank[j]++; // �傫�����̃����N�𑝂₷�D
					}else if(array[i] > array[j]){
						rank[i]++;                        
					}                    
				}
			}
		}
		return rank;
	}    

	/**
	 * �S�Ă̌��ʂ�Ԃ��܂��D
	 * @return result ����
	 */
	public ArrayList getAllResult() {
		return allResult_;
	}

	/**
	 * �S�Ă̌��ʂ�ݒ肵�܂��D
	 * @param result ����
	 */
	public void setAllResult(ArrayList result) {
		allResult_ = result;
	}

	/**
	 * su���܂߂邩��Ԃ��܂��D
	 * @return isSuValid true: su���܂߂�, false: su���܂߂Ȃ� 
	 */
	public boolean isSuValid() {
		return isSuValid;
	}

	/**
	 * su���܂߂邩��ݒ肵�܂��D
	 * @param isSuValid true: su���܂߂�, false: su���܂߂Ȃ�
	 */
	public void setSuValid(boolean isSuValid) {
		this.isSuValid = isSuValid;
	}
}
