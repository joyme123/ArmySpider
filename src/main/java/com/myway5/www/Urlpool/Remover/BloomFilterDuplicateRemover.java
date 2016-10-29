package com.myway5.www.Urlpool.Remover;

import com.google.common.hash.BloomFilter;

/**
 * 使用google的bloomFilter算法进行去重
 * 这种算法的优点在于处理大量数据的去重时效率高，但是会有误判,会将不重复的url判定为重复，这个概率很低
 * 在对精确度不高的有大量爬取需求的情况下可以使用这个
 * @author joyme
 *
 */
public class BloomFilterDuplicateRemover implements IDuplicateUrlRemover{
	private BloomFilter<String> bloomFilter;			//

	public BloomFilterDuplicateRemover(){
		///bloomFilter = BloomFilter.create(funnel, expectedInsertions)
	}
	
	public Boolean isDuplicated(String url) {
		
		return null;
	}

	public int getTotalUrlCount() {
		// TODO Auto-generated method stub
		return 0;
	}
}
