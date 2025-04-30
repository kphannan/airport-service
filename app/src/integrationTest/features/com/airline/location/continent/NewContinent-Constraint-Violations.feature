Feature: NewContinent constraint validation
  - code: 2 uppercase letters
  - name: a string containing between 2 and 52 characters

  Background:
    * url baseUrl + '/location/continent'

  @Validation
  Scenario: Code and Name are null
       Given request
             """ { keywords: "will report missing code, name fields" } """
        When method POST
           * print response
        Then status 400
         And match $.code contains( "A 2-character code is required; provided: [null]" )
         And match $.name contains( "Name is required; provided: [null]" )

  @Validation
  Scenario: Code is empty string
    Given request
              """  { code: "", name: "Some Name" } """
    When method POST
       * print response
    Then status 400
     And match $.code contains( "A 2-character code is required; provided: []" )
     And match $.code contains( "Code must be 2 uppercase characters; provided: []" )


  @Validation
  Scenario: Code is blank
    Given request
              """ { code: "  ", name: "Some Name" } """
    When method POST
       * print response
    Then status 400
     And match $.code contains( "A 2-character code is required; provided: [  ]" )
     And match $.code contains( "Code must be 2 uppercase characters; provided: [  ]" )


  @Validation
  Scenario: Code is numeric
    Given request
              """ { code: "42", name: "Some Name" } """
    When method POST
       * print response
    Then status 400
     And match $.code contains( "Code must be 2 uppercase characters; provided: [42]" )

  @Validation
  Scenario: Code is null
    Given request
              """ { name: "Some Name" } """
    When method POST
       * print response
    Then status 400
     And match $.code contains( "A 2-character code is required; provided: [null]" )

  @Validation
  Scenario: Name is blank
    Given request
              """ { code: "ZZ", name: "" } """
    When method POST
       * print response
    Then status 400
     And match $.name contains( "Name must be between 2 and 52 characters; provided: []" )
     And match $.name contains( "Name is required; provided: []" )

  @Validation
  Scenario: Name is a single character
    Given request
              """ { code: "ZZ", name: "A" } """
    When method POST
       * print response
    Then status 400
     And match $.name contains( "Name must be between 2 and 52 characters; provided: [A]" )

  @Validation
  Scenario: Name is blank
    Given request
              """
              {
                code: "ZZ",
                name: "abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ"
              }
              """
    When method POST
       * print response
    Then status 400
     And match $.name contains( "Name must be between 2 and 52 characters; provided: [abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ]" )

