//import com.gargoylesoftware.htmlunit.html.DomElement;
//import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
//import com.gargoylesoftware.htmlunit.html.HtmlElement;
//import com.gargoylesoftware.htmlunit.html.HtmlPage;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ArticleScrapper implements Runnable{
//
//    private Thread t;
//    private String threadName;
//    HtmlPage articlePage;
//    String fileName;
//    String tempText;
//    public ArticleScrapper(String name, HtmlPage articlePage, String fileName, String tempText){
//        threadName = name;
//        this.articlePage = articlePage;
//        this.fileName = fileName;
//        this.tempText = tempText;
//        System.out.println("Creating:" + threadName);
//    }
//
//    public void run() {
//        System.out.println("Running:" + threadName);
//        try {
//            List<String> articleTextList = getArticleText(articlePage, tempText);
//            Thread.sleep(50);
//            saveToFile(articleTextList, fileName);
//            Thread.sleep(50);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void start () {
//        System.out.println("Starting " +  threadName );
//        if (t == null) {
//            t = new Thread (this, threadName);
//            t.start ();
//        }
//    }
//
//    public static List<String> getArticleText(HtmlPage articlePage, String tempText) throws Exception {
//
//        List<HtmlAnchor> anchors = (List<HtmlAnchor>) articlePage.getByXPath("//a[@id='addFlashPageParameterformat_fulltext']");
//        List<String> articleTextList = new ArrayList<String>();
//        String articleText = "";
//        HtmlPage article;
//        HtmlAnchor authorAnchor;
//
//        for (HtmlAnchor anchor : anchors) {
//            System.out.println(anchor.asText());
//            article = anchor.click();
////            if (count == 3){
////                System.out.println(article.asXml());
////            }
//            List<HtmlElement> titleElements = (List<HtmlElement>) article.getByXPath("//div[@class='docViewContentHeader']//h1");
//            if (!titleElements.isEmpty()) {
//                for (HtmlElement element : titleElements) {
//                    articleText = element.asText();
//                }
//            }
//            articleText += "\r\n";
//            authorAnchor = article.getFirstByXPath("//a[@id='lateralSearch']");
//            if (authorAnchor != null) {
//                articleText += authorAnchor.asText();
//            }
//            articleText += tempText;
//            articleText += "\r\n";
//            articleText += "\r\n";
//            List<HtmlElement> elements = (List<HtmlElement>) article.getByXPath("//div[@id='fullTextZoneId']//p");
//            if (!elements.isEmpty()) {
//                for (HtmlElement element : elements) {
//                    if (element.asText().length() == 0) {
//                        continue;
//                    }
//                    articleText += element.asText();
//                    articleText += "\r\n";
//                    articleText += "\r\n";
//                }
//            }
//            DomElement wordCount = article.getFirstByXPath("//div[@class='wordCount']");
//            if (wordCount != null) {
//                articleText += wordCount.asText();
//                articleText += "\r\n";
//
//            }
//
//            DomElement copyRight = article.getFirstByXPath("//div[@id='copyRightDiv']");
//            if (copyRight != null) {
//                articleText += copyRight.asText();
//                articleText += "\r\n";
//
//            }
//
//            articleTextList.add(articleText);
//            Thread.sleep(1000);
//        }
//        return articleTextList;
//    }
//
//    public static void saveToFile(List<String> articleTextList, String fileName) throws Exception {
//        int i = 1;
//        String tempFileName;
//        String filePath;
//        List<PrintWriter> writers = new ArrayList<PrintWriter>();
//        for (String articleText:articleTextList) {
//            tempFileName = "A" + String.format("%02d", i++) + ".txt";
//            filePath = "E:\\Comslider\\On Campus Job\\Reo\\2014Temp\\" + fileName + tempFileName;
//            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(
//                    new BufferedOutputStream(
//                            new FileOutputStream(filePath, false), 1024)));
//            writers.add(printWriter);
//            printWriter.println(articleText);
//            printWriter.flush();
//        }
//
//        for (PrintWriter writer: writers) {
//            writer.flush();
//            writer.close();
//        }
//    }
//
//}
