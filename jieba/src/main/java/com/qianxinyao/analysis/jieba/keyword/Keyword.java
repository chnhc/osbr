package com.qianxinyao.analysis.jieba.keyword;

/**
 * @author Tom Qian
 * @email tomqianmaple@outlook.com
 * @github https://github.com/bluemapleman
 * @date Oct 20, 2018
 */
public class Keyword implements Comparable<Keyword>
{
	private Double tfidfvalue;
	private String name;
	private int distance;
	/**
	 * @return the tfidfvalue
	 */
	public double getTfidfvalue()
	{
		return tfidfvalue;
	}

	/**
	 * @param tfidfvalue the tfidfvalue to set
	 */
	public void setTfidfvalue(double tfidfvalue)
	{
		this.tfidfvalue = tfidfvalue;
	}


	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public Keyword(String name, double tfidfvalue)
	{
		this.name=name;
		// tfidf值只保留3位小数
		this.tfidfvalue=(double)Math.round(tfidfvalue*10000)/10000;
	}

	public Keyword(String name, double tfidfvalue ,int distance)
	{
		this.name=name;
		// tfidf值只保留3位小数
		this.tfidfvalue=(double)Math.round(tfidfvalue*10000)/10000;
		// 距离
		this.distance = distance;
	}
	
	/**
	 * 为了在返回tdidf分析结果时，可以按照值的从大到小顺序返回，故实现Comparable接口
	 */
	@Override
	public int compareTo(Keyword o)
	{
		if (this.tfidfvalue == null && o.tfidfvalue == null) {
			return 0;
		}
		if (this.tfidfvalue == null) {
			return 1;
		}
		if (o.tfidfvalue == null) {
			return -1;
		}
		if (this.tfidfvalue.equals(o.tfidfvalue) ) {
			return 0;
		}
		return this.tfidfvalue-o.tfidfvalue>0?-1:1;
	}

	/**
	 * 重写hashcode方法，计算方式与原生String的方法相同
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		long temp;
		temp = Double.doubleToLongBits(tfidfvalue);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Keyword other = (Keyword) obj;
		if (name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
//		if (Double.doubleToLongBits(tfidfvalue) != Double.doubleToLongBits(other.tfidfvalue))
//			return false;
		return true;
	}
	
	
	
}

