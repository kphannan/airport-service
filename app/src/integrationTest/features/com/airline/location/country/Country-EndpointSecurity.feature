Feature: Country API Security

  Background:
    * url baseUrl

  # ----- Verify acceptable methods -----
  @SmokeTest @SecurityCheck
  Scenario Outline: Method "<method>" on endpoint <target> should return "<response>"
    Given path <target>
    When method <method>
    Then status <response>

    Examples:
      | target             | method | response |
      | '/country'         | GET    | 200      |
      | '/country'         | PUT    | 405      |
      | '/country'         | POST   | 405      |
      | '/country'         | DELETE | 405      |
      | '/country/302757'  | GET    | 200      |
      | '/country/302757'  | PUT    | 405      |
      | '/country/302757'  | POST   | 405      |
      | '/country/302757'  | DELETE | 405      |
      | '/country/code/NA' | GET    | 200      |
      | '/country/code/NA' | PUT    | 405      |
      | '/country/code/NA' | POST   | 405      |
      | '/country/code/NA' | DELETE | 405      |
# ----- (GET)  -----
# ----- (GET)  -----
# ----- (POST)  -----
# ----- (PUT) (Update) -----
# ----- (PUT) (Create) -----
# ----- (DELETE) -----
