Feature: Continent Read Operations

  Background:
    * url baseUrl + '/location/continent'

  @Functional
  Scenario: Create, Read, Update and Delete a test-only continent
             # Create a test continent
       Given request
              """
              { code: "BB", name: "Bogus continent name", "wikiLink" : "https://en.wikipedia.org/wiki/Antarctica" }
              """
        When method POST
        Then status 201
           * def id = $.id

             # Can it be read back
       Given path id
        When method GET
        Then status 200

             # Update the name
       Given request
            """
            {
              "id" : #(id),
              "code" : "BB",
              "name" : "Bogus continent name changed"
            }
            """
        When method PUT
        Then status 200
         And match $.name == "Bogus continent name changed"

             # Add wikilink
       Given request
            """
            {
            "id" : #(id),
            "wikiLink" : "https://en.wikipedia.org/wiki/Antarctica",
            "code" : "BB",
            "name" : "name to foo",
            "keywords" : "Key1, Key2, Key3, Key4"
            }
            """
#        And print request
       When method PUT
          * print response
       Then status 200
        And match $.wikiLink == "https://en.wikipedia.org/wiki/Antarctica"

             # Read back the updated name
       Given path id
        When method GET
           * print response
        Then status 200
         And match $.name == "name to foo"
         And match $.wikiLink == "https://en.wikipedia.org/wiki/Antarctica"

             # Delete the test only continent
       Given path id
        When method DELETE
        Then status 204

             # Verify it is gone
       Given path id
        When method GET
        Then status 410
