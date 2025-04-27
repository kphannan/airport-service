Feature: Continent Read Operations

  Background:
    * url baseUrl + '/location/continent'

  @Functional
  Scenario: Create, Read, Update and Delete a test-only continent
             # Create a test continent
         And request
              """
              {
                code: "BB",
                name: "Bogus continent name" }
              }
              """
        When method POST
        Then status 201
             # Get the ID of the created entity
             * def id = $.id
             # Can it be read back
    Given path id
    When method GET
    Then status 200

             # Update the name
    And request
            """
            {
            "id" : #(id),
            "code" : "BB",
            "name" : "Bogus continent name changed"
            }
            """
    When method PUT
    Then status 200
             # Read back the updated name
    Given path id
    When method GET
    Then status 200

             # Delete the test only continent
    Given path id
    When method DELETE
    Then status 410

             # Verify it is gone
    Given path id
    When method GET
    Then status 410
