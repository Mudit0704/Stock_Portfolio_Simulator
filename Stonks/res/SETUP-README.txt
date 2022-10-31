Steps to run the program:

<How to run the jar file>

Steps to create a portfolio with 3 different stocks
1. <Start the program with jar>
The startup menu would look something like this,

Choose from the below menu:
 1 -> Create a new portfolio
 2 -> Get portfolio composition (Stock Symbol -> Stock Quantity)
 3 -> Get a portfolio value at a given date
 4 -> Save/Retrieve portfolio at a given path
 E -> Exit from the application
Your input:

2. Input "1" which stands for "1 -> Create a new portfolio", to create a portfolio
3. There would be another menu titled - "Choose from the below menu:", input "1" for option 1 to add new stock.
4. Input any valid ticker symbol against the prompt "Stock Symbol:". If you input any invalid ticker
   symbol, you would get a prompt "Invalid Ticker Symbol " and the application will ask you again to
   input symbol
5. Once the ticker symbol is validated, you would get a prompt to input the stock quantity "Stock Quantity:"
   Enter any valid quantity.
6. After you input the quantity, the prompt in step 3 asking whether input stock or quit will pop up again.
   To continue further adding stocks to the portfolio, continue the steps from 3 to 5. Repeat these
   input sequences for 3 times to enter 3 different stock values.
8. Once you end up adding 3 stocks and at the end you get the prompt again as in step 3, input "E" to
   stop inputting stocks.
9. Once you input E, you will get the starting menu again. Feel free to add 2 different stocks now,
   these will be added to another portfolio or you can jump to the next step.
10. To query the portfolio value on a given date, choose option "3" from the starting menu
    "3 -> Get a portfolio value at a given date"
11. You will get a list of portfolios from which you can select the one for which you wish to know the portfolio value.
12. You would have to input 1 as portfolio id 1 for getting value of "Portfolio1", 2 for "Portfolio2" and so on if there are any.
13. Once you choose the portfolio, by typing in the portfolio id, you would get a prompt "Please enter the date (yyyy-mm-dd):"
14. Input the date in the requested format, and you would see the portfolio value on the given date.
15. Press "E" to exit from the operation
16. Repeat steps 10 to 15 in the same manner to get portfolio value for portfolio 2, provided you created it.

================================================================================================================
                                             NOTE:
----------------------------------------------------------------------------------------------------------------
The application supports data for all the stocks' closing price for the past 100 days.
You cannot fetch the portfolio value if the input date is prior to 100 days from now or any future date.

Due to API limitations this application can process upto 5 stocks without trouble. If you add more
than 5 different stocks at a time, the application will take time to load the stock data, please keep trying.

Please note, that the application does not persist the portfolio data unless the user explicitly
decides to do so by choosing option "4" in the startup menu. But it does so once the user asks for it
and loads the portfolio data as well.