# MaxFibonacciHeap

A new search engine “DuckDuckGo” is implementing a system to count the most popular
keywords used in their search engine. They want to know what the 𝑛 most popular keywords
are at any given time. You are required to undertake that implementation. Keywords will be
given from an input file together with their frequencies. You need to use a max priority structure
to find the most popular keywords.
You must use the following data structures for the implementation.
 Max Fibonacci heap: to keep track of the frequencies of keywords
 Hash table: keywords should be used as keys for the hash table and value is the pointer to the
corresponding node in the Fibonacci heap.
You will need to perform the increase key operation many times as keywords appear in the
input keywords stream. You are only required to implement the Fibonacci heap operations that
are required to accomplish this task.

Input format
Keywords appear one per each line in the input file and starts with $ sign. In each line, an integer
will appear after the keyword and that is the count (frequency) of the keyword (There is a space
between keyword and the integer). You need to increment the keyword by that count. Queries
will also appear in the input file and once a query (for most popular keywords) appears, you should
append the output to the output file. Query will be an integer number (𝑛) without $ sign in the
beginning. You should write the top most 𝑛 keyword to the output file. When “stop” (without $
sign) appears in the input stream, program should end. Following is an example of an input file.
$facebook 5
$youtube 3
$facebook 10
$amazon 2
$gmail 4
$weather 2
$facebook 6
$youtube 8
$ebay 2
$news 2
$facebook 12
$youtube 11
$amazon 6
3
$facebook 12
$amazon 2
$stop 3
$playing 4
$gmail 15
$drawing 3
$ebay 12
$netflix 6
$cnn 5
5
stop

Output format
Once a query appears, you need to write down the most popular keywords to the output file in
descending order. Output for a query should be comma separated list without any new lines.
Once the output for a query is finished you should put a new line to write the output for the
next query. You should produce all the outputs in the output file named “output_file.txt”.
Following is the output file for the above input file.
facebook,youtube,amazon
facebook,youtube,gmail,ebay,amazon
