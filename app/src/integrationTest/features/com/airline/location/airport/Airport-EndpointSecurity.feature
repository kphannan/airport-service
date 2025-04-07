Feature: Airport API Security

  Background:
    * url baseUrl + '/location'

  # ----- Verify acceptable methods -----
  @SmokeTest @SecurityCheck
  Scenario Outline: Method "<method>" on endpoint <target> should return "<response>"
    Given path <target>
    When method <method>
    Then status <response>

    Examples:
      | target               | method | response |
      | '/airport'           | GET    | 200      |
      | '/airport'           | PUT    | 405      |
      | '/airport'           | POST   | 405      |
      | '/airport'           | DELETE | 405      |
      | '/airport/3384'      | GET    | 200      |
      | '/airport/3384'      | PUT    | 405      |
      | '/airport/3384'      | POST   | 405      |
      | '/airport/3384'      | DELETE | 405      |
      | '/airport/code/RPVM' | GET    | 200      |
      | '/airport/code/RPVM' | PUT    | 405      |
      | '/airport/code/RPVM' | POST   | 405      |
      | '/airport/code/RPVM' | DELETE | 405      |
# ----- (GET)  -----
# ----- (GET)  -----
# ----- (POST)  -----
# ----- (PUT) (Update) -----
# ----- (PUT) (Create) -----
# ----- (DELETE) -----
