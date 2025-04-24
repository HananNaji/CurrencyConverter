package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CurrencyConverter extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("محول العملات");

        Label titleLabel = new Label("محول العملات");
        titleLabel.setFont(new Font(20));

        TextField amountField = new TextField();
        amountField.setPromptText("أدخل المبلغ");

        Label amountLabel = new Label("المبلغ:");

        ComboBox<String> fromCurrency = new ComboBox<>();
        fromCurrency.getItems().addAll("USD/دولار أمريكي", "EUR/يورو", "GBP/جنيه إسترليني", "ILS/شيكل إسرائيلي", "INR/روبية هندية");

        ComboBox<String> toCurrency = new ComboBox<>();
        toCurrency.getItems().addAll("USD/دولار أمريكي", "EUR/يورو", "GBP/جنيه إسترليني", "ILS/شيكل إسرائيلي", "INR/روبية هندية");

        Label fromLabel = new Label("من:");
        Label toLabel = new Label("إلى:");

        Button convertButton = new Button("تحويل");
        Text resultText = new Text();

        convertButton.setOnAction(e -> {
            double amount = Double.parseDouble(amountField.getText());
            String fromCurrencyCode = fromCurrency.getValue().split("/")[0];
            String toCurrencyCode = toCurrency.getValue().split("/")[0];

            String result = convertCurrency(amount, fromCurrencyCode, toCurrencyCode);
            resultText.setText(result);
        });

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        grid.add(titleLabel, 0, 0, 2, 1);
        grid.add(amountLabel, 0, 1);
        grid.add(amountField, 1, 1);
        grid.add(fromLabel, 0, 2);
        grid.add(fromCurrency, 1, 2);
        grid.add(toLabel, 0, 3);
        grid.add(toCurrency, 1, 3);
        grid.add(convertButton, 0, 4, 2, 1);
        grid.add(resultText, 0, 5, 2, 1);

        Scene scene = new Scene(grid, 350, 300);
        stage.setScene(scene);
        stage.show();
    }

    private String convertCurrency(double amount, String fromCurrency, String toCurrency) {
        try {
            String urlString = String.format("https://api.exchangerate-api.com/v4/latest/%s", fromCurrency);
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String responseString = response.toString();
            String exchangeRateString = responseString.split(toCurrency + "\":")[1].split(",")[0];
            double exchangeRate = Double.parseDouble(exchangeRateString);

            double convertedAmount = amount * exchangeRate;
            return String.format("%.2f %s = %.2f %s", amount, fromCurrency, convertedAmount, toCurrency);

        } catch (Exception e) {
            e.printStackTrace();
            return "خطأ في جلب البيانات";
        }
    }
}
