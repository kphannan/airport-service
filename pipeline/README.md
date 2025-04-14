
# Pipeline

> NOTE:
> This is preliminary.
>



Houses declarations, scripts, data necessary for a full CI/CD pipeline.

- assemble
- deploy
- infra
- stages
- test


## Assemble

Assembles artifacts that will move through the pipeline.
These build upon development artifacts.

## Deploy

Perform deployment of an artifact to a single environment.
Deploy logic is agnostic of an environment.
Environment details are supplied via configuration.

## Infrastructure

Declares the topology of an environment.

## Stages

Declares the stage or phase of pipeline.

## Test

