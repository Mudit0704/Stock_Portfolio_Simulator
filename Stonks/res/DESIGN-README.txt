This entire application is designed using Model-View-Controller style.
A user may choose to create a new portfolio or save/retrieve any existing built portfolios and can
determine portfolio composition/value at any point of time.

The user may choose for an operation from a set of inputs displayed through a text based console.
The controller who is displaying the view, takes in the input and calls appropriate model methods to
perform specified actions.

The model represents a list of portfolios created/retrieved from file. The list of portfolios is
a collection of portfolio object where each portfolio object is a collection of stock objects.

Every portfolio has a list of stocks which have a valid ticker symbol and store a mapping of their
historic closing prices upto last 100 days which is used to determine the value of any portfolio on
any given date.

A stock entity represents the ticker symbol in the portfolio and stores its historic closing prices.

The stock data for historic closing prices is maintained using a hash map and populated based on
the data retrieved from a service object.

This service object is making calls to AlphaVantage API endpoint to query last 100 days closing
prices by ticker symbols and a set of actively traded ticker symbols.

In order to avoid making excessive API calls, there is a caching module which is responsible to
keep a mapping of all the valid ticker symbols and the corresponding stock objects. Whenever a
user tries to input any stock information, this caching module looks up to check if there is any
existing stock object and if so it returns the same so that the same values are not repeatedly
fetched through the API. However, this does not fully ensure that the API calls will be saved since,
we make API calls only when there is a requirement to fetch data to get portfolio value.

To validate if a stock symbol input by the user is valid/invalid, we preload a set of all actively
listed stock ticker symbols and serialize this set. This also helps avoid fully relying on the API
all the time just to validate if a user input ticker symbol is valid or not.