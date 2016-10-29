package com.myway5.www.Urlpool.Remover;

/*
 * 定义了去重的接口
 */
public interface IDuplicateUrlRemover {
	/*
	 * url地址是否重复
	 * @param url
	 * @return true 重复 false 唯一
	 */
	public Boolean isDuplicated(String url);
	
	public long getTotalUrlCount();
}
