import com.wolfram.alpha.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;

class AlphaAPI {
    // PUT YOUR APPID HERE:
    private static final String APPID = "YUVLLV-32YX2JJVVQ";

    public String fastQuery(String input) throws Exception {
        String query;
        StringBuffer result = new StringBuffer();
        query = "http://api.wolframalpha.com/v1/result?appid=" + APPID + "&i=" + URLEncoder.encode(input, "UTF-8");
        System.out.println("Fast Query URL is: " + query);
        java.net.URL url = new java.net.URL(query);
        BufferedReader read = new BufferedReader(new InputStreamReader(url.openStream()));
        result.append(read.readLine());
        System.out.println("Get it! Result is: " + result.toString());
        read.close();
        return String.valueOf(result);
    }

    public String fullResultQuery(String input) {
        StringBuffer result = new StringBuffer();
        // Use "pi" as the default query, or caller can supply it as the lone command-line argument.
        // String input = "ln_2(3)";
        // The WAEngine is a factory for creating WAQuery objects,
        // and it also used to perform those queries. You can set properties of
        // the WAEngine (such as the desired API output format types) that will
        // be inherited by all WAQuery objects created from it. Most applications
        // will only need to crete one WAEngine object, which is used throughout
        // the life of the application.
        WAEngine engine = new WAEngine();
        // These properties will be set in all the WAQuery objects created from this WAEngine.
        engine.setAppID(APPID);
        engine.addFormat("plaintext");
        // Create the query.
        WAQuery query = engine.createQuery();
        // Set properties of the query.
        query.setInput(input);
        try {
            // For educational purposes, print out the URL we are about to send:
            System.out.println("Query URL:");
            System.out.println(engine.toURL(query));
            System.out.println();
            // This sends the URL to the Wolfram|Alpha server, gets the XML result
            // and parses it into an object hierarchy held by the WAQueryResult object.
            WAQueryResult queryResult = engine.performQuery(query);
            if (queryResult.isError()) {
                result.append("Query error" + "\n");
                result.append("  error code: " + queryResult.getErrorCode() + "\n");
                result.append("  error message: " + queryResult.getErrorMessage() + "\n");
            } else if (!queryResult.isSuccess()) {
                result.append("Query was not understood; no results available.");
            } else {
                // Got a result.
                result.append("Successful query. Pods follow:\n");
                for (WAPod pod : queryResult.getPods()) {
                    if (!pod.isError()) {
                        result.append(pod.getTitle() + "\n");
                        result.append("------------" + "\n");
                        for (WASubpod subpod : pod.getSubpods()) {
                            for (Object element : subpod.getContents()) {
                                if (element instanceof WAPlainText) {
                                    result.append(((WAPlainText) element).getText() + "\n");
                                }
                            }
                        }
                        result.append("\n");
                    }
                }
                // We ignored many other types of Wolfram|Alpha output, such as warnings, assumptions, etc.
                // These can be obtained by methods of WAQueryResult or objects deeper in the hierarchy.
            }
        } catch (WAException e) {
            e.printStackTrace();
        }
        return String.valueOf(result);
    }
}
