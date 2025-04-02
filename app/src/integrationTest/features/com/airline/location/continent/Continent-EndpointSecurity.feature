Feature: Continent API Security

  Background:
    * url baseUrl

  # ----- Verify acceptable methods -----
  @SmokeTest @SecurityCheck
  Scenario Outline: Method "<method>" on endpoint <target> should return "<response>"
    Given path <target>
    When method <method>
    Then status <response>

    Examples:
      | target               | method | response |
      | '/continent'         | GET    | 200      |
      | '/continent'         | PUT    | 405      |
      | '/continent'         | POST   | 405      |
      | '/continent'         | DELETE | 405      |
      | '/continent/1'       | GET    | 200      |
      | '/continent/1'       | PUT    | 405      |
      | '/continent/1'       | POST   | 405      |
      | '/continent/1'       | DELETE | 405      |
      | '/continent/code/NA' | GET    | 200      |
      | '/continent/code/NA' | PUT    | 405      |
      | '/continent/code/NA' | POST   | 405      |
      | '/continent/code/NA' | DELETE | 405      |
# ----- (GET)  -----
# ----- (GET)  -----
# ----- (POST)  -----
# ----- (PUT) (Update) -----
# ----- (PUT) (Create) -----
# ----- (DELETE) -----
