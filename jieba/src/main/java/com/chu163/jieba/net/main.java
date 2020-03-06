package com.chu163.jieba.net;

import com.chu163.jieba.net.data.Region;
import com.chu163.jieba.net.data.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

public class main {

    private static Gson gson = new Gson();

    public static void main(String[] arg) {

        /*
            type: 1-省份 ， 2-市区 ， 3-区域

            1、 省份   type,provinceCode  1,13

            2、 市区   type,provinceCode,cityCode  2,13,1302

            3、 区域   type,provinceCode,cityCode,regionCode   3,13,1302,130202


            -----------------------------

            1、省份 去除结尾 市 省 自治区 壮族自治区 回族自治区 维吾尔自治区

            2、市区 去除结尾 市 区 盟 地区 ，自治县、自治州结尾，删除自治县、自治州 分词，

            3、自治县、自治州结尾，删除自治县、自治州 分词， 完整市、区、地区，去除市、区、地区
         */


        test4();


    }


    public static void test4() {
     /*   // 词典路径为Resource/dicts/jieba.dict
        Path path = Paths.get("D:/workspace/jiebafile/jieba.dict");
        WordDictionary.getInstance().loadUserDict(new File("D:/workspace/jiebafile/xian.dict"));


        //WordDictionary.getInstance().init(path);

        JiebaSegmenter segmenter = new JiebaSegmenter();

        String line = "宝安坪洲大千里新店开业招炒尾灶一名4500到 砧板一名4000 打荷两名3300 月休四天，包食宿 购买社保 新年初开分店上升空间大 有意者电话联系18623791698楊厨 菜品简单好操作";


        List<Word> words = segmenter.sentenceProcess(line);

        for (Word w : words) {
            System.out.println(w.getToken() + " - " + w.getTokenType());
        }*/
    }


    static String[] endP = new String[]{"市", "省", "壮族自治区", "回族自治区", "维吾尔自治区", "自治区"};

    static String[] endC = new String[]{"市", "区", "盟", "地区"};

    static String[] endR = new String[]{"市",  "县", "区", "地区"};

    public static void test1() {
        // Gson  https://blog.csdn.net/qq_34292479/article/details/86495412

      /*  com.huaban.analysis.jieba.newPage.JiebaSegmenter segmenter = new com.huaban.analysis.jieba.newPage.JiebaSegmenter();

        String pc = "http://chu163.com:8099/cook/geo/getProvinceCity";

        String Json = HttpRequestUtil.sendGet(pc);

        Response response = gson.fromJson(Json, Response.class);

        List<Province> provinces = gson.fromJson(gson.toJson(response.getData()), new TypeToken<List<Province>>() {
        }.getType());

        ArrayList<Integer> integers = new ArrayList<>();

        for (Province p : provinces) {
            String pr = p.getLabel();
            //1、省份 去除结尾 市 省 自治区 壮族自治区 回族自治区 维吾尔自治区
            for (String e : endP) {
                if (pr.endsWith(e)) {
                    pr = pr.substring(0, pr.length() - e.length());
                }
            }
            System.out.println(pr + " 1 1");
            for (City c : p.getChildren()) {
                String ci = c.getLabel();
                //  2、市区 去除结尾 市 区 盟 地区
                for (String cs : endC) {
                    if (ci.endsWith(cs)) {
                        ci = ci.substring(0, ci.length() - cs.length());
                    }
                }
                // ，自治县、自治州结尾，删除自治县、自治州 分词
                if(ci.endsWith("自治县") || ci.endsWith("自治州")){
                    ci = ci.substring(0, ci.length() - 3);
                    for(String s: segmenter.sentenceProcess(ci)){
                        System.out.println(s + " 1 2," + p.getLabel()+c.getLabel());
                    }
                }else{
                    System.out.println(ci + " 1 2," + p.getLabel()+c.getLabel());
                }
                for(Region r: getRegion(c.getValue())){
                    StringBuffer out = new StringBuffer();
                    String re = r.getName();
                    // 3、 完整市、区、地区，去除市、区、地区
                    for (String rs : endR) {
                        if (re.endsWith(rs)) {
                            //System.out.println();
                            out.append( re+" 1 3,"+p.getLabel()+c.getLabel()+r.getName()+"\r\n");
                            //writeRb();
                            re = re.substring(0, re.length() - rs.length());
                        }
                    }
                    // ，自治县、自治州结尾，删除自治县、自治州 分词
                    if(re.endsWith("自治县") || re.endsWith("自治州")){
                        re = re.substring(0, re.length() - 3);
                        for(String s: segmenter.sentenceProcess(re)){
                            //System.out.println(s+" 1 3,"+p.getLabel()+c.getLabel());
                            out.append(s+" 1 3,"+p.getLabel()+c.getLabel()+r.getName()+"\r\n");
                        }
                    }else{
                        //System.out.println(re+" 1 3,"+p.getLabel()+c.getLabel());
                        out.append(re+" 1 3,"+p.getLabel()+c.getLabel()+r.getName()+"\r\n");
                    }
                    writeRb(out.toString());
                    out = null;
                }

            }
        }*/


    }


    public static void writeRb(String text){
        try {
            FileWriter writer = new FileWriter("D:/workspace/jiebafile/xian.dict",true);
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(text);

            bw.close();
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public static List<Region> getRegion(int cityCode) {

        String r = "http://chu163.com:8099/cook/geo/listRegion?cityCode=" + cityCode;

        String Json = HttpRequestUtil.sendGet(r);

        Response response = gson.fromJson(Json, Response.class);

        return gson.fromJson(gson.toJson(response.getData()), new TypeToken<List<Region>>() {
        }.getType());
    }


    public static void test2() {
      /*  // 词典路径为Resource/dicts/jieba.dict
        Path path = Paths.get("D:/workspace/jiebafile/jieba.dict");
        WordDictionary.getInstance().loadUserDict(new File("D:/workspace/jiebafile/jieba.dict"));


        //WordDictionary.getInstance().init(path);

        JiebaSegmenter segmenter = new JiebaSegmenter();

        String line = "玉龙纳西族自治县";
      *//*  String[] sentences =
                new String[] {"这是一个伸手不见五指的黑夜。我叫孙悟空，我爱北京，我爱Python和C++。", "我不喜欢日本和服。", "雷猴回归人间。",
                        "工信处女干事每月经过下属科室都要亲口交代24口交换机等技术性器件的安装工作", "结果婚的和尚未结过婚的"};
        for (String sentence : sentences) {
            System.out.println(segmenter.process(sentence, JiebaSegmenter.SegMode.INDEX).toString());
        }*//*

        List<Word> words = segmenter.sentenceProcess(line);

        for (Word w : words) {
            System.out.println(w.getToken() + " - " + w.getTokenType());
        }
*/
    }

}
