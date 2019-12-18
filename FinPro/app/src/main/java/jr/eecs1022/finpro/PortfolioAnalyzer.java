package jr.eecs1022.finpro;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jba.roumani.lib.Stock;

/**
 * This class encapsulates a portfolio of investments and
 * provides tools for performing portfolio analysis.
 *
 * @author H. Roumani
 * @since Winter 2016
 */
public class PortfolioAnalyzer
{
    private String title;
    private List<Equity> portfolio;
    public static final long MS_PER_DAY = 24L * 3600 * 1000;
    public static final int DAY_PER_YEAR = 365;

    public PortfolioAnalyzer(String title, String[] rows)
    {
        this.title = title;
        this.portfolio = new ArrayList<Equity>();
        for (String row : rows)
        {
            Equity equity;
            String[] fields = row.split(",");
            try
            {
                String symbol = fields[0];
                int qty = Integer.parseInt(fields[1]);
                double bookValue = Double.parseDouble(fields[2]);
                Date acquired = new SimpleDateFormat("d/m/y").parse(fields[3]);
                double marketValue = this.getInvestmentMarketValue(symbol);
                double yield = this.getInvestmentYield(bookValue, marketValue, acquired);
                equity = new Equity(symbol, qty, bookValue, acquired, marketValue, yield);
            } catch (Exception e)
            {
                equity = new Equity("?", 0, 0.0, null, 0.0, 0.0);
            }
            this.portfolio.add(equity);
        }
    }

    public List<Equity> getPortfolio()
    {
        return this.portfolio;
    }

    public int getPortfolioSize()
    {
        return portfolio.size();
    }

    public String toString()
    {
        return "The " + this.title + " portfolio consists of " +
                this.getPortfolioSize() + " equities. \n" +
                "It has a market value of " +
                String.format("$%,.2f", this.getPortfolioMarketValue()) +
                " and a yield of " +
                String.format("%,.1f%% (annualized).", 100 * this.getPortfolioYield());
    }

    private double getInvestmentMarketValue(String symbol)
    {
        Stock stock = new Stock(symbol);
        return stock.getPrice();
    }

    private double getInvestmentYield(double bookValue, double marketValue, Date acquired)
    {
        // creates a date of the current time
        Date currentDate = new Date();

        // instructions unclear as to whether days are saved as full days or as fractions (will assume full days)
        long days = (currentDate.getTime() - acquired.getTime())/MS_PER_DAY;

        double result = ((marketValue - bookValue) / bookValue)*(DAY_PER_YEAR / (double)days);
        return result;
    }

    public double getPortfolioMarketValue()
    {
        double sum = 0;
        for (Equity equity : this.portfolio) sum += equity.getMarketValue() * equity.getQty();
        return sum;
    }

    public double getPortfolioYield()
    {

        double weightedYieldSum = 0;
        double weightSum = 0;
        for (Equity equity : this.portfolio)
        {
            weightedYieldSum += equity.getQty() * equity.getBookValue() * equity.getYield();
            weightSum += equity.getQty() * equity.getBookValue();
        }
        return weightedYieldSum / weightSum;

    }
}