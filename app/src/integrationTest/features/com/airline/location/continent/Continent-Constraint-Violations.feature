Feature: Continent constraint validation
  - code: 2 uppercase letters
  - name: a string containing between 2 and 52 characters

  Background:
    * url baseUrl + '/location/continent'

  @Validation
  Scenario: ID, Code and Name are null
       Given request
             """ { keywords: "will report missing code, name fields" } """
        When method PUT
           * print response
        Then status 400
         And match $.id contains( "must not be null; provided: [null]" )
         And match $.code contains( "must not be null; provided: [null]" )
         And match $.name contains( "must not be null; provided: [null]" )



  @Validation
  Scenario: Code is empty string
    Given request
              """  { id: 1, code: "", name: "Some Name" } """
    When method PUT
       * print response
    Then status 400
     And match $.code contains( "Code must be 2 uppercase characters; provided: []" )

  @Validation
  Scenario: Code is blank
    Given request
              """ { id: 1, code: "  ", name: "Some Name" } """
    When method PUT
       * print response
    Then status 400
     And match $.code contains( "Code must be 2 uppercase characters; provided: [  ]" )

  @Validation
  Scenario: Code is numeric
    Given request
              """ { id: 1, code: "42", name: "Some Name" } """
    When method PUT
       * print response
    Then status 400
     And match $.code contains( "Code must be 2 uppercase characters; provided: [42]" )

  @Validation
  Scenario: Code is null
    Given request
              """ { id: 1, name: "Some Name" } """
    When method PUT
       * print response
    Then status 400
     And match $.code contains( "must not be null; provided: [null]" )

  @Validation
  Scenario: Name is blank
    Given request
              """ { id: 1, code: "ZZ", name: "" } """
    When method PUT
       * print response
    Then status 400
     And match $.name contains( "Name must be between 2 and 52 characters; provided: []" )

  @Validation
  Scenario: Name is a single character
    Given request
              """ { id: 1, code: "ZZ", name: "A" } """
    When method PUT
       * print response
    Then status 400
     And match $.name contains( "Name must be between 2 and 52 characters; provided: [A]" )

  @Validation
  Scenario: Name is too long
    Given request
              """
              {
                id: 1,
                code: "ZZ",
                name: "abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ"
              }
              """
    When method PUT
       * print response
    Then status 400
     And match $.name contains( "Name must be between 2 and 52 characters; provided: [abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ]" )

  @Validation
  Scenario: Malformed Wiki URL
    Given request
              """
              {
                id: 1,
                code: "NA",
                name: "North America",
                wikiLink: "https://en.wikipedia.org/wiki/North _ America"
              }
              """
    When method PUT
       * print response
    Then status 400
     And match $ contains { "Possibility 1": "Malformed request body" }
     And match $ contains { "Possibility 2": "Invalid request parameters" }
     And match $ contains { "Possibility 3": "Incompatible data format" }
     And match $ contains { "Possibility 4": "Serialization errors" }
     And match $.detail contains( "JSON parse error" )
     And match $.detail contains( "type `java.net.URI` from String" )
#    And match $.name == "Field: 'name', Name must be between 2 and 52 characters; provided: [abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ]"


