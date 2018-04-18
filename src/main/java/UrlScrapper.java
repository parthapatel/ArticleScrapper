import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UrlScrapper {
    public static int count = 0;
    public static void main(String[] args) throws Exception {
        String id;
        HtmlPage articlesPage;
        HtmlPage articlesPage2;
        HtmlAnchor dateAnchor;
        HtmlAnchor articlesPage2_anchor;
        String fileName;
        String tempText;

//        List<List<HtmlAnchor>> anchorsList = new ArrayList<List<HtmlAnchor>>();
//        String url2 = "https://search.proquest.com/hnpnewyorktimes/publication/45545/citation/80704C1F21E346EDPQ/3?accountid=10351";
//        String url2 = "https://search.proquest.com/publication/11561/citation/7BEC70875FCD4CB9PQ/1?accountid=10351";
        String url2 = "https://search.proquest.com/publication/10482?accountid=10351&OpenUrlRefId=info:xri/sid:primo";
//        String url2 = "https://search.proquest.com/globalnews/publication/10327/citation/CE096E87A85D4FF8PQ/5?accountid=10351";
//        String url2 = "https://search.proquest.com/globalnews/publication/15008/citation/BC80021DB12945C1PQ/1?accountid=10351";
//        String url2 = "https://search.proquest.com/publication/10482?accountid=10351&OpenUrlRefId=info:xri/sid:primo";
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
//        webClient.setAjaxController(new AjaxController(){
//            @Override
//            public boolean processSynchron(HtmlPage page, WebRequest request, boolean async)
//            {
//                return true;
//            }
//        });
        webClient.getOptions().setCssEnabled(true);
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
        HtmlPage homePage = webClient.getPage(url2);
        HtmlAnchor decadeAnchor = (HtmlAnchor) homePage.getFirstByXPath("//a[@id='filterdecade2010-2019']");
        HtmlPage yearPage = decadeAnchor.click();
        HtmlAnchor yearAnchor = yearPage.getFirstByXPath("//a[@id='filteryear20162010-2018']");
        HtmlPage monthPage = yearAnchor.click();
        HtmlAnchor monthAnchor = monthPage.getFirstByXPath("//a[@id='filtermonth042016']");
        HtmlPage datePage = monthAnchor.click();

        for(int i=30; i>22; i--){
//            if (i == 23 || i == 15 || i == 7 )
            id = "filterissueNameApr" + i + ",2016April";
            fileName = "D201604" + String.format("%02d", i);
            tempText = "  Wall Street Journal, Eastern edition; New York, N.Y. [New York, N.Y]" + i + " Apr 2016: A.1.";
            try {
                System.out.println("\r\n" + id);
                dateAnchor = datePage.getFirstByXPath("//a[@id='" + id + "']");
                if (dateAnchor != null) {
                    articlesPage = dateAnchor.click();
                } else {
                    continue;
                }
                articlesPage2_anchor = articlesPage.getFirstByXPath("//a[@title='Page 2']");
                if (articlesPage2_anchor != null) {
                    articlesPage2 = articlesPage2_anchor.click();
                } else {
                    continue;
                }
//            System.out.println(articlesPage2.asXml());
                List<String> articlesTextList = getArticleText(articlesPage2, tempText);
                saveToFile(articlesTextList, fileName);
            }
            catch (Exception e){
                System.out.println("SKIP");
            }
//            Thread.sleep(3000);
        }
    }

    public static int getArticle(HtmlPage articlePage, String fileName, String tempText) throws IOException {

        List<HtmlAnchor> anchors = articlePage.getByXPath("//a[@id='addFlashPageParameterformat_fulltext']");
        int i = 1;
        String tempFileName;
        String filePath;
        HtmlPage article;
        HtmlAnchor authorAnchor;
        List<PrintWriter> writers = new ArrayList<PrintWriter>();
//        PrintWriter printWriter = null;
        for (HtmlAnchor anchor : anchors) {
            System.out.println(anchor.asText());
            tempFileName = "A" + String.format("%02d", i++) + ".txt";
            filePath = "E:\\Comslider\\On Campus Job\\Reo\\Temp\\" + fileName + tempFileName;
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(
                    new BufferedOutputStream(
                            new FileOutputStream(filePath, false), 1024)));
            writers.add(printWriter);
            article = anchor.click();
            List<HtmlElement> titleElements = article.getByXPath("//div[@class='docViewContentHeader']//h1");
            if (!titleElements.isEmpty()) {
                for (HtmlElement element : titleElements) {
                    printWriter.println(element.asText());
                }
            }
//                printWriter.write(titleName);
//                printWriter.write(System.getProperty("line.separator"));
            authorAnchor = article.getFirstByXPath("//a[@id='lateralSearch']");
            if (authorAnchor != null) {
                printWriter.print(authorAnchor.asText() + ". ");
            }
            printWriter.println(tempText);
            printWriter.println();
            List<HtmlElement> elements =  article.getByXPath("//div[@id='fullTextZoneId']//p");
            if (!elements.isEmpty()) {
                for (HtmlElement element : elements) {
                    if (element.asText().length() == 0) {
                        continue;
                    }
                    printWriter.println(element.asText());
                    printWriter.println();
                }
            }
            DomElement wordCount = article.getFirstByXPath("//div[@class='wordCount']");
            if (wordCount != null) {
                printWriter.println(wordCount.asText());

            }

            DomElement copyRight = article.getFirstByXPath("//div[@id='copyRightDiv']");
            if (copyRight != null) {
                printWriter.println(copyRight.asText());

            }
            printWriter.flush();
            printWriter.close();
        }

        for (PrintWriter writer:writers) {
            writer.flush();
            writer.close();
        }

        return 0;
    }

    public static List<String> getArticleText(HtmlPage articlePage, String tempText) throws Exception {

        count++;
        List<HtmlAnchor> anchors = articlePage.getByXPath("//a[@id='addFlashPageParameterformat_fulltext']");
        List<String> articleTextList = new ArrayList<String>();
        String articleText = "";
        HtmlPage article = null;
        HtmlAnchor authorAnchor;
        int i = 0;
        for (HtmlAnchor anchor : anchors) {
            ++i;
//            if (i==1 || i==2 || i==3 || i==4 || i==5 || i==6 || i==7){
//                continue;
//            }
            if (i == 6){
                break;
            }
            System.out.print(anchor.asText() + " ");
//            System.out.println(anchor.asXml() + " ");
            try {
                if (anchor != null) {
                    article = anchor.dblClick();
                }
                List<HtmlElement> titleElements = article.getByXPath("//div[@class='docViewContentHeader']//h1");
                if (!titleElements.isEmpty()) {
                    for (HtmlElement element : titleElements) {
                        articleText = element.asText();
                    }
                }
                articleText += "\r\n";
                authorAnchor = article.getFirstByXPath("//a[@id='lateralSearch']");
                if (authorAnchor != null) {
                    articleText += authorAnchor.asText();
                }
                articleText += tempText;
                articleText += "\r\n";
                articleText += "\r\n";
                List<HtmlElement> elements = article.getByXPath("//div[@id='fullTextZoneId']//p");
                if (!elements.isEmpty()) {
                    for (HtmlElement element : elements) {
                        if (element.asText().length() == 0) {
                            continue;
                        }
                        articleText += element.asText();
                        articleText += "\r\n";
                        articleText += "\r\n";
                    }
                }
                DomElement wordCount = article.getFirstByXPath("//div[@class='wordCount']");
                if (wordCount != null) {
                    articleText += wordCount.asText();
                    articleText += "\r\n";

                }

                DomElement copyRight = article.getFirstByXPath("//div[@id='copyRightDiv']");
                if (copyRight != null) {
                    articleText += copyRight.asText();
                    articleText += "\r\n";

                }

                articleTextList.add(articleText);
            }
            catch (Exception e){
                e.printStackTrace();
//                System.out.println("Skip");
            }
//            Thread.sleep(1000);
        }
        return articleTextList;
    }

    public static void saveToFile(List<String> articleTextList, String fileName) throws Exception {
        int i = 6;
        String tempFileName;
        String filePath;
        List<PrintWriter> writers = new ArrayList<PrintWriter>();
        for (String articleText:articleTextList) {
            tempFileName = "A" + String.format("%02d", i++) + ".txt";
            filePath = "E:\\Comslider\\On Campus Job\\Reo\\WSJ_New\\" + fileName + tempFileName;
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(
                    new BufferedOutputStream(
                            new FileOutputStream(filePath, false), 1024)));
            writers.add(printWriter);
            printWriter.println(articleText);
            printWriter.flush();
        }

        for (PrintWriter writer: writers) {
            writer.flush();
            writer.close();
        }
    }

}
