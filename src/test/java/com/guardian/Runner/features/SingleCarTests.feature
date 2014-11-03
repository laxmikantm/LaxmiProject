Feature: Get quote followed by login

@DLGEM-639
Scenario: Initial State:Data entry journey till Quote page
    Given I have selected to get the quote for a single car
    When I enter the single car quote and buy journey details
    Then I am able to specify whether I am a new or existing cust