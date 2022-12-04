
# Stocks (Part 3) - Team 83135

This application has been developed and iterated as part of the CS 5010 - Programming Design Paradigm course in the Fall semester 2022. 

In the notion of helping users who are new to investing, and to learn about how money could grow, this stocks application has been developed in the style of "virtual gambling". Similar to virtual gambling, our application will not use real money and investment. Instead, it will allow the user to create investment portfolios (now the option of editing existing portfolios), try out buying and selling of stock, and various investment strategies to see how they can grow (or shrink) their money with time.

This has been built in the MVC (Model-View-Controller) style. 

## Features V3 (Part 3)
Below are the list of features that are part of this iteration (also to note that these features are up and running):
- Amounts can now be invested to portfolio using fixed strategy. This can also lead to purchase of fractional shares which the user cannot purchase explicitly.
- Amounts can be invested for a certain time frame using DCA (Dollar Cost Averaging)
- Graphical user implementation to create, update and upload a flexible portfolio
- Operations on a flexible portfolio such as cost basis, composition, total and performance graph (bonus) are also implemented on the GUI
- Dollar cost averaging (recurring investments) and investing an amount on a particular date is also supported 

## Features V2 (Part 2)
Below are the list of features that are part of this iteration (also to note that these features are up and running):
- Shares can now be bought or sold based on the dates ( Flexible portfolios)
- Examine the composition of a portfolio based on a certain date. Will throw error if invalid portfolio names or dates are entered
- Determine the total value and cost basis of a portfolio upto a certain date. Will return 0 if invalid portfolio names or dates are entered or the market is closed on a particular date
- Determine the performance of a portfolio, given the start and end dates, on a simple bar graph that prints '*', where each denotes the scale of the graph.
- A commission fee of fixed rate will be applied to each transaction based on the stocks bought or sold.
- Retains the older inflexible portfolio as a tool to retrieve composition or value on certain dates.

## Features V1 (Part 1)
Below are the list of features that are part of the previous iteration (also to note that these features are up and running):
- Allows a user to create one or more portfolios with shares of one or more stocks
- Once created, shares cannot be added or removed from the portfolio
- Examine the composition of a portfolio based on the portfolio name. Will throw error if invalid portfolio names (that does not exist) are passed 
- Determine the total value of a portfolio on a certain date.
- Persist a portfolio so that it can be loaded and saved for retrieving composition and total worth.


#### Note 1: XML is the preferred choice of file formatting, which is used in creating the portfolios. However, users can also upload existing XML files that contains the portfolios. The code then parses the file and produces output based on the selected choice (Composition/Total for a particular date).

#### Note 2: API support (from Alpha vantage) used for retrieving the list of stock prices of a specific company on a range of dates. Also has the support to extend local CSV files (CSV files containing the stock prices and dates for each company for a total of 20 companies).

Further information regarding design and setup is available on the DESIGN_README.md and SETUP-README.md


## Team members

- @rahulchandak
- [@kiranshatiya](https://github.com/kiran-shatiya-5)

## Acknowledgements

 - [Alphavantage API docs](https://www.alphavantage.co/documentation/#)
 - [Pretty printing XML](https://www.baeldung.com/java-pretty-print-xml)
 - [JFree chart for GUI Line chart](https://www.jfree.org)


## Support

For support or further queries, email or message us on Teams.

