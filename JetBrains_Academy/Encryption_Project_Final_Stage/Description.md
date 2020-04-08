# Encryption-Decryption Program

The program has different algorithms to encode/decode data. 
The first one would be shifting algorithm (it shifts each letter by the specified number according to its order in the alphabet in circle). 
The second one would be based on Unicode table.

The program parses five arguments: -mode, -key and -data, -in, -out. The first argument determines the program’s mode 
(enc - encryption, dec - decryption). The second argument is an integer key to modify the message, 
and the third argument is a text or ciphertext within quotes to encrypt or decrypt.

The program reads data from -data or from a file written in the -in argument.

If there is no -mode, the program works in enc mode.
If there is no -key, the program considers that key = 0.
If there is no -data, and there is no -in the program assumes that the data is an empty string.
If there is no -out argument, the program prints data to the standard output.
If there are both -data and -in arguments, the program prefers -data over -in.

--------------------------------------
> Example 1

Input:
java Main -mode enc -in road_to_treasure.txt -out protected.txt -key 5 -alg unicode


This command must get data from the file road_to_treasure.txt, encrypt the data with the key 5, create a file called protected.txt and write ciphertext to it.

--------------------------------------
> Example 2

Input:
java Main -mode enc -key 5 -data "Welcome to hyperskill!" -alg unicode

Output:
\jqhtrj%yt%m~ujwxpnqq&

--------------------------------------
> Example 3

Input:
java Main -key 5 -alg unicode -data "\jqhtrj%yt%m~ujwxpnqq&" -mode dec

Output:
Welcome to hyperskill!

--------------------------------------
> Example 4:

Input:
java Main -key 5 -alg shift -data "Welcome to hyperskill!" -mode enc

Output:
Bjqhtrj yt mdujwxpnqq!

--------------------------------------
