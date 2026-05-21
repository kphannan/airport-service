Feature: Airport API Security

  Background:
    * url baseUrl + '/location/airport'

  # POST, PUT methods will return 400 here as these tests don't provide a request body
  # OPTIONS, HEAD should return 405 as they are not implemented
  # ----- Verify acceptable methods -----
  @SmokeTest @Functional @Foo
  Scenario Outline: <target> - "<method>" should return "<response>"
    Given path <target>
    When method <method>
    * print response
    * print responseHeaders
    Then status <response>

    Examples:
      | target                       | method | response |
      | '/count/continent'           | GET    | 200      |
      | '/count/continent/AS'        | GET    | 200      |
      | '/count/continent/AS/PH'     | GET    | 200      |
      | '/count/continent/AS/PH/CEB' | GET    | 200      |
      | '/count/continent/NA'        | GET    | 200      |
      | '/count/continent/NA/US'     | GET    | 200      |
      | '/count/continent/NA/US/FL'  | GET    | 200      |
      | '/count/country'             | GET    | 200      |
      | '/count/country/US'          | GET    | 200      |
      | '/count/country/US/GA'       | GET    | 200      |
      | '/count/region'              | GET    | 200      |
      | '/count/region/US-GA'        | GET    | 200      |
      | '/count/region/US-FL'        | GET    | 200      |


#      | '/airport'           | GET     | 200      |
#      | '/airport'           | PUT     | 405      |
#      | '/airport'           | POST    | 405      |
#      | '/airport'           | DELETE  | 405      |
#      | '/airport'           | OPTIONS | 200      |
#      | '/airport'           | HEAD    | 200      |
#      | '/airport'           | TRACE   | 405      |
#      | '/airport/3384'      | GET     | 200      |
#      | '/airport/3384'      | PUT     | 405      |
#      | '/airport/3384'      | POST    | 405      |
#      | '/airport/3384'      | PATCH   | 405      |
#      | '/airport/3384'      | DELETE  | 405      |
#      | '/airport/code/RPVM' | GET     | 200      |
#      | '/airport/code/RPVM' | PUT     | 405      |
#      | '/airport/code/RPVM' | POST    | 405      |
#      | '/airport/code/RPVM' | PATCH   | 405      |
#      | '/airport/code/RPVM' | DELETE  | 405      |
# ----- (GET)  -----
# ----- (GET)  -----
# ----- (POST)  -----
# ----- (PUT) (Update) -----
# ----- (PUT) (Create) -----
# ----- (DELETE) -----
