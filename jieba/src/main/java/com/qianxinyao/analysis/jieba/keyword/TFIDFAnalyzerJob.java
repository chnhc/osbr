package com.qianxinyao.analysis.jieba.keyword;

import com.huaban.analysis.jieba.noWord.JiebaSegmenter;
import com.huaban.analysis.jieba.noWord.WordDictionary;

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
public class TFIDFAnalyzerJob
{
	
	static HashMap<String,Double> idfMap;
	static HashSet<String> stopWordsSet;
	static double idfMedian;
	
	/**
	 * tfidf分析方法
	 * @param content 需要分析的文本/文档内容
	 * @param topN 需要返回的tfidf值最高的N个关键词，若超过content本身含有的词语上限数目，则默认返回全部
	 * @return
	 */
	public List<Keyword> analyze(String content,int topN){
		List<Keyword> keywordList=new ArrayList<>();
		
		if(stopWordsSet==null) {
			stopWordsSet=new HashSet<>();
			loadStopWords(stopWordsSet, this.getClass().getResourceAsStream("/stop_words.txt"));
		}
		if(idfMap==null) {
			idfMap=new HashMap<>();
			loadIDFMap(idfMap, this.getClass().getResourceAsStream("/idf_job.txt"));
		}
		
		Map<String, Double> tfMap=getTF(content);
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
	
	/**
	 * tf值计算公式
	 * tf=N(i,j)/(sum(N(k,j) for all k))
	 * N(i,j)表示词语Ni在该文档d（content）中出现的频率，sum(N(k,j))代表所有词语在文档d中出现的频率之和
	 * @param content
	 * @return
	 */
	private Map<String, Double> getTF(String content) {
		Map<String,Double> tfMap=new HashMap<>();
		if(content==null || content.equals(""))
			return tfMap;

		String path1 =  "D:/workspace/jiebafile/jieba11.dict";
		loadDict(path1);
		JiebaSegmenter segmenter = new JiebaSegmenter();

		List<String> segments=segmenter.sentenceProcess(content);
		Map<String,Integer> freqMap=new HashMap<>();
		
		int wordSum=0;
		for(String segment:segments) {
			//停用词不予考虑，单字词不予考虑
			if(!stopWordsSet.contains(segment) && segment.length()>1) {
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

	public static void main(String[] args)
	{
		String content="福州晋安区东二环泰禾广场商场5楼蛙功夫招聘：炒锅数名（新手锅也可以）：4000-6000，洗碗大姐2名:3500-4500，烧烤人员1名:3500-4500，打荷人员1名:3500-4500，以上人员月休4天，国家法定节假日补休，正常班，包吃住，做的好每3个月进行调薪1次，要求过年找厨师去，年前年后都可以安排回去，做满一年带薪年假7天，联系人张店：18650398903";
		//String content="招配菜一位工资5500有休息有年假地址姑苏区西环路新庄鑫园楼酒店电话13451982484肖，谢谢平台，";
		//String content="招打荷一名 3500到4000 过年不放假 月休四天 节假日另有休息 包吃包住 福利多 每月有聚餐 工作地点金鸡湖 电话 13914095814";
		int topN=15;
		TFIDFAnalyzerJob tfidfAnalyzer=new TFIDFAnalyzerJob();
		List<Keyword> list=tfidfAnalyzer.analyze(content,topN);
		for(Keyword word:list){
			if(word.getTfidfvalue() > 1){
				System.out.print(word.getName()+":"+word.getTfidfvalue()+",");
			}
		}

	}
}

