# Micro SDKs

## The Idea

What if software components would be the direct OUTPUT of tests.
The more versatile your (successful) **scenarios** are, the more useful your output component 
(more metadata, cause tests are part of the component stored as a blockchain)

So instead of tests, we speak of **scenarios**.

This document should shed some light on an idea i call micro-software-development-kit paradigm.

## Whats in it

A MicroSDK helps you implement & verify a very specific functionality shielding the developer from any surrounding
 technology such as build system, depenendencies etc.

A microSDK should help, if not fully automate the following states of a development cycle:

- Setup
- Test
- Integrate

## Learn, Iterate, Forget

The ultimate experience of microSDK should be a close loop of: Learn, Iterate and Forget. Which ultimately means
that it should be dead simple to prepare for work, work and don't contribute to cognitive overload. Only present
the user the right absraction level to solve the problem at hand.

The very specific - almost microscopic - scope of a microSDK instance probably requires that microSDKs are tailored 
instances per user/per usecase.

## What can microSDK sdk do

Because you'll need to adapt many microSDKs to changing requirements, creating such a microSDK should be pleasant. 
Thats where rebaze could come in.

## Create without barriers

Suppose you want to contribute a new functionality but don't have the time, skill, patience or money to understand 
the surrounding technology, its build systems, dependencies and everything.


### Test Harness

For testing, the pattern of a Test Harness is a generic thing to have.
The user needs to be able to configure a harness, interact with it in a meaningful way.

What we can provide as a generic thing to a test harness:

#### External Resources

Some scenarious need a running database provisioned with data, some a docker image with something else,
some need an Istio Endpoint.

### Lifecycle Support

### Automatic Documentation


## rebaze incentive

Rebaze is a developer ergonomics consuilting firm. So we care about:

- Developer Onboarding: Get started efficiently
- Developer Training: Stay on top of the curve
- Developer Testing: Tools, Strategies
- Developer Experience: Create good APIs