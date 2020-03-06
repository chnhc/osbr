package com.huaban.analysis.jieba.noWord;

public class Pair<K> {
    public K key;
    public Double freq = 0.0;

    public Pair(K key, double freq) {
	this.key = key;
	this.freq = freq;
    }

    @Override
    public String toString() {
	return "Candidate [key=" + key + ", freq=" + freq + "]";
    }

}
