package sample;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main extends Application { //todo: have a loading bar/loading circle

    private static final String URL1 = "https://www.watchcartoononline.io/boondocks-season-";
    private static final String URL1_4 = "https://www.watchcartoononline.io/the-boondocks-season-";
    private static final String URL2 = "-episode-";
    private static final String URL3 = "-english-dubbed";

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("The BoonDocks");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static String URL(int season, int episode){
        if(season == 4)
            return URL1_4+season+URL2+episode;
        else
            return URL1+season+URL2+episode;
    }



    public static class GetWebData extends Task {
        private final int season, episode;
        private final double maxValue= 1;

        public GetWebData(int season, int episode){
            this.season = season;
            this.episode = episode;
        }

        @Override
        protected String[] call() throws Exception {
            return getLink(season, episode);
        }

        private String[] getLink(int season, int episode) throws IOException {
            updateProgress(0, maxValue);
            final String frameID = "frameNewAnimeuploads0";
            String[] results = getFrameSource(frameID, season, episode); updateProgress(0.5, maxValue);
            String url = results[1];
            String link = actualGetLinkMethod(url); updateProgress(1, maxValue);



            return new String[]{results[0], link};
        }

        private String actualGetLinkMethod(String url) throws IOException {
            String webCode = webCode(url); updateProgress(0.75, maxValue);
            String first = "jw.setup";
            String second = "file: \"";
            String end = "\"";
            webCode = webCode.substring(webCode.indexOf(first));
            webCode = webCode.substring(webCode.indexOf(second) + second.length());
            return webCode.substring(0, webCode.indexOf(end));
        }

        private String[] getFrameSource(String id, int season, int episode) throws IOException {
            String webCode = webCode(Main.URL(season, episode));
            updateProgress(0.25, maxValue);
            String beginningTitle = "<title>";
            String endingTitle = " |";
            webCode = webCode.substring(webCode.indexOf(beginningTitle) + beginningTitle.length());
            String tittle = webCode.substring(0, webCode.indexOf(endingTitle));
            String beginning = "src=\"";
            String ending = "\"";

            webCode = webCode.substring(webCode.indexOf(id));
            webCode = webCode.substring(webCode.indexOf(beginning) + beginning.length());
            return new String[]{tittle, webCode.substring(0, webCode.indexOf(ending))};
        }

        private String webCode(String urlString) throws IOException {
            InputStream is = null;
            try {
                URL url = new URL(urlString);
                HttpURLConnection in = (HttpURLConnection) url.openConnection();
                in.addRequestProperty("User-Agent", "Foo?");

                in.setReadTimeout(0 /* milliseconds */);
                in.setConnectTimeout(0 /* milliseconds */);
                in.setRequestMethod("GET");
                in.setDoInput(true);
                in.connect();

                is = in.getInputStream();

                return IOUtils.toString(is, "UTF-8");
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }
    }


}
