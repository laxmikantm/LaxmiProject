@runZ

Feature: Login to Guardian website

Scenario Outline: TheLogin
	Given the user accesses the login page of the Guardian website
	When the user enters value <username> in username field
	When the user enters value <password> in password field
	And Signs himself or herself in
	Then the <status> should be shown
	
Examples:
	|username				| password			| status		|
	|IncorrectUsername		| InvalidPassword	| unsuccessful	|
	|CorrectUsername		| CorrectPassword	| successful	|