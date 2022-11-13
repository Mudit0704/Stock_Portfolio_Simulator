====================================================================================================
                              STEPS TO RUN THE PROGRAM:
====================================================================================================

Steps to create a portfolio with 3 different stocks
1. Create/Move the provided folder 'flexiblePortfolio' in the directory where you run the jar.
2. Open a command line prompt/terminal in admin/sudo mode. Type this command 'java -jar "Stonks.jar"'
The startup menu would look something like this,

Choose from the below menu:
 1 -> Create a static portfolio
 2 -> Create a flexible portfolio
 E -> Exit from the application
Your input:

2. If you press 1, follow the directions mentioned in the section "STEPS TO RUN WITH INFLEXIBLE PORTFOLIO"
3. Press 2 to test creation with flexible portfolio. You will see the following menu,

Choose from the below menu:
 1 -> Create a new portfolio at a given date
 2 -> Get portfolio composition at a given date (Stock Symbol -> Stock Quantity)
 3 -> Get a portfolio value at a given date
 4 -> Save/Retrieve portfolio at a given path
 5 -> Perform a transaction on an existing portfolio
 6 -> Determine the cost basis of an existing portfolio at a given date
 7 -> Display portfolio performance
 8 -> Set the commission fee
 E -> Exit from the application
Your input:

4. Press "1" for 1 -> Create a new portfolio at a given date
5. You would see a prompt asking you for the date like this

     Please enter the date (yyyy-mm-dd):

6. Input the date "2015-10-10" in the format as mentioned and press enter.
7. You would see another prompt

    Choose from the below menu:
     1 -> Add a new stock
     E -> Exit from the operation

8. Input "1" for option 1 to add new stock.
9. You would see another prompt

     Stock Symbol:

10. Input the ticker symbol "GOOG" or any other valid ticker symbol. If you input invalid symbol,
    you would get an error prompt "Invalid input"
11. You would see another prompt, input any quantity

     Stock Quantity:

12. You would see the prompt again as in step 7.
13. Repeat steps 8-12 for as many times to add any more stocks to portfolio, or you can choose to Exit by inputting "E" (Shift + "e").
14. If you pressed "E" then you would see the prompt as in Step 3 again.
15. Now to add stocks to portfolio on any given date choose option "5" for 5 -> Perform a transaction on an existing portfolio
16. You would see another prompt like below

    Choose from available portfolios (eg: Portfolio1 -> give 1):
    Portfolio1(Creation datetime: 2022-11-13 15:00:30)

17. Follow the prompt and choose the portfolio input by inputting the corresponding number, in this case, choose 1
18. Once input, you would see another prompt,

    Please provide stock details for the transaction:
    Stock Symbol:

19. Input "VZ" or any other valid stock ticker symbol.
20. You would see the prompt, input the date as mentioned below

    Please enter date for the transaction:
    Please enter the date (yyyy-mm-dd): 2016-10-10

21. Then you would see another prompt

    Choose from the below menu:
     1 -> Purchase a new stock
     2 -> Sell a stock
     E -> Exit from the operation

22. Input "1" to purchase VZ stocks.
23. You should now see

    Purchased
    Press E to exit from the operation

24. Input "E" to come out of this.
25. Repeat steps 15-24 to add another stock. In step 20, input the date as "2020-10-10".
    Do this again with different stock ticker and different dates.
26. Once you are done, and you see the menu as in step 3, you can choose option "3" to get portfolio value
    on any date.
27. Choose from available portfolio, as in this prompt

    Choose from available portfolios (eg: Portfolio1 -> give 1):
    Portfolio1(Creation datetime: 2022-11-13 15:00:30)
    Your input: 1

28. Input date "2016-01-01", refer the below example,

    Please enter the date (yyyy-mm-dd): 2016-01-01
    Portfolio1
    814.36
    Press E to exit from the operation

29. Press "E" to exit, and you would see the start-up menu again(step 3).
30. Repeat 26-29. This time input the date "2019-01-01", refer the example below,

    Please enter the date (yyyy-mm-dd): 2019-01-01
    Portfolio1
    1193.40
    Press E to exit from the operation

31. Press "E" to exit, and you would see the start-up menu again(step 3).
32. To get cost basis, input "6", you would see the below prompt,

    Choose from available portfolios (eg: Portfolio1 -> give 1):
    Portfolio1(Creation datetime: 2022-11-13 15:00:30)
    Your input: 1

33. You would see another prompt, refer below,

    Please enter the date (yyyy-mm-dd): 2015-10-30

34. You would see the cost basis, Input "E" from the operation and repeat steps 32-33 but this time input date as "2020-10-10"
    refer the below,

    Choose from available portfolios (eg: Portfolio1 -> give 1):
    Portfolio1(Creation datetime: 2022-11-13 15:00:30)
    Your input: 1
    Please enter the date (yyyy-mm-dd): 2020-10-10

35. You would see a different cost basis now since you must have purchased different stocks between the previous 2
    dates you queried cost basis.

================================================================================================================
                                             NOTE:
----------------------------------------------------------------------------------------------------------------
The application supports data for all the stocks' closing price for the 6 years.
If you input any stock which didn't exist at any point in time, then its value will be considered as 0 while computing
portfolio value.


Please note, the application will not persist the portfolio unless the user explicitly specifies through the menu option "4".

In order to upload user generated XMLs, the user will have to create an XML file as per the format in the provided demo.xml file and save in the same
directory as that of the jar file.


The user will also have to ensure that the order of transactions/quantity history is in decreasing order of dates,
i.e. the most recent date of transaction should be 1st for each respective stock. Refer the updated
demo-v2.xml



====================================================================================================
                              STEPS TO RUN WITH INFLEXIBLE PORTFOLIO:
====================================================================================================

Steps to run the program:

Steps to create a portfolio with 3 different stocks
1. Open a command line prompt/terminal in admin/sudo mode. Type this command 'java -jar "Stonks.jar"'
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
15. Press "E" (Shift + "E"/Uppercase E) to exit from the operation
16. Repeat steps 10 to 15 in the same manner to get portfolio value for portfolio 2, provided you created it.

================================================================================================================
                                             NOTE:
----------------------------------------------------------------------------------------------------------------
The application supports data for all the stocks' closing price for the past 100 days.
You cannot fetch the portfolio value if the input date is prior to 100 days from now or any future date.

Due to API limitations this application can process upto 5 stocks without trouble. If you add more
than 5 different stocks at a time, the application will take time to load the stock data, please try after a minute or so.

Please note, the application will not persist the portfolio unless the user explicitly specifies through the menu option "4".

In order to upload user generated XMLs, the user will have to create an XML file as per the format in the provided demo.xml file and save in the same
directory as that of the jar file.