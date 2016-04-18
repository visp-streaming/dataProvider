# How to use the StreamingDataProvider

- implement your CustomGeneratorJob
- configure the properties in the application properties
- configure optional cleanup activities in the ApplicationStartup
- run the data provider with
```sh
$ mvn spring-boot:run
```