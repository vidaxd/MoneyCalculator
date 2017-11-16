package moneycalculator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MoneyCalculator {

    public static void main(String[] args) throws Exception {
        MoneyCalculator moneyCalculator = new MoneyCalculator();
        moneyCalculator.execute();
    }
    
    private Map<String, Currency> currencies = new HashMap<>();
    private double amount;
    private double exchangeRate; 
    private Currency currencyFrom;
    private Currency currencyTo;
    
    public MoneyCalculator(){
        currencies.put("USD", new Currency("USD", "Dólar americano", "$"));
        currencies.put("EUR", new Currency("EUR", "Euros", "€"));
        currencies.put("GBP", new Currency("GBP", "Libras Esterlinas", "£"));
    }
    
    private void execute() throws Exception {
        input();
        process();
        output();
    }
    

    private void input() {
        System.out.println("Introduzca una cantidad");
        Scanner scanner = new Scanner(System.in);
        amount = Double.parseDouble(scanner.next());   
        
        System.out.println("Introduzca divisa inicial");
        currencyFrom = currencies.get(scanner.next());
        System.out.println("Introduzca divisa destino");
        currencyTo = currencies.get(scanner.next());   
    }

    private void process() throws Exception {
        exchangeRate = getExchangeRate(currencyFrom.getCode(), currencyTo.getCode());
    }

    private void output() {
        System.out.println(amount + currencyFrom.getSymbol() + " equivalen a " + amount * exchangeRate + currencyTo.getSymbol());
    }
    
    private static double getExchangeRate(String from, String to) throws Exception{
        URL url = new URL("http://api.fixer.io/latest?base=" + from + "&symbols=" + to);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        InputStreamReader input = new InputStreamReader(connection.getInputStream());
        try (BufferedReader reader = new BufferedReader(input)){
            String line = reader.readLine();
            line = line.substring(line.indexOf(to)+5, line.indexOf("}"));
            return Double.parseDouble(line);
        }
    }
    
    
}
