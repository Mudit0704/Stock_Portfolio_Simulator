====================================================================================================
                         ASSIGNMENT 6: DESIGN CHANGES FOR EXISTING DESIGN:
====================================================================================================

Changes over existing design in this iteration:
----------------------------------------------------------
Model:
--------
1. For model we added a new DateNavigator singleton class in the AbstractPortfoliosModel class
without making any significant changes over the existing FlexiblePortfoliosModel class.
2. Additional methods were added for AbstractPortfolio class to make the StrategicPortfolio class
compatible with the AbstractPortfolio.
3. The rest of the model interfaces remain intact and unchanged.

These are the only changes on the model side.
NO EXISTING INTERFACES HAVE CHANGED.

----------------------------------------------------------
View:
--------

To ensure we are reusing the methods defined in the IView interface for a GUI view, we have performed
interface segregation such that all the common methods between TextBased and GUI view are moved
to a new interface ICommonView. We extend this new interface in IView ensuring a passive change thereby
not impacting previous functionality and neither causing any change to previous code.

----------------------------------------------------------
Controller:
--------

NO CHANGES TO CONTROLLER's EXISTING DESIGN.

----------------------------------------------------------
New Features Implementation:
----------------------------------------------------------

Model:
--------
In order to support the new features for strategic portfolio, we simply extended the IFlexiblePortfolioModel
with IStrategicFlexiblePortfolioModel interface. This model interface was implemented by
StrategicFlexiblePortfolioModel and is a class adapter over FlexiblePortfoliosModel.

Likewise, the inner StrategicPortfolio class as well. This is extending the IFlexiblePortfolio interface
and is a class adapter over FlexiblePortfolio.

For dollar cost averaging, we have implemented builder strategy to implement a normal fractional
based strategy using which a user can specify any future investment or invest using dollar cost
averaging, which can be applied at once on an existing portfolio or can be applied on a portfolio as
an existing strategy.

We implemented a singleton DateNavigator class which uses the existing stock API to query the available
trading dates using the oldest trading stock company available through AlphaVantage API.

All the investments which are part of DCA or normal investment in future dates(ongoing) are being
persisted in the corresponding strategy xml file which is retrieved everytime all the portfolios are
retrieved. While doing this, we ensure that all the transactions which are scheduled to be executed
as part of the ongoing DCA are executed as soon as the portfolios are retrieved.

We have performed code reuse in the model in this iteration by using class adapters over flexible
portfolios and using the previously created Abstract classes to our benefit as internal code refactoring.

----------------------------------------------------------
View:
--------

The ICommonView interface is further extended by IGUIView interface which contains methods defined
to implement the new GUI view.

IGUIView is then implemented by GUIView concrete class containing all the logic for our GUI view and to
ensure we have proper modularity of code and not one big single view class, we have implemented the
Command Design Pattern for all the different functionalities/commands offered in the UI. So for each
button, the listener calls the respective command class logic to implement its functionality.

We have implemented the pattern by the use of a new interface ICommandHandler that contains the basic
method that is implemented by AbstractCommandHandlers class. To reuse common code for all the
commands we have made use of AbstractCommandHandlers class which is then extended by all the
individual commands.

To ensure that the application doesn't
hang while performing any task, we have implemented multi-threading by extending the inbuilt
SwingWorker class for the private classes in each command.

----------------------------------------------------------
Controller:
--------

To ensure a proper decoupled controller and view, we have created a new interface IFeatures, that
defines all the methods provided by controller to provide a proper way of communication between
the model and view without exposing either of them to each other.

We have created a new controller GUIPortfolioController class which is passed the
StrategicFlexiblePortfoliosModel, GUIView and FlexibleView objects. The controller is implemented
in such a way that it can switch between GUI view (GUIView) and the text based view (FlexibleView)
based on user input. The controller implements the IFeatures interface.

====================================================================================================
                                           DESIGN CHANGES FOR EXISTING DESIGN:
====================================================================================================

Changes over existing design in this iteration:
----------------------------------------------------------
1. For controller, we created a helper class to store few common methods of both the new Flexible
   and inflexible controllers and the new controller classes has-a instance of this helper class
   object.(Code reuse through composition).

Enhancement:
---------------------
We can now support stock values more than 100 days(upto year 2014) for earlier inflexible portfolio.


NO EXISTING INTERFACES HAVE CHANGED.

This is the only place where we changed our existing design in order to reuse the existing code and
avoid duplication.

In order to provide new feature support, we did not make any design changes in the model and the
view, we just extended the model interfaces and offered new methods in the new interface by
following the open-close principle. Hence, this is backward compatible as well.



====================================================================================================
                                           UPDATED DESIGN:
====================================================================================================
For adding new features, we followed the open-close principle and Liskov's substitution principle
from SOLID principles.
The new features added do not change the existing design except for 1 change as mentioned above.
And the new classes have been ensured to follow Single Responsibility principle as well.

-------------------------
High Level:
-------------------------
This application still follows the Model-View-Controller style.
A user may choose to create a new Flexible portfolio or continue to use the existing Inflexible
portfolio.


If the user chooses Inflexible portfolio, the flow and design is unchanged as we still continue to
support it.
For new FlexiblePortfolio, we have extended the IPortfoliosModel interface with
IFlexiblePortfoliosModel interface. The new interface specifies new methods for supporting the new
features in addition to inheriting all the IPortfoliosModel methods. Likewise, the inner layer also
does the same. It extends IPortfolio by IFlexiblePortfolio, and it also offers the new methods for
new features. In addition, to avoid repeating code for future classes, we abstracted the Flexible
Model into AbstractPortfoliosModel abstract class and made specific methods
such as creating new portfolio instance, etc. have been declared as abstract methods.

The inner Stock layer does not need any changes, even if we need to change the service, we just need
to implement a new service as per the IStockService specification and pass the new service to the
Stock instances. Furthermore, we created a new Factory class that creates any type of stock service
instance to be used by the model and the inner.

Thus, our stock layer continues to be unchanged with respect to different features and services.

Since performance visualizer on its own has a functionality, we have created a separate class
that is responsible to generate performance graphs based on any data input by the FlexiblePortfolio.

For controller, we created another interface IFlexibleController which extends the original
IPortfolioController. The FlexiblePortfolioController implements IFlexiblePortfolioController and
extends the PortfolioController. This way the FlexiblePortfolioController reuses the methods of
PortfolioController wherever necessary.

In addition to this, there are some methods that are common to both controllers and these are not
specific to the model type. Hence, these methods are put into the ControllerHelper class whose
instance both these controllers use to call the same functionality.

For the view side, we created FlexibleView which implements the IView interface.

The new Controller is passed an instance of this new view and the new model.


-------------------------
Low Level:
-------------------------
Internally the new flexible portfolio maintains a mapping of all the stocks a portfolio can have
and the quantities of those stocks at any point in time(Date). This map is used to compute the
portfolio value, determine the portfolio composition at any given date.

This map is also used to validate the purchase/sell requests made by the user. For example,
If the user asks for any transaction of a specified stock on a date which falls between any 2
transaction dates of the same stock, the request is invalidated, and we maintain a consistent state
of the portfolio.


The transaction fee is input by the user through the controller and the model uses this transaction
fee at the time of portfolio creation or the transacting some stocks.

The default transaction fee if not input by the user is 0.

The cost basis is also maintained in a similar fashion. Each stock has a mapping of the dates where
cost basis has changed. Cost basis is determined based on the stocks purchased and the transaction
fee set by the user at the time of portfolio creation. The cost basis changes every time there is a
transaction(add/sell).


Information about the portfolio, such as
- Historic quantity of all the stocks, updated when there is a transaction
- Creation date of the portfolio
- Cost-basis history of the portfolio
- Stocks tickers
- Stock values at present
- Current holding

are persisted into the XML file in thew 'flexiblePortfolio' directory.

A user can also determine the portfolio performance based on the mapping of stocks and their
quantities on different dates.

In order to follow the Single responsibility principle, we seperated the performance visualizer from
the new FlexiblePortfolio into a different class.

Based on different input timespan, we see different portfolio performance graphs. Mainly we can see
the graphs in 3 different scales, namely either days, months or years.

When the controller asks the Model for performance over a period of time, the FlexiblePortfolio,
which has an instance of the performance visualizer, invokes methods of the visualizer to
get the performance data.

The user can continue to input portfolio information through the previous XML format.
The user will have to generate a new XML file in a directory - 'flexiblePortfolio'.

The user will have to ensure that the sequence of transaction is in correct order in order to
load the portfolio correctly.

We continue to use the caching module and the validation set of ticker symbols as in the previous
iteration to avoid making excessive API calls.

====================================================================================================
                                           EXISTING DESIGN(Unchanged):
====================================================================================================
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