
# Stocks

## How to run the program:
The steps to run this program are unchanged and can be referred from the SETUP-README.md
We have added another option in the text based menu to "Rebalance a portfolio."

The sequence of flow for text UI is as below,
``` bash
Enter Choice : 8
Enter the portfolio name whose rebalance you want : 
```

Input the portfolio name
Then you would get a prompt like below,

```bash
Enter the portfolio name whose rebalance you want : TestPortfolioRebalance
Enter the date (yyyy-MM-dd) for rebalance: 
```
Enter a valid input date and then hit enter

You should see the portfolio composition to get an idea 
about its composition.

Next, input all the available stock tickers in the portfolio 
and your wished proportions to re-balance the portfolio.

Once you are done, it performs the re-balancing and you can
verify by checking the portfolio composition.

GUI FLOW: 

Click on the option 8. Rebalance a portfolio

A window asking for the portfolio name and the date at which the rebalancing is required will open.
Enter the portfolio name as "College" and the date as "2022-11-30" and click on Submit.

Next, select the available stock tickers in the portfolio and enter your wished proportions to
re-balance the portfolio.

Once done, a message will pop up stating that you have reached the total of 100, you won't be able to
add more and the Done button will be enabled. Click on Done and after the rebalancing is done, a
message box displaying the success status will display.

Click on OK and you will see the portfolio name and date window again. If you want to rebalance 
another portfolio, provide the necessary details otherwise click on back to go back to the main menu.


## Parts that work:
Given a portfolio, for a given date and intended weights by percentage
of the portfolio value at the specified date, the portfolio stocks' 
weights are adjusted.

This operation can be carried out both through the text based UI
and the GUI.

## Change summary:
### Model:

For model, we added a couple of methods to the interface to support the re-balancing operation.
We did this to comply with the existing architecture and avoid breaking the MVC by exposing anything 
other than the Model to be exposed to controller.

1. Added the following methods in the model interface 
   - `rebalanceExistingPortfolio` to handle portfolio re-balancing. (Model.java line 356)
   - `getTickerSymbolsInPortfolio` to return a list of available stocks to re-balance on a given date. (Model.java line 367)
2. Implemented the above 2 methods in the Model implementation class,
   - `rebalanceExistingPortfolio` ModelImpl line 274
   - `getTickerSymbolsInPortfolio` ModelImpl line 301
3. The corresponding methods were to be supported in the portfolio instances as well.
   - `rebalanceStockInPortfolio` to re-balance the specified portfolio, (Portfolio.java line 69)
   - `getStocksInPortfolio` to get the list of stocks in the specified portfolio, (Portfolio.java line 71)
4. Implemented the above 2 methods in the PortfolioImpl class
   - `rebalanceStockInPortfolio` PortfolioImpl line 223
   - `getStocksInPortfolio` PortfolioImpl line 237

The rest of the interfaces remain unchanged.

### Controller:
1. For the GUI controller, first we added a new choice 8 for rebalancing a portfolio
   .Then, we added three new methods to the Features interface to keep it consistent with
   the existing design and implementation approach to avoid breaking the MVC. We added a method to rebalance a portfolio, to add stocks and their respective weights for rebalancing
   and lastly to get all the stock ticker symbols in the portfolio at the rebalancing date
   provided by the user. The same were implemented in the StockControllerGUIImpl class.
2. As for the text based controller, we added a new choice 8 for rebalancing a portfolio in 
   FlexiblePF class and implemented the private helper method required for its logic implementation.
3. The existing methods and interface signatures were left untouched.

### View:
1. For the GUI view, first we added the option for rebalancing a portfolio and its respective listeners.
2. Next, we also added four new methods in the ViewGUI interface, one for displaying the 
   window responsible to get the inputs of stocks and their weightages, second for displaying stock
   added successfully, third for displaying 100 weight reached successfully and lastly to display
   rebalancing success. All these methods were then implemented in ViewGUIImpl class.
3. As for the text based view, we created a new class RebalanceStockViewImpl which extends the older view
   class and overrode the method to display the menu containing a rebalancing option and for operations
   following the main menu were implemented reusing the existing code.
4. The existing methods and interface signatures were left untouched.
