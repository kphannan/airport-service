Feature: IATA Airport code CRUD operations

  Background:
    * url baseUrl + '/location/airport/iata'

  @PostDeployment
  Scenario: Retrieve a list of IATA airport codes
    Given path ''
    When method GET
    Then status 404
    * karate.abort()
    # Then status 200
    # And $ #array
    And match $.content contains {"iataAirportCode":"MSP"}

  # ----- (GET) Lookup existing airport -----
  @PostDeployment
  Scenario Outline: Spot check IATA airport "<id>" code exists
    Given path '/<id>'
    When method GET
    Then status 404
    * karate.abort()
    # Then status 200
    And match $.iataAirportCode == "<id>"

    Examples:
      | id  |
      | ATL |
      | MSP |

  # ----- (GET) lookup non-existent codes -----
  @PostDeployment
  Scenario Outline: Ensure unknown IATA airport code "<id>" is not found
    Given path '/<id>'
    When method GET
    Then status 404
    * karate.abort()

    # Then status 200
    Examples:
      | id  |
      | SOS |
      | ZZZ |

  # ----- Add airport code (POST) -----
  @PostDeployment
  Scenario Outline: Add a new IATA airport code "<id>"
    Given path ''
    And request {iataAirportCode: "<id>" }
    When method POST
    Then status 404
    * karate.abort()

    # Then status 201
    # # When method GET
    # # Then status 200
    # # And match $.iata_code == <id>
    # # And match $ == {"iataAirportCode":"<id>"}
    # And match $.iataAirportCode == "<id>"
    Examples:
      | id  |
      | YYZ |
      | LAX |

  # ----- Put (Update) -----
  # Scenario: Put (Update) an IATA code
  # Given path '/iata/ATL'
  # # When method GET
  # # Then status 200
  # And request {iataAirportCode: "ATL" }
  # When method PUT
  # Then status 200
  # ----- Put (Create) -----
  # Scenario: Put an IATA code that does not yet exist
  # Given path '/iata/SOS'
  # When method GET
  # Then status 404
  # And request {iataAirportCode: "SOS" }
  # When method PUT
  # Then status 201
  # Given path '/iata/SOS'
  # When method DELETE
  # Then status 204
  # ----- Delete -----
  @PostDeployment
  Scenario: Add, then delete an IATA airport code "SOS"
    Given path ''
    And request {iataAirportCode: "SOS" }
    When method POST
    Then status 404
    * karate.abort()
    # Then status 201
    # now delete it - this is the real test
    Given path '/SOS'
    When method DELETE
    Then status 204
