package com.chu163.jieba.analysis;

import com.huaban.analysis.jieba.noWord.JiebaSegmenter;
import com.huaban.analysis.jieba.noWord.SegToken;
import com.huaban.analysis.jieba.noWord.WordDictionary;
import com.qianxinyao.analysis.jieba.keyword.Keyword;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Tom Qian
 * @email tomqianmaple@outlook.com
 * @github https://github.com/bluemapleman
 * @date Oct 20, 2018
 * tfidf算法原理参考：http://www.cnblogs.com/ywl925/p/3275878.html
 * 部分实现思路参考jieba分词：https://github.com/fxsjy/jieba
 */
public class ChuAnalyzer
{
	
	private HashMap<String,Double> idfMap;
	private HashSet<String> stopWordsSet;
	private double idfMedian;
	private String ciKuPath;

	/**
	 * @param ciKuPath 结巴分词，词库地址
	 * @param idfPath  对应 idf 的文档地址
	 */
	public ChuAnalyzer( String ciKuPath, String idfPath) {
		if(stopWordsSet==null) {
			stopWordsSet=new HashSet<>();
			loadStopWords(stopWordsSet, this.getClass().getResourceAsStream("/stop_words.txt"));
		}
		if(idfMap==null) {
			idfMap=new HashMap<>();
			loadIDFMap(idfMap, this.getClass().getResourceAsStream(idfPath));
		}
		this.ciKuPath = ciKuPath;
	}

	public List<Keyword> analyze(String content,int topN){
		return analyze(content,topN,false);
	}

	/**
	 * tfidf分析方法
	 * @param content 需要分析的文本/文档内容
	 * @param topN 需要返回的tfidf值最高的N个关键词，若超过content本身含有的词语上限数目，则默认返回全部

	 * @return
	 */
	public List<Keyword> analyze(String content,int topN, boolean index){
		List<Keyword> keywordList=new ArrayList<>();

		Map<String, Double> tfMap=getTF(content, ciKuPath, index);
		for(String word:tfMap.keySet()) {
			// 若该词不在idf文档中，则使用平均的idf值(可能定期需要对新出现的网络词语进行纳入)
			if(idfMap.containsKey(word)) {
				keywordList.add(new Keyword(word,idfMap.get(word)*tfMap.get(word)));
			}else
				keywordList.add(new Keyword(word,idfMedian*tfMap.get(word)));
		}
		
		Collections.sort(keywordList);
		
		if(keywordList.size()>topN) {
			int num=keywordList.size()-topN;
			for(int i=0;i<num;i++) {
				keywordList.remove(topN);
			}
		}
		return keywordList;
	}

	private Map<String, Double> getTF(String content, String path) {
		return getTF(content, path ,false);
	}

	/**
	 * tf值计算公式
	 * tf=N(i,j)/(sum(N(k,j) for all k))
	 * N(i,j)表示词语Ni在该文档d（content）中出现的频率，sum(N(k,j))代表所有词语在文档d中出现的频率之和
	 * @param content
	 * @param path  结巴分词，词库地址
	 * @return
	 */
	private Map<String, Double> getTF(String content, String path, boolean index) {
		Map<String,Double> tfMap=new HashMap<>();
		if(content==null || content.equals(""))
			return tfMap;


		loadDict(path);
		JiebaSegmenter segmenter = new JiebaSegmenter();
		List<String> segments;
		if(index){
			List<SegToken> segments1 =segmenter.process(content, JiebaSegmenter.SegMode.INDEX);
			 segments= new ArrayList<>();
			for(SegToken st : segments1){
				segments.add(st.word);
			}
		}else{
			 segments=segmenter.sentenceProcess(content);
		}

		Map<String,Integer> freqMap=new HashMap<>();
		
		int wordSum=0;
		for(String segment:segments) {
			//停用词不予考虑，单字词不予考虑
			if(!stopWordsSet.contains(segment)) {
				wordSum++;
				if(freqMap.containsKey(segment)) {
					freqMap.put(segment,freqMap.get(segment)+1);
				}else {
					freqMap.put(segment, 1);
				}
			}
		}
		
		// 计算double型的tf值
		for(String word:freqMap.keySet()) {
			tfMap.put(word,freqMap.get(word)*0.1/wordSum);
		}
		
		return tfMap; 
	}
	
	/**
	 * 默认jieba分词的停词表
	 * url:https://github.com/yanyiwu/nodejieba/blob/master/dict/stop_words.utf8
	 * @param set
	 */
	private void loadStopWords(Set<String> set, InputStream in){
		BufferedReader bufr;
		try
		{
			bufr = new BufferedReader(new InputStreamReader(in));
			String line=null;
			while((line=bufr.readLine())!=null) {
				set.add(line.trim());
			}
			try
			{
				bufr.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * idf值本来需要语料库来自己按照公式进行计算，不过jieba分词已经提供了一份很好的idf字典，所以默认直接使用jieba分词的idf字典
	 * url:https://raw.githubusercontent.com/yanyiwu/nodejieba/master/dict/idf.utf8
	 */
	private void loadIDFMap(Map<String,Double> map, InputStream in ){
		BufferedReader bufr;
		try
		{
			bufr = new BufferedReader(new InputStreamReader(in));
			String line=null;
			while((line=bufr.readLine())!=null) {
				String[] kv=line.trim().split(" ");
				map.put(kv[0],Double.parseDouble(kv[1]));
			}
			try
			{
				bufr.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			// 计算idf值的中位数
			List<Double> idfList=new ArrayList<>(map.values());
			Collections.sort(idfList);
			idfMedian=idfList.get(idfList.size()/2);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	// 自定义结巴词库加载
	public static void loadDict(String path) {
		WordDictionary.getInstance().loadUserDict(Paths.get(path));
	}

}

