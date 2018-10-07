import app.BusinessApp;
import app.BusinessView;
import javafx.application.Application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BusinessMain {
    private static Map<String, String> optionsMap = new HashMap<>();

    public static void main(String[] args) {
        try (Stream<String> stream = Files.lines(Paths.get("config.txt"))) {
            optionsMap = stream
                    .collect(Collectors.toMap(k -> k.substring(0, k.indexOf(":")),
                            v -> v.substring(v.indexOf(":") + 1)));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        switch (optionsMap.get("mode")) {
            case "0":
                BusinessApp.main(new String[]{optionsMap.get("path"), optionsMap.get("filename")});
                break;
            case "1":
                Application.launch(BusinessView.class, optionsMap.get("path"), optionsMap.get("filename"));
                break;
            default:
                BusinessView.launch(optionsMap.get("path"), optionsMap.get("filename"));
                break;
        }
    }
}
