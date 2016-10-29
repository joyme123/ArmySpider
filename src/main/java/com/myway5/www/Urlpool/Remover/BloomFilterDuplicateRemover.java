package com.myway5.www.Urlpool.Remover;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;

/**
 * 使用google的bloomFilter算法进行去重
 * 这种算法的优点在于处理大量数据的去重时效率高，但是会有误判,会将不重复的url判定为重复，这个概率很低
 * 在对精确度不高的有大量爬取需求的情况下可以使用这个
 * @author joyme
 *
 */
public class BloomFilterDuplicateRemover implements IDuplicateUrlRemover{
	private BloomFilter<String> bloomFilter;			//
	private long length;
	
	public enum UrlFunnel implements Funnel<String>{
		INSTANCE;
		public void funnel(String from, PrimitiveSink into) {
			into.putUnencodedChars(from);
		}
	}
	
	/**
	 * 构造函数
	 * @param expectedInsertions 预计要插入的总url数量，假设平均一个url是30个数字或字母组成
	 * 那么用hashMap的方式，1000万个url占用的空间是:10000000*30*2/1024/1024 = 570M,
	 * 这样光存储这些url用来去重就占用了570M的内存空间，而用bloomFilter的方式，是使用一个位向量进行去重，
	 * 占用空间会大大减少
	 */
	public BloomFilterDuplicateRemover(int expectedInsertions){
		this.bloomFilter = BloomFilter.create(UrlFunnel.INSTANCE, expectedInsertions);
	}
	/**
	 * 构造函数,默认预期有10000000万的url插入
	 */
	public BloomFilterDuplicateRemover(){
		this.bloomFilter = BloomFilter.create(UrlFunnel.INSTANCE, 10000000);
	}
	
	public Boolean isDuplicated(String url) {
		if(!bloomFilter.mightContain(url)){
			bloomFilter.put(url);
			this.length++;
			return false;
		}
		return true;
	}

	public long getTotalUrlCount() {
		return this.length;
	}
}
