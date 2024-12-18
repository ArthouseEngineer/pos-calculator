#!/bin/bash

PORT="9000"
if [ -z "$PID" ]; then
  PID=$(lsof -t -i:"$PORT")
fi

# If PID is found, kill the process
if [ -n "$PID" ]; then
  echo "Stopping Spring Boot application with PID: $PID"
  kill -2 "$PID" # Sends SIGINT for shutdown and
  echo "Application stopped."
else
  echo "No running application found on port $PORT or with name $APP_NAME."
fi
