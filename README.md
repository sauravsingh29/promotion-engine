# [PROMOTION-ENGINE]()

## Description
Promotion engine run against the selected cart to be processed and analyse against then configured
promotion promotional rule; apply on then and then calculate the final amount to be paid by user for 
his cart.

## Setup
#### Pre-requisite
1. Java8
2. Maven
3. Git Client

#### Run locally
1. Checkout/Clone project
    ```shell script
    git clone https://github.com/sauravsingh29/promotion-engine.git
    ```
2. Import project to STS or IntelliJ as maven project.
   >OR
3. Goto project root folder 
    ```shell script
    > cd promotion-engine
    # To run the application from command line
    > mvn spring-boot:run
    # To run test
    > mvn test
    ```
4. Once you have the application up and running you can test the application via [Swagger UI](http://localhost:8080/swagger-ui.html) or 
can adjust the

## Api Descriptions

 Api Path | Http Method | Section | Description
:---: | :---: | :---: | :---:
/cart | POST | cart-processing-api | Process user’s cart; apply promotion if any applicable and return total amount.
/products | GET | product-api | Get all product SKUs with prices.
/products | POST | product-api | Add product (SKU) with price.
/promotion | GET | promotion-api | Get all configured promotions.
/promotion | POST | promotion-api | Add new promotion.

> Note: While adding new promotion discountType attribute can be AMOUNT or PERCENTAGE. Can be configured many types in later use cases.

## Features
1. Upon application startup example from pdf product will be added.
2. Upon application startup example from promotions will be added.

## Limitations
* All products and promotions will be stored in-memory; no persistence technique has been used.