JFLAGS = -encoding ISO-8859-1 -verbose
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = $(shell for file in `find echecs/ -name *.java`;do echo $$file; done)

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(shell for file in `find echecs/ -name *.class`;do rm $$file; done)
