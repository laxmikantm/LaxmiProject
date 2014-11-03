@runG

Feature: Login to Guardian website

Scenario Outline: MyLogin
	Given the user accesses the login page of Guardian website
	When the user enters value <username> in username field
	When the user enters value <password> in password field
	And clicks enter
	Then the <status> should be shown
	
Examples:
	|username				| password			| status		|
	|IncorrectUsername		| InvalidPassword	| unsuccessful	|
	|CorrectUsername		| CorrectPassword	| successful	|