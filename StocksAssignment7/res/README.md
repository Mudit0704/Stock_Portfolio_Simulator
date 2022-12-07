
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

## Parts that work:
Given a portfolio, for a given date and intended weights by percentage
of the portfolio value at the specified date, the portfolio stocks' 
weights are adjusted.

This operation can be carried out both through the text based UI
and the GUI.

## Change summary:
### Model:
1. For model, we added a couple of methods to the interface to keep consistent with the existing design.
    We did this to comply with the existing architecture and avoid breaking the MVC by exposing anything other than the Model to be exposed to controller.
2. We added the method to re-balance a portfolio in the model. The corresponding method had to be added to the Portfolios interface as well.
3. The rest of the methods and interface signatures were left untouched.

### Controller:

### View:
