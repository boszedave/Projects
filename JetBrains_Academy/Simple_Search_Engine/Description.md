# Simple Search Engine

The program reads data from a text file - a person, represented by a first name, last name, and an optional email.

It uses a data structure called Inverted Index. It maps each word to all positions/lines/documents in which the word occurs. As a result, when we receive a query, we can immediately find the answer without any comparisons.

It supports complex queries containing a sequence of words and a strategy that determines how to match data:

If the strategy is ALL, the program should print lines containing all words from the query.
If the strategy is ANY, the program should print lines containing at least one word from the query.
If the strategy is NONE, the program should print lines that do not contain words from the query at all.
