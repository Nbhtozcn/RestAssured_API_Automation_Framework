package P01_Nescafe;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test Case: The subpages of nescafe.com/tr should be checked for a 404 error to verify if they return the correct pages.
 * Test steps:
 * Fetch and store "/tr" subpages in an array.
 * Visit each subpage one by one.
 * Check if the visited page returns an error or not.
 * Print the results to the console.
 */

public class Nescafe_Subpages_Test {
    @Test
    public void subPagesCheckerTest() {
        /** Step 1: Fetch and store "/tr" subpages in an array. */
        String sitemapUrl = "https://www.nescafe.com/tr/sitemap.xml";
        Response response = RestAssured.get(sitemapUrl);
        String sitemapContent = response.getBody().asString();
        List<String> urls = extractUrlsFromSitemapUsingRegex(sitemapContent);

        /** Step 2: Check if the visited page returns an error or not.*/
        for (String url : urls) {
            boolean isPageError = isPageError(url);
        /** Step3: Print the results to the console.*/
            System.out.println(url + " - " + (isPageError ? "Error" : "OK"));
        }

    }

    private List<String> extractUrlsFromSitemapUsingRegex(String sitemapContent) {
        List<String> urls = new ArrayList<>();
        String regex = "https://www\\.nescafe\\.com/tr/[a-zA-Z0-9\\-\\/]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sitemapContent);
        while (matcher.find()) {
            urls.add(matcher.group());
        }
        return urls;
    }

    private boolean isPageError(String url) {
        Response response = RestAssured.head(url);
        int statusCode = response.getStatusCode();
        return statusCode == 404;
    }
}
