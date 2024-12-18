### Candlesticks solution

##### How to run tests

Run test:
```./gradlew clean test```

##### Run app steps:

- first up docker container from compose file

```bash	
docker-compose up -d --wait
```

- second migrate DB if first time run, for DB initialization

```./gradlew flywayMigrate```

- finally run the app :)

```./gradlew bootRun```

- After stop working with app, don't forget down containers and stop the running application

```bash	
sh stop-app.sh
docker-compose down
```

### Solution explaination

* Project contains helper endpoint - `/most-frequent-isin` that can help us find 
ISIN with the highest number of quotes in the last specified interval. 

* You can use Integrated IDEA http-client for make request to endpoints - [isin-request-sender.http](candlesticks-client.http) 

## So the main task was

The system only needs to return the candlesticks for the last 30 minutes, including the most recent prices.
If there weren't any quotes received for more than a minute, instead of missing candlesticks in the 30 minute window, values from the previous candle are reused.
## Algorithm 

* The main algorithm implemented by the Query in method - `getCandlesticksByInterval`
[QuoteRepository.java](src/main/java/org/tr/candlesticksolution/repository/QuoteRepository.java)
* If there are any gaps in candlestick data within the requested time period, they are filled using the last known values from the previous minute. This ensures that each minute has complete candlestick information.
* If the very first candlestick's information is missing at the start of the time period, the query searches backward in time to find the most recent `candlestick` data. This search is guided by the `firstCandlestickBackFillUntil` parameter, which defaults to 2 hour ago

You can check work of this algorithm by the Integration test.

## Integration Testing

You can check how the algorithm works by the test in the [QuoteRepositoryIntegrationTest.java](src/test/java/org/tr/candlesticksolution/repository/integration/QuoteRepositoryIntegrationTest.java)

* The integration tests for the candlestick data retrieval logic ensure that the application correctly handles various scenarios involving missing data and time intervals.
One key test case is implemented in the `should_CandlestickHistory_IsStarted_FromDateOfFirstQuote_In_Query_range` method. This test verifies that the candlestick history starts from the date of the first available quote within the query range based.


* And the second key test check the `firstCandlestickBackFillUntil` works correctly, and find data for start of Candlesticks.










