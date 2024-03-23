# Crypto Exchange App

A basic application which is calling the https://api-pub.bitfinex.com/v2/tickers endpoint to display
crypto data.

This project adheres to Android design and development best practices, such as:
- Clean Architecture
- Unit tests
- Jetpack Compose with Kotlin Flows and Coroutines

It serves a dual purpose: acting as a practical reference for the kind of applications the developer
can create, while also providing a platform to explore and implement new technologies.

## Features

A screen for crypto currencies which display the following data:

- name
- symbol
- last trade price
- relative change to yesterday, percentage wise
- a search bar which filters from the existing crypto currencies

The data updates every 5 seconds while the screen is displayed to the user.

Network connectivity updates in the form of a snackbar.

## Teck Stack

- Kotlin
- Jetpack Compose
- MVI
- Coroutines
- Ktor
- HILT
- Navigation Compose (to be implemented to if additional screens are to be added)
- JUnit4 for unit testing

## Demo

![Crypto Exchange Demo](crytpo_exchange.gif)
