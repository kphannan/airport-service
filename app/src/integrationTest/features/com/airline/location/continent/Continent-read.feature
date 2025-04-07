Feature: Continent Read Operations

  Background:
    * url baseUrl + '/location/continent'

  @PostDeployment
  Scenario: Retrieve full list of Continents
        Given: path ''
        When: method GET
        Then: status 200
