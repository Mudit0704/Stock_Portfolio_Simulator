
# DESIGN-README.md - Stocks (Part 3) Team 83135

The Stocks application design has been implemented as below:

- MVC (Model View Controller) style, the design has been categorised into three parts
- The Controller will call the View and the Model and delegate the functionalities accordingly (functionalities include Create portfolio, Upload existing portfolio, Modify portfolio, Composition of a portfolio, Total price of a portfolio and Performance of a portfolio)
- The View displays the menus, options and appropriate prompts for the user on a text based UI and Graphical UI as requested by the controller
- The Model contains various methods which includes validating inputs, reading and writing to the XML file
- Main function will be used to call the controller and initiate the sequence of actions
- Tests for each of the modules (Model, View and Controller) has been written accordingly

## Navigating through the project

#### src directory:
- has 3 packages - controller, model and view
- each of the package contains the interfaces and implementation classes to create a Flexible and Inflexible portfolio, and perform operations on those (cost basis, total, composition, performance graph, dollar cost averaging)
- Previously the portfolio was maintained in the format "Ticker symbol - number of shares". This same format is retained for the Inflexible portfolio 
```bash 
<portfolioNames>
    <pfName name="College">
        <Stock StockName="AAPL">22</Stock>
        <Stock StockName="AMZN">24</Stock>
    </pfName>
    <pfName name="Savings">
        <Stock StockName="AMZN">22</Stock>
        <Stock StockName="AAPL">11</Stock>
    </pfName>
</portfolioNames>
 
```

- For the Flexible portfolio the format "CommissionFee - DateBought - Ticker symbol - Number of shares" is followed, where the 'Commission Fee' denoting the fee added for each transaction, 'Date bought' denoting the stock purchase date, the 'Ticker symbol' denoting the company for which the user is purchasing stocks from, and the 'Number of shares' denoting the number of shares. Sample portfolio is as follows:


```bash
<portfolioNames>
    <pfName name="College">
        <Stock CommissionFee="30.0" DateBought="2021-03-01" StockName="AAPL">20</Stock>
        <Stock CommissionFee="24.0" DateBought="2021-04-01" StockName="AMZN">22</Stock>
        <Stock CommissionFee="11.0" DateBought="2021-05-06" StockName="AAPL">10</Stock>
    </pfName>
    <pfName name="Health">
        <Stock CommissionFee="20.0" DateBought="2022-05-04" StockName="AAPL">21</Stock>
    </pfName>
</portfolioNames>
```

#### test directory:
- has 3 test files - one for the mock, controller and view respectively.

#### res directory:
- has the runnable JAR file, HTML documentation, UML Class diagrams, supporting files and the README files

## Design implementation and changes

#### Version 3 (Part 3):

- Controller - Has new interfaces (StockControllerGUI, Features) and its implementation class (StockControllerGUIImpl) for the Graphical UI 
- Model - Has a new interface for the Dollar cost averaging and a class that implements the interface methods
- View - Has a new interface (StockViewGUI) and implementation class (StockViewGUIImpl) for the Graphical UI

#### Version 2 (Part 2):

- Controller - New classes with helper methods, implementing the existing interface have been added to support features of Flexible portfolio and Inflexible portfolio (Cost basis, Value, Composition and Performance graph)
- Model - New model interface called 'Portfolio' is created which handles the portfolio operations like 'composition', 'total', 'cost basis' etc. of a portfolio. Similarly, an interface called 'Validator' is created which has a consolidated method list all the validation checks (dates, ticker symbols, number of shares, portfolio names etc.). Also, an interface called 'DataSources' is created which is responsible for fetching the data from API for the corresponding stocks
- View - Design remains the same with addition of methods to print Flexible portfolio menus and prompts

#### Version 1 (Part 1):

- Controller - Has an interface (StockController) and it's implementation class (StockControllerImpl)
- Model - Has an interface (StockModel) and it's implementation class (StockModelImpl)
- View - Has an interface (StockView) and it's implementation class (StockViewImpl)

## Team members

- @rahulchandak
- [@kiranshatiya](https://github.com/kiran-shatiya-5)

## Support

For support or further queries, email or message us on Teams.

