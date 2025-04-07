Feature: Health, Liveness, Readiness probes

  Background:
    * url baseUrl + '/actuator'

  # ----- Verify acceptable methods -----
  @SmokeTest @PostDeployment @SecurityCheck
  Scenario Outline: Method "<method>" on endpoint <target> should return "<response>"
    Given path <target>
    When method <method>
    Then status <response>

    Examples:
      | target              | method | response |
      | '/health'           | GET    | 200      |
      | '/health'           | PUT    | 405      |
      | '/health'           | POST   | 405      |
      | '/health'           | DELETE | 405      |
      | '/health/liveness'  | GET    | 200      |
      | '/health/liveness'  | PUT    | 405      |
      | '/health/liveness'  | POST   | 405      |
      | '/health/liveness'  | DELETE | 405      |
      | '/health/readiness' | GET    | 200      |
      | '/health/readiness' | PUT    | 405      |
      | '/health/readiness' | POST   | 405      |
      | '/health/readiness' | DELETE | 405      |
# ----- (GET)  -----
# ----- (GET)  -----
# ----- (POST)  -----
# ----- (PUT) (Update) -----
# ----- (PUT) (Create) -----
# ----- (DELETE) -----
