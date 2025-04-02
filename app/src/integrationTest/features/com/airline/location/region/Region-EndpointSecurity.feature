


Feature: Region API Security

    Background:
        * url baseUrl


    # ----- Verify acceptable methods -----
    @SmokeTest
    @SecurityCheck
    Scenario Outline: Method "<method>" on endpoint <target> should return "<response>"
        Given path <target>
        When method <method>
        Then status <response>

        Examples:
            | target                | method | response |
            | '/region'             | GET    | 200      |
            | '/region'             | PUT    | 405      |
            | '/region'             | POST   | 405      |
            | '/region'             | DELETE | 405      |
            | '/region/305779'      | GET    | 200      |
            | '/region/305779'      | PUT    | 405      |
            | '/region/305779'      | POST   | 405      |
            | '/region/305779'      | DELETE | 405      |
            | '/region/code/PH-MDC' | GET    | 200      |
            | '/region/code/PH-MDC' | PUT    | 405      |
            | '/region/code/PH-MDC' | POST   | 405      |
            | '/region/code/PH-MDC' | DELETE | 405      |


# ----- (GET)  -----

# ----- (GET)  -----

# ----- (POST)  -----

# ----- (PUT) (Update) -----

# ----- (PUT) (Create) -----

# ----- (DELETE) -----
