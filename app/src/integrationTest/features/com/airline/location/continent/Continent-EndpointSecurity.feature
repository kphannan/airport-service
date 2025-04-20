Feature: Continent API Security

  Background:
    * url baseUrl + '/location'

  # POST, PUT methods will return 400 here as these tests don't provide a request body
  # ----- Verify acceptable methods -----
  @SmokeTest @SecurityCheck
  Scenario Outline: <target> - "<method>" should return "<response>"
    Given path <target>
    When method <method>
    Then status <response>

    Examples:
      | target               | method  | response |
      | '/continent'         | GET     | 200      |
      | '/continent'         | PUT     | 400      |
      | '/continent'         | POST    | 400      |
      | '/continent'         | DELETE  | 400      |
      | '/continent'         | OPTIONS | 204      |
      | '/continent'         | HEAD    | 204      |
      | '/continent'         | TRACE   | 405      |
      | '/continent/1'       | GET     | 200      |
      | '/continent/99'      | PUT     | 405      |
      | '/continent/99'      | POST    | 405      |
      | '/continent/99'      | PATCH   | 204      |
      | '/continent/99'      | DELETE  | 204      |
      | '/continent/code/NA' | GET     | 200      |
      | '/continent/code/NA' | PUT     | 405      |
      | '/continent/code/NA' | POST    | 405      |
      | '/continent/code/NA' | PATCH   | 405      |
      | '/continent/code/NA' | DELETE  | 405      |
# ----- (GET)  -----
# ----- (GET)  -----
# ----- (POST)  -----
# ----- (PUT) (Update) -----
# ----- (PUT) (Create) -----
# ----- (DELETE) -----
