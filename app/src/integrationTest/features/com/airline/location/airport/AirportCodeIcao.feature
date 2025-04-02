
Feature: ICAO Airport code CRUD operations

    Background:
        * url baseUrl + '/airport/icao'
    # * configure report = { showLog: true, showAllSteps: false, logPrettyRequest: true, logPrettyResponse: true }


    @PostDeployment
    Scenario: Retrieve a list of ICAO airport codes
        Given path ''
        When method GET
        Then status 404
        * karate.abort()
        #  Then status 200
        And match $.content contains { icaoAirportCode: 'KORD' }


    # ----- (GET) Lookup existing airport -----
    @PostDeployment
    Scenario Outline: Spot check ICAO airport code "<id>"
        Given path '/<id>'
        When method GET
        Then status 404
        * karate.abort()
        #  Then status 200
        And match $.icaoAirportCode == "<id>"

        Examples:
            | id   |
            | KATL |
            | KMSP |

    # ----- (GET) lookup non-existent codes -----
    @PostDeployment
    Scenario Outline: Ensure unknown ICAO airport code "<id>" is not found
        Given path '/<id>'
        When method GET
        Then status 404

        Examples:
            | id   |
            | KSOS |
            | KZZZ |
    # | KYYZ |
    # | KLAX |



    # ----- Add airport code (POST) -----
    Scenario Outline: Add a new ICAO airport code "<id>"
        Given path ''
        And request {icaoAirportCode: "<id>" }
        When method POST
        Then status 404
        * karate.abort()
        #  Then status 201
        And match $.icaoAirportCode == "<id>"

        Examples:
            | id   |
            | KYYZ |
            | KLAX |



# ----- Put (Update) -----

# ----- Put (Create) -----


# ----- Delete -----
# Scenario: Add, then delete an ICAO airport code
#     Given path ''
#       And request {icaoAirportCode: "KSOS" }
#      When method POST
#      Then status 201
#         # now delete it - this is the real test
#     Given path '/KSOS'
#      When method DELETE
#      Then status 204
