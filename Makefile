all:
	javac -Xlint:unchecked *.java

clean:
	rm -f *.class

test:
	java Test
