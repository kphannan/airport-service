Feature: Health, Liveness, Readiness probes

  Background:
    * url host + '/actuator'

  # ----- (GET)  -----
  @SmokeTest @PostDeployment @Health
  Scenario: Body of /health includes text 'OK'
    Given path '/health'
    When method GET
    Then status 200
    # Then status 242
    And match $.components.db.status == 'UP'
    And match $.status == 'UP'

  # And match $ contains { "status": 'UP' }
  # TODO revisit what is composed in the /liveness check
  @SmokeTest @PostDeployment @Health
  Scenario: Liveness probe is only a ping indicator
    Given path '/health/liveness'
    When method GET
    Then status 200
    # And match $.components.ping.status == 'UP'
    And match $.status == 'UP'

  # TODO revisit what is composed in the /readiness check
  @SmokeTest @PostDeployment @Health
  Scenario: Readiness probe includes database indicator
    Given path '/health/readiness'
    When method GET
    Then status 200
    # And match $.components.db.status == 'UP'
    And match $.status == 'UP'
# ----- (GET)  -----
# ----- (POST)  -----
# ----- (PUT) (Update) -----
# ----- (PUT) (Create) -----
# ----- (DELETE) -----
