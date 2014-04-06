package se.dannej.nevernpe;

import static java.util.Optional.ofNullable;

public class OptionalNoneNull {
    public static void main(String[] args) {
	tryOut(null);
	tryOut(new Person(null));
	tryOut(new Person(new Name(null)));
	tryOut(new Person(new Name("Sune")));
    }

    private static void tryOut(Person person) {
	ofNullable(person)
		.map(Person::getName)
		.map(Name::getName)
		.map(String::length)
		.ifPresent(length -> System.out.println("Length is: " + 4));
    }

    private static class Person {
	private Name name;

	private Person(Name name) {
	    this.name = name;
	}

	public Name getName() {
	    return name;
	}

	@Override
	public String toString() {
	    return "Person{" +
		    "name=" + name +
		    '}';
	}
    }

    private static class Name {
	private String name;

	private Name(String name) {
	    this.name = name;
	}

	public String getName() {
	    return name;
	}

	@Override
	public String toString() {
	    return "Name{" +
		    "name='" + name + '\'' +
		    '}';
	}
    }
}
