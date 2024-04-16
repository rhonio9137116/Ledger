# Ledger Application - Author:Neo Xu
This is a Maven Project built using latest stable release version of Java 22, SpringBoot 3.2.4 and make use of the Kafka as the message broker and H2 in-memory Database.

Access to H2 Database Console while application running
```
http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:testdb
User Name: sa
Password: <leave it empty>
```

Access to Swagger URL. 
Note:This is auto genreated by Swagger, as is for demo project I don't have enough time to properly document every restful API here

http://localhost:8080/swagger-ui/index.html

### Reference Documentation

You have to build a microservice called “Ledger” using the latest java and spring boot version.
The Requirement of the microservice is asfollow:
1. Ledger is the source of truth for balances of assets and liabilities of an entity. So, the
   main responsibility of the ledger not only is to provide the latest but also all historical
   balances of any assets of an entity.
2. The ledger should support multi asset accounts owned by any entity. Examples of assets
   are fiat currency, crypto, stock, bond etc. Each asset is represented as a wallet. One
   entity can have multiple accounts. One account can have multiple wallets of different
   assets.
3. The client of the ledger should be able to move assets from one wallet to another.
4. The client of the ledger should be able to make multiple movements of assets in a single
   request.
5. All the requests to the ledger have to be executed as “all or nothing”.
6. The ledger should be able to manage the lifecycle of an account. For example OPEN
   state of an account means postings can happen to/from any wallets of the account.
   CLOSED means postings cannot happen to/from any wallets of the account. There could
   be more lifecycle state of an account. You have to design and document them for the
   client of the ledger.
7. The client of the ledger should be able to change the state of the account from one to
   another.
8. Similar to the account lifecycle there could be multiple life cycles of postings(movement).
   Such as PENDING, CLEARED, FAILED. You have to design and document them for the
   client of the ledger.
9. The client of the ledger should be able to change to postings it has done before.
10. The ledger should broadcast any balance change of any wallet for its client to listen to.
11. The ledger should broadcast any movement happening in the ledger for its client to
    listen to.
12. The client of the ledger should be able to make movement requests in asynchronous
    mode.
13. The client may request any historical balance of a wallet. The ledger should be able to
    provide that. Such as the client might request for the balance of a wallet at a given
    timestamp. The ledger should be able to entertain the client with the response.
14. Communication between the client can be established using both synchronous and
    asynchronous modes. Even though we prefer asynchronous communication where
    possible.
15. Ledger database will be very write heavy so design the database structure and usage
    keeping that in mind. Hints: “CQRS”.
16. The ledger should have a manual for its clients so that the client can use it efficiently.

If in some cases you feel requirements are not specific enough, you can make reasonable
    assumptions and document them. We will check your ability in implementing a scalable solution
    breaking down high level requirements into smaller technical parts and implementing them in
    code.
### Project Structure
The following guides illustrate each Module of the Project. When Maven build, the Module [model] should build first, as it is dependency of Module [application]. 
* application - this is the Ledger main application
* kafka-broker - this is the local Kafka broker
* model - this is the model classes of Restful API and Kafka Avro Message, The Avro Model classes are generated from schema file. We extract this as Module so as the client of Ledger can import the jar file to make use of the same model classes


### Run Local
The following guides illustrate how to run the application in local
* Run the local Kafka Broker: com/example/ledger/KafkaBroker.java
* Run the Ledger application: com/example/ledger/Ledger.java

### Database Tables
![Entity Relation Diagram](ER-Diagram.png "Title")

### Preload Data
The application preload some data from src/main/resources/data.sql

UserId 1 has two accounts, named "FIRST ACCOUNT" and "SECOND ACCOUNT", The "SECOND ACCOUNT" is empty account.

"FIRST ACCOUNT" contains 3 Wallets, named "FIRST WALLET", "SECOND WALLET" and "THIRD WALLET".

"FIRST WALLET" contains 3 Currency Assets, named "Euro", "UK Pound" and "HK Dollar".

"SECOND WALLET" contains mixed Assets of Stock, Crypto and Bond, named "HSBC", "LLOY", "BP", "Ethereum", "Cardano", "U.S. 2Y" and "U.K. 10Y".

"THIRD WALLET" is empty wallet.

### Restful API
The following guides illustrate of Restful API. 
* assetlist API for listing the wallets and assets of the user. This is synchronous call. Below Example shows the response of preload Data.
  
```
localhost:8080/asset/list/v1
Request
{
    "requestId": "requestId",
    "userId": 1
}
Response
{
    "responseId": "requestId",
    "userId": 1,
    "walletViews": [
        {
            "walletId": 1,
            "walletName": "FIRST WALLET",
            "assetViews": [
                {
                    "assetId": 1,
                    "assetName": "Euro",
                    "units": 1.1
                },
                {
                    "assetId": 2,
                    "assetName": "UK Pound",
                    "units": 1.2
                },
                {
                    "assetId": 3,
                    "assetName": "HK Dollar",
                    "units": 1.3
                }
            ]
        },
        {
            "walletId": 2,
            "walletName": "SECOND WALLET",
            "assetViews": [
                {
                    "assetId": 7,
                    "assetName": "HSBC",
                    "units": 1.4
                },
                {
                    "assetId": 8,
                    "assetName": "LLOY",
                    "units": 1.5
                },
                {
                    "assetId": 9,
                    "assetName": "BP",
                    "units": 1.6
                },
                {
                    "assetId": 12,
                    "assetName": "Ethereum",
                    "units": 1.7
                },
                {
                    "assetId": 13,
                    "assetName": "Cardano",
                    "units": 1.8
                },
                {
                    "assetId": 18,
                    "assetName": "U.S. 2Y",
                    "units": 1.9
                },
                {
                    "assetId": 19,
                    "assetName": "U.K. 10Y",
                    "units": 2.0
                }
            ]
        },
        {
            "walletId": 3,
            "walletName": "THIRD WALLET",
            "assetViews": []
        }
    ]
}
```

* Account Lifecycle API for changing the status of the account of user. This is synchronous call, allow user to open or close account.

```
localhost:8080/account/lifecycle/v1
Request
{
    "requestId": "requestId",
    "accountId": "1",
    "userId": 1,
    "status": "CLOSED"
}
Response
{
    "responseId": "requestId",
    "accountId": 1,
    "userId": 1,
    "status": "CLOSED"
}
```
* assetBalanceHistory API for retrieving asset balance history. This is synchronous call, retrieve the data from AssetBalanceHistory Database table

Any Kafka Event "assetPriceEventProvided" arrived to Kafka Topic "assetPriceEventProvidedTopic" will cause the application to record data to the AssetBalanceHistory Database table

Please see section of KafkaTopic "walletBalanceEventProvidedTopic" for Event "WalletBalanceEventProvided".

```
localhost:8080/asset/balance/history/v1
Request
{
    "requestId": "requestId",
    "userId": 1,
	"startDate": "2024-04-01",
	"endDate": "2024-12-30"
}
Response
{
  "responseId": "requestId",
  "userId": 1,
  "walletViews": [
    {
      "walletId": 1,
      "walletName": "FIRST WALLET",
      "assetViews": [
        {
          "assetId": 2,
          "assetName": "UK Pound",
          "balanceHistoryViews": [
            {
              "units": 1.2,
              "unitPrice": 0.6724707,
              "assetBalance": 0.8069648742675781,
              "timestamp": "2024-04-15 21:12:47.402"
            },
            {
              "units": 1.2,
              "unitPrice": 1.1526166,
              "assetBalance": 1.383139967918396,
              "timestamp": "2024-04-15 21:12:30.395"
            },
            {
              "units": 1.2,
              "unitPrice": 1.2089682,
              "assetBalance": 1.4507617950439453,
              "timestamp": "2024-04-15 21:11:46.387"
            }
          ]
        },
        {
          "assetId": 3,
          "assetName": "HK Dollar",
          "balanceHistoryViews": [
            {
              "units": 1.3,
              "unitPrice": 7.6432886,
              "assetBalance": 9.936274528503418,
              "timestamp": "2024-04-15 21:13:09.393"
            },
            {
              "units": 1.3,
              "unitPrice": 7.350477,
              "assetBalance": 9.555620193481445,
              "timestamp": "2024-04-15 21:12:18.386"
            }
          ]
        }
      ]
    },
    {
      "walletId": 2,
      "walletName": "SECOND WALLET",
      "assetViews": [
        {
          "assetId": 18,
          "assetName": "U.S. 2Y",
          "balanceHistoryViews": [
            {
              "units": 1.9,
              "unitPrice": 4.687517,
              "assetBalance": 8.906282424926758,
              "timestamp": "2024-04-15 21:13:07.387"
            }
          ]
        },
        {
          "assetId": 7,
          "assetName": "HSBC",
          "balanceHistoryViews": [
            {
              "units": 1.4,
              "unitPrice": 653.8259,
              "assetBalance": 915.3562622070312,
              "timestamp": "2024-04-15 21:12:20.393"
            }
          ]
        },
        {
          "assetId": 13,
          "assetName": "Cardano",
          "balanceHistoryViews": [
            {
              "units": 1.8,
              "unitPrice": 0.77381474,
              "assetBalance": 1.3928664922714233,
              "timestamp": "2024-04-15 21:12:37.401"
            },
            {
              "units": 1.8,
              "unitPrice": 1.0501424,
              "assetBalance": 1.8902562856674194,
              "timestamp": "2024-04-15 21:12:36.385"
            }
          ]
        }
      ]
    }
  ]
}

c.e.ledger.assetPriceTicker.Scheduler    : PriceTicker:: assetId:13 [Cardano] priceChanged from 0.4536 to 0.60620916 ::
AssetPriceEventProvidedProducer          : Produced AssetPriceEventProvided to Topic:assetPriceEventProvidedTopic -> {"assetId": 13, "assetName": "Cardano", "unitPrice": 0.60620916}
AssetPriceEventProvidedConsumer          : Consumed assetPriceEventProvided from Topic:assetPriceEventProvidedTopic-> {"assetId": 13, "assetName": "Cardano", "unitPrice": 0.60620916}
c.e.ledger.api.service.AssetServiceImpl  : Balance Changed for userId:1 wallet:SECOND WALLET, assets size:7, Total Balance:$7.213889062404633
WalletBalanceEventProvidedProducer       : Produced WalletBalanceEventProvided to Topic:walletBalanceEventProvidedTopic -> {"walletId": 2, "walletName": "SECOND WALLET", "userId": 1, "balance": 7.213889062404633}
c.e.ledger.api.service.AssetServiceImpl  : Record WalletAssetBalanceHistory, userId:1 walletName:SECOND WALLET assetName:Cardano units:1.8 balance:$1.0911765
```
* Assets Posting API - this support multiple Asset Movements from one wallet to another wallet return the Posting with PENDING status

This API inserts data into denormalized Database table Posting and PostingAssetMovement with status "PENDING" and synchronously returns the Posting with PENDING status in the Restful API response. Then it asynchronously publish Event "PostingEventRequest" to Kafka Topic "postingEventRequestTopic".

This application subscribes to Kafka Topic "postingEventRequestTopic". When Event "PostingEventRequest" arrives to the topic, it starts to try to move the asset from one wallet to another wallet of the same user.

1. If Account status is "CLOSED", it updates the Posting status to "FAILED" in the DB and publishes another Event "PostingEventProvided" to Kafka Topic "postingEventProvidedTopic" for broadcasting to any client subscribed.

2. If asset movements all Success it updates the Posting status to "CLEARED" in the DB and publishes another Event "PostingEventProvided" to Kafka Topic "postingEventProvidedTopic" for broadcasting to any client subscribed.

3. If any asset movement Failed, it rollbacks all intending asset movements that may have successful, updates the Posting status to "FAILED" in the DB and publishes another Event "PostingEventProvided" to Kafka Topic "postingEventProvidedTopic" for broadcasting to any client subscribed.

Please refer KafkaTopic "postingEventProvidedTopic". Clients can subscribe to this topic for Posting status update. 

The below sample request move asset HSBC Stock(walletAssetOwnershipId 4) and LLOY Stock(walletAssetOwnershipId 5) from "SECOND WALLET"(walletId 2) wallet to "THIRD WALLET"(walletId 3) wallet.
Once request sent, you can try asset list API "localhost:8080/asset/list/v1" to view and confirm the movement.


[Overall CQRS Design Concept]
The application allows users request a posting of moving the asset from one wallet to another. This is Write Command of publishing the Event to postingEventRequestTopic. The application subscribe to postingEventRequestTopic this is command handler accepts these commands and invokes methods of update Database Table Posting and PostingAssetMovement Table, synchronize ReadModel by update Database WalletAssetOwnership Table. At the end the application publishes Event to postingEventProvidedTopic for Posting status update for broadcasting to clients subscribe to it.  





```
localhost:8080/asset/posting/v1
Request
{
    "requestId": "requestId",
    "userId": 1,
    "assetMovements": [
        {
            "walletAssetOwnershipId": 4,
            "walletIdFrom": 2,
            "walletIdTo": 3
        }, 
        {
            "walletAssetOwnershipId": 5,
            "walletIdFrom": 2,
            "walletIdTo": 3
        }
    ]
}
Response
{
    "responseId": "requestId",
    "postingId": 1,
    "assetMovements": [
        {
            "walletAssetOwnershipId": 4,
            "walletIdFrom": 2,
            "walletIdTo": 3
        },
        {
            "walletAssetOwnershipId": 5,
            "walletIdFrom": 2,
            "walletIdTo": 3
        }
    ],
    "postingStatus": "PENDING"
}

PostingEventRequestProducer   : Produced PostingEventRequest to Topic:postingEventRequestTopic -> {"postingId": 1, "userId": 1}
PostingEventRequestConsumer   : Consumed PostingEventRequest from Topic:postingEventRequestTopic-> {"postingId": 1, "userId": 1}
PostingEventProvidedProducer  : Produced PostingEventProvided to Topic:postingEventProvidedTopic -> {"postingId": 1, "userId": 1, "status": "CLEARED"}
```
* Replay Assets Posting API. This API replays existing Posting, the application duplicates the existing posting in the DB and return the new (duplicated) postingId with PENDING status.

Similar to Assets Posting API, the processing of posting is done asynchronously. Please refer KafkaTopic "postingEventProvidedTopic".

Clients can subscribe to this topic for Posting status update.

```
localhost:8080/asset/posting/replay/v1
Request
{
    "requestId": "requestId",
    "userId": 1,
    "postingId": 1
}

Response
{
    "responseId": "requestId",
    "postingId": 3,
    "postingStatus": "PENDING"
}
```


### Kafka Topics clients can subscribe to
The following guides illustrate of Kafka Event Topic client can subscribe to
*  KafkaTopic "walletBalanceEventProvidedTopic". Subscribe to this topic for wallet balance update caused by live asset price changed.

The AssetPriceTickerScheduler periodically(every 10 seconds) publish Event "assetPriceEventProvided" to Kafka Topic "assetPriceEventProvidedTopic" for asset unit price update.

This application subscribes to Kafka Topic "assetPriceEventProvidedTopic", when event "assetPriceEventProvided" consumed from this Topic by the application, it calculates the wallet balance and publishes another Event "WalletBalanceEventProvided" to KafkaTopic "walletBalanceEventProvidedTopic" for broadcasting to any client subscribed, then inserts data into AssetBalanceHistory Database table. Client can later use assetBalanceHistory API to retrieve balance history.

```
KafkaTopic "walletBalanceEventProvidedTopic"
Event "WalletBalanceEventProvided"
{
  "walletId": 1,
  "walletName": "FIRST WALLET",
  "userId": 1,
  "balance": 2.9279747009277344
}
```

*  KafkaTopic "postingEventProvidedTopic". Subscribe to this topic for posting status updated after finish processing assets movement posting.

Please refer to Assets Posting Request restful API

This application subscribes to Kafka Topic "postingEventRequestTopic". When Event "PostingEventRequest" arrives to the topic, it starts to try to move the asset from one wallet to another wallet of the same user. At the end it publishes event to postingEventProvidedTopic for clients to subscribe.

```
KafkaTopic "postingEventProvidedTopic"
Event "PostingEventProvided"
{
  "postingId": 1,
  "userId": 1,
  "status": "CLEARED"
}
```

