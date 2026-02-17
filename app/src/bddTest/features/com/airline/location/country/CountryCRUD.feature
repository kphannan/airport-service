Feature: Country CRUD operations

  Background:
    * url baseUrl + '/location'

  # * configure report = { showLog: true, showAllSteps: false, logPrettyRequest: true, logPrettyResponse: true }
  @PostDeployment
  Scenario: Retrieve a default paged list of Countries
    Given path '/country'
     When method GET
     Then status 200
      And match $.pageable contains { "offset": 0, "pageNumber": 0, "pageSize": 20 }
      And match $ contains { "totalElements": 249, "size": 20, "number": 0 }

  # And match $.content contains {"iataAirportCode":"MSP"}
  # ----- (GET) Lookup existing airport -----
  @PostDeployment
  Scenario Outline: Spot check that Country with code "<id>" exits with name "<name>"
    Given path '/country/<id>'
     When method GET
     Then status 200
      And match $ contains { "id": <id>, "name": "<name>" }

    Examples:
      | id     | name       |
      | 302566 | Cape Verde |
      | 302568 | Algeria    |
# ----- (GET) lookup non-existent codes -----
# Scenario Outline: Ensure unknown IATA airport code "<id>" is not found
# Given path '/<id>'
# When method GET
# Then status 404
# Examples:
# | id  |
# | SOS |
# | ZZZ |
# ----- Add airport code (POST) -----
# ----- Put (Update) -----
# ----- Put (Create) -----
# ----- Delete -----
