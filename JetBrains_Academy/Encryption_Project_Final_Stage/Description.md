In this project, you will learn how to encrypt and decrypt messages and texts using simple algorithms. 
We should note that such algorithms are not suitable for industrial use because they can easily be cracked, 
but these algorithms demonstrate some general ideas about encryption.

The program has different algorithms to encode/decode data. 
The first one would be shifting algorithm (it shifts each letter by the specified number according to its order in the alphabet in circle). 
The second one would be based on Unicode table.

The program must parse three arguments: -mode, -key and -data. The first argument should determine the program’s mode 
(enc - encryption, dec - decryption). The second argument is an integer key to modify the message, 
and the third argument is a text or ciphertext within quotes to encrypt or decrypt.

All the arguments are guaranteed to be passed to the program. If for some reason it turns out to be wrong:

If there is no -mode, the program should work in enc mode.
If there is no -key, the program should consider that key = 0.
If there is no -data, the program should assume that the data is an empty string.

The program must parse two additional arguments -in and -out to specify the full name of a file to read data and to write the result.
Arguments -mode, -key, and -data should still work as before.

The program reads data from -data or from a file written in the -in argument.

If there is no -mode, the program should work in enc mode.
If there is no -key, the program should consider that key = 0.
If there is no -data, and there is no -in the program should assume that the data is an empty string.
If there is no -out argument, the program must print data to the standard output.
If there are both -data and -in arguments, your program should prefer -data over -in.

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
