


Feature: Region CRUD operations

    Background:
        * url baseUrl
    # * configure report = { showLog: true, showAllSteps: false, logPrettyRequest: true, logPrettyResponse: true }


    @PostDeployment
    Scenario: Retrieve a default paged list of Regions
        Given path '/region'
        When method GET
        Then status 200
        And match $.pageable contains { "offset": 0, "pageNumber": 0, "pageSize": 20 }
        And match $ contains { "totalElements": 3941, "size": 20, "number": 0 }

    # And match $.content contains {"iataAirportCode":"MSP"}

    # ----- (GET) Lookup known region -----
    @PostDeployment
    Scenario Outline: Spot check that Region with code "<id>" exits with name "<name>"
        Given path '/region/<id>'
        When method GET
        Then status 200
        And match $ contains { "id": <id>, "name": "<name>" }

        Examples:
            | id     | name           |
            | 306086 | Georgia        |
            | 306067 | Midway Islands |

# ----- (GET) lookup non-existent region codes -----
# Scenario Outline: Ensure unknown Region code "<id>" is not found
#     Given path '/<id>'
#      When method GET
#      Then status 404

#     Examples:
#     | id  |
#     | SOS |
#     | ZZZ |


# ----- Add airport code (POST) -----

# ----- Put (Update) -----

# ----- Put (Create) -----

# ----- Delete -----
