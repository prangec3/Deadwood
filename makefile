all:
	find . -name \*.java -exec javac -d ./bin/ {} \+
	java -cp ./bin/ Deadwood
run:
	clear
	java -cp ./bin/ Deadwood

clean:
	rm ./bin/*.class
